CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS status (
    nome VARCHAR(30) PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);

INSERT INTO status (nome, descricao) VALUES
('ABERTA', 'Aguardando pagamento'),
('VENCIDA', 'Passou do vencimento'),
('PAGA', 'Quitada'),
('CANCELADA', 'Cancelada antes do pagamento'),
('PARCELADA', 'Pagamento realizado em partes'),
('ESTORNADA', 'Reembolso ou devolução'),
('EM_DISPUTA', 'Pagamento contestado');

CREATE TABLE IF NOT EXISTS categoria(
    nome VARCHAR(30) PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);

INSERT INTO categoria (nome, descricao) VALUES
('DESPESA_FIXA', 'Contas recorrentes como aluguel, água, luz'),
('DESPESA_VARIAVEL', 'Gastos que mudam mensalmente, como materiais e transporte'),
('RECEITA_RECORRENTE', 'Pagamentos fixos como mensalidades'),
('RECEITA_VARIAVEL', 'Entradas financeiras de vendas avulsas ou serviços pontuais'),
('IMPOSTOS_TAXAS', 'Tributos e obrigações legais'),
('INVESTIMENTOS', 'Aplicações financeiras ou aquisição de bens');

CREATE TABLE IF NOT EXISTS origem (
    nome VARCHAR(30) PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);

INSERT INTO origem (nome, descricao) VALUES
('FORNECEDOR', 'Contas relacionadas a compras ou serviços adquiridos'),
('CLIENTE', 'Pagamentos de vendas ou prestação de serviços'),
('FUNCIONARIO', 'Salários, benefícios e reembolsos'),
('INSTITUICAO_FINANCEIRA', 'Empréstimos, financiamentos e juros bancários'),
('GOVERNO', 'Tributos e incentivos fiscais');

CREATE TABLE IF NOT EXISTS usuario (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    nome VARCHAR(100),
    login VARCHAR(50),
    senha VARCHAR(255),
    email VARCHAR(50)
);

INSERT INTO usuario (id, nome, login, senha, email) VALUES
('e9b1f85d-4a58-4c2e-bb8b-3a41f8a9d1c7', 'Sofia Dev', 'dev', '123456','dev@gmail.com'),
('f3a6df49-8b26-4890-9b56-2ddc94e8f1f1', 'User Teste', 'teste', '123456','teste@gmail.com');

CREATE TABLE IF NOT EXISTS conta (
    id INT PRIMARY KEY,
    vencimento DATE NOT NULL,
    descricao VARCHAR (100) NOT NULL,
    valor DOUBLE PRECISION NOT NULL,
    data_pagamento DATE,
    observacao TEXT,
    imagem TEXT,
    status_enum VARCHAR(30),
    origem_enum VARCHAR(30),
    categoria_enum VARCHAR(30),
    usuario_id UUID,
    FOREIGN KEY (status_enum) REFERENCES "status" (nome),
    FOREIGN KEY (origem_enum) REFERENCES "origem" (nome),
    FOREIGN KEY (categoria_enum) REFERENCES "categoria" (nome),
    FOREIGN KEY (usuario_id) REFERENCES "usuario" (id)
);

INSERT INTO conta (id, vencimento, descricao, valor, data_pagamento, observacao, imagem, status_enum, origem_enum, categoria_enum, usuario_id)
VALUES
(1, '2026-01-01', 'Conta teste', 2000.00, null, 'é isso', 'sem imagem', 'ABERTA', 'CLIENTE', 'DESPESA_FIXA', 'f3a6df49-8b26-4890-9b56-2ddc94e8f1f1');