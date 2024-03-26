package com.student.service.impl;

import com.alibaba.fastjson.JSON;
import com.student.Constant.RedisKey;
import com.student.mapper.MenuMapper;
import com.student.mapper.StudentMapper;
import com.student.mapper.SubjectMapper;
import com.student.mapper.TeacherMapper;
import com.student.pojo.page;
import com.student.pojo.subject;
import com.student.pojo.vo.subjectVo;
import com.student.service.ExcelService;
import com.student.service.SubjectService;
import com.student.util.BaseContext;
//import com.student.util.rabbitMQ.MQSender;
import com.student.util.redis.LockRedis;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class SubjectServiceImpl implements SubjectService {

    private static final LockRedis lock = new LockRedis();

    @Value("${file.readPath}")
    private String readFilePath;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate<Object, Object> listRedisTemplate;

    @Resource
    private RedisTemplate<String, String> mapRedisTemplate;

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private ExcelService excelService;

//    @Autowired
//    private MQSender mqSender;

    private HashMap<String, Boolean> localOverMap = new HashMap<String, Boolean>();

    /**
     * 同步
     */
    @Override
    public void findByMultiThreads() {
        int timeSize = 10;
        CountDownLatch countDownLatch = new CountDownLatch(timeSize);
        for (int i = 0; i < timeSize; i++) {
            threadPoolTaskExecutor.submit(new Runnable() { //线程池
                @Override
                public void run() {
                    try {
                        //数据内容查询和数据操作

                    } catch (Exception e) {
                        System.out.println("exception" + e.getMessage());
                    } finally {
                        countDownLatch.countDown(); //当前线程的任务执行完毕，任务计数器-1
                    }
                }
            });
            try {
                countDownLatch.await(); //主线程等待所有的子任务结束，如果有一个子任务没有完成则会一直等待
                System.out.println("-------执行完毕-------");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public page<subject> query(Integer page, Integer size) {
        page<subject> pages = new page<>();
        int count = subjectMapper.queryCount();
        List<subject> multiCombineResult = excelService.getMultiCombineResult(count);
        List<subject> lists = multiCombineResult.subList((page - 1) * size, (page - 1) * size + size);
        pages.setData(lists);
        pages.setTotal(count);
        return pages;
    }
    @Override
    public page<subject> queryAllSubject(Integer page, Integer size) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", (page - 1) * size);
        params.put("size", size);
        //查询
        List<subject> list = new ArrayList<>();
        List<subjectVo> subjectVos = subjectMapper.queryAll(params);
        for (subjectVo subjectVo : subjectVos) {
            subject sb = new subject();
            String id = subjectVo.getId();
            String name = subjectVo.getName();
            Integer gradeMax = Integer.parseInt(subjectVo.getGrade_max());
            Integer gradeMin = Integer.parseInt(subjectVo.getGrade_min());
            String teacherName = subjectVo.getTeacherName();
            List<String> strings = menuMapper.queryLabelList(gradeMax, gradeMin);
            sb.setGradeBetween(strings.get(0) + "~" + strings.get(strings.size() - 1));
            sb.setTeacherName(teacherName);
            sb.setName(name);
            sb.setId(id);
            list.add(sb);
        }
        int count = subjectMapper.queryCount();
        page<subject> subjectpage = new page<>();
        subjectpage.setData(list);
        subjectpage.setTotal(count);
        return subjectpage;
    }

    @Override
    public List<subject> queryAllSubjectGiveUp(Integer page, Integer size) throws ExecutionException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        params.put("page", (page - 1) * size);
        params.put("size", size);
        //查询 用于互不相干并行查询
        Callable getList = new Callable<List<subjectVo>>() {
            @Override
            public List<subjectVo> call() throws Exception {
                List<subjectVo> subjectVos = subjectMapper.queryAll(params);
                return subjectVos;
            }
        };

        Callable getListDetail = new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                List<String> strings = subjectMapper.queryTeacherName();
                return strings;
            }
        };
        FutureTask<List<subjectVo>> getListBack = new FutureTask<List<subjectVo>>(getList);
        FutureTask<List<String>> getListDetailBack = new FutureTask<List<String>>(getList);
        threadPoolTaskExecutor.submit(getListBack);
        threadPoolTaskExecutor.submit(getListDetail);
        List<subjectVo> subjectVos = getListBack.get();
        List<String> strings = getListDetailBack.get();
        threadPoolTaskExecutor.shutdown();
        return null;
    }

    @Override
    public Map<String, Object> connectSubject(String subId) {
        // 进入选课页面时就应该把所有课查出来存入缓存 默认选课失败
        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("message", -1);
        // 从BaseContext把用户信息取出来
        String userName = BaseContext.getCurrentId();
        //String userName = "1";
        lock.lock();
        try{
            //synchronized (userName.intern()){
            // 查询当前用户选课的记录 先看缓存 再决定查不查数据库
            String s = mapRedisTemplate.opsForValue().get(RedisKey.CACHE_SUB_CHOOSE_KEY + userName + subId);
            // 已选这门课 直接返回
            if("1".equals(s)){
                return HashMap;
            }
            // 未选 查询此门课程当前剩余量
            String countSub = stringRedisTemplate.opsForValue().get(RedisKey.CACHE_SUB_COUNT_KEY + subId);
            Integer integer = Integer.valueOf(countSub);
            // 小于零直接返回
            if(Integer.valueOf(countSub) <= 0){
                return HashMap;
            }
            // 大于零 更新缓存库存量
            stringRedisTemplate.opsForValue().set(RedisKey.CACHE_SUB_COUNT_KEY + subId,String.valueOf(integer - 1));
            mapRedisTemplate.opsForValue().set(RedisKey.CACHE_SUB_CHOOSE_KEY + userName + subId, "1");
            // 将用户信息和subId 传入队列存储 后续做数据库增减
//            mqSender.send(subId,userName);
            //}
        }catch(Exception e){
            e.printStackTrace();
            return HashMap;
        }finally {
            lock.unlock();
        }
        HashMap.put("message", 0);
        return HashMap;
    }

    @Override
    public Map<String, Object> doSubjectChoose(String subId) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", -1);
        //主线程中取出用户名
        String stuName = BaseContext.getCurrentId();
        String stock = stringRedisTemplate.opsForValue().get(RedisKey.CACHE_SUB_KEY);
        if (Integer.valueOf(stock) < 0) {
            afterPropertiesSet();
            String stock2 = stringRedisTemplate.opsForValue().get(RedisKey.CACHE_SUB_KEY);
            if(Integer.valueOf(stock2) < 0){
                localOverMap.put(stock2, true);
                return map;
            }
        }
        RLock lock1 = redissonClient.getLock("lock:subject:" + stuName);
        boolean islock = lock1.tryLock();
        if(!islock){
            return map;
        }
        try {
            //代理对象
            SubjectService proxy = (SubjectService)AopContext.currentProxy();
            return proxy.connectSubName(subId,stuName);
        }finally {
            lock.unlock();
        }

    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> connectSubName(String subId, String stuName) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", -1);
        if(subId.equals("")){
            throw new RuntimeException("subId-异常");
        }
        // 查询当前用户选课的记录 先看缓存 再决定查不查数据库
        String redisId = mapRedisTemplate.opsForValue().get(RedisKey.CACHE_SUB_CHOOSE_KEY + stuName + subId);
        // 已选这门课 直接返回
        if("1".equals(redisId)){
            return map;
        }
        if(redisId == null){
            //查询
            String id = studentMapper.queryMiddleById(subId,stuName);
            if("1".equals(id)){
                return map;
            }
        }
        //减库存并关联课程结果
        subjectMapper.update(subId);
        studentMapper.connectSub(subId,stuName,"1");
        map.put("message", 0);
        return map;
    }
    //初始化方法
    public void afterPropertiesSet() {
        List<subject> subjects = JSON.parseArray(stringRedisTemplate.opsForValue().get(RedisKey.CACHE_SUB_KEY),subject.class);
        if (subjects == null) {
            subjects = subjectMapper.queryAllReal();
            stringRedisTemplate.opsForValue().set(RedisKey.CACHE_SUB_KEY, JSON.toJSONString(subjects),60, TimeUnit.SECONDS);
            return;
        }
        for (subject goods : subjects) {
            //初始化
            stringRedisTemplate.opsForValue().set(RedisKey.CACHE_SUB_COUNT_KEY + goods.getId(), String.valueOf(goods.getCount()));
            localOverMap.put(goods.getId(), false);
        }
    }

    @Override
    public page<subject> chooseSubject(Integer page, Integer size) {
        page<subject> pages = new page<>();
        List<subject> subjects = JSON.parseArray(stringRedisTemplate.opsForValue().get(RedisKey.CACHE_SUB_KEY),subject.class);
        if (subjects == null) {
            page<subject> query = query(page, size);
            stringRedisTemplate.opsForValue().set(RedisKey.CACHE_SUB_KEY, JSON.toJSONString(query.getData()),60, TimeUnit.SECONDS);
            for (subject datum : query.getData()) {
                int countSub = datum.getCount();
                String id = datum.getId();
                stringRedisTemplate.opsForValue().set(RedisKey.CACHE_SUB_COUNT_KEY + id, String.valueOf(countSub));
            }
            return query;
        } else {
            int count = subjects.size();
            for (subject datum : subjects) {
                int countSub = datum.getCount();
                String id = datum.getId();
                stringRedisTemplate.opsForValue().set(RedisKey.CACHE_SUB_COUNT_KEY + id, String.valueOf(countSub));
            }
            pages.setData(subjects);
            pages.setTotal(count);
            return pages;
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeChoose(String subId, String stuName) {
        subjectMapper.update(subId);
        studentMapper.connectSub(subId,stuName,"1");
    }
}
