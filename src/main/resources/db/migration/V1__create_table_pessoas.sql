CREATE TABLE tb_pessoas (
    id UUID NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefone VARCHAR(15) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT uk_pessoa_email UNIQUE (email)
);
