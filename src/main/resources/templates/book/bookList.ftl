<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>图书管理页面</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <script src="http://cdn.bootcss.com/bootstrap/2.3.1/js/bootstrap-alert.min.js"></script>
</head>
<body>
<h1>port:<span th:text="${port}"></span></h1>
<a href="/mmall/book/bookAdd.html">添加图书</a>
<div class="table-responsive">
    <table class="table">
        <tr>
            <th>序号</th>
            <th>编号</th>
            <th>图书名称</th>
            <th>创建时间</th>
            <th>操作</th>
        </tr>
        <tr th:each="book: ${bookList}">
            <td th:text="${bookStat.index+1}"></td>
            <td th:text="${book.id}"></td>
            <td th:text="${book.bookName}"></td>
            <td th:text="${book.createTime}"></td>
            <td>
                <a class="btn btn-info btn-xs" role="button" th:href="@{../book/preUpdate/{id}(id=${book.id})}">
                    <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                    修改</a>
                <a class="btn btn-danger btn-xs" role="button" th:href="@{../book/delete?id={id}(id=${book.id})}">
                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                    删除</a>
            </td>
        </tr>
        <tr align="right">
            <td rowspan="5">
                <!-- th:href="@{../book/list(start=${start-1},limit=10)}"
th:href="@{../book/list(start=${start+1}>${totalPages-1}?${totalPages-1}:${start+1},limit=10)}"

                <a class="btn btn-default" role="button"
                   th:onclick="'javascript:previousPage(\''+${start-1}+'\',\''+${limit}+'\')'">上一页</a>
                <span th:text="${start+1}>${totalPages}?${totalPages}:${start+1}"></span>/<span
                        th:text="${totalPages}"></span>
                <a class="btn btn-default" role="button"
                   th:onclick="'javascript:nextPage(\''+${start+1}+'\',\''+${limit}+'\',\''+${totalPages}+'\')'">下一页</a>
-->
                <nav aria-label="Page navigation">
                    <ul class="pagination">
                        <li>
                            <a href="#" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                        <li th:each="count:${#numbers.sequence(1,totalPages)}"><a
                                    th:href="@{../book/list(start=${count-1},limit=${limit})}"
                                    th:text="${count}">1</a>
                        </li>
                        <li>
                            <a href="#" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </td>
        </tr>
    </table>
</div>
<!--模态框组件-->
<div class="modal fade" id="myModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3>提示</h3>
            </div>
            <div class="modal-body">
                <p>已是第一页</p>
            </div>
            <div class="modal-footer">
                <button class="btn btn-info" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<script>
    function previousPage(start, limit) {
        if (start < 0) {
            $(".modal-body").find("p").text("已是第一页");
            $("#myModal").modal("show");
            return;
        }
        location.href = "../book/list?limit=" + limit + "&start=" + start;
    }

    function nextPage(start, limit, totalPages) {
        if (start >= totalPages) {
            $(".modal-body").find("p").text("已是最后一页");
            $("#myModal").modal("show");
            return;
        }
        location.href = "../book/list?limit=" + limit + "&start=" + start;
    }
</script>
</body>
</html>