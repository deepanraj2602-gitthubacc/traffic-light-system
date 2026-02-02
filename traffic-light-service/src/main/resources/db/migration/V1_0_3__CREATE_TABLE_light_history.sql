
-- CREATE TABLE light_history
CREATE TABLE IF NOT EXISTS light_history (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    direction VARCHAR(10),
    color VARCHAR(10),
    changed_at TIMESTAMP,
    intersection_id_name VARCHAR(100) NOT NULL,
	intersection_auto_run_status VARCHAR(10) NOT NULL,
    created_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    version INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    );
