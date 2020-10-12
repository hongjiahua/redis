package com.gnnu.dao;

import com.gnnu.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author lzh
 * create 2019-09-18-22:32
 */
@Mapper
public interface StudentDao {

    @Select("select * from student")
    List<Student> queryAll();

    @Select("select * from student where sname = #{sname}")
    Student findStudentByName(String sname);

    @Update("UPDATE student SET sname = CASE WHEN (#{snum} != NULL) AND (#{snum} != '') THEN #{snum},score = CASE WHEN (#{score} != NULL) AND (#{score} != '') THEN #{score} WHERE sname = #{sname}")
    int updateStudent(Student student);

    @Delete("delete from student where sname = #{sname}")
    int deleteStudentByName(String sname);

}
