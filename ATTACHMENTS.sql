CREATE TABLE attachments (
	tag_id INTEGER NOT NULL,
	file_id INTEGER NOT NULL,
	PRIMARY KEY(tag_id,file_id)
)