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
	<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
	<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.9/jquery-ui.min.js" type="text/javascript"></script>
	<script type="text/javascript" language="javascript" src='script/jquery.mobile-1.0b1.js'></script>
	<script src="script/jquery.ui.map.js" type="text/javascript"></script>
</head>

<body>
<!-- item-detail tab -->
<div data-role="page" id="map-tab">
	<div data-role="header">
		<h1>Mobile App Demo</h1>
	</div>
	<script type="text/javascript">
		// Start page
		var mapdata = { destination: new google.maps.LatLng(59.3327881, 18.064488100000062) };
		$('#map-tab').live("pagecreate", function() {
			$('#map_canvas').gmap(
			{ 'center' : mapdata.destination,
				'zoom' : 12,
				'mapTypeControl' : false,
				'navigationControl' : false,
				'streetViewControl' : false,
				'callback' : function(map) {
					$('#map_canvas').gmap('addMarker',
					{ 'position' : map.getCenter(),
						'animation' : google.maps.Animation.DROP
					});
				}
			});
			$('#map_canvas').click(function() {
				$.mobile.changePage('#map-tab', 'slide');
			});
		});
	</script>
	<div data-role="content">
		<div class="ui-bar-c ui-corner-all ui-shadow" style="padding:1em;">
			<div id="map_canvas" style="height:300px;"></div>
		</div>
	</div>
	<div data-role="footer">Copyright M.Nakahara</div>
</div>
</body>
</html>
