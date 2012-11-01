<?php 

	define('REQUEST_POI_DOWNLOAD', 0);
	define('REQUEST_DATA_UPLOAD', 1);
	define('REQUEST_DATA_DOWNLOAD', 2);
	define('DB_HOST', 'localhost');
	define('DB_NAME', 'sharedhere');
	define('DB_USER', 'root');
	define('DB_PASSWORD', '');

	if (isset($_POST['request_id']) && $_POST['request_id'] != '') {
		// Connect to MySQL DB an
		$conn = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
		mysql_select_db(DB_NAME, $conn);

		$out = array();
		// Check for type of request
		if ($_POST['request_id'] == REQUEST_POI_DOWNLOAD) {
			$mysql_result = mysql_query('SELECT * FROM poi');
			
			$i=0;
			while($row = mysql_fetch_array($mysql_result)) {
				$out[$i]['lat'] = $row['latitude'];
				$out[$i]['lon'] = $row['longitude'];
				$i++;
			}
		} else if  ($_POST['request_id'] == REQUEST_DATA_DOWNLOAD) {
			// TODO write logic
		} else if  ($_POST['request_id'] == REQUEST_DATA_UPLOAD) {
			// TODO write logic
		}

		print(json_encode($out));
					
		mysql_close();
	} else {
		print('NoPOI');
	}

?>