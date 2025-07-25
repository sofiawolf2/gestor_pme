CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS status (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);

INSERT INTO status (descricao) VALUES
('ABERTA'),
('VENCIDA'),
('PAGA'),
('CANCELADA'),
('PARCELADA'),
('ESTORNADA'),
('EM DISPUTA');

CREATE TABLE IF NOT EXISTS categoria(
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);

INSERT INTO categoria (descricao) VALUES
('DESPESA FIXA'),
('DESPESA VARIAVEL'),
('RECEITA RECORRENTE'),
('RECEITA VARIAVEL'),
('IMPOSTOS TAXAS'),
('INVESTIMENTOS');

CREATE TABLE IF NOT EXISTS origem (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);

INSERT INTO origem (descricao) VALUES
('FORNECEDOR'),
('CLIENTE'),
('FUNCIONARIO'),
('INSTITUICAO FINANCEIRA'),
('GOVERNO');

CREATE TABLE IF NOT EXISTS role (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(20)
);
INSERT INTO role (nome) VALUES ('ROLE_ADMIN'), ('ROLE_USER');

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
    id SERIAL PRIMARY KEY,
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

INSERT INTO conta ( vencimento, descricao, valor, data_pagamento, observacao, imagem, status_id, origem_id, categoria_id, user_id)
VALUES
('2026-01-01', 'Conta teste', 2000.00, null, 'é isso', null, 1, 1, 1, 'f3a6df49-8b26-4890-9b56-2ddc94e8f1f1'),
('2027-01-05', 'Segunda conta teste', 3000.00, null, 'testando', null, 2,2,2, 'f3a6df49-8b26-4890-9b56-2ddc94e8f1f1'),
('2030-01-05', 'Terceira conta teste', 3000.00, null, 'testando3', null, 3,2,5, 'e9b1f85d-4a58-4c2e-bb8b-3a41f8a9d1c7');

CREATE TABLE IF NOT EXISTS funcao (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);

INSERT INTO funcao (descricao) VALUES
('ANALISTA'),
('DESENVOLVEDOR'),
('COORDENADOR'),
('ESTAGIARIO'),
('DIRETOR');

CREATE TABLE IF NOT EXISTS funcionario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    funcao_id INTEGER,
    cpf VARCHAR(14) NOT NULL,
    telefone VARCHAR(14) NOT NULL,
    salario  DOUBLE PRECISION NOT NULL,
    ativo BOOLEAN NOT NULL,
    FOREIGN KEY (funcao_id) REFERENCES "funcao" (id)

);
INSERT INTO funcionario (nome, funcao_id, cpf, telefone, salario, ativo) VALUES
('Ana Maria Braga', 2, '100.100.100-18', '5581910001000', 2000.00, true),
('Carlos Richard', 1, '100.100.100-19', '5581910001030', 2000.00, true);

CREATE TABLE IF NOT EXISTS tipo_pix (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);

INSERT INTO tipo_pix (descricao) VALUES
('TRANSFERÊNCIA'),
('COBRANÇA'),
('SAQUE'),
('TROCO'),
('PARCELADO'),
('GARANTIDO'),
('AUTOMÁTICO');

CREATE TABLE IF NOT EXISTS pix (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR (30) NOT NULL,
    funcionario_id INTEGER,
    tipo_pix_id INTEGER,
    FOREIGN KEY (funcionario_id) REFERENCES "funcionario" (id),
    FOREIGN KEY (tipo_pix_id) REFERENCES "tipo_pix" (id)
);
INSERT INTO pix (descricao, funcionario_id, tipo_pix_id) VALUES
('Primeiro pix para testes', 1, 1),
('Segundo pix para testes', 2, 2);

CREATE TABLE IF NOT EXISTS tipo_despesa (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);
INSERT INTO tipo_despesa (descricao) VALUES
('FIXA'),
('VARIAVEL'),
('OPERACIONAL'),
('FINANCEIRA'),
('NAO RECORRENTE');

CREATE TABLE IF NOT EXISTS despesa_funcionario (
    id SERIAL PRIMARY KEY,
    funcionario_id INTEGER NOT NULL,
    dt_entrada DATE DEFAULT CURRENT_DATE,
    valor DOUBLE PRECISION NOT NULL,
    observacao TEXT,
    tipo_despesa_id INTEGER NOT NULL,
    FOREIGN KEY (funcionario_id) REFERENCES "funcionario" (id),
    FOREIGN KEY (tipo_despesa_id) REFERENCES "tipo_despesa" (id)
);

INSERT INTO despesa_funcionario (funcionario_id, valor,observacao, tipo_despesa_id) VALUES
(1,300.00,'Primeira despesa de funcionário para testes',1),
(1,400.00,'Segunda despesa de func',3);

CREATE TABLE IF NOT EXISTS banco (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);

INSERT INTO banco (descricao) VALUES
('Itau'),
('Banco do Brasil'),
('Bradesco'),
('Santander'),
('Caixa Econômica');

CREATE TABLE IF NOT EXISTS tipo_conta (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(30) NOT NULL
);
INSERT INTO tipo_conta (descricao) VALUES
('CORRENTE'),
('POUPANÇA'),
('SALARIAL'),
('EMPRESARIAL'),
('CONJUNTA'),
('DIGITAL'),
('PRE PAGA'),
('MEI');

CREATE TABLE IF NOT EXISTS agencia (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(20) NOT NULL,
    conta VARCHAR(20) NOT NULL,
    banco_id INTEGER NOT NULL,
    funcionario_id INTEGER NOT NULL,
    tipo_conta_id INTEGER NOT NULL,
    FOREIGN KEY (banco_id) REFERENCES "banco" (id),
    FOREIGN KEY (funcionario_id) REFERENCES "funcionario" (id),
    FOREIGN KEY (tipo_conta_id) REFERENCES "tipo_conta" (id)

);

INSERT INTO agencia (nome,conta,banco_id,funcionario_id,tipo_conta_id) VALUES
('Primeiro teste', 'conta teste', 1, 1, 1),
('Segundo teste', 'conta teste', 2, 2, 2);