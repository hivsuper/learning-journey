CREATE TABLE admins (
    id INT(11) NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_by INT(11) DEFAULT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME DEFAULT NULL,
    PRIMARY KEY (id),
	UNIQUE INDEX uk_username (username)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB;

INSERT INTO admins (id, username, password, role, created_at) VALUES (1, 'root', '$2a$10$EBtKSguK4sFKbwHdYR/HQenEWl0GW6i8cF/5bQiqGgUkGZ6vx7eL6', 'ADMIN', NOW());