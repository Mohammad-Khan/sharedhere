<?php

require_once('./Constants.php');
require_once('./Functions.php');

// sanity check
if (!isset($_GET['request_id']) &&
    !isset($_GET['filename']) &&
    !isset($_GET['latitude']) &&
    !isset($_GET['longitude']) &&
    ($request_type != REQUEST_DATA_DOWNLOAD)) {
    print("Please make sure all input parameters are correct");
    exit;
}

$latitude = sh_truncate($_GET['latitude'], ".", 4);
$longitude = sh_truncate($_GET['longitude'], ".", 4);
$filename = $_GET['filename'];

readfile(CONTENT_DIR."/$latitude/$longitude/$filename");
?>
