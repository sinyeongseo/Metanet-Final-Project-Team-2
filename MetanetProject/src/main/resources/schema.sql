CREATE TABLE IF NOT EXISTS log (
	log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_url VARCHAR(200) NOT NULL,
    request_method VARCHAR(50) NOT NULL,
    response_status INT NOT NULL,
    client_ip VARCHAR(45) NOT NULL,
    request_time DATETIME NOT NULL,
    response_time DATETIME NOT NULL
);