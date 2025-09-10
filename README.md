# 📚 BookStore API

Uma API RESTful desenvolvida com Spring Boot para gerenciar empréstimos de livros em bibliotecas online ou presenciais. Permite operações com livros, autores, clientes e pedidos. O projeto é modularizado por domínio e inclui autenticação com JWT, OAuth2 com Google, múltiplas camadas (controller, service, repository, validation, DTO, model...) e documentação com Swagger.

## 🚀 Tecnologias Utilizadas
- Java 21
- Spring Boot
  - Spring Web
  - Spring Data JPA
  - Spring Security (JWT e OAuth2 com Google)
- PostgreSQL
- Docker
- Swagger (Springdoc OpenAPI)
- Lombok
- MapStruct

## 🏗️ Arquitetura

A API segue uma **arquitetura em camadas**, com os pacotes organizados por tipo de responsabilidade. Os domínios (`book`, `author`, `client`, `order`) estão distribuídos dentro dessas camadas, o que facilita a manutenção, escalabilidade e reutilização de componentes.

**Estrutura dos pacotes:**

- `model` — Representações das entidades (livros, autores, clientes, pedidos)
- `dto` — Objetos de transferência de dados para entrada e saída
- `mapper` — Mapeamento entre entidades e DTOs com MapStruct
- `controller` — Manipulação das requisições HTTP
- `service` — Lógica de negócio centralizada
- `repository` — Acesso ao banco de dados com Spring Data JPA
- `validator` — Validações auxiliares e regras específicas
- `security` — Autenticação e autorização com JWT e OAuth2
- `exception` — Tratamento global de erros com `@ControllerAdvice`
- `config`, `docs`, `util`, `common` — Suporte geral e configurações da aplicação

Essa organização permite que cada camada tenha uma função bem definida, mantendo o código limpo, modular e alinhado com os princípios da arquitetura limpa.

## 🔐 Segurança

A autenticação é feita com JWT (JSON Web Token):

- Endpoints públicos:
  - `POST /auth/login`
  - `POST /client`
- Os demais endpoints exigem o token JWT no cabeçalho:


## 📌 Endpoints Disponíveis

- **Pedidos**
- `POST /orders{id}/return` → devolve um livro emprestado
- `POST /orders` → cria um novo pedido
- `GET /orders` → busca todos os pedidos com filtros
- `GET /orders/{id}` → busca pedido por ID
- `PUT /orders/{id}` → atualiza pedido por ID
- `DELETE /orders/{id}` → exclui pedido por ID

- **Autores**
- `POST /authors` → cria um novo autor
- `GET /authors` → busca todos os autores com filtros
- `GET /authors/{id}` → busca autor por ID
- `PUT /authors/{id}` → atualiza autor por ID
- `DELETE /authors/{id}` → exclui autor por ID

- **Livros**
- `POST /books` → cria um novo livro
- `GET /books` → busca todos os livros com filtros
- `GET /books/{id}` → busca livro por ID
- `PUT /books/{id}` → atualiza livro por ID
- `DELETE /books/{id}` → exclui livro por ID

- **Clientes**
- `POST /clients` → cria um novo cliente
- `GET /client` → busca todos os clientes com filtros
- `GET /client/{id}` → busca cliente por ID
- `PUT /client/{id}` → atualiza cliente por ID
- `DELETE /client/{id}` → exclui cliente por ID

As permissões são gerenciadas com `@PreAuthorize` e filtros do Spring Security.

## 🌐 Autenticação OAuth2 com Google

A API permite login com contas do Google:

- Endpoint:
- `/oauth2/authorization/google`
- Após a autenticação, o JWT é gerado automaticamente.
- Configuração via `application.yml`:

```yaml
client-id: ${CLIENT_ID}
client-secret: ${CLIENT_SECRET}
```


## 📌 API Documentation

Documentação disponível após iniciar a aplicação:

👉 http://localhost:8081/swagger-ui/index.html#/

## 📁 Log

Os logs são registrados no arquivo `bookstore.log`, conforme configurado em `application.yml`.

---

## ▶️ Como Rodar

### ✅ Pré-requisitos
- Java 21
- Maven
- Docker (for PostgreSQL database)
- Configured Google OAuth2 account

### 📦 Inicie o PostgreSQL com o Docker (opcional)
```bash
docker run --name postgres-bookstore -e POSTGRES_PASSWORD=senha -p 5432:5432 -d postgres
```

### 🧪 Configure `application.yml`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bookstore
    username: postgres
    password: senha
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${CLIENT_ID}
            client-secret: ${CLIENT_SECRET}
```

### 1. Clone o repositório
```bash
git clone https://github.com/CaiquePirs/bookstore.git
```

### 2. Configurar `application.yml` (veja acima)

### 3. Rode a applicação
```bash
./mvnw spring-boot:run
```

---

## 👨‍💻 Author

BookStore API foi desenvolvido por mim **Caique Pires**. Contribuições são bem vindas!

## 📧 Contact

[![Gmail](https://img.shields.io/badge/Email-Gmail-red?style=flat&logo=gmail)](mailto:pirescaiq@gmail.com)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-LinkedIn-blue?style=flat&logo=linkedin)](https://www.linkedin.com/in/caique-pires-8843aa332)
