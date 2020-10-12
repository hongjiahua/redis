package com.gnnu;

import com.gnnu.entity.Student;
import com.gnnu.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class StudentController {
    @Autowired
    private StudentService studentService;

    @RequestMapping("/getall")
    @ResponseBody
    public List<Student> getall() {
        return studentService.queryAll();

    }

    @RequestMapping("/getuser")//获取用户
    @ResponseBody
    public Student getuser() {
        return studentService.findUserById("hjh");

    }
}
