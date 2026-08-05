# RoomFlow

API REST para gerenciamento de reservas de salas, desenvolvida como projeto final de curso.

O projeto implementa o cadastro e gerenciamento de salas, usuários e reservas, aplicando regras de negócio diretamente na camada de domínio, validação de conflitos de horário, tratamento padronizado de erros e testes automatizados.

## ✨ Destaques

- Arquitetura em camadas
- Regras de negócio encapsuladas nas entidades
- Validações desacopladas utilizando Strategy Pattern
- Tratamento de erros padronizado com `ProblemDetail` (RFC 9457)
- Persistência com Spring Data JPA e Hibernate
- Testes unitários de domínio, services e controllers
- Docker Compose para execução da aplicação
- Pipeline de Integração Contínua com GitHub Actions

---

# Tecnologias

- Java 26
- Spring Boot 4.1
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Bean Validation
- PostgreSQL
- Maven
- JUnit 5
- Mockito
- Docker
- Docker Compose
- GitHub Actions

---

# Funcionalidades

## Salas

- Cadastro de salas
- Atualização de informações
- Ativação e desativação de salas
- Consulta de disponibilidade por período
- Preservação do histórico de reservas (não existe exclusão física)

## Usuários

- Cadastro
- Atualização
- Remoção por anonimização
- Preservação do histórico de reservas para auditoria

## Reservas

- Solicitação de reserva
- Cancelamento de reserva
- Paginação das consultas
- Consulta por sala
- Consulta por período

---

# Regras de Negócio

A API aplica diversas regras diretamente na camada de domínio.

- Não é permitido reservar uma sala desativada.
- Não é permitido realizar reservas para usuários inativos.
- A data de início deve ser anterior à data de término.
- Reservas utilizam **intervalo semiaberto**, permitindo que uma reserva termine exatamente quando outra começa sem gerar conflito.
- Horários conflitantes são rejeitados.
- Salas não são removidas fisicamente, preservando todo o histórico de reservas.
- Usuários removidos têm seus dados anonimizados, mantendo a integridade histórica das reservas.

---

# Arquitetura

```
controller/
│
├── recebe requisições HTTP
├── valida DTOs
└── delega para os services

service/
│
├── orquestra casos de uso
└── coordena regras de negócio

model/
│
├── entidades JPA
└── invariantes do domínio

repository/
│
└── acesso aos dados com Spring Data JPA

dto/
│
└── contratos de entrada e saída da API

validacoes/
│
└── estratégias de validação das reservas

exception/
│
└── exceções de negócio e tratamento centralizado
```

---

# Endpoints

## Salas

| Método | Endpoint | Descrição |
|---------|----------|-----------|
| GET | `/api/v1/salas` | Lista salas ativas |
| GET | `/api/v1/salas/{id}` | Busca sala por ID |
| GET | `/api/v1/salas/disponiveis` | Consulta disponibilidade por período |
| POST | `/api/v1/salas` | Cadastra sala |
| PUT | `/api/v1/salas/{id}` | Atualiza sala |
| PATCH | `/api/v1/salas/ativar/{id}` | Ativa sala |
| PATCH | `/api/v1/salas/desativar/{id}` | Desativa sala |

## Usuários

| Método | Endpoint | Descrição |
|---------|----------|-----------|
| GET | `/api/v1/usuarios` | Lista usuários ativos |
| GET | `/api/v1/usuarios/{id}` | Busca usuário por ID |
| POST | `/api/v1/usuarios` | Cadastra usuário |
| PUT | `/api/v1/usuarios/{id}` | Atualiza usuário |
| DELETE | `/api/v1/usuarios/{id}` | Anonimiza usuário |

## Reservas

| Método | Endpoint | Descrição |
|---------|----------|-----------|
| GET | `/api/v1/reservas` | Lista reservas (paginado) |
| GET | `/api/v1/reservas/ativas` | Lista reservas ativas |
| GET | `/api/v1/reservas/{id}` | Busca reserva por ID |
| GET | `/api/v1/reservas/sala?idSala=` | Lista reservas de uma sala |
| GET | `/api/v1/reservas/periodo` | Consulta reservas por período |
| POST | `/api/v1/reservas` | Solicita reserva |
| PATCH | `/api/v1/reservas/cancelar/{id}` | Cancela reserva |

---

# Como executar

## Utilizando Docker Compose (recomendado)

```bash
docker compose up --build
```

A aplicação ficará disponível em:

```
http://localhost:8080
```

O comando inicia automaticamente:

- API RoomFlow
- Banco PostgreSQL

---

## Executando localmente

Pré-requisitos:

- Java 26
- Maven
- PostgreSQL

Configure as variáveis de ambiente necessárias e execute:

```bash
mvn spring-boot:run
```

---

# Executando os testes

```bash
mvn test
```

A suíte de testes cobre:

- Entidades de domínio
- Regras de validação
- Services
- Controllers (`@WebMvcTest`)

Utilizando:

- JUnit 5
- Mockito

---

# Estrutura do Projeto

```
src
└── main
    └── java
        └── br.com.pedrobuzolin.roomflow
            ├── controller
            ├── dto
            ├── exception
            ├── model
            ├── repository
            ├── service
            └── validacoes
```

---

# Próximas melhorias

- Testes de integração utilizando `@DataJpaTest`
- Documentação automática da API com OpenAPI/Swagger
- Autenticação e autorização com Spring Security
- Cache para consultas de disponibilidade
- Observabilidade (logs estruturados e métricas)

---

# Objetivos do projeto

Este projeto foi desenvolvido com foco em aplicar boas práticas de desenvolvimento backend utilizando Spring Boot, incluindo:

- Modelagem orientada a domínio
- Separação de responsabilidades
- Encapsulamento das regras de negócio
- Tratamento consistente de exceções
- Testes automatizados
- Persistência com JPA/Hibernate
- Boas práticas na construção de APIs REST
- Integração contínua com GitHub Actions

---

# Licença

Este projeto foi desenvolvido para fins de estudo e portfólio.