CREATE TABLE attachments (
	tag_id INTEGER NOT NULL,
	file_id INTEGER NOT NULL,
	PRIMARY KEY(tag_id,file_id),
	FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE,
	FOREIGN KEY (file_id) REFERENCES files(file_id) ON DELETE CASCADE
)