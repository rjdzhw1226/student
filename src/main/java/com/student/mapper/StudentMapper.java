package com.student.mapper;

import com.student.pojo.student;
import com.student.pojo.studentTest;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {
    @Select("select * from student where 1=1")
    public List<student> queryAll();

    @Select("select count(1) from student where 1=1")
    public int queryCount();

    @Select("select  * from student where 1=1 limit #{page},#{size}")
    public List<student> queryAllLimit(Map<String, Object> map);
    @Select("select * from student where id = #{id}")
    public student queryById(String id);

    @Update("update student set station=#{station} where id=#{id}")
    public int update(String station, String id);

    @Insert("insert into student (id, name, grade, grade_class, phone, age, gender, station,url) values (#{id},#{name},#{grade},#{grade_class},#{phone},#{age},#{gender},#{station},#{url})")
    public int add(student student);

    @Update("update student set id=#{studentId},name=#{name},grade=#{grade},grade_class=#{grade_class},phone=#{phone},age=#{age},gender=#{gender},url=#{url},station=#{station} where id=#{idEdit}")
    public int edit(String studentId,String name,String grade,String grade_class,String phone, String age, String gender,String station,String url, String idEdit);

    @Select("select  * from student where name like concat('%',#{queryStr},'%') or id like concat('%',#{queryStr},'%') or phone like concat('%',#{queryStr},'%') limit #{page},#{size}")
    public List<student> queryLike(String queryStr, Integer page, Integer size);
    @Select("select count(1) from student where name like concat('%',#{queryStr},'%') or id like concat('%',#{queryStr},'%') or phone like concat('%',#{queryStr},'%')")
    public int queryCountLike(String queryStr);

    @Delete("delete from student where id = #{id}")
    public int deleteId(String id);

    @Delete("delete from student where id in #{ids};")
    public int deleteIds(String ids);

    @Select("select  * from student where grade = #{grade} and grade_class = #{gradeClass}")
    public List<student> queryByGradeClass(String grade, String gradeClass);

    @Select("select  * from student where grade = #{grade}")
    public List<student> queryStudentBetweenGrade(String grade);


    @Insert("<script>"+"INSERT INTO student(id, name, grade, grade_class, phone, age, gender, station,url)\n" +
            "        VALUES\n" +
            "        <foreach collection=\"list\"  item=\"tag\" separator=\",\" index=\"index\">\n" +
            "            (#{tag.id},#{tag.name},#{tag.grade},#{tag.grade_class},#{tag.phone},#{tag.age},#{tag.gender},#{tag.station},#{tag.url})\n" +
            "        </foreach>"+"</script>")
    public void insertArticleTag(List<student> list);

    @Insert("<script>"+"INSERT INTO studenttest(id, name, grade, grade_class, phone, age, gender, station,url)\n" +
            "        VALUES\n" +
            "        <foreach collection=\"list\"  item=\"tag\" separator=\",\" index=\"index\">\n" +
            "            (#{tag.id},#{tag.name},#{tag.grade},#{tag.grade_class},#{tag.phone},#{tag.age},#{tag.gender},#{tag.station},#{tag.url})\n" +
            "        </foreach>"+"</script>")
    public void insertArticleTagTest(List<studentTest> list);
    @Insert("<script>"+"LOAD DATA LOCAL INFILE #{path,jdbcType=VARCHAR} INTO TABLE student CHARACTER SET utf8\n" +
            "        FIELDS TERMINATED BY ','\n" +
            "        LINES TERMINATED BY '\\n'"+"</script>")
    public void load_file(@Param("path") String path);

    @Select("select * from student where name = #{name}")
    public student queryByName(String name);
    @Select("select name from student where 1=1")
    public List<String> queryName();

}
