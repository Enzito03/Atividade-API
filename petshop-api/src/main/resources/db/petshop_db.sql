-- ============================================================
-- PetShop API - Script de criação do banco de dados
-- PostgreSQL
-- ============================================================

-- Criar banco de dados (execute como superuser)
CREATE DATABASE petshop_db
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'pt_BR.UTF-8'
    LC_CTYPE = 'pt_BR.UTF-8'
    TEMPLATE = template0;

-- Conectar ao banco
\c petshop_db;

-- ============================================================
-- TABELAS
-- ============================================================

CREATE TABLE IF NOT EXISTS categorias (
    id          BIGSERIAL PRIMARY KEY,
    nome        VARCHAR(100) NOT NULL UNIQUE,
    descricao   VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS produtos (
    id          BIGSERIAL PRIMARY KEY,
    nome        VARCHAR(150) NOT NULL,
    descricao   VARCHAR(500),
    preco       NUMERIC(12, 2) NOT NULL,
    estoque     INTEGER NOT NULL DEFAULT 0,
    url_imagem  VARCHAR(500),
    ativo       BOOLEAN NOT NULL DEFAULT TRUE,
    categoria_id BIGINT NOT NULL REFERENCES categorias(id)
);

CREATE TABLE IF NOT EXISTS clientes (
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(150) NOT NULL,
    cpf             CHAR(11) NOT NULL UNIQUE,
    email           VARCHAR(150) NOT NULL UNIQUE,
    telefone        VARCHAR(11),
    data_nascimento DATE,
    logradouro      VARCHAR(200),
    numero          VARCHAR(10),
    complemento     VARCHAR(100),
    bairro          VARCHAR(100),
    cidade          VARCHAR(100),
    estado          CHAR(2),
    cep             CHAR(8),
    ativo           BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS pedidos (
    id                BIGSERIAL PRIMARY KEY,
    cliente_id        BIGINT NOT NULL REFERENCES clientes(id),
    data_pedido       TIMESTAMP NOT NULL DEFAULT NOW(),
    data_atualizacao  TIMESTAMP,
    status            VARCHAR(30) NOT NULL DEFAULT 'AGUARDANDO_PAGAMENTO',
    valor_total       NUMERIC(12, 2) NOT NULL DEFAULT 0,
    valor_frete       NUMERIC(12, 2) NOT NULL DEFAULT 0,
    endereco_entrega  VARCHAR(500),
    observacao        VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS itens_pedido (
    id              BIGSERIAL PRIMARY KEY,
    pedido_id       BIGINT NOT NULL REFERENCES pedidos(id),
    produto_id      BIGINT NOT NULL REFERENCES produtos(id),
    quantidade      INTEGER NOT NULL,
    preco_unitario  NUMERIC(12, 2) NOT NULL,
    subtotal        NUMERIC(12, 2) NOT NULL
);

-- ============================================================
-- ÍNDICES
-- ============================================================

CREATE INDEX idx_produtos_categoria ON produtos(categoria_id);
CREATE INDEX idx_produtos_ativo ON produtos(ativo);
CREATE INDEX idx_pedidos_cliente ON pedidos(cliente_id);
CREATE INDEX idx_pedidos_status ON pedidos(status);
CREATE INDEX idx_itens_pedido ON itens_pedido(pedido_id);

-- ============================================================
-- DADOS DE EXEMPLO
-- ============================================================

INSERT INTO categorias (nome, descricao) VALUES
('Alimentação', 'Rações, petiscos e complementos alimentares'),
('Higiene e Beleza', 'Shampoos, condicionadores, escova e acessórios de banho'),
('Brinquedos', 'Brinquedos para cães e gatos'),
('Acessórios', 'Coleiras, guias, camas e casinhas'),
('Saúde', 'Medicamentos, suplementos e produtos veterinários'),
('Aquários', 'Produtos para peixes e aquários');

INSERT INTO produtos (nome, descricao, preco, estoque, ativo, categoria_id) VALUES
('Ração Premium Cão Adulto 15kg', 'Ração super premium para cães adultos de médio e grande porte', 189.90, 50, true, 1),
('Ração Gato Castrado 3kg', 'Ração específica para gatos castrados, controle de peso', 79.90, 35, true, 1),
('Petisco Ossinho Dental 200g', 'Petisco para limpeza dental de cães', 24.90, 100, true, 1),
('Shampoo Neutro para Cães 500ml', 'Shampoo suave com pH neutro, indicado para uso frequente', 34.90, 60, true, 2),
('Condicionador Pelagem Brilhante 500ml', 'Condicionador para cães e gatos, hidratação profunda', 29.90, 45, true, 2),
('Bola de Borracha Maciça', 'Bola resistente para cães de médio e grande porte', 19.90, 80, true, 3),
('Arranhador para Gatos com Sisal', 'Arranhador vertical com 3 andares e brinquedo com mola', 89.90, 20, true, 3),
('Coleira Ajustável Nylon P', 'Coleira resistente nylon, ajustável, para cães pequenos', 22.90, 70, true, 4),
('Cama Pet Ortopédica M', 'Cama com espuma de memória para cães e gatos de porte médio', 129.90, 15, true, 4),
('Antipulgas Coleira 8 meses', 'Coleira antipulgas e carrapatos, proteção de 8 meses', 69.90, 40, true, 5);

INSERT INTO clientes (nome, cpf, email, telefone, logradouro, numero, bairro, cidade, estado, cep) VALUES
('Maria Silva Santos', '12345678901', 'maria.santos@email.com', '11987654321', 'Rua das Flores', '123', 'Jardim Primavera', 'São Paulo', 'SP', '01310100'),
('João Pedro Oliveira', '98765432100', 'joao.oliveira@email.com', '11976543210', 'Av. Paulista', '1000', 'Bela Vista', 'São Paulo', 'SP', '01311000'),
('Ana Carolina Lima', '11122233344', 'ana.lima@email.com', '11965432109', 'Rua Augusta', '500', 'Consolação', 'São Paulo', 'SP', '01305000');
