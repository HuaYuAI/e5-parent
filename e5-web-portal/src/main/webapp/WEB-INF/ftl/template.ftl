<html>
    <!-- html的注释-->
    <#-- freemark 的注释-->
    <head>
        <meta charset="UTF-8">
        <title>测试freemarker</title>
    </head>

    <body>
        <#-- 基本属性-->
        <#if username!='张三'>
          欢迎您，${username}
        </#if>

        <#-- 对象-->
        用户的id：${user.id}，用户的名称${user.name}
        <#-- assign指令：起名一个全局变量-->
        <#assign age=19>
        ${age}

        <#if age<20>
            小孩
            <#elseif (age>=20) && (age<50)>
               中年
            <#else>
              老年
        </#if>
        <#include "common.ftl">
    </body>
</html>