<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@include file="/inc/taglibs.jsp" %>
<c:if test="${!empty page.data}">
<c:set var="crType" value="${! empty crType?crType:'radio' }"/>
<c:set value="${(pageNo-1)*pageSize}" var="startNo"></c:set>
<table cellpadding="0" cellspacing="0" class="table" layouth="138">
<thead>
<tr>
<th width="50px">序号</th>
<c:forEach items="${clunmList}" var="item" begin="1">
<th>${item }</th></c:forEach>
</tr>
</thead>
<tbody>
<c:forEach items="${page.data}" var="item" varStatus="status">
<tr rel="${item[0] }"target="sid_key">
<td>
<%--<input type="${crType}" name="valueList" value="${item[0] }"/>--%>
${status.index+1+startNo }</td>
<c:forEach items="${item}" var="item1" begin="1"><td>${item1 }</td></c:forEach>
</tr>
</c:forEach>
</tbody>
</table>
</c:if>
<c:if test="${empty page.data}">
<center>
<table cellpadding="0" cellspacing="0"><tr><td><b><h4>未查询到相关数据，请重新选择查询条件！</h4></b></td></tr></table></center>
</c:if>