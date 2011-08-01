<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<body>

<portlet:renderURL var="renderUrl"/>
<portlet:actionURL var="actionUrl"/>

<table>
    <tr>
        <c:forEach items="${imageIds}" var="imageId" varStatus="status">
        <td>
            <a title="Ändra" href="<%= actionUrl %>&action=submitIconUrl&imageId=${imageId}">
                <img class="imglink" src="/image/image_gallery?img_id=${imageId}"/>
            </a>
        </td>
        <c:if test="${status.count % 4 == 0}">
        <%-- NEW ROW --%>
    </tr>
    <tr>
        </c:if>
        </c:forEach>
    </tr>
</table>
</body>
</html>
