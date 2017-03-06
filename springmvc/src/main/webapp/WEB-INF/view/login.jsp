<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/2/17
  Time: 10:42
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<table>
    <tr>
        <td>id</td>
        <td><input id="id" value="${id}"/></td>
    </tr>
    <tr>
        <td>name</td>
        <td><input id="name" value="${name}"/></td>
    </tr>
    <tr>
        <td>status</td>
        <td><input id="status" value="true"/></td>
    </tr>
    <tr>
        <td><input type="button" id="profile" value="Profile——GET"/></td>
        <td><input type="button" id="login" value="Login——POST"/></td>
    </tr>
</table>

</body>
<script src="<%=request.getContextPath()%>/static/js/jquery-1.9.1.js"></script>
<script type="text/javascript">

    $(document).ready(function () {

        $("#profile").click(function () {
            profile();
        });
        $("#login").click(function () {
            login1();
        });
    });
    function profile() {
        var url = 'http://localhost:8090/spring-json/json/person/profile/';
        var query = $('#id').val() + '/' + $('#name').val() + '/'
            + $('#status').val();
        url += query;
        alert(url);
        $.get(url, function (data) {
            alert("id: " + data.id + "\nname: " + data.name + "\nstatus: "
                + data.status);
        });
    }
    function login() {
        var mydata = '{"name":"' + $('#name').val() + '","id":"'
            + $('#id').val() + '","status":"' + $('#status').val() + '"}';
        alert(mydata);
        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/Account/login?id=1&name=打啊',
            processData: false,
            dataType: 'json',
            data: mydata,
            success: function (data) {
                alert("id: " + data.id + "\nname: " + data.name + "\nstatus: "
                    + data.status);
            },
            error: function () {
                alert('Err...');
            }
        });
    }
    function login1() {
        var mydata = '{"name":"' + $('#name').val() + '","id":"'
            + $('#id').val() + '","status":"' + $('#status').val() + '"}';
        alert(mydata);
        $.ajaxSetup({
            contentType : 'application/json'
        });
        $.post('/Account/login', mydata,
            function(data) {
                alert("id: " + data.id + "\nname: " + data.name
                    + "\nstatus: " + data.status);
            }, 'json');
    };

</script>
</html>
