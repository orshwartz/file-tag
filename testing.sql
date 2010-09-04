connect 'jdbc:derby://localhost:1527/myDB;create=true;user=me;password=mine;';
SELECT filename FROM files WHERE file_id IN 
(SELECT file_id FROM tags NATURAL JOIN attachments 
WHERE tag = 'gomel' OR tag = 'zz' EXCEPT 
SELECT file_id FROM tags NATURAL JOIN attachments
WHERE tag = 'bb'); 