package com.student.pojo;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.student.util.excel.ExcelImport;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "studentTest")
public class studentTest{
    @ExcelImport("学号")
    @Column(name = "id",type = MySqlTypeConstant.VARCHAR,length = 128)
    private String id;
    @ExcelImport("姓名")
    @Column(name = "name",type = MySqlTypeConstant.VARCHAR,length = 128)
    private String name;
    @ExcelImport("年级")
    @Column(name = "grade",type = MySqlTypeConstant.VARCHAR,length = 128)
    private String grade;
    @ExcelImport("班级")
    @Column(name = "grade_class",type = MySqlTypeConstant.VARCHAR,length = 128)
    private String grade_class;
    @ExcelImport("手机号")
    @Column(name = "phone",type = MySqlTypeConstant.VARCHAR,length = 128)
    private String phone;
    @ExcelImport("年龄")
    @Column(name = "age",type = MySqlTypeConstant.VARCHAR,length = 128)
    private String age;
    @ExcelImport("性别")
    @Column(name = "gender",type = MySqlTypeConstant.VARCHAR,length = 128)
    private String gender;
    @ExcelImport("状态")
    @Column(name = "station",type = MySqlTypeConstant.VARCHAR,length = 128)
    private String station;
    @ExcelImport("头像")
    @Column(name = "url",type = MySqlTypeConstant.VARCHAR,length = 128)
    private String url;

}
