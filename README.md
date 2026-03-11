# 📋 VideoCore Reports

<div align="center">

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_videocore-reports&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_videocore-reports)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_videocore-reports&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_videocore-reports)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_videocore-reports&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_videocore-reports)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_videocore-reports&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_videocore-reports)

</div>

Microsserviço de relatórios do ecossistema VideoCore, responsável por gerenciar status de processamento de vídeos, fornecer API REST e atualizar clientes em tempo real via WebSocket. Desenvolvido como parte do curso de Arquitetura de Software da FIAP (Tech Challenge).

<div align="center">
  <a href="#visao-geral">Visão Geral</a> •
  <a href="#arquitetura">Arquitetura</a> •
  <a href="#repositorios">Repositórios</a> •
  <a href="#tecnologias">Tecnologias</a> •
  <a href="#postman">Postman</a> •
  <a href="#instalacao">Instalação</a> •
  <a href="#deploy">Fluxo de Deploy</a> •
  <a href="#contribuicao">Contribuição</a>
</div><br>

> 📽️ Vídeo de demonstração da arquitetura: [https://youtu.be/k3XbPRxmjCw](https://youtu.be/k3XbPRxmjCw)<br>

---

<h2 id="visao-geral">📋 Visão Geral</h2>

O **VideoCore Reports** é o microsserviço responsável por gerenciar os relatórios de processamento de vídeo. Ele recebe eventos do Azure Service Bus, persiste dados no Cosmos DB e notifica clientes em tempo real via WebSocket (STOMP).

### Principais Responsabilidades

- **API REST**: Endpoints para consulta de relatórios e geração de SAS URLs
- **WebSocket**: Broadcast de atualizações de status em tempo real via STOMP
- **Persistência**: Armazenamento de relatórios no Azure Cosmos DB
- **Eventos**: Consumo e publicação de eventos via Azure Service Bus
- **Storage**: Geração de SAS URLs para upload/download de vídeos e imagens

### API Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/latest` | Relatórios mais recentes do usuário |
| `GET` | `/{reportId}` | Relatório específico por ID |
| `GET` | `/video/download/url` | SAS URL para download de imagens |
| `GET` | `/video/upload/url` | SAS URL para upload de vídeo |

### WebSocket

| Protocolo | Endpoint | Tópico |
|-----------|----------|--------|
| **STOMP** | WebSocket base URL | `/topic` |

---

<h2 id="arquitetura">🧱 Arquitetura</h2>

<details>
<summary>Expandir para mais detalhes</summary>

### 🎯 Clean Architecture

O projeto segue os princípios de **Clean Architecture** com separação clara de responsabilidades:

```
core/
├── domain/           # Entidades e regras de negócio
├── usecases/         # Casos de uso da aplicação
└── interfaceadapters/
    ├── presenter/    # Formatação de respostas HTTP
    └── mapper/       # Conversão domínio ↔ DTO

infrastructure/
├── in/               # Adaptadores de entrada
│   └── websocket/    # Handlers STOMP
├── out/              # Adaptadores de saída
│   ├── websocket/    # WebSocket sender
│   ├── persistence/
│   │   ├── cosmosdb/     # Repositório Cosmos DB
│   │   └── blobstorage/  # Azure Blob Storage
│   └── event/azsvcbus/   # Azure Service Bus
└── common/           # Configurações compartilhadas
```

### 🔄 Fluxo de Dados

```
Frontend (REST/WebSocket)
    ↓
ReportController → ReportPresenter (HTTP)
WebSocketHandler → WebSocketSender (STOMP)
    ↓
Use Cases (Business Logic)
    ↓
CosmosDbReportRepository (Persistence)
AzureBlobStorageRepository (File Storage)
AzSvcEventSender (Event Publishing)
```

### 🔌 Integrações

| Serviço | Tipo | Descrição |
|---------|------|-----------|
| **Azure Cosmos DB** | Síncrona | Persistência de relatórios |
| **Azure Blob Storage** | Síncrona | Upload/download de vídeos e imagens (SAS URLs) |
| **Azure Service Bus** | Assíncrona | Publicação/consumo de eventos de status |
| **WebSocket (STOMP)** | Real-time | Broadcast de atualizações para o frontend |

### 📊 Observabilidade

- **Traces**: OpenTelemetry (OTLP gRPC)
- **Métricas**: Micrometer (OTLP gRPC)
- **Logs**: Logstash JSON format
- **Health Checks**: Spring Actuator (`/actuator/health`)

### ☸️ Kubernetes

| Recurso | Configuração |
|---------|-------------|
| **Replicas** | 2 (HPA: 1-2) |
| **CPU** | Limite: 500m |
| **Memória** | Limite: 1GB |
| **Startup Probe** | `/actuator/health` |
| **Liveness Probe** | `/actuator/health/liveness` |
| **Readiness Probe** | `/actuator/health/readiness` |

### 📦 Estrutura do Projeto

```
videocore-reports/
├── reports/
│   ├── build.gradle              # Dependências e build config
│   ├── src/main/
│   │   ├── java/com/soat/fiap/videocore/reports/
│   │   │   ├── ReportsApplication.java
│   │   │   ├── core/
│   │   │   │   ├── domain/
│   │   │   │   ├── usecases/
│   │   │   │   └── interfaceadapters/
│   │   │   └── infrastructure/
│   │   │       ├── in/websocket/
│   │   │       ├── out/
│   │   │       │   ├── websocket/
│   │   │       │   ├── persistence/cosmosdb/
│   │   │       │   ├── persistence/blobstorage/
│   │   │       │   └── event/azsvcbus/
│   │   │       └── common/
│   │   └── resources/
│   │       ├── application.yaml
│   │       ├── application-local.yaml
│   │       ├── application-prod.yaml
│   │       └── logback-spring.xml
│   └── src/test/
├── docker/
│   └── Dockerfile                # GraalVM Native Image
├── kubernetes/
│   ├── Chart.yaml                # Helm Chart
│   ├── values.yaml               # Configurações Helm
│   └── templates/
│       ├── deploymentset.yaml
│       ├── service.yaml
│       ├── ingress.yaml
│       ├── hpa.yaml
│       └── configmap.yaml
├── terraform/
│   ├── main.tf                   # Helm + APIM
│   └── variables.tf
└── .github/workflows/
    ├── ci.yaml                   # Build, test, OpenAPI
    └── cd.yaml                   # Terraform apply
```

</details>

---

<h2 id="repositorios">📁 Repositórios do Ecossistema</h2>

| Repositório | Responsabilidade | Tecnologias |
|-------------|------------------|-------------|
| **videocore-infra** | Infraestrutura base (AKS, VNET, APIM, Key Vault) | Terraform, Azure, AWS |
| **videocore-db** | Banco de dados | Terraform, Azure Cosmos DB |
| **videocore-frontend** | Interface web do usuário | Next.js 16, React 19, TypeScript |
| **videocore-reports** | Microsserviço de relatórios (este repositório) | Java 25, Spring Boot 4, Cosmos DB |
| **videocore-worker** | Microsserviço de processamento de vídeo | Java 25, Spring Boot 4, FFmpeg |
| **videocore-observability** | Stack de observabilidade | OpenTelemetry, Jaeger, Prometheus, Grafana |

---

<h2 id="tecnologias">🔧 Tecnologias</h2>

| Categoria | Tecnologia |
|-----------|------------|
| **Linguagem** | Java 25 |
| **Framework** | Spring Boot 4.0.1 |
| **Banco de Dados** | Azure Cosmos DB (NoSQL) |
| **Mensageria** | Azure Service Bus |
| **Storage** | Azure Blob Storage |
| **WebSocket** | Spring WebSocket (STOMP) |
| **Observabilidade** | OpenTelemetry, Micrometer, Logstash |
| **Documentação** | SpringDoc OpenAPI (Swagger) |
| **Build** | Gradle |
| **Compilação** | GraalVM Native Image |
| **Container** | Docker |
| **Orquestração** | Kubernetes (Helm) |
| **IaC** | Terraform |
| **CI/CD** | GitHub Actions |
| **Qualidade** | SonarQube |

---

<h2 id="postman">🔗 Postman</h2>

### Workspace: https://www.postman.com/pedroferrarezzo-2950189/workspace/fiap-soat-tech-team-8

### Variáveis das Collections

#### VideoCore Auth Collection 🔑
- **azFuncLocalUrl:** `http://localhost:7025`
- **cognitoDomainUrl:** `https://videocore-auth.auth.sa-east-1.amazoncognito.com`
    > ℹ️ Ou consultar output terraform: `cognito_code_get_token_url`

#### VideoCore Reports Collections 🌐⚙️🔄
> ⚠️ Os endpoints `HTTP` e `WebSocket` estão distribuídos em diferentes `collections` pois o `Postman` não suporta a criação de uma única contendo diferentes tipos de `endpoint`.
- **reportsHttpLocalUrl:** `http://localhost:8081`
- **reportsHttpsApimUrl:** `https://videocore-apim.azure-api.net`
    > ℹ️ Ou consultar output terraform: `apim_gateway_url`
- **reportsWsLocalUrl:** `ws://localhost:8081`
- **reportsWssApimUrl:** `wss://videocore-apim.azure-api.net`
    > ℹ️ Ou consultar output terraform: `apim_ws_gateway_url`
- **videoCoreStartSubscription:** consultar output terraform: `apim_videocore_start_subscription_key`
    > ℹ️ Ou capturar via `Azure Console`
- **reportsAuthorizationHeader:** consultar `access_token` retornado pelo `Cognito` pós autenticação

---

<h2 id="instalacao">🚀 Instalação e Uso</h2>

### Variáveis de Ambiente

```bash
SPRING_AZURE_COSMOS_URI=                    # Endpoint do Cosmos DB
SPRING_AZURE_COSMOS_DATABASE=               # Nome do database
AZ_SVC_BUS_CONNECTION_STRING=               # Connection string Service Bus
AZURE_BLOB_STORAGE_CONNECTION_STRING=       # Connection string Blob Storage
AZURE_BLOB_STORAGE_VIDEO_CONTAINER_NAME=    # Container de vídeos
AZURE_BLOB_STORAGE_IMAGE_CONTAINER_NAME=    # Container de imagens
WEBSOCKET_BASE_ENDPOINT=                    # Endpoint base do WebSocket
```

### Desenvolvimento Local

```bash
# Clonar repositório
git clone https://github.com/FIAP-SOAT-TECH-TEAM/videocore-reports.git
cd videocore-reports/reports

# Configurar variáveis de ambiente
cp env-example .env

# Compilar
./gradlew build

# Executar
./gradlew bootRun

# Executar testes
./gradlew test
```

### Documentação da API

Após iniciar a aplicação:
- **Swagger UI**: http://localhost:9090/swagger-ui.html
- **OpenAPI Spec**: http://localhost:9090/v3/api-docs

---

<h2 id="deploy">⚙️ Fluxo de Deploy</h2>

<details>
<summary>Expandir para mais detalhes</summary>

### Pipeline CI

1. **Build**: JDK 25, Gradle com cache
2. **Testes**: Execução de testes automatizados
3. **OpenAPI**: Geração do spec Swagger
4. **Terraform**: Format, validate, plan

### Pipeline CD

1. **Terraform Apply**: Provisionamento via Helm no AKS
2. **Docker**: Build de imagem GraalVM Native Image
3. **Registry**: Push para Azure Container Registry (ACR)

### Proteções

- Branch `main` protegida
- Nenhum push direto permitido
- Todos os checks devem passar

### Ordem de Provisionamento

```
1. videocore-infra          (AKS, VNET, APIM, Key Vault)
2. videocore-db             (Cosmos DB)
3. videocore-observability  (Jaeger, Prometheus, Grafana)
4. videocore-reports        (Este repositório)
5. videocore-worker         (Microsserviço de processamento)
6. videocore-frontend       (Interface web)
```

</details>

---

<h2 id="contribuicao">🤝 Contribuição</h2>

### Fluxo de Contribuição

1. Crie uma branch a partir de `main`
2. Implemente suas alterações
3. Execute os testes: `./gradlew test`
4. Abra um Pull Request
5. Aguarde aprovação de um CODEOWNER

### Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

---

<div align="center">
  <strong>FIAP - Pós-graduação em Arquitetura de Software</strong><br>
  Tech Challenge 4
</div>

### Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

---

<div align="center">
  <strong>FIAP - Pós-graduação em Arquitetura de Software</strong><br>
  Tech Challenge
</div>