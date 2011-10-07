<%@page session="false" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<portlet:renderURL var="add" portletMode="VIEW">
    <portlet:param name="action" value="edit"/>
</portlet:renderURL>
<portlet:actionURL var="saveMobilePage" name="saveMobilePage" portletMode="VIEW"/>
<portlet:actionURL var="saveMobileArticle" name="saveMobileArticle" portletMode="VIEW"/>

<div class="moblie-settings">
    <c:if test="${removeAction ne null}">
        <div class="portlet-msg-success">Company ExpandoValue [${removeAction}] har tagits bort.</div>
    </c:if>
    <c:if test="${saveAction ne null}">
        <div class="portlet-msg-success">Ändringarna på Company ExpandoValue [${saveAction}] har sparats.</div>
    </c:if>

    <fieldset>
        <legend>Mobila stil-klasser</legend>
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
    <br/>
    <c:if test="${saveActionPage ne null}">
        <div class="portlet-msg-success">Sida har ändrats [${saveActionPage}].</div>
    </c:if>
    <c:if test="${saveActionPageFailed ne null}">
        <div class="portlet-msg-error">Ändringen av sida kunde inte sparats [${saveActionPageFailed}].</div>
    </c:if>
    <fieldset>
        <legend>Mobil sidor</legend>
        <table class="lfr-table moblie-settings-table">
            <tr class="moblie-settings-head">
                <th width="100">Nyckel</th>
                <th width="100">URL</th>
                <th>Vald startsida</th>
            </tr>
            <form:form method="POST" action="${saveMobilePage}" commandName="startPage">
                <form:hidden path="expandoKey"/>
                <tr>
                    <td>${startPage.expandoKey}</td>
                    <td>${startPage.friendlyUrl}</td>
                    <td>
                        <form:select path="layoutId" onchange="this.form.submit();">
                            <form:option value="">-- Ingen sida vald --</form:option>
                            <form:options items="${topPages}"/>
                        </form:select>
                    </td>
                </tr>
            </form:form>
            <form:form method="POST" action="${saveMobilePage}" commandName="loginPage">
                <form:hidden path="expandoKey"/>
                <tr>
                    <td>${loginPage.expandoKey}</td>
                    <td>${loginPage.friendlyUrl}</td>
                    <td>
                        <form:select path="layoutId" onchange="this.form.submit();">
                            <form:option value="">-- Ingen sida vald --</form:option>
                            <form:options items="${publicPages}"/>
                        </form:select>
                    </td>
                </tr>
            </form:form>
        </table>
    </fieldset>
    <br/>
    <fieldset>
        <legend>Mobil-tema artiklar</legend>
        <table class="lfr-table moblie-settings-table">
            <tr class="moblie-settings-head">
                <th width="100">Flik</th>
                <th width="100">Nyckel</th>
                <th width="50">ArtikelId</th>
                <th width="50">Version</th>
                <th width="100">ArtikelNamn</th>
                <th width="100">Structure</th>
                <th width="100">Template</th>
                <th></th>
                <th></th>
            </tr>
            <tr>
                <form:form method="POST" action="${saveMobileArticle}" commandName="workArticle">
                    <form:hidden path="expandoKey"/>
                    <td>Koll på jobbet</td>
                    <td>${workArticle.expandoKey}</td>
                    <td><form:input path="articleId"/></td>
                    <td>${workArticle.version}</td>
                    <td>${workArticle.articleName}</td>
                    <td>${workArticle.structureName}</td>
                    <td>${workArticle.templateName}</td>
                    <td><input type="submit" value="Spara"/></td>
                </form:form>
            </tr>
            <tr>
                <form:form method="POST" action="${saveMobileArticle}" commandName="searchArticle">
                    <form:hidden path="expandoKey"/>
                    <td>Sök</td>
                    <td>${searchArticle.expandoKey}</td>
                    <td><form:input path="articleId"/></td>
                    <td>${searchArticle.version}</td>
                    <td>${searchArticle.articleName}</td>
                    <td>${searchArticle.structureName}</td>
                    <td>${searchArticle.templateName}</td>
                    <td><input type="submit" value="Spara"/></td>
                </form:form>
            </tr>
            <tr>
                <form:form method="POST" action="${saveMobileArticle}" commandName="userArticle">
                    <form:hidden path="expandoKey"/>
                    <td>Användare</td>
                    <td>${userArticle.expandoKey}</td>
                    <td><form:input path="articleId"/></td>
                    <td>${userArticle.version}</td>
                    <td>${userArticle.articleName}</td>
                    <td>${userArticle.structureName}</td>
                    <td>${userArticle.templateName}</td>
                    <td><input type="submit" value="Spara"/></td>
                </form:form>
            </tr>
        </table>
    </fieldset>
</div>