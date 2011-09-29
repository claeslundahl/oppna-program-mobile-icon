<%@page session="false" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<portlet:renderURL var="add" portletMode="VIEW">
    <portlet:param name="action" value="edit"/>
</portlet:renderURL>

<div class="moblie-settings">
    <c:if test="${removeAction ne null}">
        <div class="portlet-msg-success">Company ExpandoValue [${removeAction}] har tagits bort.</div>
    </c:if>
    <c:if test="${saveAction ne null}">
        <div class="portlet-msg-success">Ändringarna på Company ExpandoValue [${saveAction}] har sparats.</div>
    </c:if>

    <fieldset>
        <legend>Mobile styles</legend>
        <table class="lfr-table moblie-settings-table">
            <tr class="moblie-settings-head">
                <th width="100">Nyckel</th>
                <th width="100">Style class</th>
                <th></th>
                <th></th>
            </tr>
            <c:forEach items="${mobileIconStyles}" var="mobileIconStyle">
                <portlet:renderURL var="edit" portletMode="VIEW">
                    <portlet:param name="action" value="edit"/>
                    <portlet:param name="expandoKey" value="${mobileIconStyle.key}"/>
                </portlet:renderURL>
                <portlet:actionURL var="delete" name="delete" portletMode="VIEW">
                    <portlet:param name="expandoKey" value="${mobileIconStyle.key}"/>
                </portlet:actionURL>
                <tr class="${(cnt.count % 2 == 0) ? 'cs-even' : 'cs-odd'} cs-row">
                    <td>${mobileIconStyle.key}</td>
                    <td>${mobileIconStyle.value}</td>
                    <td><a class="edit" href="${edit}">Ändra</a></td>
                    <td><a class="remove" href="${delete}">Ta bort</a></td>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="4">
                    <a class="add" title="Lägg till Mobile Style" href="${add}">Lägg till</a>
                </td>
            </tr>
        </table>
    </fieldset>
</div>