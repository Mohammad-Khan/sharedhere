<?php 

define('REQUEST_POI_DOWNLOAD', 0);
define('REQUEST_DATA_UPLOAD', 0);
define('REQUEST_DATA_DOWNLOAD', 0);
define('DB_HOST', 'localhost');
define('DB_NAME', 'sharedhere');
define('DB_USER', 'root');
define('DB_PASSWORD', '');

if (isset($_POST['id']) && $_POST['id']) != '') {
	// Connect to MySQL DB an
	$conn = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
	mysql_select_db(DB_NAME, $conn);
	
	// Check for type of request
	if ($_POST['id'] == REQUEST_POI_DOWNLOAD) {
		$out = mysql_query('SELECT * FROM poi');
	} else if  ($_POST['id'] == REQUEST_DATA_DOWNLOAD) {
		// TODO write logic
	} else if  ($_POST['id'] == REQUEST_DATA_UPLOAD) {
		// TODO write logic
	}
	
	return json_encode($out);		
}

?>