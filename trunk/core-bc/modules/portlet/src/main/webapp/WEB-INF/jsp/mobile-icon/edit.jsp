<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="${pageContext.request.contextPath}/css/style.css" type="text/css" rel="stylesheet"/>

<style type="text/css">
    table td {
        padding: 5px;
    }

    .odd-row {
        background-color: #D1D6DC;
    }

    .even-row {

    }
</style>

<html>
<body>

<portlet:renderURL var="editRenderUrl" portletMode="EDIT"/>
<portlet:actionURL var="cancelActionUrl" portletMode="EDIT">
    <portlet:param name="action" value="cancel"/>
</portlet:actionURL>
<portlet:actionURL var="saveActionUrl" portletMode="EDIT">
    <portlet:param name="action" value="save"/>
</portlet:actionURL>
<portlet:actionURL var="editIconUrl" portletMode="EDIT">
    <portlet:param name="action" value="editIconUrl"/>
</portlet:actionURL>

<div style="max-width: 400px; border: 1px solid #D1D6DC; border-radius: 5px;">
    <form id="<portlet:namespace/>editForm" action="${saveActionUrl}" method="post">

        <table style="width: 100%;">
            <tr class="even-row">
                <td><b>Titel:</b></td>
                <td>
                    <input type="text" name="title" value="${title}">
                </td>
            </tr>
            <tr class="odd-row">
                <td><b>Ikon:</b></td>
                <td>
                    <input type="hidden" name="imageId" value="${imageId}">
                    <a id="editIconAnchor" title="Ändra" href="${editRenderUrl}&action=editIconUrl">
                        <c:choose>
                            <c:when test="${not empty imageId}">
                                <img src="/image/image_gallery?img_id=${imageId}"
                                     style="max-height: 60px; max-width: 120px"/>
                            </c:when>
                            <c:otherwise>
                                <img src="/vgr-theme/images/dockbar/settings.png"/>
                            </c:otherwise>
                        </c:choose>
                    </a>
                    <script type="text/javascript">
                        function editIcon() {
                            console.log("editIcon");
                            var form = document.getElementById("<portlet:namespace/>editForm");
                            console.log(form);
                            form.action = "${editIconUrl}";
                            form.submit();

                            return false;
                        }

                        var anchorTag = document.getElementById("editIconAnchor");
                        console.log("anchorTag " + anchorTag);
                        anchorTag.onclick = editIcon;
                    </script>

                </td>
            </tr>
            <tr class="even-row">
                <table>
                    <tr>
                        <td><% System.out.println("target: " + request.getParameter("target") + " - " + request.getAttribute("target")); %>
                            <input type="radio" name="target" value="url" ${target eq 'url' ? "checked='checked'" : ''}>
                        </td>
                        <td><b>Mål-URL:</b></td>
                        <td>
                            <input type="text" name="targetUrl" value="${targetUrl}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="radio" name="target"
                                   value="widget" ${target eq 'widget' ? "checked='checked'" : ''}>
                        </td>
                        <td><b>Widget-skript:</b></td>
                        <td>
                            <textarea rows="8" cols="40" name="widgetScript"><c:out escapeXml="true"
                                                                                    value="${widgetScript}"></c:out></textarea>

                        </td>
                    </tr>
                </table>
            </tr>
            <tr class="even-row">
                <td><b>Räknare:</b></td>
                <td>
                    <select name="counterService">
                        <option label="- Ingen räknare existerar -" value=""/>
                        <c:forEach items="${allCounterServices}" var="counter">
                            <c:choose>
                                <c:when test="${counter eq counterService}">
                                    <option label="${counter}" value="${counter}" selected="selected" />
                                </c:when>
                                <c:otherwise>
                                    <option label="${counter}" value="${counter}" />
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </table>
        <div style="padding: 7px; ">
            <input type="submit" name="submitInput" value="Spara"/>
            <a class="buttonlink" href="${cancelActionUrl}">Avbryt</a>
        </div>
    </form>
</div>
</body>
</html>
