<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>图书管理页面</title>
</head>
<body>
<a href="/mmall/book/bookAdd.html">添加图书</a>
<table>
    <tr>
        <th>编号</th>
        <th>图书名称</th>
        <th>操作</th>
    </tr>
    <tr th:each="book: ${bookList}">
        <td th:text="${book.id}"></td>
        <td th:text="${book.bookName}"></td>
        <td>
            <a th:href="@{../book/preUpdate/{id}(id=${book.id})}">修改</a>
            <a th:href="@{../book/delete?id={id}(id=${book.id})}">删除</a>
        </td>
    </tr>
</table>
</body>
</html>