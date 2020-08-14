package com.ajpeng.mall.mmall.dao;


import com.ajpeng.mall.mmall.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface BookDao extends JpaRepository<Book, Integer>, Serializable {
}
