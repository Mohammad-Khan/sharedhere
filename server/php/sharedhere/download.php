<?php

require_once('./Constants.php');

	if (isset($_POST['request_id']) && $_POST['request_id'] != '') {
		// Connect to MySQL DB
		$conn = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
		mysql_select_db(DB_NAME, $conn);

		// Check for type of request
		if ($_POST['request_id'] == REQUEST_POI_DOWNLOAD) {
			$latitude = $_POST['latitude'];
			$longitude = $_POST['longitude'];
			$location_subquery = "SELECT id FROM location WHERE latitude = $latitude AND ".
				"longitude = $longitude"; 
			$query = "SELECT filename,description,timestamp FROM content ".
					"WHERE location_id IN ($location_subquery)";
			$mysql_result = mysql_query($query);
			if(!$mysql_result) {
				print("Database Query faild: ".mysql_error());
				exit;
			}
			
			$output = array();
			$i=0;
			while($row = mysql_fetch_array($mysql_result)) {
				$output[$i]['filename'] = $row['filename'];
				$output[$i]['description'] = $row['description'];
				$output[$i]['timestamp'] = $row['timestamp'];
				$i++;
			}
		} else {
			$output = 'Invalid Request';
		}

		print(json_encode($output));
					
		mysql_close();
	} else {
		print('NoPOI');
	}

?>