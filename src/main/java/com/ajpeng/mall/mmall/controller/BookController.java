package com.ajpeng.mall.mmall.controller;

import com.ajpeng.mall.mmall.dao.BookDao;
import com.ajpeng.mall.mmall.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookDao bookDao;

    @Value("${server.port}")
    private String port;

    @RequestMapping(value = "/list")
    public ModelAndView list(HttpSession session,
                             @RequestParam(value = "start", defaultValue = "0") Integer start,
                             @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        ModelAndView mav = new ModelAndView();
        start = start < 0 ? 0 : start;
        Sort sort = Sort.by("createTime");
        Pageable pageable = PageRequest.of(start, limit, sort);  //分页信息
        Page<Book> all = bookDao.findAll(pageable);
        mav.addObject("bookList", all);
        mav.addObject("port", port);
        mav.addObject("name", session.getAttribute("name"));
        mav.addObject("start", start);
        mav.addObject("limit", limit);
        mav.addObject("totalPages", all.getTotalPages());
        mav.setViewName("book/bookList");
        log.info("查询成功");
        return mav;
    }

    /**
     * 添加图书
     *
     * @param book
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(HttpSession session, Book book) {
        session.setAttribute("name", book.getBookName());
        bookDao.save(book);
        log.info("新增成功");
        return "redirect:/book/list";
    }

    @GetMapping(value = "/preUpdate/{id}")
    public ModelAndView preUpdate(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("book", bookDao.getOne(id));
        mav.setViewName("book/bookUpdate");
        log.info("修改成功,参数:" + id);
        return mav;
    }

    /**
     * 修改图书
     *
     * @param book
     * @return
     */
    @PostMapping(value = "/update")
    public String update(Book book) {
        log.info("修改,参数:" + book.toString());
        bookDao.save(book);
        log.info("修改成功,参数:" + book.toString());
        return "redirect:/book/list";
    }

    /**
     * 删除图书
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(Integer id) {
        bookDao.deleteById(id);
        log.info("删除成功,参数:" + id);
        return "redirect:/book/list";
    }

}
