CREATE OR REPLACE CONTEXT session_ctx USING session_ctx_pkg ACCESSED GLOBALLY;

CREATE OR REPLACE PACKAGE session_ctx_pkg
IS
  PROCEDURE set_session_id(p_session_id IN VARCHAR2);
  PROCEDURE set_session_ctx(p_attr IN VARCHAR2,
                            p_value IN VARCHAR2);
  PROCEDURE clear_session(p_session_id IN VARCHAR2);
  PROCEDURE clear_context;
END;

CREATE OR REPLACE PACKAGE BODY session_ctx_pkg
IS
  session_id_global VARCHAR2(32 CHAR);

  PROCEDURE set_session_id(p_session_id IN VARCHAR2)
  IS
  BEGIN
    session_id_global := p_session_id;
    DBMS_SESSION.SET_IDENTIFIER(p_session_id);
  END set_session_id;

  PROCEDURE set_session_ctx(p_attr IN VARCHAR2, p_value IN VARCHAR2)
  IS
  BEGIN
    DBMS_SESSION.SET_CONTEXT(
        namespace  => 'session_ctx',
        attribute  => p_attr,
        value      => p_value,
        username   => USER,
        client_id  => session_id_global);
  END set_session_ctx;

  PROCEDURE clear_session(p_session_id IN VARCHAR2)
  IS
  BEGIN
    DBMS_SESSION.SET_IDENTIFIER(p_session_id);
    DBMS_SESSION.CLEAR_IDENTIFIER;
  END clear_session;

  PROCEDURE clear_context
  IS
  BEGIN
    DBMS_SESSION.CLEAR_CONTEXT('session_ctx', session_id_global);
  END clear_context;
END;


CREATE OR REPLACE FUNCTION get_session_ctx RETURN VARCHAR2
IS
BEGIN
  RETURN SYS_CONTEXT('session_ctx', 'USER_USERNAME');
end;

CREATE OR REPLACE FUNCTION get_session_id RETURN VARCHAR2
IS
  id VARCHAR2(32 CHAR);
BEGIN
  SELECT SYS_CONTEXT('userenv', 'client_identifier') INTO id FROM dual;
  RETURN id;
end;



BEGIN
  SET_SESSION_CTX('USER_USERNAME', 'SUPER_USER2');
  commit;
end;

BEGIN
  DBMS_OUTPUT.put_line(GET_SESSION_CTX());
end;