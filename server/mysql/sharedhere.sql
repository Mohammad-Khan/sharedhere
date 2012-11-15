-- DROP DATABASE IF EXISTS sharedhere;

CREATE DATABASE sharedhere;

USE sharedhere;

CREATE TABLE location (
        id INT PRIMARY KEY AUTO_INCREMENT,
        latitude DECIMAL(6,4),
        longitude DECIMAL(7,4)
);

CREATE TABLE content (
	id INT PRIMARY KEY AUTO_INCREMENT,
	description VARCHAR(256),
    timestamp DATETIME,
    filename VARCHAR(256),
    size DECIMAL,
    location_id INT,
    FOREIGN KEY(location_id) REFERENCES location(id)
);

CREATE TABLE log (
    id INT PRIMARY KEY AUTO_INCREMENT,
    op_type ENUM('ContentInsert','ContentSelect','ContentDelete', 'LocationInsert','LocationSelect','LocationDelete'),
    timestamp DATETIME,
    query VARCHAR(4096),
    client_name VARCHAR(128)
);

-- Trigger to log INSERT event on content table 
DELIMITER |

CREATE TRIGGER log_content_insert BEFORE INSERT ON `content`
FOR EACH ROW
BEGIN
    
	DECLARE op_type INT;
	DECLARE due_query VARCHAR(1024);
	DECLARE time_stamp DATETIME;
	-- DECLARE client VARCHAR(100); -- To be used in future to make the logging useful
	SET op_type = 'ContentInsert';
	SET time_stamp  = CONCAT(CURDATE(), " ", CURTIME());
	SET due_query = (SELECT info FROM INFORMATION_SCHEMA.PROCESSLIST WHERE id = CONNECTION_ID());
    INSERT INTO `log`(`op_type`, `timestamp`,`query`) VALUES (op_type, time_stamp, due_query);
END;
|
DELIMITER ;

-- Trigger to log UPDATE event on content table
DELIMITER |

CREATE TRIGGER log_content_update BEFORE UPDATE ON `content`
FOR EACH ROW
BEGIN
    
	DECLARE op_type INT;
	DECLARE update_query VARCHAR(1024);
	DECLARE time_stamp DATETIME;
	-- DECLARE client VARCHAR(100); -- To be used in future to make the logging useful
	SET op_type = 'ContentUpdate';
	SET time_stamp  = CONCAT(CURDATE(), " ", CURTIME());
	SET update_query = (SELECT info FROM INFORMATION_SCHEMA.PROCESSLIST WHERE id = CONNECTION_ID());
    INSERT INTO `log`(`op_type`, `timestamp`,`query`) VALUES (op_type, time_stamp, update_query);
END;
|
DELIMITER ;

-- Trigger to log DELETE event on content table
DELIMITER |

CREATE TRIGGER log_content_delete BEFORE DELETE ON `content`
FOR EACH ROW
BEGIN
    
	DECLARE op_type INT;
	DECLARE delete_query VARCHAR(1024);
	DECLARE time_stamp DATETIME;
	-- DECLARE client VARCHAR(100); -- To be used in future to make the logging useful
	SET op_type = 'ContentDelete';
	SET time_stamp  = CONCAT(CURDATE(), " ", CURTIME());
	SET delete_query = (SELECT info FROM INFORMATION_SCHEMA.PROCESSLIST WHERE id = CONNECTION_ID());
    INSERT INTO `log`(`op_type`, `timestamp`,`query`) VALUES (op_type, time_stamp, delete_query);
END;
|
DELIMITER ;

-- Trigger to log INSERT event on content table 
DELIMITER |

CREATE TRIGGER log_location_insert BEFORE INSERT ON `location`
FOR EACH ROW
BEGIN
    
	DECLARE op_type INT;
	DECLARE due_query VARCHAR(1024);
	DECLARE time_stamp DATETIME;
	-- DECLARE client VARCHAR(100); -- To be used in future to make the logging useful
	SET op_type = 'LocationInsert';
	SET time_stamp  = CONCAT(CURDATE(), " ", CURTIME());
	SET due_query = (SELECT info FROM INFORMATION_SCHEMA.PROCESSLIST WHERE id = CONNECTION_ID());
    INSERT INTO `log`(`op_type`, `timestamp`,`query`) VALUES (op_type, time_stamp, due_query);
END;
|
DELIMITER ;

-- Trigger to log UPDATE event on content table
DELIMITER |

CREATE TRIGGER log_lcation_update BEFORE UPDATE ON `location`
FOR EACH ROW
BEGIN
    
	DECLARE op_type INT;
	DECLARE update_query VARCHAR(1024);
	DECLARE time_stamp DATETIME;
	-- DECLARE client VARCHAR(100); -- To be used in future to make the logging useful
	SET op_type = 'LocationUpdate';
	SET time_stamp  = CONCAT(CURDATE(), " ", CURTIME());
	SET update_query = (SELECT info FROM INFORMATION_SCHEMA.PROCESSLIST WHERE id = CONNECTION_ID());
    INSERT INTO `log`(`op_type`, `timestamp`,`query`) VALUES (op_type, time_stamp, update_query);
END;
|
DELIMITER ;

-- Trigger to log DELETE event on location table
DELIMITER |

CREATE TRIGGER log_location_delete BEFORE DELETE ON `location`
FOR EACH ROW
BEGIN
    
	DECLARE op_type INT;
	DECLARE delete_query VARCHAR(1024);
	DECLARE time_stamp DATETIME;
	-- DECLARE client VARCHAR(100); -- To be used in future to make the logging useful
	SET op_type = 'LocationDelete';
	SET time_stamp  = CONCAT(CURDATE(), " ", CURTIME());
	SET delete_query = (SELECT info FROM INFORMATION_SCHEMA.PROCESSLIST WHERE id = CONNECTION_ID());
    INSERT INTO `log`(`op_type`, `timestamp`,`query`) VALUES (op_type, time_stamp, delete_query);
END;
|
DELIMITER ;
