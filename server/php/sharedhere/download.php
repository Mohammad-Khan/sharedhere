<?php

require_once('./Constants.php');
require_once('./Functions.php');

if (!isset($_GET['request_id']) ||
    !isset($_GET['filename']) ||
    !isset($_GET['latitude']) ||
    !isset($_GET['longitude']) ||
    ($_GET['request_id'] != REQUEST_DATA_DOWNLOAD)) 
	{
	http_response_code(400); // Bad request
    print("Please make sure all input parameters are correct");
    exit;
	}

$conn = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
mysql_select_db(DB_NAME, $conn);


$latitude = sh_truncate($_GET['latitude'], ".", 4);
$longitude = sh_truncate($_GET['longitude'], ".", 4);
$filename = basename($_GET['filename']);

$query = "SELECT size FROM content,content_at,location WHERE content.filename = \"$filename\" AND location.latitude = \"$latitude\" AND location.longitude = \"$longitude\" ";

$mysql_result = mysql_query($query);

$size = 0;

if (!$mysql_result) {
		http_response_code(500); // Set Http response to "Internal Server Error"
		print("MySQL Insert Query failed on line 45" . mysql_error());
		exit;
}

while($row = mysql_fetch_array($mysql_result)) {
	$size = $row['size'];
}


$file = CONTENT_DIR."$size/$filename";

	print($file);

if (is_file($file)) {
	readfile($file);
} else {
	print("No such file");
	http_response_code(404); // Not Found
	print("No such file");
	exit;
}

?>
