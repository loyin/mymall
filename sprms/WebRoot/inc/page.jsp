<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="panelBar">
	<div class="pages">
		<span>显示</span>
		<select name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
			<option value="${page.pageSize }" selected="selected">${page.pageSize }</option>
			<option value="10">10</option>
			<option value="20">20</option>
			<option value="50">50</option>
			<option value="100">100</option>
		</select>
		<span>条，共${page.totalCount}条/${page.totalPageCount}页&nbsp;${licheng}</span>
	</div>
	<div class="pagination" targetType="navTab" totalCount="${page.totalCount}" numPerPage="${page.pageSize}" 
	pageNumShown="10" currentPage="${page.pageNo}"></div>
</div>