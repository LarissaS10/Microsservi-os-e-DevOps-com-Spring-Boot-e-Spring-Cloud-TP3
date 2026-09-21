# API Vendas — Autenticação e Autorização com JWT (TP3)

## Arquitetura da solução

O sistema é composto por microsserviços independentes, registrados num Discovery Server e acessados através de um API Gateway, que é o único ponto de entrada externo:
                    ┌──────────────────┐
                    │  Eureka Server    │
                    │   (porta 8761)    │
                    └─────────▲─────────┘
                              │ registro/descoberta
    ┌─────────────┬──────────┼───────────┬──────────────┐
    │             │          │           │              │

┌───────▼──────┐ ┌────▼───────┐ ┌▼─────────┐ ┌▼───────────┐ ┌▼────────────┐
│ Config Server│ │API Gateway │ │auth-service│ │produtos-svc│ │vendas/clientes│
│ (porta 8888)│ │(porta 8085)│ │(porta 8084)│ │(porta 8081)│ │ (8082/8083) │
└──────────────┘ └─────┬──────┘ └────────────┘ └────────────┘ └───────────────┘
│ valida token em toda requisição (exceto login/cadastro/refresh)
▼
cliente externo


O **auth-service** é totalmente independente dos demais: tem seu próprio banco de dados (H2 em memória, `authdb`), não chama nem depende de nenhum outro microsserviço de negócio para funcionar.

A proteção das rotas acontece de forma centralizada, no **API Gateway**: um filtro global (`TokenFilter`) intercepta toda requisição, confere se existe um token JWT válido no header `Authorization`, e só deixa passar se o token for autêntico, não estiver expirado e for do tipo correto (token de acesso, não de refresh).

## Tecnologia escolhida: JWT (JSON Web Token)

A autenticação usa dois tokens JWT com finalidades diferentes:

- **Access token**: usado para acessar as rotas protegidas. Vida curta — expira em **15 minutos**.
- **Refresh token**: usado apenas para obter um novo access token, sem precisar fazer login novamente. Vida longa — expira em **7 dias**.

Os dois são assinados com a mesma chave secreta (HMAC-SHA), e o Gateway confere não só a assinatura, mas também um campo `type` dentro do token, para impedir que um refresh token seja usado como se fosse um access token.

## Como executar os serviços

Suba os serviços nesta ordem (cada um espera o anterior estar de pé):

1. `eureka-server` (porta 8761)
2. `config-server` (porta 8888)
3. `auth-service` (porta 8084)
4. `produtos-service`, `vendas-service`, `clientes-service` (portas 8081, 8082, 8083)
5. `gateway` (porta 8085)

Todo acesso externo deve ser feito através do Gateway: `http://localhost:8085`.

## Como realizar autenticação

**1. Cadastrar um usuário** (rota pública):

POST http://localhost:8085/auth-service/usuarios
Content-Type: application/json

{
"nome": "Maria",
"email": "maria@email.com",
"senha": "123456"
}

Resposta: `201 Created` com o id do usuário.

**2. Fazer login** (rota pública):

POST http://localhost:8085/auth-service/usuarios/login
Content-Type: application/json

{
"email": "maria@email.com",
"senha": "123456"
}

Resposta: `201 Created`
```json
{
  "token": "eyJhbGciOi...",
  "refreshToken": "eyJhbGciOi..."
}
```

Credenciais inválidas (email inexistente ou senha errada) retornam `401 Unauthorized`.

**3. Usar o token nas requisições seguintes**, no header:

Authorization: Bearer eyJhbGciOi...


## Como utilizar o endpoint de refresh

Quando o access token expira (após 15 minutos), em vez de fazer login de novo, envie o refresh token:

POST http://localhost:8085/auth-service/usuarios/refresh
Content-Type: application/json

{
"refreshToken": "eyJhbGciOi..."
}


Resposta: `200 OK`
```json
{
  "token": "eyJhbGciOi..."
}
```
(um novo access token, válido por mais 15 minutos)

Se o refresh token estiver expirado, inválido, ou não for do tipo "refresh", a resposta é `401 Unauthorized`.

## Endpoints públicos (não exigem token)

| Endpoint | Descrição |
|---|---|
| `POST /auth-service/usuarios` | Cadastro de novo usuário |
| `POST /auth-service/usuarios/login` | Login (retorna access token + refresh token) |
| `POST /auth-service/usuarios/refresh` | Renovação do access token |

## Endpoints protegidos (exigem `Authorization: Bearer <token>`)

| Endpoint | Descrição |
|---|---|
| `GET /produtos-service/produtos` | Lista todos os produtos |
| `GET /produtos-service/produtos/{id}` | Busca um produto por id |
| `GET /clientes-service/clientes` | Lista todos os clientes |
| `GET /vendas-service/vendas` | Lista todas as vendas |
| `POST /vendas-service/vendas` | Registra uma nova venda |

Qualquer requisição a essas rotas sem um token válido retorna `401 Unauthorized`.

## Exemplos de requisições para teste

**Tentativa sem autenticação (deve falhar):**

GET http://localhost:8085/produtos-service/produtos
→ 401 Unauthorized


**Acesso autenticado (deve funcionar):**

GET http://localhost:8085/produtos-service/produtos
Authorization: Bearer <access_token>
→ 200 OK


**Fluxo completo de teste:**
1. `POST /auth-service/usuarios` → cadastra usuário → `201`
2. `POST /auth-service/usuarios/login` → obtém token + refreshToken → `201`
3. `GET /produtos-service/produtos` com o token → `200`
4. `POST /auth-service/usuarios/refresh` com o refreshToken → obtém novo token → `200`
5. `GET /produtos-service/produtos` com o novo token → `200`
6. `POST /auth-service/usuarios/login` com senha errada → `401`
