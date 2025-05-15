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
    descricao VARCHAR(255) NOT NULL
);

INSERT INTO origem (nome, descricao) VALUES
('FORNECEDOR', 'Contas relacionadas a compras ou serviços adquiridos'),
('CLIENTE', 'Pagamentos de vendas ou prestação de serviços'),
('FUNCIONARIO', 'Salários, benefícios e reembolsos'),
('INSTITUICAO_FINANCEIRA', 'Empréstimos, financiamentos e juros bancários'),
('GOVERNO', 'Tributos e incentivos fiscais');