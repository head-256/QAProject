CREATE OR REPLACE PROCEDURE insert_user(p_username VARCHAR2,
                                        p_email VARCHAR2,
                                        p_password_salt CHAR,
                                        p_password VARCHAR2,
                                        p_role VARCHAR2,
                                        p_status VARCHAR2,
                                        p_avatar_ext VARCHAR2,
                                        p_user_id OUT NUMBER)
IS
BEGIN
  INSERT INTO USERS (username, email, password_salt, password, role, status, avatar_ext)
  VALUES (p_username, p_email, p_password_salt, p_password, p_role, p_status, p_avatar_ext) RETURNING id INTO p_user_id;
END;

CREATE OR REPLACE PROCEDURE delete_user(p_user_id NUMBER)
IS
BEGIN
  DELETE FROM USERS WHERE id = p_user_id;
END;

CREATE OR REPLACE PROCEDURE update_user(p_username VARCHAR2,
                                        p_email VARCHAR2,
                                        p_password_salt CHAR,
                                        p_password VARCHAR2,
                                        p_role VARCHAR2,
                                        p_status VARCHAR2,
                                        p_avatar_ext VARCHAR2,
                                        p_id NUMBER)
IS
BEGIN
  UPDATE USERS
  SET USERNAME=p_username,
      EMAIL=p_email,
      PASSWORD_SALT=p_password_salt,
      PASSWORD=p_password,
      ROLE=p_role,
      STATUS=p_status,
      AVATAR_EXT=p_avatar_ext
  WHERE ID = p_id;
END;

CREATE OR REPLACE FUNCTION find_user_by_id(p_user_id NUMBER) RETURN SYS_REFCURSOR
IS
  user_cursor SYS_REFCURSOR;
BEGIN
  OPEN user_cursor FOR SELECT username, email, password_salt, password, role, status, avatar_ext
                       FROM USERS
                       WHERE id = p_user_id;
  RETURN user_cursor;
END;

CREATE OR REPLACE FUNCTION find_user_by_username(p_username VARCHAR2) RETURN SYS_REFCURSOR
IS
  user_cursor SYS_REFCURSOR;
BEGIN
  OPEN user_cursor FOR SELECT id, email, password_salt, password, role, status, avatar_ext
                       FROM USERS
                       WHERE username = p_username;
  RETURN user_cursor;
END;

CREATE OR REPLACE FUNCTION find_user_by_email(p_email VARCHAR2) RETURN SYS_REFCURSOR
IS
  user_cursor SYS_REFCURSOR;
BEGIN
  OPEN user_cursor FOR SELECT id, username, password_salt, password, role, status, avatar_ext
                       FROM USERS
                       WHERE email = p_email;
  RETURN user_cursor;
END;

CREATE OR REPLACE FUNCTION find_users(p_offset NUMBER, p_limit NUMBER) RETURN SYS_REFCURSOR
IS
  users_cursor SYS_REFCURSOR;
BEGIN
  OPEN users_cursor FOR SELECT id,
                               username,
                               email,
                               password_salt,
                               password,
                               role,
                               status,
                               avatar_ext
                        FROM USERS
                        ORDER BY id OFFSET p_offset ROWS FETCH NEXT p_limit ROWS ONLY;
  RETURN users_cursor;
END;

CREATE OR REPLACE FUNCTION get_user_count RETURN NUMBER
IS
  user_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO user_count FROM USERS;
  RETURN user_count;
END;

CREATE OR REPLACE PROCEDURE save_user_to_context(p_id NUMBER, p_username VARCHAR2, p_email VARCHAR2, p_role VARCHAR2,
                                                 p_status VARCHAR2, p_avatar_ext VARCHAR2)
IS
BEGIN
  SESSION_CTX_PKG.SET_SESSION_CTX('USER_ID', TO_CHAR(p_id));
  SESSION_CTX_PKG.SET_SESSION_CTX('USER_USERNAME', p_username);
  SESSION_CTX_PKG.SET_SESSION_CTX('USER_EMAIL', p_email);
  SESSION_CTX_PKG.SET_SESSION_CTX('USER_ROLE', p_role);
  SESSION_CTX_PKG.SET_SESSION_CTX('USER_STATUS', p_status);
  SESSION_CTX_PKG.SET_SESSION_CTX('USER_AVATAR', p_avatar_ext);
END;

CREATE OR REPLACE TYPE USER_ AS OBJECT
(
  id         NUMBER(19),
  username   VARCHAR2(32 CHAR),
  email      VARCHAR2(190 CHAR),
  role       VARCHAR2(10 CHAR),
  status     VARCHAR2(13 CHAR),
  avatar_ext VARCHAR2(64 CHAR)
);

CREATE OR REPLACE FUNCTION get_user_from_context RETURN USER_
IS
  ctx_name VARCHAR2(30 CHAR);
BEGIN
  ctx_name := 'session_ctx';
  IF SYS_CONTEXT(ctx_name, 'USER_ID') IS NULL THEN
    RETURN NULL;
  END IF;
  RETURN USER_(TO_NUMBER(SYS_CONTEXT(ctx_name, 'USER_ID')), SYS_CONTEXT(ctx_name, 'USER_USERNAME'),
               SYS_CONTEXT(ctx_name, 'USER_EMAIL'), SYS_CONTEXT(ctx_name, 'USER_ROLE'),
               SYS_CONTEXT(ctx_name, 'USER_STATUS'), SYS_CONTEXT(ctx_name, 'USER_AVATAR'));
END;