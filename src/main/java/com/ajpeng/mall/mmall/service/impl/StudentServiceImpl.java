package com.ajpeng.mall.mmall.service.impl;

import com.ajpeng.mall.mmall.dao.StudentDao;
import com.ajpeng.mall.mmall.entity.Student;
import com.ajpeng.mall.mmall.service.StudentService;

import javax.annotation.Resource;

public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentDao studentDao;

    @Override
    public void add(Student student) {
        studentDao.save(student);
    }
}
