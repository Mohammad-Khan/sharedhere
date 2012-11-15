<?php

require_once('./Constants.php');
require_once('./Functions.php');

if (!isset($_GET['request_id']) &&
    !isset($_GET['filename']) &&
    !isset($_GET['latitude']) &&
    !isset($_GET['longitude']) &&
    ($_GET['request_id'] == REQUEST_DATA_DOWNLOAD)) {
    print("Please make sure all input parameters are correct");
    exit;
}

$latitude = sh_truncate($_GET['latitude'], ".", 4);
$longitude = sh_truncate($_GET['longitude'], ".", 4);
$filename = basename($_GET['filename']);

$file = CONTENT_DIR."/$latitude/$longitude/$filename";

if (is_file($file)) {
	readfile($file);
} else {
    print("No such file");
	exit;
}

?>
