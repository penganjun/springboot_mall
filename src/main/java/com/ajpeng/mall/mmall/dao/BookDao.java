package com.ajpeng.mall.mmall.dao;


import com.ajpeng.mall.mmall.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDao extends JpaRepository<Book, Integer> {
}
