<html>
    <head>
        <meta charset="UTF-8">
        <title>测试freemark</title>
    </head>
   <body>
                     学生基本信息:<br/>
      id${student.id}<br/>
                     姓名${student.name}<br/>
                     年龄${student.age}<br/>
                     地址${student.address}<br/>
                    学生列表信息 <br/>
      <table border="1">
        <tr>
            <th>序号</th>
            <th>学号</th>
            <th>姓名</th>
            <th>年龄</th>
            <th>地址</th>
        </tr>
        <#list stuList as stu>
            <#if stu_index % 2==0>
                <tr bgcolor="red">
            <#else>
                <tr bgcolor="blude">
            </#if>
            
                <td>${stu_index}</td>
                <td>${stu.id}</td>
                <td>${stu.name}</td>
                <td>${stu.age}</td>
                <td>${stu.address}</td>
            </tr>
        </#list>
      </table><br/>
      显示日期：${date?string("yyyy-MM-dd HH:mm:ss")}<br/>
      null值的处理:${val!"默认值"}<br/>
      使用if判断null值
      <#if val??>
        val是有值的,值为${val}
      <#else>
        val为null
      </#if><br/>
      include标签测试
      <#include "hello.ftl">
              
   </body>
</html>