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
<!-- item-tab -->
<div data-role="page" id="item-tab" data-add-back-btn="true">
	<div data-role="header" >
		<h1>Mobile App Demo</h1>
	</div>
	<div data-role="content">

		<!-- ******************************************* item ******************************************* -->
		<div class="message" id="item-show-message" style="display:none">
		</div>
		<div class="gsc-search-box" id="item-search-ctr">
			<form name="item-search-form" id="item-search-form">
				<input type="hidden" name="q" id="q" value="<%= request.getParameter("q") %>"/>
				<input type="hidden" name="item-searchby" id="item-searchby" value="product"/>
			</form>
		</div>
		<!-- list container -->
		<div class="results" style="border:0;" id="item-list-ctr">
			<ul id="item-listview" data-role="listview" data-theme="g"></ul>
		</div>
	</div>
	<div data-role="footer">Copyright M.Nakahara</div>
</div>
<script type="text/javascript">
	$('#item-tab').live('pageshow',function(event, ui){
		search('item');
	});
</script>

</body>
</html>
