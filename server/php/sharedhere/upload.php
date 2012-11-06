<?php

require_once('./Constants.php');

$request_type = $_POST['request_id'];
if ($request_type != REQUEST_DATA_UPLOAD) {
	print  ("Wrong request type");
	exit;
}

	$posted_file_name = $_FILES['sharedfile']['name'];
	$upload_dir = './content/';
	$upload_file_path = $upload_dir . basename($posted_file_name);

	$conn = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
	if (!$conn) print("Database connection failed"); 
	if (!mysql_select_db(DB_NAME, $conn)) print("Database selection failed");
	$query = 'INSERT INTO content(timestamp, filename) VALUES("'.
		date('Y-m-d H:i:s') .'","' . $posted_file_name . '")';
	$mysql_result = mysql_query($query);
	if (!$mysql_result) {
		print("MySQL Insert Query failed".mysql_error());
		exit;
	}
	
	if (move_uploaded_file($_FILES['sharedfile']['tmp_name'], $upload_file_path)) {
    	print ("Upload succeeded");
	} else {
    	print ("File copy operation failed");
	}

?>