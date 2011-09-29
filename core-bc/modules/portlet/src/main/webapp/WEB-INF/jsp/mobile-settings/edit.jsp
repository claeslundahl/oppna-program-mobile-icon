<%@page session="false" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<portlet:actionURL var="saveMobileIconStyle" name="saveMobileIconStyle" portletMode="VIEW"/>
<portlet:renderURL var="abort" portletMode="VIEW"/>

<div class="moblie-settings">
    <fieldset>
        <legend>
            <c:choose>
                <c:when test="${currentMobileIconStyle.key eq prefix}">
                    Skapa ny MobileIconStyle
                </c:when>
                <c:otherwise>
                    Ändra ${currentMobileIconStyle.key}
                </c:otherwise>
            </c:choose>
        </legend>

        <form:form method="POST" action="${saveMobileIconStyle}" commandName="currentMobileIconStyle">
            <table class="lfr-table">
                <tr>
                    <c:choose>
                        <c:when test="${currentMobileIconStyle.key eq prefix}">
                            <td><form:label path="key">Nyckel</form:label></td>
                            <td><form:input path="key" size="50"/> <form:errors path="key"
                                                                                cssClass="portlet-msg-error"/></td>
                        </c:when>
                        <c:otherwise>
                            <form:hidden path="key"/>
                            <td>Nyckel</td>
                            <td>${currentMobileIconStyle.key}</td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <td><form:label path="value">Mobil style class</form:label></td>
                    <td><form:input path="value" size="50"/> <form:errors path="value"
                                                                          cssClass="portlet-msg-error"/></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="buttons">
                            <input type="submit" value="Spara"/>
                                <%--<input type="button" value="Ta bort" onclick="confirmDelete('${deleteItSystem}');"/>--%>
                            <a href="${abort}"><input type="button" value="Avbryt"/></a>
                        </div>
                    </td>
                </tr>
            </table>
        </form:form>
    </fieldset>
</div>