<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
    <fieldset>
        <legend>Konfiguration</legend>

        <form:form method="POST" id="<portlet:namespace/>editForm" action="${saveActionUrl}" commandName="prefs">
            <table style="width: 100%;">
                <tr class="even-row">
                    <td><b>Titel:</b></td>
                    <td>
                        <form:input path="title"/>
                    </td>
                </tr>
                <tr class="odd-row">
                    <td><b>Icon Style</b></td>
                    <td>
                        <form:select path="iconStyle">
                            <form:option value="">- Ingen style vald -</form:option>
                            <form:options items="${allIconStyles}"/>
                        </form:select>
                    </td>
                </tr>
                <tr class="odd-row">
                    <td><b>Ikon:</b></td>
                    <td>
                        <form:hidden path="imageId"/>
                        <a id="editIconAnchor" title="Ändra" href="${editRenderUrl}&action=editIconUrl">
                            <c:choose>
                                <c:when test="${not empty prefs.imageId}">
                                    <img src="/image/image_gallery?img_id=${prefs.imageId}"
                                         style="max-height: 60px; max-width: 120px"/>
                                </c:when>
                                <c:otherwise>
                                    <img src="/vgr-theme/images/dockbar/settings.png"/>
                                </c:otherwise>
                            </c:choose>
                        </a>
                        <script type="text/javascript">
                            //This script snippet makes the surrounding form submit, meaning that we can save all input
                            //values to when we come back from "editIconUrl". If javascript is enabled we can still edit
                            //the icon url but then we will lose unsaved input values.
                            function editIcon() {
                                var form = document.getElementById("<portlet:namespace/>editForm");
                                form.action = "${editIconUrl}";
                                form.submit();

                                return false;
                            }

                            var anchorTag = document.getElementById("editIconAnchor");
                            anchorTag.onclick = editIcon;
                        </script>

                    </td>
                </tr>
                <tr class="even-row">
                    <td>
                        <form:radiobutton path="target" value="url"/>
                        <b>Mål-URL:</b></td>
                    <td>
                        <form:input path="targetUrl"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <form:radiobutton path="target" value="widget"/>
                        <b>Widget-skript:</b></td>
                    <td>
                        <form:textarea rows="8" cols="40" path="widgetScript" htmlEscape="true"/>
                    </td>
                </tr>
                <tr class="odd-row">
                    <td><b>Räknare:</b></td>
                    <td>
                        <form:select path="counterService">
                            <form:option value="">- Ingen räknare vald -</form:option>
                            <form:options items="${allCounterServices}"/>
                        </form:select>
                    </td>
                </tr>
                <tr class="even-row">
                    <td><b>Uppdateringsintervall<br/> räknare (s):</b></td>
                    <td>
                        <form:input path="updateInterval"/>
                    </td>
                </tr>
            </table>

            <div style="padding: 7px; ">
                <input type="submit" name="submitInput" value="Spara"/>
                <a class="buttonlink" href="${cancelActionUrl}">Avbryt</a>
            </div>
        </form:form>
    </fieldset>
</div>

</body>
</html>
