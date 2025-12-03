ALTER TABLE classmates
    MODIFY COLUMN id CHAR(36) NOT NULL;

UPDATE classmates
    SET id = UUID();