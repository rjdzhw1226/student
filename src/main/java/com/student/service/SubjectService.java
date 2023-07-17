package com.student.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.student.Constant.RedisKey;
import com.student.mapper.MenuMapper;
import com.student.mapper.StudentMapper;
import com.student.mapper.SubjectMapper;
import com.student.mapper.TeacherMapper;
import com.student.pojo.page;
import com.student.pojo.student;
import com.student.pojo.subject;
import com.student.pojo.vo.subjectVo;
import com.student.util.BaseContext;
import com.student.util.rabbitMQ.MQSender;
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
public class SubjectService {
    @Value("${file.readPath}")
    private String readFilePath;

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

    @Autowired
    private MQSender mqSender;

    /**
     * 同步
     */
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

    public page<subject> query(Integer page, Integer size) {
        page<subject> pages = new page<>();
        int count = subjectMapper.queryCount();
        List<subject> multiCombineResult = excelService.getMultiCombineResult(count);
        List<subject> lists = multiCombineResult.subList((page - 1) * size, (page - 1) * size + size);
        pages.setData(lists);
        pages.setTotal(count);
        return pages;
    }

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

    public Map<String, Object> connectSubject(String subId) {
        // 进入选课页面时就应该把所有课查出来存入缓存 默认选课失败
        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("message", -1);
        // 从BaseContext把用户信息取出来
        //String userName = BaseContext.getCurrentId();
        String userName = "1";
        synchronized (userName.intern()){
            // 查询当前用户选课的记录 先看缓存 再决定查不查数据库
            String s = mapRedisTemplate.opsForValue().get(RedisKey.CACHE_SUB_CHOOSE_KEY + subId);
            // 已选这门课 直接返回
            if(userName.equals(s)){
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
            mapRedisTemplate.opsForValue().set(RedisKey.CACHE_SUB_CHOOSE_KEY + subId, userName);
            // 将用户信息和subId 传入队列存储 后续做数据库增减
            mqSender.send(subId,userName);
        }
        HashMap.put("message", 0);
        return new HashMap<>();
    }
    @Transactional(rollbackFor = Exception.class)
    public void executeChoose(String subId, String stuName) {
        subjectMapper.update(subId);
        studentMapper.connectSub(subId,stuName);
    }
}
