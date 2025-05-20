CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS status (
    id INTEGER PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);

INSERT INTO status (id, descricao) VALUES
(1,'ABERTA'),
(2,'VENCIDA'),
(3,'PAGA'),
(4,'CANCELADA'),
(5,'PARCELADA'),
(6,'ESTORNADA'),
(7,'EM_DISPUTA');

CREATE TABLE IF NOT EXISTS categoria(
    id INTEGER PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);

INSERT INTO categoria (id, descricao) VALUES
(1,'DESPESA_FIXA'),
(2,'DESPESA_VARIAVEL'),
(3,'RECEITA_RECORRENTE'),
(4,'RECEITA_VARIAVEL'),
(5,'IMPOSTOS_TAXAS'),
(6,'INVESTIMENTOS');

CREATE TABLE IF NOT EXISTS origem (
    id INTEGER PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);

INSERT INTO origem (id, descricao) VALUES
(1,'FORNECEDOR'),
(2,'CLIENTE'),
(3,'FUNCIONARIO'),
(4,'INSTITUICAO_FINANCEIRA'),
(5,'GOVERNO');

CREATE TABLE IF NOT EXISTS role (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(20)
);
INSERT INTO role (id, nome) VALUES (1,'ROLE_ADMIN'), (2,'ROLE_USER');

CREATE TABLE IF NOT EXISTS "user" (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
        nome VARCHAR(100),
        login VARCHAR(50),
        senha VARCHAR(255),
        email VARCHAR(50),
        role_id SERIAL,
        FOREIGN KEY (role_id) REFERENCES "role" (id)
);

INSERT INTO "user" (id, nome, login, senha, email, role_id) VALUES
('e9b1f85d-4a58-4c2e-bb8b-3a41f8a9d1c7', 'Sofia Dev', 'dev', '$2a$12$ndx7/NNxNAbUGeBwsT62oujXqSBGU/wOIiWO4O3UGbYs0WjkyJM9i','dev@gmail.com', 1),
('f3a6df49-8b26-4890-9b56-2ddc94e8f1f1', 'User Teste', 'teste', '$2a$12$ndx7/NNxNAbUGeBwsT62oujXqSBGU/wOIiWO4O3UGbYs0WjkyJM9i','teste@gmail.com', 2);

CREATE TABLE IF NOT EXISTS conta (
    id INTEGER PRIMARY KEY,
    vencimento DATE NOT NULL,
    descricao VARCHAR (100) NOT NULL,
    valor DOUBLE PRECISION NOT NULL,
    data_pagamento DATE,
    observacao TEXT,
    imagem TEXT,
    status_id INTEGER,
    origem_id INTEGER,
    categoria_id INTEGER,
    user_id UUID,
    FOREIGN KEY (status_id) REFERENCES "status" (id),
    FOREIGN KEY (origem_id) REFERENCES "origem" (id),
    FOREIGN KEY (categoria_id) REFERENCES "categoria" (id),
    FOREIGN KEY (user_id) REFERENCES "user" (id)
);

INSERT INTO conta (id, vencimento, descricao, valor, data_pagamento, observacao, imagem, status_id, origem_id, categoria_id, user_id)
VALUES
(1, '2026-01-01', 'Conta teste', 2000.00, null, 'é isso', 'sem imagem', 1, 1, 1, 'f3a6df49-8b26-4890-9b56-2ddc94e8f1f1');