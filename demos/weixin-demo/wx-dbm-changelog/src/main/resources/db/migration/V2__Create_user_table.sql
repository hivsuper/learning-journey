CREATE TABLE users (
    id INT(11) NOT NULL AUTO_INCREMENT,
    open_id VARCHAR(255) NOT NULL,
    phone VARCHAR(255) DEFAULT NULL,
    role VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME DEFAULT NULL,
    PRIMARY KEY (id),
	UNIQUE INDEX uk_openid (open_id)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB;