CREATE TABLE t_user (
	id INT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    create_time DATETIME NOT NULL,
    modify_time DATETIME NULL
);