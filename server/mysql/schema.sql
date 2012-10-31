create table client (
	id int primary key AUTO_INCREMENT,
	device_id varchar(100),
	latitude DECIMAL(10,8),
	longitude DECIMAL(11,8),
	timestamp DATETIME
);

create table content (
	id int primary key AUTO_INCREMENT,
	timestamp DATETIME,
	filename VARCHAR(200)
);

create table poi (
	id int primary key AUTO_INCREMENT,
	latitude DECIMAL(10,8),
	longitude DECIMAL(11,8)
);

CREATE TABLE log (
	id INT PRIMARY KEY AUTO_INCREMENT,
	op_type TINYINT, -- 0=poi, 1=down, 2=up
	client_id INT,
	timestmpa DATETIME,
	query VARCHAR(500),
	FOREIGN KEY(client_id) REFERENCES client(id)
);