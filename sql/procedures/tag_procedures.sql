CREATE OR REPLACE PROCEDURE insert_tag(p_text VARCHAR2, p_tag_id OUT NUMBER)
IS
BEGIN
  INSERT INTO TAGS (text) VALUES (p_text) RETURNING id INTO p_tag_id;
END;

CREATE OR REPLACE PROCEDURE delete_tag(p_tag_id NUMBER)
IS
BEGIN
  DELETE FROM TAGS WHERE id = p_tag_id;
END;

CREATE OR REPLACE PROCEDURE update_tag(p_text VARCHAR2, p_tag_id NUMBER)
IS
BEGIN
  UPDATE TAGS SET text=p_text WHERE ID = p_tag_id;
END;

CREATE OR REPLACE FUNCTION get_tag_text_by_id(p_tag_id NUMBER) RETURN VARCHAR2
IS
  tag_text VARCHAR2(32 CHAR);
BEGIN
  SELECT text INTO tag_text FROM TAGS WHERE id = p_tag_id;
  RETURN tag_text;
END;

CREATE OR REPLACE FUNCTION get_tag_id_by_text(p_tag_text VARCHAR2) RETURN NUMBER
IS
  tag_id NUMBER;
BEGIN
  BEGIN
    SELECT id INTO tag_id FROM TAGS WHERE text = p_tag_text;
  EXCEPTION
    WHEN no_data_found THEN
      RETURN 0;
  END;
  RETURN tag_id;
END;

CREATE OR REPLACE FUNCTION find_tags_by_question_id(p_question_id NUMBER) RETURN SYS_REFCURSOR
IS
  tags_cursor SYS_REFCURSOR;
BEGIN
  OPEN tags_cursor FOR SELECT TAGS.id, TAGS.text
                       FROM TAGS
                              JOIN QUESTION_JOIN_TAG ON TAGS.id = QUESTION_JOIN_TAG.tag_id
                       WHERE QUESTION_JOIN_TAG.question_id = p_question_id
                       ORDER BY id;
  RETURN tags_cursor;
END;