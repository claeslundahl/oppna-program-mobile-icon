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

    .mobile-icon {
        height: 150px;
        width: 150px;
    }
    #overlay {
        width: 40px;
        height: 40px;
        position: relative;
        left: 120px;
        top: -60px;
        z-index: 0;
    }

    .stretch {
        width: 100%;
        height: 100%;
    }

    #quick-message {
        height: 100%;
        left: 0px;
        line-height: 40px;
        position: absolute;
        text-align: center;
        width: 100%;
        border: solid 1px;
        border-radius: 20px 20px 20px 20px;
        -moz-border-radius: 20px;
        -webkit-border-radius: 20px;
        background-color: #ffffff;
    }

    .title-text {
        text-decoration: none;
        width:150px;
        text-align: center;
    }

</style>

<portlet:actionURL var="showWidget">
    <portlet:param name="action" value="showWidget"/>
</portlet:actionURL>

<div style="text-align: left;">

    <c:choose>
        <c:when test="${target eq 'url'}">
            <a href="${targetUrl}" target="_blank" style="text-decoration: none; ">
                <c:choose>
                    <c:when test="${not empty imageId}">
                        <img class="mobile-icon" src="/image/image_gallery?img_id=${imageId}"/>
                    </c:when>
                    <c:otherwise>
                        <img class="mobile-icon" src="${pageContext.request.contextPath}/images/not_found.jpg"/>
                    </c:otherwise>
                </c:choose>
                <div class="title-text" >${title}</div>
            </a>
        </c:when>
        <c:otherwise>
            <a href="${showWidget}" style="text-decoration: none; ">
                <c:choose>
                    <c:when test="${not empty imageId}">
                        <img class="mobile-icon" src="/image/image_gallery?img_id=${imageId}"/>
                    </c:when>
                    <c:otherwise>
                        <img class="mobile-icon" src="${pageContext.request.contextPath}/images/not_found.jpg"/>
                    </c:otherwise>
                </c:choose>
                <div class="title-text">${title}</div>
            </a>
        </c:otherwise>
    </c:choose>
</div>
<div id="overlay">
    <span id="quick-message">15</span>
</div>