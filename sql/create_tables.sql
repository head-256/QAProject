CREATE TABLE users
(
  id            NUMBER(19, 0) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  username      VARCHAR2(32 CHAR)                                                                NOT NULL UNIQUE,
  email         VARCHAR2(190 CHAR)                                                               NOT NULL UNIQUE,
  password_salt CHAR(6 CHAR)                                                                     NOT NULL,
  password      VARCHAR2(32 CHAR)                                                                NOT NULL,
  role          VARCHAR2(10 CHAR) DEFAULT 'user' CHECK (role IN ('user', 'admin', 'superadmin')) NOT NULL,
  status        VARCHAR2(13 CHAR) DEFAULT 'available' CHECK (status IN ('available', 'banned'))  NOT NULL,
  avatar_ext    VARCHAR2(64 CHAR)                                                                NULL
);

CREATE TABLE questions
(
  id             NUMBER(19, 0) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  creation_date  NUMBER(19, 0)                                                                   NOT NULL,
  last_edit_date NUMBER(19, 0)                                                                   NOT NULL,
  title          VARCHAR2(64 CHAR)                                                               NOT NULL,
  text           CLOB                                                                            NOT NULL,
  status         VARCHAR2(7 CHAR) DEFAULT 'open' CHECK (status IN ('open', 'closed', 'deleted')) NOT NULL,
  user_id        NUMBER(19, 0)                                                                   NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE answers
(
  id             NUMBER(19, 0) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  creation_date  NUMBER(19, 0)          NOT NULL,
  last_edit_date NUMBER(19, 0)          NOT NULL,
  text           CLOB                   NULL,
  is_deleted     NUMBER(3, 0) DEFAULT 0 NOT NULL,
  author_id      NUMBER(19, 0)          NOT NULL,
  question_id    NUMBER(19, 0)          NOT NULL,
  FOREIGN KEY (author_id) REFERENCES users (id),
  FOREIGN KEY (question_id) REFERENCES questions (id) ON DELETE CASCADE
);

CREATE TABLE marks
(
  user_id   NUMBER(19, 0) NOT NULL,
  answer_id NUMBER(19, 0) NOT NULL,
  value     NUMBER(3, 0)  NOT NULL,
  PRIMARY KEY (user_id, answer_id),
  FOREIGN KEY (user_id) REFERENCES users (id),
  FOREIGN KEY (answer_id) REFERENCES answers (id) ON DELETE CASCADE
);

CREATE TABLE tags
(
  id   NUMBER(19, 0) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  text VARCHAR2(32 CHAR) NOT NULL UNIQUE
);

CREATE TABLE question_join_tag
(
  question_id NUMBER(19, 0) NOT NULL,
  tag_id      NUMBER(19, 0) NOT NULL,
  PRIMARY KEY (question_id, tag_id),
  FOREIGN KEY (question_id) REFERENCES questions (id),
  FOREIGN KEY (tag_id) REFERENCES tags (id)
);

CREATE OR REPLACE VIEW tags_view(question_id, tag_text) AS
SELECT question_join_tag.question_id,
       tags.text
FROM question_join_tag
       JOIN tags ON
  tags.id = question_join_tag.tag_id;

