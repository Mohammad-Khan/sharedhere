<?php 

require_once('Constants.php');

if (!isset($_POST['request_id']) &&
    ($_POST['request_id'] == REQUEST_POI_DOWNLOAD)) {
    print("Please make sure all input parameters are correct");
    exit;
}

$conn = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
mysql_select_db(DB_NAME, $conn);

$output = array();

$mysql_result = mysql_query('SELECT latitude,longitude FROM location');
			
$i = 0;
while($row = mysql_fetch_array($mysql_result)) {
	$output[$i]['latitude'] = $row['latitude'];
	$output[$i]['longitude'] = $row['longitude'];
	$i++;
}

print(json_encode($output));
					
mysql_close();

?>
