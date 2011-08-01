<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">
    A:link {
        text-decoration: none
    }

    A:visited {
        text-decoration: none;
        color: black;
    }

    A:active {
        text-decoration: none
    }

    A:hover {
        text-decoration: none;
    }
</style>

<portlet:actionURL var="showWidget">
    <portlet:param name="action" value="showWidget"/>
</portlet:actionURL>

<div style="text-align: center;">

    <c:choose>
        <c:when test="${target eq 'url'}">
            <a href="${targetUrl}" target="_blank" style="text-decoration: none; ">
                <c:choose>
                    <c:when test="${not empty imageId}">
                        <img src="/image/image_gallery?img_id=${imageId}" />
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/images/not_found.jpg" />
                    </c:otherwise>
                </c:choose>
                <div style="text-decoration: none">${title}</div>
            </a>
        </c:when>
        <c:otherwise>
            <a href="${showWidget}" style="text-decoration: none; ">
                <c:choose>
                    <c:when test="${not empty imageId}">
                        <img src="/image/image_gallery?img_id=${imageId}" />
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/images/not_found.jpg" />
                    </c:otherwise>
                </c:choose>
                <div style="text-decoration: none">${title}</div>
            </a>
        </c:otherwise>
    </c:choose>
</div>