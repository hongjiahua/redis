package com.gnnu.service;

import com.gnnu.entity.Student;

import java.util.List;

public interface StudentService {
    public Student findUserById(String sname);

    public int updateUser(Student student);

    public int deleteUserById(String sname);

    public List<Student> queryAll();
}
