CREATE OR REPLACE PROCEDURE insert_mark(p_user_id NUMBER, p_answer_id NUMBER, p_value NUMBER)
IS
BEGIN
  INSERT INTO MARKS (user_id, answer_id, value) VALUES (p_user_id, p_answer_id, p_value);
END;

CREATE OR REPLACE PROCEDURE delete_mark(p_user_id NUMBER, p_answer_id NUMBER)
IS
BEGIN
  DELETE FROM MARKS WHERE user_id = p_user_id AND answer_id = p_answer_id;
END;

CREATE OR REPLACE PROCEDURE update_mark(p_value NUMBER, p_user_id NUMBER, p_answer_id NUMBER)
IS
BEGIN
  UPDATE MARKS SET value = p_value WHERE user_id = p_user_id AND answer_id = p_answer_id;
END;

CREATE OR REPLACE FUNCTION get_mark_value_by_id(p_user_id NUMBER, p_answer_id NUMBER) RETURN NUMBER
IS
  mark_value NUMBER;
BEGIN
  BEGIN
    SELECT value INTO mark_value FROM MARKS WHERE user_id = p_user_id AND answer_id = p_answer_id;
  EXCEPTION
    WHEN no_data_found THEN
      RETURN -1;
  END;
  RETURN mark_value;
END;

CREATE OR REPLACE FUNCTION get_user_rating(p_user_id NUMBER, p_value NUMBER) RETURN NUMBER
IS
  rating NUMBER;
BEGIN
  SELECT COUNT(*) INTO rating
  FROM MARKS
         JOIN ANSWERS ON MARKS.answer_id = ANSWERS.id
  WHERE ANSWERS.author_id = p_user_id
    AND value = p_value;
  RETURN rating;
END;

CREATE OR REPLACE FUNCTION get_answer_rating(p_answer_id NUMBER, p_value NUMBER) RETURN NUMBER
IS
  rating NUMBER;
BEGIN
  SELECT COUNT(*) INTO rating FROM MARKS WHERE answer_id = p_answer_id AND value = p_value;
  RETURN rating;
END;