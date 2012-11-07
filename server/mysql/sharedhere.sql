CREATE TABLE location (
        id INT PRIMARY KEY AUTO_INCREMENT,
        latitude DECIMAL(10,8),
        longitude DECIMAL(11,8),
        radius INT
);

CREATE TABLE content (
        id INT PRIMARY KEY AUTO_INCREMENT,
        filename VARCHAR(200),
        location_id INT,
        timestamp DATETIME,
        description VARCHAR(200),
        FOREIGN KEY(location_id) REFERENCES location(id)
);

CREATE TABLE log (
        id INT PRIMARY KEY AUTO_INCREMENT,
        op_type TINYINT, -- 0=poi, 1=down, 2=up
        client_id INT,
        timestmpa DATETIME,
        query VARCHAR(500)
);
