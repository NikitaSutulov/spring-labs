CREATE SEQUENCE dictionary_id_seq START 1;

CREATE TABLE language
(
    code VARCHAR(10) PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE word
(
    id            SERIAL PRIMARY KEY,
    value         VARCHAR(255)                           NOT NULL,
    language_code VARCHAR(10) REFERENCES language (code) ON DELETE CASCADE NOT NULL
);

CREATE TABLE dictionary
(
    id             INT PRIMARY KEY DEFAULT nextval('dictionary_id_seq'),
    name           VARCHAR(255)                           NOT NULL,
    language1_code VARCHAR(10) REFERENCES language (code) ON DELETE CASCADE NOT NULL,
    language2_code VARCHAR(10) REFERENCES language (code) ON DELETE CASCADE NOT NULL
);

CREATE TABLE translation
(
    id                 SERIAL PRIMARY KEY,
    dictionary_id      INT REFERENCES dictionary (id) ON DELETE CASCADE,
    word_id            INT REFERENCES word (id) ON DELETE CASCADE,
    translated_word_id INT REFERENCES word (id) ON DELETE CASCADE
);
