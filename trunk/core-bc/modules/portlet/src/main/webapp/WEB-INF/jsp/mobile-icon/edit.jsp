<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<link href="${pageContext.request.contextPath}/css/style.css" type="text/css" rel="stylesheet"/>

<html>
<body>

<portlet:actionURL var="cancelActionUrl" portletMode="EDIT">
    <portlet:param name="action" value="cancel"/>
</portlet:actionURL>
<portlet:actionURL var="saveActionUrl" portletMode="EDIT">
    <portlet:param name="action" value="save"/>
</portlet:actionURL>

<div style="max-width: 400px; border: 1px solid #D1D6DC; border-radius: 5px;">
    <fieldset>
        <legend>Konfiguration</legend>

        <form:form method="POST" id="editForm" action="${saveActionUrl}" commandName="prefs">
            <table id="editMobileIconTable" style="width: 100%;">
                <tr class="even-row">
                    <td><b>Titel:</b></td>
                    <td>
                        <form:input path="title"/>
                    </td>
                </tr>
                <tr class="odd-row">
                    <td><b>Stilklass</b></td>
                    <td>
                        <form:select path="iconStyle">
                            <form:option value="">- Ingen stilklass vald -</form:option>
                            <form:options items="${allIconStyles}"/>
                        </form:select>
                    </td>
                </tr>
                <tr class="even-row">
                    <td>
                        <form:radiobutton path="target" value="url"/>
                        <b>Mål-URL:</b></td>
                    <td>
                        <form:textarea rows="4" cols="40" path="targetUrl" htmlEscape="true"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <form:radiobutton path="target" value="widgetUrl"/>
                        <b>Widget-URL:</b></td>
                    <td>
                        <form:textarea rows="4" cols="40" path="widgetUrl" htmlEscape="true"/>
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
