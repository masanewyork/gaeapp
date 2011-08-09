<%--
  Created by IntelliJ IDEA.
  User: masa
  Date: Jul 22, 2011
  Time: 4:07:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Shipping page</title></head>
<body>
<form name="shipping-form" id="shipping-form" method="POST" action="/shipping">
	<input type="hidden" value="put" name="action"/>
	Item Name:<input type="text" name="itemName" class="gsc-search-button"/>
	Vessel Name:<input type="text" name="vesselName" class="gsc-search-button"/>
	Quantity:<input type="text" name="quantity" class="gsc-search-button"/>
	<input type="submit" value="Load" class="gsc-search-button"/>
</form>

</body>
</html>