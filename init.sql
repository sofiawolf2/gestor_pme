CREATE TABLE status (
    nome VARCHAR(20) PRIMARY KEY,
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