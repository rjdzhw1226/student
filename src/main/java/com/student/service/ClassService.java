package com.student.service;

import com.student.mapper.*;
import com.student.pojo.*;
import com.student.pojo.dto.classDto;
import com.student.pojo.vo.FileVo;
import com.student.pojo.vo.classVo;
import com.student.util.BatchInsertThread;
import com.student.util.BatchInsertThreadAuto;
import com.student.util.CommonUtil;
import com.student.util.PoiUtil;
import com.student.util.file.writeFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClassService {

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Value("${file.readPath}")
    private String path;

    @Value("${file.loadPath}")
    private String LoadPath;

    private static final String XLSX = ".xlsx";
    private static final String XLS = ".xls";

    @Transactional
    public classVo query(String id) {
        classVo classVo = new classVo();
        classDto query = classMapper.query(id);
        String id1 = query.getId();
        String father_id = query.getFather_id();
        String teacher_id = query.getTeacher_id();
        Integer countC = query.getCount();
        String labelGrade = menuMapper.queryTreeById(id1);
        String labelGradeClass = menuMapper.queryTreeTwoId(father_id);
        String teacherName = teacherMapper.queryById(teacher_id);
        List<student> students = studentMapper.queryByGradeClass(labelGradeClass, labelGrade);
        classVo.setName(labelGradeClass+"-"+labelGrade);
        classVo.setId(id1);
        classVo.setFather_id(teacher_id);
        classVo.setTeacherName(teacherName);
        classVo.setTeacher_id(teacher_id);
        classVo.setStudents(students);
        classVo.setCountC(countC);
        return classVo;
    }

    public List<String> AsyncTask() {
        List<String> result = new ArrayList<>();
        try {
            Callable call = new Callable() {
                @Override
                public List<String> call() throws Exception {
                    List<String> list = new ArrayList<>();
                    List<String> collect = list.stream().collect(Collectors.toList());
                    return collect;
                }
            };

            Future<List<String>> future =  threadPoolTaskExecutor.submit(call);
            //获取结果
            result = future.get();
            return result;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }
    @Transactional
    public List<List<String>> findStudentByClass(String name) {
        String grade = name.split("-")[0];
        String gradeClass = name.split("-")[1];
        List<student> students = studentMapper.queryByGradeClass(grade, gradeClass);
        final List<FileVo> list = new ArrayList<>();
        // 获取需要的线程数
        int timeSize = 10;
        //分组
        int size = students.size() % timeSize;
        //int remainders = size > 0 ? students.size() / 10 + 1 : students.size() / 10;
        int remainders = (students.size() - size) / timeSize;
        CountDownLatch countDownLatch=new CountDownLatch(timeSize + 1);
        for(int i = 0; i < timeSize + 1; i++){
            if(i != timeSize){
                //数据分割
                List<student> subList = students.subList(remainders * i, remainders * i + remainders);
                threadPoolTaskExecutor.submit(() -> {
                    //线程池
                    try {
                        //数据操作
                        FileVo fileVo = extractedAdd(subList);
                        list.add(fileVo);
                    }catch (Exception e) {
                        System.out.println("exception"+e.getMessage());
                    }finally{
                        countDownLatch.countDown(); //当前线程的任务执行完毕，任务计数器-1
                    }
                });
            }else {
                List<student> subList = students.subList(remainders * i, remainders * i + size);
                threadPoolTaskExecutor.submit(() -> {
                    //线程池
                    try {
                        //数据操作
                        FileVo fileVo = extractedAdd(subList);
                        list.add(fileVo);
                    }catch (Exception e) {
                        System.out.println("exception"+e.getMessage());
                    }finally{
                        countDownLatch.countDown(); //当前线程的任务执行完毕，任务计数器-1
                    }
                });
            }
/*            List<student> subList = students.subList(i * remainders, Math.min(students.size(), i * remainders + remainders));
            threadPoolTaskExecutor.submit(() -> {
                //线程池
                try {
                    //数据操作
                    FileVo fileVo = extractedAdd(subList);
                    list.add(fileVo);
                }catch (Exception e) {
                    System.out.println("exception"+e.getMessage());
                }finally{
                    countDownLatch.countDown(); //当前线程的任务执行完毕，任务计数器-1
                }
            });*/
        }
        try {
            countDownLatch.await();    //主线程等待所有的子任务结束，如果有一个子任务没有完成则会一直等待
            System.out.println("-------执行完毕-------");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将异步处理的list整合
        List<List<String>> getDownloadList = new ArrayList<>();
        for (FileVo vo : list) {
            getDownloadList.addAll(vo.getDownloadList());
        }
        log.info("getDownloadList:{}"+getDownloadList);
        String[] strArray = new String[]{"学号","姓名","年级","班级","手机号","年龄","性别","状态","头像"};
        try {
            PoiUtil.exportCSVFile(strArray,getDownloadList,0,path + "student" +UUID.randomUUID() +".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getDownloadList;
    }

    private FileVo extractedAdd(List<student> sublist) {
        FileVo fileVo = new FileVo();
        List<List<String>> downloadList = new ArrayList<>();
        extracted(downloadList,sublist);
        fileVo.setDownloadList(downloadList);
        return fileVo;
    }

    public List<List<String>> findStudentByClassString(String name) {
        List<List<String>> list = new ArrayList<>();
        String grade = name.split("-")[0];
        String gradeClass = name.split("-")[1];
        List<student> students = studentMapper.queryByGradeClass(grade, gradeClass);
        extracted(list, students);
        return list;
    }

    private void extracted(List<List<String>> list, List<student> students) {
        for (student student : students) {
            List<String> listChildren = new LinkedList<>();
            String studentId = student.getId();
            listChildren.add(studentId);
            String stName = student.getName();
            listChildren.add(stName);
            String station = student.getStation();
            listChildren.add(station);
            String stGrade = student.getGrade();
            listChildren.add(stGrade);
            String grade_class = student.getGrade_class();
            listChildren.add(grade_class);
            String gender = student.getGender();
            listChildren.add(gender);
            String age = student.getAge();
            listChildren.add(age);
            String phone = student.getPhone();
            listChildren.add(phone);
            String url = student.getUrl();
            listChildren.add(url);
            list.add(listChildren);
        }
    }

    public classVo findStudentByClassChange(String name) {
        String grade = name.split("-")[0];
        String gradeClass = name.split("-")[1];
        List<student> students = studentMapper.queryByGradeClass(grade, gradeClass);
        classVo classV = new classVo();
        classV.setStudents(students);
        return classV;
    }
    public void upDataTableAuto(MultipartFile file, List<String> nameList){
        writeFile wf = new writeFile();
        Workbook workbook = wf.getWorkBook(file);
        wf.ExcelMap(workbook,nameList);
        for (int j = 0; j < nameList.size(); j++) {
            ClassService sc = new ClassService();
            Class aClass = sc.complierAndRun(nameList.get(j));
            try{
                // 解析表格数据
                InputStream in = null;
                String fileName = "";
                if (file != null) {
                    // 上传文件解析
                    in = file.getInputStream();
                    fileName = CommonUtil.getString(file.getOriginalFilename()).toLowerCase();
                }
                Workbook book = null;
                if (fileName.endsWith(XLSX)) {
                    book = new XSSFWorkbook(in);
                } else if (fileName.endsWith(XLS)) {
                    POIFSFileSystem poifsFileSystem = new POIFSFileSystem(in);
                    book = new HSSFWorkbook(poifsFileSystem);
                } else {
                    throw new RuntimeException("文件上传格式不正确");
                }
                Sheet sheet = book.getSheetAt(0);
                //excel数据总量
                int rowSize = sheet.getLastRowNum() + 1;
                //每个线程处理的数量
                int count=1000;
                //开启的线程数
                int runSize=(rowSize/count)+1;
                //时间统计
                long time1 = System.currentTimeMillis();
                // 创建两个个计数器
                CountDownLatch begin = new CountDownLatch(1);
                CountDownLatch end = new CountDownLatch(runSize);
                for(int i=0;i<runSize;i++){
                    int startIdx=0;
                    int endIdx=0;
                    if((i+1)==runSize){
                        startIdx = (i * count);
                        endIdx = rowSize;
                    }else{
                        startIdx = (i * count);
                        endIdx = (i + 1) * count;
                    }
                    BatchInsertThreadAuto thread = new BatchInsertThreadAuto(sheet, begin, end, excelService,startIdx,endIdx,aClass);
                    threadPoolTaskExecutor.execute(thread);
                }
                begin.countDown();
                end.await();
                threadPoolTaskExecutor.shutdown();
                //时间统计
                long time2 = System.currentTimeMillis();
                log.info("学生文件数据入库总耗时："+(time2-time1));
                in.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }
    public void upData(MultipartFile file){
        try{
            // 解析表格数据
            InputStream in = null;
            String fileName = "";
            if (file != null) {
                // 上传文件解析
                in = file.getInputStream();
                fileName = CommonUtil.getString(file.getOriginalFilename()).toLowerCase();
            }
            Workbook book = null;
            if (fileName.endsWith(XLSX)) {
                book = new XSSFWorkbook(in);
            } else if (fileName.endsWith(XLS)) {
                POIFSFileSystem poifsFileSystem = new POIFSFileSystem(in);
                book = new HSSFWorkbook(poifsFileSystem);
            } else {
                throw new RuntimeException("文件上传格式不正确");
            }
            Sheet sheet = book.getSheetAt(0);
            //excel数据总量
            int rowSize = sheet.getLastRowNum() + 1;
            //每个线程处理的数量
            int count=1000;
            //开启的线程数
            int runSize=(rowSize/count)+1;
            //时间统计
            long time1 = System.currentTimeMillis();
            // 创建两个个计数器
            CountDownLatch begin = new CountDownLatch(1);
            CountDownLatch end = new CountDownLatch(runSize);
            for(int i=0;i<runSize;i++){
                int startIdx=0;
                int endIdx=0;
                if((i+1)==runSize){
                    startIdx = (i * count);
                    endIdx = rowSize;
                }else{
                    startIdx = (i * count);
                    endIdx = (i + 1) * count;
                }
                BatchInsertThread thread = new BatchInsertThread(sheet, begin, end, excelService,startIdx,endIdx);
                threadPoolTaskExecutor.execute(thread);
            }
            begin.countDown();
            end.await();
            threadPoolTaskExecutor.shutdown();
            //时间统计
            long time2 = System.currentTimeMillis();
            log.info("学生文件数据入库总耗时："+(time2-time1));
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Transactional
    @Async("taskExecutor")
    public void upDataGiveUp(List<student> students) {
        log.info(Thread.currentThread().getName() + "正在处理请求");
        for (student student : students) {
            int add = studentMapper.add(student);
            log.info("i:{}"+add);
        }
        log.info("请求处理完成");
    }

    private Class complierAndRun(String name){
        try {

            System.out.println(System.getProperty("user.dir"));
            //动态编译
            JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
            String filename = "D:/DOWNLOAD/"+name+".java";
            int status = javac.run(null, null, null, "-d", System.getProperty("user.dir")+"/target/classes/com/student/pojo",filename);
            if(status!=0){
                System.out.println("没有编译成功！");
            }

            //动态执行
            Class clz = Class.forName(name);//返回与带有给定字符串名的类 或接口相关联的 Class 对象。
            Class aClass = DyBeanRegister(clz, name.toLowerCase());
            return aClass;
        } catch (Exception e) {
            log.error("error", e);
            return null;
        }
    }

    private static <T> Class<?> DyBeanRegister(Class<T> clazz,String name) {
        // 创建ApplicationContext
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        // 获取Bean定义注册器
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) context.getBeanFactory();

        // 创建Bean定义
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(clazz);

        // 注册Bean定义
        registry.registerBeanDefinition(name, beanDefinition);

        // 启动应用上下文
        context.refresh();

        // 获取动态注册的Bean实例
        T bean = context.getBean(clazz);
        Class<?> aClass = bean.getClass();

        // 关闭应用上下文
        context.close();

        return aClass;
    }

    public List<card> queryCard() {
        List<card> query = classMapper.queryCard();
        return query;
    }
}
