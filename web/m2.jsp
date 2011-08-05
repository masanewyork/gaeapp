<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta name="viewport" content="width=device-width">
	<title>Mobile App Demo</title>

	<link href="stylesheets/jquery.mobile-1.0b1.min.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" language="javascript" src='script/jquery-1.6.min.js'></script>
	<script type="text/javascript" language="javascript" src='script/ajax.util3.js'></script>
	<script type="text/javascript" language="javascript" src='script/jquery.mobile-1.0b1.js'></script>
</head>

<body>
<!-- item-detail tab -->
<div data-role="page" id="item-detail-tab" data-rel="back">
	<div data-role="header">
		<h1>Mobile App Demo</h1>
	</div>
	<div data-role="content">

		<!-- ******************************************* item ******************************************* -->
		<div class="message" id="item-detail-show-message" style="display:none">
		</div>
		<!-- list container -->
		<div class="results" style="border:0;" id="item-detail-ctr">
			<form name="item-create-form" id="item-create-form">
			<table width="200" cellspacing="0" cellpadding="0">
			 <tr>
				  <td>Item Name</td>
			   <td ><span class="readonly"><input type="text" style="width: 185px;" autocomplete="off" class="gsc-input" maxlength="2048" name="name" id="name" /></span></td>
			 </tr>
			 <tr>
			   <td>Selling Price</td>
			   <td><input type="text" style="width: 185px;" autocomplete="off" class="gsc-input" maxlength="2048" name="price" id="price" /></td>
			 </tr>
			 <tr>
			   <td>Product</td>
				 <td><input type="text" style="width: 185px;" autocomplete="off" class="gsc-input" maxlength="2048" name="product" id="product" /></td>
			 </tr>
			   </table>
			   </form>
		</div>
	</div>
	<div data-role="footer">Copyright M.Nakahara</div>
</div>
<script type="text/javascript">
	$('#item-detail-tab').live('pageshow',function(event, ui){
	//  alert('Item Tab page was just shown: '+ zzz);
		edit('item', '<%= request.getParameter("q")%>');
	});
</script>

</body>
</html>
