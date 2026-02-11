
-- CREATE TABLE traffic_light
CREATE TABLE IF NOT EXISTS traffic_light (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    direction VARCHAR(10) NOT NULL,
    color VARCHAR(10) NOT NULL,
    last_changed_at TIMESTAMP,
	intersection_id INT UNSIGNED NOT NULL,
	created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NULL,
    version INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    FOREIGN KEY (intersection_id) REFERENCES intersection (id)
    );
