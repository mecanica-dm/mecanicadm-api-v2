# [011]: [Arquitetura Limpa Purista para Novas Features]

* **Status**: Aceito
* **Data**: 30/05/2026

## 1. Contexto e Problema

O projeto evoluiu para uma separação mais rígida entre núcleo de negócio e detalhes de infraestrutura, especialmente na feature `vehicle`. Ao criar novas features, existe o risco de misturar validação HTTP, persistência, mensagens internacionalizadas e regras de negócio na mesma camada, o que reduz testabilidade, aumenta acoplamento e dificulta evolução.

Também foi definido que exceções de negócio devem ser tratadas como erros esperados do domínio/aplicação, enquanto erros técnicos permanecem na infraestrutura e resultam em respostas 500.

## 2. Decisão

Adotamos a seguinte organização como padrão para novas features:

- **`core` é o centro da aplicação**:
  - contém entidades de domínio, value objects, regras de negócio e comportamentos;
  - contém use cases que apenas orquestram fluxo;
  - contém portas (`ports`) para acesso a persistência e integrações externas;
  - não depende de Spring Web, JPA, validação HTTP ou qualquer detalhe de infraestrutura.

- **`infra` é a borda técnica**:
  - contém controllers, DTOs de request/response, mappers, repositórios JPA, configurações e handlers globais;
  - converte entrada HTTP em comandos/queries do core;
  - converte exceções de domínio em respostas HTTP;
  - concentra validações específicas de transporte (ex.: `@NotBlank`, `@LicensePlate`, `@Valid`).

- **Use cases recebem apenas tipos do core**:
  - comandos e queries vivem no pacote do core;
  - controllers fazem a tradução entre DTOs da API e comandos/queries do core.

- **Exceções seguem contrato de domínio**:
  - exceções de negócio devem estender `DomainExceptionCore`;
  - cada exceção centralizada no módulo deve carregar `messageKey` e tipo/status de erro compatível com o contrato do projeto;
  - erros inesperados/técnicos são tratados pela infraestrutura como `500 Internal Server Error`.

- **Validação de formato fica na borda HTTP**:
  - validações como placa, e-mail, tamanho de senha e campos obrigatórios devem ficar em request DTOs ou parâmetros do controller;
  - o domínio deve validar apenas regras de negócio e consistência interna.

## 3. Diretrizes de Estrutura para uma Nova Feature

Ao criar uma nova feature, seguir este fluxo:

### 3.1. Núcleo de domínio
- Criar a entidade principal em `core/<feature>/domain`.
- Colocar regras de negócio, invariantes e comportamentos dentro da própria entidade.
- Usar `DomainExceptionCore` para erros de negócio.

### 3.2. Contratos de entrada do caso de uso
- Criar `Commands` e `Queries` no pacote do core, por exemplo:
  - `core/<feature>/usecase/command`
  - `core/<feature>/usecase/query`
- Esses tipos não devem depender de DTOs da API nem de classes do Spring.

### 3.3. Portas do domínio
- Definir interfaces de persistência/integração em `core/<feature>/domain/port`.
- A interface do core deve descrever intenção, não implementação.

### 3.4. Casos de uso
- Criar use cases no core para cada operação relevante.
- O use case deve:
  - validar fluxo de negócio;
  - chamar entidades e portas;
  - não conhecer controller, request, response, JPA ou HTTP.

### 3.5. Infraestrutura e adaptação HTTP
- Criar controller em `infra/features/<feature>/api`.
- Criar DTOs de request/response em `infra/features/<feature>/api/dto`.
- Mapear DTOs da API para commands/queries do core.
- Validar campos de entrada com Bean Validation na borda.

### 3.6. Persistência
- Criar entidade JPA em `infra/features/<feature>/persistence/entity`.
- Criar mapper entre domínio e entidade JPA.
- Criar implementação da porta do core em `infra/features/<feature>/persistence/jpa`.

### 3.7. Configuração
- Registrar os use cases em uma classe de configuração da feature, se necessário.
- Manter wiring, beans e dependências externas fora do core.

### 3.8. Tratamento de exceções
- Centralizar o tratamento em `GlobalExceptionHandler`.
- Traduzir exceções de domínio para status HTTP adequados.
- Garantir que `500` fique reservado a falhas inesperadas/técnicas.

### 3.9. Testes
- Testar domínio com unit tests puros.
- Testar use cases com mocks da porta.
- Testar controller com integração web e validação.
- Testar repositório/adapter com integração de persistência quando necessário.

## 4. Checklist Prático para Criar uma Nova Feature

Ao iniciar uma feature nova, seguir este checklist:

1. Definir a entidade de domínio e suas regras.
2. Criar as exceções do módulo em uma classe centralizada `[Dominio]Exceptions`.
3. Criar a porta de persistência/integração no core.
4. Criar os commands/queries do core.
5. Implementar os use cases no core.
6. Criar DTOs de request/response na infra.
7. Criar controller e fazer o mapeamento DTO ↔ command/query.
8. Criar entidade JPA, mapper e implementação da porta.
9. Registrar os beans da feature, se aplicável.
10. Adicionar tratamento de exceções e chaves de i18n necessárias.
11. Escrever testes unitários e de integração por camada.
12. Garantir que o core não importe nada de `infra`, `springframework.web`, `jakarta.persistence` ou `jakarta.validation`.

## 5. Consequências

### Positivas
- Menor acoplamento entre negócio e tecnologia.
- Mais facilidade para testar o core sem contexto Spring.
- Mudanças de API ou persistência impactam menos o domínio.
- Estrutura mais previsível para novas features.
- Melhor alinhamento com as ADRs de lógica no domínio, exceções e i18n.

### Negativas / Custos
- Mais arquivos por feature.
- Mais etapas de mapeamento entre borda e core.
- Maior disciplina necessária para não “vazar” dependências da infra para o core.
- Pode haver sensação de boilerplate em features pequenas, mas isso é aceito em troca de isolamento e clareza arquitetural.

## 6. Relação com Outras ADRs

Esta ADR complementa e reforça:

- [ADR 003 - Lógica de Negócio no Domínio](docs/adr/003-logica_negocio_dominio.md)
- [ADR 004 - Padrão de Exceções Modulares](docs/adr/004-padrao_excecoes_modulares.md)
- [ADR 006 - Contrato de Exceções de Domínio e i18n](docs/adr/006-contrato_excecoes_dominio_i18n.md)
- [ADR 010 - Estratégia de i18n e Múltiplos Idiomas](docs/adr/010-estrategia_i18n_multi_idioma.md)

O `vehicle` passa a ser o exemplo de referência para novas features com esta organização.

