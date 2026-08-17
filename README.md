# Job API

API REST para uma plataforma de recrutamento, permitindo o gerenciamento de usuários, candidatos, empresas, vagas e candidaturas.

## 🎯 Objetivo

Desenvolver uma API de recrutamento aplicando conceitos de desenvolvimento backend, autenticação, segurança, persistência de dados e arquitetura de software.

## 🚀 Tecnologias

* Java 17
* Spring Boot
* Spring Data JPA
* Spring Security
* JWT
* PostgreSQL
* Docker
* Docker Compose
* Flyway
* Maven
* Swagger / OpenAPI
* Render

## 📌 Funcionalidades

* Cadastro e autenticação de usuários
* Controle de acesso por perfil
* Gerenciamento de candidatos
* Gerenciamento de empresas
* Cadastro e gerenciamento de vagas
* Busca e filtros de vagas
* Candidaturas
* Gerenciamento do processo seletivo
* Documentação da API com Swagger

## 🌐 Deploy

* **Documentação (Swagger UI):** [Acessar Swagger](https://job-api-0tcq.onrender.com/swagger-ui/index.html)
* **Ambiente de Produção (API Base):** [job-api-0tcq.onrender.com](https://job-api-0tcq.onrender.com)

## 📄 Status

🚧 Em desenvolvimento.

## 🏗️ Estrutura

O projeto é organizado por módulos e responsabilidades:

```text
src/main/java/com/rique/job_api/
├── auth/          # Autenticação e usuários
├── candidate/     # Candidatos
├── company/       # Empresas
.
.
.
├── common/        # Recursos compartilhados
├── config/        # Configurações
├── exception/     # Exceções
├── handler/       # Tratamento global de exceções
└── JobApiApplication.java
```

Os módulos são organizados conforme suas responsabilidades, utilizando componentes como `Controller`, `Service`, `Repository`, `Entity` e `DTO`.

## 🗄️ Banco de Dados

* PostgreSQL
* JPA / Hibernate
* Flyway para versionamento das migrations

## 🔐 Segurança

* Spring Security
* Autenticação baseada em JWT
* Controle de acesso por perfil
* BCrypt para proteção de senhas
