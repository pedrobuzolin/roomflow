# RoomFlow

API REST para gerenciamento de salas e reservas, desenvolvida com **Java e Spring Boot**, utilizando PostgreSQL para persistência dos dados.

O projeto implementa o gerenciamento de usuários, salas e reservas, com foco em regras de negócio, integridade dos dados, controle de conflitos de horários e organização em camadas.

## Destaques técnicos

- Arquitetura monolítica em camadas.
- Regras de negócio encapsuladas nas entidades de domínio.
- Validações desacopladas utilizando Strategy Pattern.
- Controle de conflitos de reservas com bloqueio pessimista (`PESSIMISTIC_WRITE`).
- Persistência com Spring Data JPA e Hibernate.
- Versionamento do banco de dados com Flyway.
- Validação do schema com Hibernate (`ddl-auto=validate`).
- Tratamento padronizado de erros com `ProblemDetail` (RFC 9457).
- Testes automatizados com JUnit 5 e Mockito.
- Containerização da aplicação e do banco de dados com Docker Compose.
- Integração contínua com GitHub Actions.

## Tecnologias

| Tecnologia | Utilização |
|---|---|
| Java 26 | Linguagem |
| Spring Boot 4.1 | Framework principal |
| Spring Web MVC | Construção da API REST |
| Spring Data JPA | Persistência de dados |
| Hibernate | ORM |
| Bean Validation | Validação de dados |
| PostgreSQL 17 | Banco de dados |
| Flyway | Versionamento de migrations |
| Maven | Gerenciamento de dependências e build |
| JUnit 5 | Testes automatizados |
| Mockito | Simulação de dependências nos testes |
| Docker | Containerização |
| Docker Compose | Orquestração dos containers |
| GitHub Actions | Integração contínua |

## Funcionalidades

### Salas
- Cadastro e atualização de salas.
- Ativação e desativação.
- Consulta de disponibilidade por período.
- Preservação do histórico de reservas, sem exclusão física.

### Usuários
- Cadastro e atualização.
- Consulta de usuários.
- Anonimização de dados na remoção.
- Preservação do histórico de reservas.

### Reservas
- Solicitação e cancelamento de reservas.
- Consulta de reservas por sala e período.
- Listagem paginada.
- Consulta de reservas ativas.
- Validação de conflitos de horários.

## Regras de negócio

O sistema aplica regras para preservar a consistência das operações:

- Salas desativadas não podem receber novas reservas.
- Usuários inativos não podem realizar reservas.
- A data de início deve ser anterior à data de término.
- Reservas com horários conflitantes são rejeitadas.
- Os intervalos são semiabertos: uma reserva pode terminar exatamente no horário em que outra começa.
- A criação de reservas utiliza bloqueio pessimista da sala para controlar operações concorrentes.
- Salas não são excluídas fisicamente.
- Usuários removidos têm seus dados anonimizados, preservando o histórico relacionado às reservas.

## Arquitetura

O projeto utiliza arquitetura em camadas, separando responsabilidades:

```text
controller/
    Recebe requisições HTTP,
    valida DTOs e delega aos serviços.

service/
    Orquestra os casos de uso
    e coordena as operações.

model/
    Contém as entidades JPA
    e regras de negócio do domínio.

repository/
    Responsável pelo acesso
    e persistência dos dados.

dto/
    Define os contratos de entrada
    e saída da API.

validacoes/
    Contém estratégias de validação
    das reservas.

exception/
    Contém exceções de negócio
    e tratamento centralizado de erros.
```

## Endpoints

Base URL: `http://localhost:8080`

### Salas

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/v1/salas` | Lista salas ativas |
| GET | `/api/v1/salas/{id}` | Busca sala por ID |
| GET | `/api/v1/salas/disponiveis` | Consulta disponibilidade por período |
| POST | `/api/v1/salas` | Cadastra sala |
| PUT | `/api/v1/salas/{id}` | Atualiza sala |
| PATCH | `/api/v1/salas/ativar/{id}` | Ativa sala |
| PATCH | `/api/v1/salas/desativar/{id}` | Desativa sala |

### Usuários

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/v1/usuarios` | Lista usuários ativos |
| GET | `/api/v1/usuarios/{id}` | Busca usuário por ID |
| POST | `/api/v1/usuarios` | Cadastra usuário |
| PUT | `/api/v1/usuarios/{id}` | Atualiza usuário |
| DELETE | `/api/v1/usuarios/{id}` | Anonimiza usuário |

### Reservas

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/v1/reservas` | Lista reservas paginadas |
| GET | `/api/v1/reservas/ativas` | Lista reservas ativas |
| GET | `/api/v1/reservas/{id}` | Busca reserva por ID |
| GET | `/api/v1/reservas/sala?idSala={id}` | Lista reservas de uma sala |
| GET | `/api/v1/reservas/periodo` | Consulta reservas por período |
| POST | `/api/v1/reservas` | Solicita reserva |
| PATCH | `/api/v1/reservas/cancelar/{id}` | Cancela reserva |

## Como executar

### Pré-requisitos

- Java 26
- Maven
- Docker e Docker Compose

### Utilizando Docker Compose

O projeto utiliza variáveis de ambiente para configurar as credenciais do PostgreSQL.

No PowerShell, configure:

```powershell
$env:DB_USER="roomflow"
$env:DB_PASSWORD="sua_senha"
```

Como o Dockerfile utiliza um JAR previamente compilado, execute o build antes de iniciar os containers:

```powershell
.\mvnw.cmd clean package -DskipTests
```

Em seguida, inicie a aplicação:

```powershell
docker compose up --build
```

A aplicação ficará disponível em:

`http://localhost:8080`

O PostgreSQL ficará disponível na porta `5432`.

O Docker Compose inicia os seguintes serviços:

- **app:** aplicação Spring Boot.
- **db:** banco de dados PostgreSQL.

O banco utiliza um volume persistente para preservar os dados entre reinicializações dos containers.

Para encerrar os serviços:

```powershell
docker compose down
```

### Executando localmente

É necessário possuir uma instância PostgreSQL em execução e configurar as seguintes variáveis de ambiente:

```powershell
$env:DB_HOST="localhost:5432"
$env:DB_NAME="roomflow"
$env:DB_USER="roomflow"
$env:DB_PASSWORD="sua_senha"
```

Execute a aplicação com:

```powershell
.\mvnw.cmd spring-boot:run
```

As migrations do banco são gerenciadas pelo Flyway durante a inicialização da aplicação.

## Testes automatizados

Para executar os testes:

```powershell
.\mvnw.cmd test
```

O projeto utiliza:

- **JUnit 5:** estruturação e execução dos testes.
- **Mockito:** simulação de dependências.
- **Spring Boot Test:** suporte aos testes da aplicação.

A cobertura atual contempla testes unitários de domínio, regras de validação, serviços e controllers.

## Escopo atual

Esta versão utiliza arquitetura monolítica e não implementa autenticação e autorização de usuários.

A aplicação foi desenvolvida para fins de estudo e portfólio, com foco na construção de APIs REST, modelagem de domínio, persistência relacional e aplicação de regras de negócio.

## Objetivos de desenvolvimento

O RoomFlow foi desenvolvido para consolidar conhecimentos em:

- Desenvolvimento backend com Java e Spring Boot.
- Construção e organização de APIs REST.
- Modelagem de entidades e relacionamentos JPA.
- Encapsulamento de regras de negócio.
- Controle de transações e concorrência.
- Integridade e versionamento de banco de dados.
- Testes automatizados.
- Containerização e integração contínua.
