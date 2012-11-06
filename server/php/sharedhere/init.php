<?php 

require_once('Constants.php');

	if (isset($_POST['request_id']) && $_POST['request_id'] != '') {
		// Connect to MySQL DB
		$conn = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
		mysql_select_db(DB_NAME, $conn);

		$output = array();
		// Check for type of request
		if ($_POST['request_id'] == REQUEST_POI_DOWNLOAD) {
			$mysql_result = mysql_query(
				'SELECT latitude,longitude,radius FROM location WHERE is_poi="true"'
			);
			
			$i=0;
			while($row = mysql_fetch_array($mysql_result)) {
				$output[$i]['latitude'] = $row['latitude'];
				$output[$i]['longitude'] = $row['longitude'];
				$output[$i]['radius'] = $row['radius'];
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