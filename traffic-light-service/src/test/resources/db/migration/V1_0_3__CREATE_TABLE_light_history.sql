
-- CREATE TABLE light_history
CREATE TABLE IF NOT EXISTS light_history (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    direction VARCHAR(10) NOT NULL,
    color VARCHAR(10) NOT NULL,
    changed_at TIMESTAMP NOT NULL,
    intersection_id_name VARCHAR(100) NOT NULL,
	intersection_auto_run_status VARCHAR(10) NOT NULL,
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NULL,
    version INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    );
