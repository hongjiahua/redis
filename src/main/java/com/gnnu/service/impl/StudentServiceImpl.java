package com.gnnu.service.impl;

import com.gnnu.dao.StudentDao;
import com.gnnu.entity.Student;
import com.gnnu.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lzh
 * create 2019-09-18-22:33
 */
@Service
public class StudentServiceImpl implements StudentService, Serializable {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Student> queryAll() {
        return studentDao.queryAll();
    }

    /**
     * 获取用户策略：先从缓存中获取用户，没有则取数据表中 数据，再将数据写入缓存
     */
    @Override
    public Student findUserById(String sname) {
        String key = "user_" + sname;

        ValueOperations<String, Student> operations = redisTemplate.opsForValue();

        //判断redis中是否有键为key的缓存
        boolean hasKey = redisTemplate.hasKey(key);

        if (hasKey) {
            Student student = operations.get(key);
            System.out.println("从缓存中获得数据：" + student.getSname());
            System.out.println("------------------------------------");
            return student;
        } else {
            Student student = studentDao.findStudentByName(sname);
            System.out.println("查询数据库获得数据：" + student.getSname());
            System.out.println("------------------------------------");

            // 写入缓存
            operations.set(key, student, 5, TimeUnit.HOURS);
            return student;
        }
    }

    /**
     * 更新用户策略：先更新数据表，成功之后，删除原来的缓存，再更新缓存
     */
    @Override
    public int updateUser(Student student) {
        ValueOperations<String, Student> operations = redisTemplate.opsForValue();
        int result = studentDao.updateStudent(student);
        if (result != 0) {
            String key = "user_" + student.getId();
            boolean haskey = redisTemplate.hasKey(key);
            if (haskey) {
                redisTemplate.delete(key);
                System.out.println("删除缓存中的key-----------> " + key);
            }
            // 再将更新后的数据加入缓存
            Student userNew = studentDao.findStudentByName(student.getSname());
            if (userNew != null) {
                operations.set(key, userNew, 3, TimeUnit.HOURS);
            }
        }
        return result;
    }

    /**
     * 删除用户策略：删除数据表中数据，然后删除缓存
     */
    @Override
    public int deleteUserById(String sname) {
        int result = studentDao.deleteStudentByName(sname);
        String key = "studemt_" + sname;
        if (result != 0) {
            boolean hasKey = redisTemplate.hasKey(key);
            if (hasKey) {
                redisTemplate.delete(key);
                System.out.println("删除了缓存中的key:" + key);
            }
        }
        return result;
    }

}
