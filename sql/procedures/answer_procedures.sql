CREATE OR REPLACE PROCEDURE insert_answer(p_creation_date NUMBER, p_last_edit_date NUMBER, p_text CLOB,
                                          p_author_id NUMBER, p_question_id NUMBER, p_is_deleted NUMBER,
                                          p_answer_id OUT NUMBER)
IS
BEGIN
  INSERT INTO ANSWERS (creation_date, last_edit_date, text, author_id, question_id, is_deleted)
  VALUES (p_creation_date, p_last_edit_date, p_text, p_author_id, p_question_id,
          p_is_deleted) RETURNING id INTO p_answer_id;
END;

CREATE OR REPLACE PROCEDURE delete_answer(p_answer_id NUMBER)
IS
BEGIN
  DELETE FROM ANSWERS WHERE id = p_answer_id;
END;

CREATE OR REPLACE PROCEDURE update_answer(p_creation_date NUMBER, p_last_edit_date NUMBER, p_text CLOB,
                                          p_author_id NUMBER, p_question_id NUMBER, p_is_deleted NUMBER,
                                          p_answer_id NUMBER)
IS
BEGIN
  UPDATE ANSWERS
  SET CREATION_DATE=p_creation_date,
      LAST_EDIT_DATE=p_last_edit_date,
      TEXT=p_text,
      AUTHOR_ID=p_author_id,
      QUESTION_ID=p_question_id,
      IS_DELETED=p_is_deleted
  WHERE ID = p_answer_id;
END;

CREATE OR REPLACE FUNCTION find_answer_by_id(p_answer_id NUMBER) RETURN SYS_REFCURSOR
IS
  answer_cursor SYS_REFCURSOR;
BEGIN
  OPEN answer_cursor FOR SELECT creation_date, last_edit_date, text, author_id, question_id, is_deleted
                         FROM answers
                         WHERE id = p_answer_id;
  RETURN answer_cursor;
END;

CREATE OR REPLACE FUNCTION find_answers_by_question_id(p_question_id NUMBER) RETURN SYS_REFCURSOR
IS
  answers_cursor SYS_REFCURSOR;
BEGIN
  OPEN answers_cursor FOR SELECT id, creation_date, last_edit_date, text, author_id, is_deleted
                          FROM answers
                          WHERE question_id = p_question_id
                          ORDER BY id;
  RETURN answers_cursor;
END;

CREATE OR REPLACE FUNCTION find_not_deleted_answers_by_question_id(p_question_id NUMBER) RETURN SYS_REFCURSOR
IS
  answers_cursor SYS_REFCURSOR;
BEGIN
  OPEN answers_cursor FOR SELECT id, creation_date, last_edit_date, text, author_id, is_deleted
                          FROM answers
                          WHERE question_id = p_question_id
                            AND is_deleted = 0
                          ORDER BY id;
  RETURN answers_cursor;
END;

CREATE OR REPLACE FUNCTION get_answer_count_by_question_id(p_question_id NUMBER) RETURN NUMBER
IS
  answer_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO answer_count FROM ANSWERS WHERE question_id = p_question_id;
  RETURN answer_count;
END;

CREATE OR REPLACE FUNCTION get_not_deleted_answer_count_by_question_id(p_question_id NUMBER) RETURN NUMBER
IS
  answer_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO answer_count FROM ANSWERS WHERE question_id = p_question_id AND is_deleted = 0;
  RETURN answer_count;
END;