<?php

require_once('./Constants.php');
require_once('./Functions.php');

if (!isset($_POST['request_id']) ||
	!isset($_POST['latitude']) ||
	!isset($_POST['longitude']) ||
	($_POST['request_id'] != REQUEST_DATA_LIST)) {
	http_response_code(400); // Bad request
	print("Please make sure all input parameters are correct");
	exit;
}

$conn = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
mysql_select_db(DB_NAME, $conn);

$latitude = sh_truncate($_POST['latitude'], ".", 4);
$longitude = sh_truncate($_POST['longitude'], ".", 4);

$query = "SELECT filename,timestamp,size,description,latitude,longitude FROM content,location WHERE latitude = \"$latitude\" AND longitude = \"$longitude\" AND location.id = content.location_id";

$mysql_result = mysql_query($query);

if(!$mysql_result) {
	print("Database Query faild: ".mysql_error());
	exit;
}
			
$output = array();
$i = 0;
while($row = mysql_fetch_array($mysql_result)) {
	$output[$i]['filename'] = $row['filename'];
	$output[$i]['timestamp'] = $row['timestamp'];
	$output[$i]['size'] = $row['size'];
	$output[$i]['description'] = $row['description'];
	$output[$i]['latitude'] = $row['latitude'];
	$output[$i]['longitude'] = $row['longitude'];
	$i++;
}

// If no data found set response code to "not found"
if (count($output) == 0) http_response_code(404);

print(json_encode($output));
					
mysql_close();
?>
