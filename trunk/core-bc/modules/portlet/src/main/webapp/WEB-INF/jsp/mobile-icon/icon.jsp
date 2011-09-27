<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<portlet:actionURL var="showWidget">
    <portlet:param name="action" value="showWidget"/>
</portlet:actionURL>
<portlet:resourceURL var="fetchCount" />

<c:set var="appCssClass" value="" scope="page" />

<c:choose>
	<c:when test="${title eq 'Nyheter'}">
		<c:set var="appCssClass" value="app-news" scope="page" />
	</c:when>
	<c:when test="${title eq 'Insidan'}">
		<c:set var="appCssClass" value="app-intranet" scope="page" />
	</c:when>
	<c:when test="${title eq 'Epost'}">
		<c:set var="appCssClass" value="app-mail" scope="page" />
	</c:when>
	<c:when test="${title eq 'Uppgifter'}">
		<c:set var="appCssClass" value="app-todos" scope="page" />
	</c:when>
	<c:when test="${title eq 'Dokument'}">
		<c:set var="appCssClass" value="app-documents" scope="page" />
	</c:when>
	<c:when test="${title eq 'Kontakter'}">
		<c:set var="appCssClass" value="app-contacts" scope="page" />
	</c:when>
	<c:when test="${title eq 'Kalender'}">
		<c:set var="appCssClass" value="app-calendar" scope="page" />
	</c:when>
	<c:when test="${title eq 'Väntetider'}">
		<c:set var="appCssClass" value="app-waiting-times" scope="page" />
	</c:when>
	<c:when test="${title eq 'Filmer'}">
		<c:set var="appCssClass" value="app-movies" scope="page" />
	</c:when>
</c:choose>

<div class="${appCssClass}">

	<c:set var="linkURL" value="${showWidget}" scope="page" />
	<c:set var="targetVal" value="" scope="page" />
	<c:if test="${target eq 'url'}">
		<c:set var="linkURL" value="${targetUrl}" scope="page" />
		<c:set var="targetVal" value="_BLANK" scope="page" />
	</c:if>
	
	<a href="${linkURL}" target="${targetVal}" class="app-link">
	    <h1>
	    	<c:set var="countCssClass" value="" scope="page" />
	    	<c:if test="${count eq '-'}">
	    		<c:set var="countCssClass" value="aui-helper-hidden" scope="page" />
	    	</c:if>
			<span class="app-title">${title}</span> <c:if test="${not empty count}"><span id="<portlet:namespace/>quick-message" class="count ${countCssClass}"><span>${count}</span></span></c:if>
	   	</h1>
	</a>
</div>

<script type="text/javascript">
	AUI().ready('aui-base', 'aui-io-request', function(A) {
		var updateTimer, updateIO;
		
		var countNode = A.one('#<portlet:namespace/>quick-message');
		var updateURL = '${fetchCount}';
		var updateInterval = ${updateInterval * 1000};
		
		
		var onUpdateSuccess = function(e, id, xhr) {
			var responseValue = xhr.responseText;
			
			//countNode
	        if (responseValue == null || responseValue == '') {
	        	countNode.hide();
	        } else {
	        	countNode.show();
	        	
	        	var countNodeInner = countNode.one('span');
	        	
	            responseValue = responseValue.toString().substring(0, 6);
	            countNodeInner.html(responseValue);
	        }
		}
		
		updateIO = A.io.request(updateURL, {
			autoLoad : false,
			cache: false,
			dataType: 'json',
			method : 'GET'
		});
		
		// Attach success handler to io request
	    updateIO.on('success', onUpdateSuccess);
		
		// Function that performs an update
		var updateCounter = function() {
			updateIO.stop();
			updateIO.start();
		}
		
		updateTimer = A.later(updateInterval, countNode, updateCounter);
	});
</script>