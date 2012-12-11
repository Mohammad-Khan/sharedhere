<?php

function sh_location_id($db_conn, $latitude, $longitude) {
	$query = "SELECT * from location where latitude = \"$latitude\" and longitude = \"$longitude\"";
	$mysql_result = mysql_query($query);
	$num_rows = mysql_num_rows($mysql_result);

	if ($num_rows > 0) {
		$row = mysql_fetch_row($mysql_result);
		return $row[0];
	} else {
		return -1;
	}
}

function sh_mkdir($dir, $permission) {
	if (!is_dir($dir)) {
		if (mkdir($dir, $permission)) {
			return true;
		} else {
			return false;
		}
	}

	return true;
}

function sh_truncate($string, $char, $num) {
	return substr($string, 0, 1+strpos($string, $char)+$num);               
}

function identical($fn1, $fn2)
{

}


?>
