package com.ajpeng.mall.mmall.dao;

import com.ajpeng.mall.mmall.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDao extends JpaRepository<Student, Integer> {


}
