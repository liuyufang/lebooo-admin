<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="page" type="org.springframework.data.domain.Page" required="true"%>
<%@ attribute name="paginationSize" type="java.lang.Integer" required="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
int current =  page.getNumber() + 1;
int begin = Math.max(1, current - paginationSize/2);
int end = Math.min(begin + (paginationSize - 1), page.getTotalPages());

request.setAttribute("current", current);
request.setAttribute("begin", begin);
request.setAttribute("end", end);
%>

<div class="pagination">
		 <% if (page.hasPreviousPage()){%>
                <a class="pageup" href="?page=${current-1}&sortType=${sortType}&${searchParams}">上一页</a>
         <%}else{%>
                <a class="pageup disabled" href="#">上一页</a>
         <%} %>
 
        <a class="active" href="?page=${current}&sortType=${sortType}&${searchParams}">${current}</a>
        /
        <% if (current == end) { %>
            <a class="active" href="?page=${end}&sortType=${sortType}&${searchParams}">${end}</a>
        <% } else { %>
            <a href="?page=${end}&sortType=${sortType}&${searchParams}">${end}</a>
        <% } %>


	  	 <% if (page.hasNextPage()){%>
               	<a class="pagedown" href="?page=${current+1}&sortType=${sortType}&${searchParams}">下一页</a>
         <%}else{%>
                <a class="pagedown disabled" href="#">下一页</a>
         <%} %>

</div>

