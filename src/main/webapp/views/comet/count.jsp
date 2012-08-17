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
    <script type='text/javascript'>
      function updateCount(c) {
        document.getElementById('count').innerHTML = c;
        // comment location.href if it is Http Streaming
        parent.hidden.location.href = "<c:url value="/hidden_comet" />";
      };
    </script>
  </head>
  <body>
    <center>
      <h3>Comet Example: Counter with Hidden Frame</h3>
      <p>
      <b id="count">&nbsp;</b>
      <p>
    </center>
  </body>
</html>
