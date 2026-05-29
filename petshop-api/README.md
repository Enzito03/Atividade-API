# 🐾 PetShop API

API REST para e-commerce de produtos para pets, desenvolvida com **Spring Boot 3** e **PostgreSQL**.

---

## 🛠️ Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 3.2.5 |
| Spring Data JPA | - |
| PostgreSQL | 15+ |
| Lombok | - |
| SpringDoc OpenAPI (Swagger) | 2.5.0 |
| Maven | 3.8+ |

---

## 📁 Estrutura do Projeto

```
petshop-api/
├── src/
│   └── main/
│       ├── java/com/petshop/api/
│       │   ├── config/          # Configurações (Swagger)
│       │   ├── controller/      # Controllers REST
│       │   ├── dto/
│       │   │   ├── request/     # DTOs de entrada
│       │   │   └── response/    # DTOs de resposta
│       │   ├── entity/          # Entidades JPA
│       │   ├── exception/       # Exceções customizadas e Handler global
│       │   ├── repository/      # Repositórios Spring Data JPA
│       │   └── service/         # Regras de negócio
│       └── resources/
│           ├── application.properties
│           └── db/
│               └── petshop_db.sql   # Script do banco
└── pom.xml
```

---

## ⚙️ Configuração e Execução

### Pré-requisitos

- Java 21+
- Maven 3.8+
- PostgreSQL 15+

### 1. Criar o banco de dados

```bash
# Conectar ao PostgreSQL
psql -U postgres

# Criar banco e tabelas (execute o script)
psql -U postgres -f src/main/resources/db/petshop_db.sql
```

### 2. Configurar credenciais

Edite o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/petshop_db
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

### 3. Executar a aplicação

```bash
# Instalar dependências e compilar
mvn clean install

# Executar
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

---

## 📖 Documentação dos Endpoints

Acesse o **Swagger UI** em: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🔗 Endpoints

### Categorias — `/api/v1/categorias`

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/v1/categorias` | Listar todas as categorias |
| GET | `/api/v1/categorias/{id}` | Buscar categoria por ID |
| POST | `/api/v1/categorias` | Criar nova categoria |
| PUT | `/api/v1/categorias/{id}` | Atualizar categoria |
| DELETE | `/api/v1/categorias/{id}` | Excluir categoria |

### Produtos — `/api/v1/produtos`

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/v1/produtos` | Listar produtos (filtros: `nome`, `categoriaId`, paginação) |
| GET | `/api/v1/produtos/{id}` | Buscar produto por ID |
| GET | `/api/v1/produtos/categoria/{categoriaId}` | Listar por categoria |
| GET | `/api/v1/produtos/estoque-baixo?quantidade=5` | Produtos com estoque baixo |
| POST | `/api/v1/produtos` | Cadastrar produto |
| PUT | `/api/v1/produtos/{id}` | Atualizar produto |
| DELETE | `/api/v1/produtos/{id}` | Desativar produto (soft delete) |

### Clientes — `/api/v1/clientes`

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/v1/clientes` | Listar clientes (filtro: `nome`, paginação) |
| GET | `/api/v1/clientes/{id}` | Buscar cliente por ID |
| POST | `/api/v1/clientes` | Cadastrar cliente |
| PUT | `/api/v1/clientes/{id}` | Atualizar cliente |
| DELETE | `/api/v1/clientes/{id}` | Desativar cliente (soft delete) |

### Pedidos — `/api/v1/pedidos`

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/v1/pedidos` | Listar pedidos (filtro: `status`, paginação) |
| GET | `/api/v1/pedidos/{id}` | Buscar pedido por ID |
| GET | `/api/v1/pedidos/cliente/{clienteId}` | Pedidos de um cliente |
| POST | `/api/v1/pedidos` | Criar novo pedido |
| PATCH | `/api/v1/pedidos/{id}/status` | Atualizar status |
| PATCH | `/api/v1/pedidos/{id}/cancelar` | Cancelar pedido |

---

## 📦 Exemplos de Requisição

### Criar Categoria
```json
POST /api/v1/categorias
{
  "nome": "Alimentação",
  "descricao": "Rações e petiscos para pets"
}
```

### Criar Produto
```json
POST /api/v1/produtos
{
  "nome": "Ração Premium 15kg",
  "descricao": "Ração super premium para cães adultos",
  "preco": 189.90,
  "estoque": 50,
  "categoriaId": 1
}
```

### Criar Cliente
```json
POST /api/v1/clientes
{
  "nome": "Maria Silva",
  "cpf": "12345678901",
  "email": "maria@email.com",
  "telefone": "11987654321",
  "logradouro": "Rua das Flores",
  "numero": "123",
  "bairro": "Centro",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01310100"
}
```

### Criar Pedido
```json
POST /api/v1/pedidos
{
  "clienteId": 1,
  "valorFrete": 15.00,
  "enderecoEntrega": "Rua das Flores, 123 - Centro - São Paulo/SP",
  "itens": [
    { "produtoId": 1, "quantidade": 2 },
    { "produtoId": 4, "quantidade": 1 }
  ]
}
```

### Atualizar Status do Pedido
```json
PATCH /api/v1/pedidos/1/status
{
  "status": "PAGO"
}
```

---

## 🔄 Fluxo de Status do Pedido

```
AGUARDANDO_PAGAMENTO → PAGO → EM_SEPARACAO → ENVIADO → ENTREGUE
                                                    ↘
                                                  CANCELADO
```

> Pedidos com status `ENTREGUE` ou `CANCELADO` não podem ter o status alterado.  
> Ao cancelar um pedido, o estoque dos produtos é devolvido automaticamente.

---

## 🧩 Modelo de Dados

```
Categoria (1) ──── (N) Produto
Cliente (1)   ──── (N) Pedido
Pedido  (1)   ──── (N) ItemPedido
Produto (1)   ──── (N) ItemPedido
```

---

## ✅ Funcionalidades Implementadas

- [x] CRUD completo de Categorias
- [x] CRUD completo de Produtos com soft delete
- [x] CRUD completo de Clientes com soft delete
- [x] Criação e acompanhamento de Pedidos
- [x] Controle automático de estoque
- [x] Devolução de estoque ao cancelar pedido
- [x] Paginação e filtros nos endpoints de listagem
- [x] Validação de dados de entrada com Bean Validation
- [x] Tratamento global de exceções
- [x] Documentação Swagger/OpenAPI
- [x] Dados de exemplo no script SQL
