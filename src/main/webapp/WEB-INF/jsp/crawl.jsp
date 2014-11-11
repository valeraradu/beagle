<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="edu.uci.ics.crawler4j.examples.basic.BasicCrawlController"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<jsp:include page="head.jsp" />
</head>
<body>
	<%-- Navbar --%>
	<jsp:include page="navbar.jsp" />

	<%-- Body --%>
	<div class="container">

		<%
			WebApplicationContext context = WebApplicationContextUtils
					.getWebApplicationContext(getServletContext());
			pageContext.setAttribute("bean",context.getBean("basicCrawlController"));

			if (((BasicCrawlController) context.getBean("basicCrawlController")).isStoped()) {
				pageContext.setAttribute("label", "Start");
				pageContext.setAttribute("btName", "button1");
			} else {
				pageContext.setAttribute("label", "Stop");
				pageContext.setAttribute("btName", "button2");
			}
		%>

		<table width="75%">
			<tr>
				<td>Type Start Url
					<form action="${pageContext.request.contextPath}/myservlet"	method="post">
						<input type="text" name="name" />
						<input type="submit" name="${btName}" value="${label} " />
					</form>
				</td>
			</tr>
		</table>
		

		<%-- Search result --%>
		<jsp:include page="search-results.jsp" />
	</div>
	<%-- Upload Dialog --%>
	<jsp:include page="upload.jsp" />

	<%-- Notifications --%>
	<jsp:include page="notifications.jsp" />

	<%-- Footer --%>
	<jsp:include page="footer.jsp" />

	<%-- Scripts --%>
	<script src="javascripts/lib/jquery-1.7.2.min.js" type="text/javascript"></script>
	<script src="javascripts/lib/bootstrap-2.1.dev.min.js" type="text/javascript"></script>
	<script src="javascripts/lib/jquery.ui.widget.js" type="text/javascript"></script>
	<script src="javascripts/lib/jquery.iframe-transport.js" type="text/javascript"></script>
	<script src="javascripts/lib/jquery.fileupload.js" type="text/javascript"></script>
	<script src="javascripts/lib/jquery.fileupload-ui.js" type="text/javascript"></script>
    <script src="javascripts/lib/dropzone.js"></script>

	<script src="javascripts/utils.js" type="text/javascript"></script>
	<script src="javascripts/notification.js" type="text/javascript"></script>
	<script src="javascripts/main.js" type="text/javascript"></script>
</body>
</html>
