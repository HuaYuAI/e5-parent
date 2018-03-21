<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>商品描述</title>
  </head>
  
  <body>
  	<table  cellspacing="0" width="60%" border="1"  class="Ptable">
     <tbody>
     <c:forEach items="${jsonObj}" var="item">
          <tr>
               <th class="tdTitle" colspan="2">${item.group }</th>
          </tr>
          <c:forEach items="${item.params}" var="pa">
          <tr>
               <td>${pa.k }</td>
               <td>${pa.v }</td>
          </tr>
          </c:forEach>
      </c:forEach>
     </tbody>
</table>
  	
    ${desc.itemDesc}
  </body>
</html>
