<?php

require_once('./Constants.php');
require_once('./Functions.php');

if (!isset($_POST['request_id']) ||
	!isset($_POST['latitude']) ||
	!isset($_POST['longitude']) ||
	!isset($_POST['description']) ||
	!isset($_FILES['sharedfile']['name']) ||
	($_POST['request_id'] != REQUEST_DATA_UPLOAD) ||
	($_FILES['sharedfile']['size'] <= 0)) {
	print("Please make sure all input parameters are correct");
	exit;
}

$request_type = $_POST['request_id'];
$latitude = sh_truncate($_POST['latitude'], ".", 4);
$longitude = sh_truncate($_POST['longitude'], ".", 4);
$description = $_POST['description'];
$posted_file_name = $_FILES['sharedfile']['name'];
$posted_file_size = $_FILES['sharedfile']['size'];

$upload_dir = "content/$latitude/$longitude/";
$upload_file_path = $upload_dir . basename($posted_file_name);

if (is_file($upload_file_path)) {
	print("Filename already exists");
	exit;
}

// make sure directory for upload exists
if (sh_mkdir("content/$latitude", 0777)) {
	if (sh_mkdir("content/$latitude/$longitude", 0777)) {
		print("Upload dir successfully created");
	} else {
		print("Could not create upload dir content/$latitude/$longitude");
		exit;
	}
} else {
	print("Could not create upload dir content/$latitude");
	exit;
}
	
// connect to database
$conn = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
if (!$conn) print("Database connection failed"); 
if (!mysql_select_db(DB_NAME, $conn)) print("Database selection failed");

// get location id, if not found we have a new location that we need to add
$location_id = sh_location_id($conn, $latitude, $longitude);

if ($location_id < 0) {
	$query = 'INSERT INTO location(latitude, longitude) VALUES("' . $latitude . '","' . $longitude . '")';	
	$mysql_result = mysql_query($query);

	if (!$mysql_result) {
		print("MySQL Insert Query failed" . mysql_error());
		exit;
	}

	$location_id = sh_location_id($conn, $latitude, $longitude);
}

// insert info about file
$query = 'INSERT INTO content(location_id, filename, timestamp, size, description) VALUES("' . $location_id . '","' . $posted_file_name . '","' . date('Y-m-d H:i:s') . '","' . $posted_file_size . '","' . $description . '")';
$mysql_result = mysql_query($query);
if (!$mysql_result) {
	print("MySQL Insert Query failed" . mysql_error());
	exit;
}

// move file into place
if (move_uploaded_file($_FILES['sharedfile']['tmp_name'], $upload_file_path)) {
   	print("Upload succeeded");
} else {
   	print("File copy operation failed");
}

?>
