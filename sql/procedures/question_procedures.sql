CREATE OR REPLACE PROCEDURE insert_question(p_title VARCHAR2, p_text CLOB, p_creation_date NUMBER,
                                            p_last_edit_date NUMBER, p_status VARCHAR2, p_user_id NUMBER,
                                            p_question_id OUT NUMBER)
IS
BEGIN
  INSERT INTO QUESTIONS (title, text, creation_date, last_edit_date, status, user_id)
  VALUES (p_title, p_text, p_creation_date, p_last_edit_date, p_status, p_user_id) RETURNING id INTO p_question_id;
END;

CREATE OR REPLACE PROCEDURE delete_question(p_question_id NUMBER)
IS
BEGIN
  DELETE FROM QUESTIONS WHERE id = p_question_id;
END;

CREATE OR REPLACE PROCEDURE update_question(p_title VARCHAR2, p_text CLOB, p_creation_date NUMBER,
                                            p_last_edit_date NUMBER, p_status VARCHAR2, p_user_id NUMBER,
                                            p_question_id NUMBER)
IS
BEGIN
  UPDATE QUESTIONS
  SET title=p_title,
      text=p_text,
      creation_date=p_creation_date,
      last_edit_date=p_last_edit_date,
      status=p_status,
      user_id=p_user_id
  WHERE id = p_question_id;
END;

CREATE OR REPLACE PROCEDURE insert_question_tag_relation(p_question_id NUMBER, p_tag_id NUMBER)
IS
BEGIN
  INSERT INTO QUESTION_JOIN_TAG (question_id, tag_id) VALUES (p_question_id, p_tag_id);
END;

CREATE OR REPLACE PROCEDURE delete_question_tag_relations(p_question_id NUMBER)
IS
BEGIN
  DELETE FROM QUESTION_JOIN_TAG WHERE question_id = p_question_id;
END;

CREATE OR REPLACE FUNCTION find_question_by_id(p_question_id NUMBER) RETURN SYS_REFCURSOR
IS
  question_cursor SYS_REFCURSOR;
BEGIN
  OPEN question_cursor FOR SELECT creation_date, title, text, last_edit_date, status, user_id
                           FROM QUESTIONS
                           WHERE id = p_question_id;
  RETURN question_cursor;
END;

CREATE OR REPLACE FUNCTION get_question_count RETURN NUMBER
IS
  question_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO question_count FROM QUESTIONS;
  RETURN question_count;
END;

CREATE OR REPLACE FUNCTION find_questions_by_user_id(p_user_id NUMBER) RETURN SYS_REFCURSOR
IS
  questions_cursor SYS_REFCURSOR;
BEGIN
  OPEN questions_cursor FOR SELECT id, creation_date, title, text, last_edit_date, status
                            FROM QUESTIONS
                            WHERE user_id = p_user_id
                            ORDER BY id;
  RETURN questions_cursor;
END;

CREATE OR REPLACE FUNCTION find_not_deleted_questions_by_user_id(p_user_id NUMBER) RETURN SYS_REFCURSOR
IS
  questions_cursor SYS_REFCURSOR;
BEGIN
  OPEN questions_cursor FOR SELECT id, creation_date, title, text, last_edit_date, status
                            FROM QUESTIONS
                            WHERE user_id = p_user_id
                              AND status != 'deleted'
                            ORDER BY id;
  RETURN questions_cursor;
END;

CREATE OR REPLACE FUNCTION find_latest_questions_with_offset(p_offset NUMBER, p_count NUMBER) RETURN SYS_REFCURSOR
IS
  questions_cursor SYS_REFCURSOR;
BEGIN
  OPEN questions_cursor FOR SELECT id, creation_date, title, text, last_edit_date, status, user_id
                            FROM QUESTIONS
                            ORDER BY last_edit_date DESC, id OFFSET p_offset ROWS FETCH NEXT p_count ROWS ONLY;
  RETURN questions_cursor;
END;

CREATE OR REPLACE FUNCTION find_latest_not_deleted_questions_with_offset(p_offset NUMBER, p_count NUMBER) RETURN SYS_REFCURSOR
IS
  questions_cursor SYS_REFCURSOR;
BEGIN
  OPEN questions_cursor FOR SELECT id, creation_date, title, text, last_edit_date, status, user_id
                            FROM QUESTIONS
                            WHERE status != 'deleted'
                            ORDER BY last_edit_date DESC, id OFFSET p_offset ROWS FETCH NEXT p_count ROWS ONLY;
  RETURN questions_cursor;
END;