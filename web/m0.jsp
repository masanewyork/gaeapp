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
<div data-role="page" id="product-tab">

	<div data-role="header">
		<h1>Mobile App Demo</h1>
	</div>
	<div data-role="content">

		<!-- ******************************************* product ******************************************* -->
		<div class="message" id="product-show-message" style="display:none">
		</div>
		<!-- search container -->
		<div class="gsc-search-box" id="product-search-ctr">
			<!-- section title -->
			<h2>All Products</h2>
			<form name="product-search-form" id="product-search-form">
				<label>Name</label>
				<input type="text" name="q" id="q" class="gsc-input"/>
				<input type="button" value="Search" onclick="search('product')" class="gsc-search-button"/>
			</form>
		</div>
		<!-- list container -->
		<div class="results" style="border:0;" id="product-list-ctr">
			<ul id="product-listview" data-role="listview" data-theme="g"></ul>
		</div>
	</div>
	<div data-role="footer">Copyright M.Nakahara</div>
</div>
</body>
</html>
