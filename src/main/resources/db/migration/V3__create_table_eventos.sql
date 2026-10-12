CREATE TABLE tb_eventos (
    id UUID NOT NULL,
    nome VARCHAR(150) NOT NULL,
    data_hora_evento TIMESTAMP NOT NULL,
    local VARCHAR(200) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    qtde INTEGER NOT NULL,

    PRIMARY KEY (id)
);
