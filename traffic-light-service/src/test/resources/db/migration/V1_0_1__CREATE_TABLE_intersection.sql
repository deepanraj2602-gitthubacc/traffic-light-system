
-- CREATE TABLE intersection
CREATE TABLE IF NOT EXISTS intersection (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_name VARCHAR(100) NOT NULL UNIQUE,
	auto_run_status VARCHAR(10) NOT NULL,
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NULL,
    version INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
    );

-- id & id_name column index already created as table created
-- CREATE INDEX auto_run_status
CREATE INDEX indx_auto_run_status ON intersection (auto_run_status);
