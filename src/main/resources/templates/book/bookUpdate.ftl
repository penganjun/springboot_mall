<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>图书更新页面</title>
</head>
<body>
<form action="/book/update" method="post">
    <input type="hidden" name="id" th:value="*{book.id}"/>
    图书名称：<input type="text" name="bookName" th:value="*{book.bookName}"/><br/>
    <input type="submit" value="提交"/>
</form>
</body>
</html>