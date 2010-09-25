CREATE TABLE attachments (
	tag_id INTEGER NOT NULL,
	file_id INTEGER NOT NULL,
	PRIMARY KEY (tag_id,file_id),
	FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE,
	FOREIGN KEY (file_id) REFERENCES files(file_id) ON DELETE CASCADE
);

CREATE VIEW freq_desc_tags (tag_id, tag_frequency) AS
	SELECT tag_id, COUNT(tag_id)
	FROM attachments
	GROUP BY tag_id
	ORDER BY COUNT(tag_id) DESC;