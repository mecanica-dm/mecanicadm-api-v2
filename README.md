# Mecânica DM - API de Gestão para Oficinas

[![SonarCloud](https://img.shields.io/badge/SonarCloud-Security_&_Quality-orange?logo=sonarcloud)](https://sonarcloud.io/project/overview?id=mecanica-dm_mecanicadm-api-v1)
![Versão da API](https://img.shields.io/badge/version-0.0.1-blue)

API RESTful para o sistema **Mecânica DM**, uma solução completa para gerenciamento de ordens de serviço, clientes, estoque e fluxo de trabalho em oficinas mecânicas.

---

## 📚 Documentação Externa e Modelagem

- **[Storytelling (Egon.io)](https://github.com/user-attachments/assets/fd2adfad-1fa9-469d-958a-1cf902666b36)**: Storytelling inicial do fluxo de funcionamento da mecânica.
- **[Dicionário de Dados (Notion)](https://lopsided-hourglass-4f8.notion.site/Dicion-rio-de-dados-32f0a8ca8e738075ae44c9ec0b5180b3?source=copy_link)**: Visão detalhada das entidades e relacionamentos do banco de dados.
- **[Event Storming (Miro)](https://miro.com/app/board/uXjVIT7cD_4=/?share_link_id=217316260154)**: Mapeamento de domínio e comportamento orientado a eventos do sistema.
- **[Dashboard do SonarCloud](https://sonarcloud.io/project/overview?id=mecanica-dm_mecanicadm-api-v1)**: Análise contínua de qualidade de código, vulnerabilidades e cobertura de testes.

---

## ✨ Funcionalidades Principais

- **🔩 Gestão de Ordens de Serviço**: Ciclo de vida completo, desde a criação, diagnóstico, execução até a entrega.
- **💰 Geração e Controle de Orçamentos**: Criação detalhada de orçamentos vinculados às O.S., controle de status de aprovação pelo cliente e gestão de custos de peças e mão de obra.
- **🖨️ Impressão de Orçamento**: Geração de relatórios de orçamento em formato PDF, retornados via API como string Base64 para facilitar o tráfego e a integração.
- **👤 Gestão de Clientes e Veículos**: Cadastro e consulta de clientes e veículos.
- **📦 Controle de Estoque**: Gerenciamento de materiais, com dedução automática de estoque ao adicionar em uma O.S.
- **🛠️ Catálogo de Serviços (Mão de Obra)**: Cadastro dos serviços prestados pela oficina.
- **🔐 Autenticação e Autorização**: Segurança baseada em JWT e criptografia de credenciais.
- **📊 Analytics**: Endpoints para extração de métricas e relatórios de performance.
- **🌐 Suporte a Múltiplos Idiomas**: Mensagens de erro e validação em Português, Inglês e Espanhol.

### 🖨️ Testando a Impressão de Orçamento (Base64 -> PDF)

A rota de impressão de orçamentos retorna o arquivo PDF codificado em uma string **Base64**. Para visualizar o arquivo gerado durante os seus testes no Swagger ou Postman:

1. Copie a string Base64 retornada no corpo da resposta (`response body`).
2. Acesse uma ferramenta confiável de conversão online, como:
    - **[Base64.guru - Decode Base64 to PDF](https://base64.guru/converter/decode/pdf)**
3. Cole a string copiada no campo principal do site e clique em "Decode". A visualização do arquivo PDF começará imediatamente.

---

## 🚀 Tecnologias Utilizadas

| Categoria | Tecnologia |
|-----------|--------------------------------------------------------------------------------|
| **Core** | Java 21, Spring Boot 3 |
| **Dados** | Spring Data JPA, PostgreSQL, Flyway (Migrations) |
| **Segurança** | Spring Security, JWT (Java JWT) |
| **Documentação**| Springdoc (Swagger/OpenAPI 3) |
| **Testes** | JUnit 5, Mockito, REST Assured, H2 (Banco em memória) |
| **Build** | Maven |
| **Container** | Docker, Docker Compose |

### Uso do PostgreSQL

O time decidiu pela utilização do PostgreSQL pelo fato de ser um banco relacional, robusto, de fácil configuração e uso. Os membros já tinham experiência com o uso de PostgreSQL e chegaram em um acordo de utilizá-lo como padrão do projeto.

---

## 🏛️ Arquitetura e Decisões (ADRs)

O projeto segue uma arquitetura em camadas, inspirada em princípios de _Clean Architecture_ e _Domain-Driven Design (DDD)_, para garantir separação de responsabilidades, testabilidade e manutenibilidade. Além disso, as diretrizes de código são guiadas por **Architecture Decision Records (ADRs)** armazenadas no projeto.

- `domain`: Contém as entidades, agregados e regras de negócio principais.
- `usecase`: Orquestra o fluxo de operações, implementando os casos de uso através de `Commands` e `Queries`.
- `adapter`: Conecta o núcleo da aplicação com o mundo exterior (Controllers, Repositories).
- `service`: Implementações concretas dos casos de uso isolados.
- `infra`: Configurações de infraestrutura, segurança, tratamento global de exceções e configurações de banco de dados.

### 📝 Nossas ADRs (`docs/adr/`):

- **[ADR 001 - Nomenclatura de Consultas](docs/adr/001-nomenclatura_consultas.md)**
- **[ADR 002 - Padrão UseCases e Commands](docs/adr/002-padrao_usecases_commands.md)**
- **[ADR 003 - Lógica de Negócio no Domínio](docs/adr/003-logica_negocio_dominio.md)**
- **[ADR 004 - Padrão de Exceções Modulares](docs/adr/004-padrao_excecoes_modulares.md)**
- **[ADR 005 - Estratégia de Soft Delete e Auditoria](docs/adr/005-estrategia_soft_delete_auditoria.md)**
- **[ADR 006 - Contrato de Exceções de Domínio e i18n](docs/adr/006-contrato_excecoes_dominio_i18n.md)**
- **[ADR 007 - Pirâmide e Tipos de Testes](docs/adr/007-piramide_tipos_testes.md)**
- **[ADR 008 - Padrões de Testes de Integração](docs/adr/008-padroes_testes_integracao.md)**
- **[ADR 009 - Nomenclatura e Documentação de Testes](docs/adr/009-nomenclatura_documentacao_testes.md)**
- **[ADR 010 - Estratégia de i18n e Múltiplos Idiomas](docs/adr/010-estrategia_i18n_multi_idioma.md)**
---

## 🏁 Como Começar

### Pré-requisitos

- [Docker](https://www.docker.com/get-started) e [Docker Compose](https://docs.docker.com/compose/install/)

### 1. Ambiente Completo com Docker (Recomendado)

A forma mais simples de executar todo o ambiente (API + Banco de Dados) é com Docker Compose.

Na raiz do projeto, execute:
```bash
docker compose up -d --build
```
O comando irá:
1. Iniciar um container PostgreSQL.
2. Construir a imagem da API.
3. Iniciar a API, conectando-a ao banco de dados.
4. Executar as migrações do Flyway e popular o banco com dados iniciais.

A API estará disponível em `http://localhost:8080`.

---

## 📄 Documentação da API

Com a API em execução, a documentação interativa do Swagger UI fica disponível em:

`http://localhost:8080/swagger-ui.html`

A especificação OpenAPI 3 pode ser acessada em `/v3/api-docs`.

### 🔑 Credenciais de Acesso Iniciais

- **Email**: `admin@mecanicadm.com`
- **Senha**: `Senha123`

---

## ✅ Testes

O projeto possui uma suíte de testes unitários e de integração para garantir a qualidade e a estabilidade do código.

Para executar todos os testes via Maven:
```bash
mvn clean verify
```
Os relatórios de cobertura de testes (Jacoco) são gerados em `target/site/jacoco/`.

---
