<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="s"  uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
  <head>
    <title>Comet Example: Counter with Hidden Frame</title>
  </head>
  <body>
    <frameset>
      <iframe name="hidden" src="<c:url value="/servlet/hidden_comet" />" frameborder="0" height="0" width="100%"></iframe>
      <iframe name="counter" src="<c:url value="/views/comet/count.jsp" />"  frameborder="0" height="70%" width="100%"></iframe>
      <iframe name="button" src="<c:url value="/views/comet/button.jsp" />" frameborder="0" height="30%" width="100%"></iframe>
    </frameset></iframe>
  </body>
</html>
