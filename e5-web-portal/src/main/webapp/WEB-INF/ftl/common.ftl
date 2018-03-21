<table border="1" cellspacing="0" cellpadding="1">
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>出生日期</td>
    </tr>
<#list userList as user>
    <#if user_index%2==0>
    <tr bgcolor="#a9a9a9" >
    <#else>
    <tr>
    </#if>
    <td>${user.id}</td>
    <td>${user.name!"无此用户"}</td>
    <td>${user.birthday?string("yyyy-MM-dd HH:mm:ss")}</td>
</tr>
</#list>
</table>