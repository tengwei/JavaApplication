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
        <td><input id="id" value="${user.id}"/></td>
    </tr>
    <tr>
        <td>name</td>
        <td><input id="name" value="${user.name}"/></td>
    </tr>
    <tr>
        <td>status</td>
        <td><input id="status" value="true"/></td>
    </tr>

</table>
<fieldset>
    <legend>登录</legend>
    <form:form commandName="user">
        <form:hidden path="id"/>
        <ul>
            <li><form:label path="name">用户名:</form:label><form:input
                    path="name"/></li>

                <button type="submit">登录</button>
                <button type="reset">重置</button>
            </li>
        </ul>
    </form:form></fieldset>

</body>

</html>
