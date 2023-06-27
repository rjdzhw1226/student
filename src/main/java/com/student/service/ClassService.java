package com.student.service;

import com.student.mapper.*;
import com.student.pojo.*;
import com.student.util.CommonUtil;
import com.student.util.TestBatchInsertThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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

    @Transactional
    public void findStudentByClass() {
        int timeSize = 1;  // 获取需要的线程数
        CountDownLatch countDownLatch=new CountDownLatch(timeSize);
        for(int i = 0; i < timeSize; i++){
            threadPoolTaskExecutor.submit(new Runnable() {   //线程池
                @Override
                public void run() {
                    try {
                        //数据内容查询和数据操作
                        extractedAdd();
                    }catch (Exception e) {
                        System.out.println("exception"+e.getMessage());
                    }finally{
                        countDownLatch.countDown(); //当前线程的任务执行完毕，任务计数器-1
                    }
                }
            });

        }
        try {
            countDownLatch.await();    //主线程等待所有的子任务结束，如果有一个子任务没有完成则会一直等待
            System.out.println("-------执行完毕-------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void extractedAdd() {


    }

    public classVo findStudentByClassChange(String name) {
        String grade = name.split("-")[0];
        String gradeClass = name.split("-")[1];
        return null;
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
                TestBatchInsertThread thread = new TestBatchInsertThread(sheet, begin, end, excelService,startIdx,endIdx);
                threadPoolTaskExecutor.execute(thread);
            }
            begin.countDown();
            end.await();
            threadPoolTaskExecutor.shutdown();
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
}
