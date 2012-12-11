<?php

//A majority of my contributions to the upload.php involved reordering the various functions.

require_once('./Constants.php');
require_once('./Functions.php');

if (!isset($_POST['request_id']) ||
	!isset($_POST['latitude']) ||
	!isset($_POST['longitude']) ||
	!isset($_POST['description']) ||
	!isset($_FILES['sharedfile']['name']) ||
	($_POST['request_id'] != REQUEST_DATA_UPLOAD) ||
	($_FILES['sharedfile']['size'] <= 0)) {
	http_response_code(400); // Set response code to "bad request"
	print("Please make sure all input parameters are correct");
	exit;
}

$request_type = $_POST['request_id'];
$latitude = sh_truncate($_POST['latitude'], ".", 4);
$longitude = sh_truncate($_POST['longitude'], ".", 4);
$description = $_POST['description'];
$posted_file_name = $_FILES['sharedfile']['name'];
$posted_file_size = $_FILES['sharedfile']['size'];

// connect to database
$conn = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
if (!$conn) {
	http_response_code(500); // Set Http response to "Internal Server Error"
	print("Database connection failed");
	exit; 
}
if (!mysql_select_db(DB_NAME, $conn)) {
	http_response_code(500); // Set Http response to "Internal Server Error" 
	print("Database selection failed");
	exit;
}


// get location id, if not found we have a new location that we need to add
$location_id = sh_location_id($conn, $latitude, $longitude);

if ($location_id < 0) {
	$query = 'INSERT INTO location(latitude, longitude) VALUES(' . $latitude . ', ' . $longitude . ')';	
	$mysql_result = mysql_query($query);

	if (!$mysql_result) {
		http_response_code(500); // Set Http response to "Internal Server Error" 
		print("MySQL Insert Query failed" . mysql_error());
		exit;
	}

	$location_id = sh_location_id($conn, $latitude, $longitude);
}

//Set upload directory.
$upload_dir = "content/$posted_file_size/";
$upload_file_path = $upload_dir . basename($posted_file_name);


// make sure directory for upload exists

if (sh_mkdir("content/$posted_file_size", 0777)) {
		print("Upload dir successfully created");
	} else {
		http_response_code(500); // Set Http response to "Internal Server Error" 
		print("Could not create upload dir content/$location_id");
		exit;
	}

//Tells PHP file if it should upload the file or not. Will not upload the file if this is false.
$uploadfile = false; 

//Handle a duplicate file
if (is_file($upload_file_path) == false) {
	//http_response_code(405); // Set Http response to "Method not allowed" ... Took this out because it causes problems with how I handle duplicates. -- Cooper
	print("Filename already exists");
	$uploadfile = true;
	
	// insert info about file
	$date_in = date('Y-m-d H:i:s');
	$query = 'INSERT INTO content(filename, timestamp, size, description) VALUES("' . $posted_file_name . '","' . $date_in . '","' . $posted_file_size . '","' . $description . '")';

	$mysql_result = mysql_query($query);
	
	if (!$mysql_result) {
		http_response_code(500); // Set Http response to "Internal Server Error"
		print("MySQL Insert Query failed" . mysql_error());
		exit;
	}
	
	
}

//Choose which query to use based on whether or not the file was uploaded already or not.
if ($uploadfile == true) {
		$query = "SELECT id FROM content WHERE filename = \"$posted_file_name\" and size = \"$posted_file_size\"";	
	}
else {
		$query = "SELECT id FROM content,content_at WHERE filename = \"$posted_file_name\" AND size = \"$posted_file_size\" AND content_at.location_id != \"$location_id\"";
	 }


$mysql_result = mysql_query($query);

//Get the content id
$content_id = 0;
while($row = mysql_fetch_array($mysql_result)) {
	$content_id = $row['id'];

}
	//Used these to help debug any problems I was having. - Cooper.
	//print("C:");
	//print($content_id);
	//print("L:");
	//print($location_id);

if ($content_id > 0) {	
	$query = 'INSERT INTO content_at(content_id, location_id) VALUES(' . $content_id . ',' . $location_id . ')';

	$mysql_result = mysql_query($query);

	if (!$mysql_result) {
		http_response_code(500); // Set Http response to "Internal Server Error"
		print("MySQL Insert Query failed on line 122" . mysql_error());
		exit;
	}
	
}
// move file into place
if ($uploadfile == true) {
	if (move_uploaded_file($_FILES['sharedfile']['tmp_name'], $upload_file_path)) {
		print("Upload succeeded!");
	} else {
		http_response_code(500); // Set Http response to "Internal Server Error"
		print("File copy operation failed");
		exit;
	}
	
}

?>
