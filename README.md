# 📋 VideoCore Reports

<div align="center">

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_videocore-reports&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_videocore-reports)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_videocore-reports&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_videocore-reports)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_videocore-reports&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_videocore-reports)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_videocore-reports&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_videocore-reports)

</div>

Microsserviço de relatórios do ecossistema VideoCore, responsável por gerenciar status de processamento de vídeos, fornecer API REST e atualizar clientes em tempo real via WebSocket. Desenvolvido como parte do curso de Arquitetura de Software da FIAP (Hackaton).

<div align="center">
  <a href="#visao-geral">Visão Geral</a> •
  <a href="#repositorios">Repositórios</a> •
  <a href="#tecnologias">Tecnologias</a> •
  <a href="#infra">Infraestrutura</a> •
  <a href="#estrutura">Estrutura</a> •
  <a href="#terraform">Terraform</a> •
  <a href="#arquitetura">Arquitetura</a> •
  <a href="#dominio">Domínio</a> •
  <a href="#dbtecnicos">Débitos Técnicos</a> •
  <a href="#limitacoesqt">Limitações de Quota</a> •
  <a href="#deploy">Fluxo de Deploy</a> •
  <a href="#instalacao">Instalação</a> •
  <a href="#contribuicao">Contribuição</a>
</div><br>

> 📽️ Vídeo de demonstração da arquitetura: [https://youtu.be/k3XbPRxmjCw](https://youtu.be/k3XbPRxmjCw)<br>

---

<h2 id="visao-geral">📋 Visão Geral</h2>

<details>
<summary>Expandir para mais detalhes</summary>

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

</details>

---

<h2 id="repositorios">📁 Repositórios do Ecossistema</h2>

<details>
<summary>Expandir para mais detalhes</summary>

| Repositório | Responsabilidade | Tecnologias |
|-------------|------------------|-------------|
| **videocore-infra** | Infraestrutura base | Terraform, Azure, AWS |
| **videocore-db** | Banco de dados | Terraform, Azure Cosmos DB |
| **videocore-auth** | Microsserviço de autenticação | C#, .NET 9, ASP.NET |
| **videocore-reports** | Microsserviço de relatórios | Java 25, GraalVM, Spring Boot 4, Cosmos DB |
| **videocore-worker** | Microsserviço de processamento de vídeo | Java 25, GraalVM, Spring Boot 4, FFmpeg |
| **videocore-notification** | Microsserviço de notificações | Java 25, GraalVM, Spring Boot 4, SMTP |
| **videocore-frontend** | Interface web do usuário | Next.js 16, React 19, TypeScript |

</details>

---

<h2 id="tecnologias">🔧 Tecnologias</h2>

<details>
<summary>Expandir para mais detalhes</summary>

| Categoria | Tecnologia |
|-----------|------------|
| **Linguagem** | Java 25 (GraalVM) |
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

</details>

---

<h2 id="infra">🌐 Infraestrutura</h2>

<details>
<summary>Expandir para mais detalhes</summary>

### ☸️ Recursos Kubernetes

| Recurso | Descrição |
|------------------------|-----------------------------------------------------------------------------------------------------|
| **Deployment** | Pods com health probes, limites de recursos e variáveis de ambiente |
| **Service** | Exposição interna no cluster |
| **Ingress** | Roteamento via Azure Application Gateway (Layer 7) |
| **ConfigMap** | Configurações não sensíveis |
| **HPA** | Escalabilidade automática baseada em CPU |
| **SecretProviderClass** | Integração com Azure Key Vault para gerenciamento de segredos |

- O **Application Gateway** recebe tráfego em um **Frontend IP privado**
- Roteamento direto para os IPs dos Pods (**Azure CNI + Overlay**)
- Path exposto: `/reports`

> ⚠️ Após o deploy (CD), aguarde cerca de **5 minutos** para que o **AGIC** finalize a configuração do Application Gateway.

### 🔌 Integrações

| Serviço | Tipo | Descrição |
|---------|------|-----------|
| **Azure Cosmos DB** | Síncrona | Persistência de relatórios |
| **Azure Blob Storage** | Síncrona | Upload/download de vídeos e imagens (SAS URLs) |
| **Azure Service Bus** | Assíncrona | Publicação/consumo de eventos de status |
| **WebSocket (STOMP)** | Real-time | Broadcast de atualizações para o frontend |

### 🔐 Azure Key Vault Provider (CSI)

- Sincroniza secrets do Azure Key Vault com Secrets do Kubernetes
- Monta volumes CSI com `tmpfs` dentro dos Pods
- Utiliza o CRD **SecretProviderClass**

> ⚠️ Caso o valor de uma secret seja alterado no Key Vault, é necessário **reiniciar os Pods**, pois variáveis de ambiente são injetadas apenas na inicialização.
>
> Referência: <https://learn.microsoft.com/en-us/azure/aks/csi-secrets-store-configuration-options>

### 👁️ Observabilidade

- **Logs**: Envio para `NewRelic` via `Open Telemetry Collector` utilizando protocolo `OTLP + GRPC`
- **Métricas**: Envio para `NewRelic` via `Open Telemetry Collector` utilizando protocolo `OTLP + GRPC`
- **Tracing**: Envio para `NewRelic` via `Open Telemetry Collector` utilizando protocolo `OTLP + GRPC`
- **Dashboards**: Visualização na UI do `NewRelic`

</details>

---

<h2 id="estrutura">📦 Estrutura do Projeto</h2>

<details>
<summary>Expandir para mais detalhes</summary>

```text
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
├── docs/                         # Assets de documentação
└── .github/workflows/
    ├── ci.yaml                   # Build, test, OpenAPI
    └── cd.yaml                   # Terraform apply
```

</details>

---

<h2 id="terraform">🗄️ Módulos Terraform</h2>

<details>
<summary>Expandir para mais detalhes</summary>

O código `HCL` desenvolvido segue uma estrutura modular:

| Módulo | Descrição |
|--------|-----------|
| **helm** | Implantação do Helm Chart da aplicação, consumindo as informações necessárias via `Terraform Remote State` |
| **apim** | Importação da especificação `OpenAPi` deste microsserviço no `APIM` para configuração das rotas |

> ⚠️ Os outpus criados são consumidos posteriormente em pipelines via `$GITHUB_OUTPUT` ou `Terraform Remote State`, para compartilhamento de informações. Tornando, desta forma, dinãmico o provisionamento da infraestrutura.

</details>

---

<h2 id="arquitetura">🧱 Arquitetura</h2>

<details>
<summary>Expandir para mais detalhes</summary>

### 📌 Princípios Adotados

- **DDD**: Bounded context de pedido isolado
- **Clean Architecture**: Domínio independente de frameworks
- **Separação de responsabilidades**: Cada camada tem responsabilidade bem definida
- **Independência de frameworks**: Domínio não depende de Spring ou outras bibliotecas
- **Testabilidade**: Lógica de negócio isolada facilita testes unitários
- **Inversão de Dependência**: Classes utilizam abstrações, nunca implementações concretas diretamente
- **Injeção de Dependência**: Classes recebem via construtor os objetos que necessitam utilizar
- **SAGA Coreografada**: Comunicação assíncrona via eventos
- **Comunicação Síncrona Resiliente**: Embora ainda não possua comunicações síncronas, apenas assíncronas, caso o projeto evolua, serão implementadas usando padrões de resiliência como Circuit Beaker e Service Discovery

### 🎯 Clean Architecture

O projeto segue os princípios de **Clean Architecture** com separação clara de responsabilidades:

```text
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

### 📊 Diagrama de Arquitetura: Saga Coreografado

![Diagrama Domínio DDD](docs/diagrams/saga-diagram.svg)

</details>

---

<h2 id="dominio">📽️ Domínio</h2>

<details>
<summary>Expandir para mais detalhes</summary>

### 📖 Dicionário de Linguagem Ubíqua

| Termo | Descrição |
|-------|-----------|
| **Relatório** | Documento que apresenta o resultado do processamento de um vídeo enviado pelo usuário, incluindo status, imagens extraídas e informações relevantes. |
| **Usuário Autenticado** | Pessoa que acessa a plataforma, envia vídeos e consulta seus relatórios. |
| **Vídeo** | Arquivo enviado pelo usuário para ser analisado e processado pela plataforma. |
| **Processamento de Vídeo** | Sequência de etapas automáticas realizadas sobre um vídeo, como análise, extração de imagens e geração de dados para o relatório. |
| **Status do Processamento** | Situação atual do processamento do vídeo, podendo ser, por exemplo, pendente, em andamento, concluído ou com erro. |
| **Imagem do Minuto** | Imagem extraída de um vídeo em um minuto específico, utilizada para compor o relatório. |
| **Metadados** | Informações complementares sobre o vídeo ou relatório, como duração, nome do arquivo, horários, etc. |
| **Corte de Frame por Minuto** | Ponto do vídeo (em minutos) onde uma imagem foi extraída para análise. |
| **Percentual de Progresso** | Indicador do avanço do processamento do vídeo, geralmente expresso em porcentagem. |
| **Nome do Vídeo** | Identificação do vídeo enviado pelo usuário, utilizada para referência nos relatórios. |
| **Evento de Erro de Processamento** | Notificação de que ocorreu uma falha durante o processamento do vídeo, registrada no relatório. |
| **Notificação de Relatório** | Comunicação enviada ao usuário sobre mudanças ou conclusões no status do seu relatório. |
| **Consulta de Relatórios** | Ação do usuário de buscar seus relatórios, seja o mais recente, um específico ou estatísticas agregadas. |
| **Estatísticas de Relatórios** | Informações agregadas sobre os relatórios do usuário, como quantidade, status e datas. |
| **URL de Upload de Vídeo** | Endereço temporário fornecido ao usuário para envio seguro de vídeos à plataforma. |
| **URL de Download de Imagens** | Endereço temporário fornecido ao usuário para baixar imagens extraídas do vídeo processado. |
| **Atualização de Relatório** | Ação de modificar informações de um relatório existente, geralmente após novo processamento ou correção. |
| **Salvamento de Relatório** | Registro de um novo relatório no sistema após o processamento de um vídeo. |
| **Erro de Relatório** | Situação em que não foi possível gerar ou acessar um relatório devido a problemas no processamento ou autorização. |

### 📊 Diagrama de Domínio e Sub-Domínios (DDD Estratégico)

![Diagrama Domínio DDD](docs/diagrams/domain-diagram-reports.svg)

</details>

---

<h2 id="dbtecnicos">⚠️ Débitos Técnicos</h2>

<details>
<summary>Expandir para mais detalhes</summary>

| Débito | Descrição | Impacto |
|--------|-----------|---------|
| **Separar Geração de URLs** | Extrair responsabilidade de gerar `Pre-Signed URLs` para outro microsserviço | Reduz acoplamento e melhora escalabilidade |
| **Transactional Outbox Pattern** | Implementar padrão para evitar escrita duplicada na SAGA coreografada | Garate síncronia entre atualização do DB e publicação de eventos |
| **Migrar Linguagem Compilada** | Para máximizar a performance deste microsserviço, utilizou-se a GraalVM para criação de uma imagem nativa. Embora os ganhos sejam notórios, observou-se o uso intensivo de `JNI`, `Reflections`, entre outras coisas, e o compilador precisa conhecer tudo que for dinãmico em tempo de build `(reachability metadata)`. Neste sentido, utilizar uma linguagem nativamente compilada (Go, Rust...) pode trazer ganhos de manutenção no futuro | Melhora da manutenabilidade |
| **Workload Identity** | Usar Workload Identity para Pods acessarem recursos Azure (atual: Azure Key Vault Provider) | Melhora de segurança e gestão de credenciais |
| **Implementar DLQ** | Implementar lógica de reprocessamento do evento de atualização do status de processamento de um vídeo, em caso de falha | Resiliência |
| **Impacto Cache** | Analisar durante testes E2E o impacto do caching nos endpoints de consulta, especificamente durante o processamento de um vídeo ou geração de URLs | Prevenção de bugs e redução de complexidade |
| **CQRS/DDD** | O banco de dados está desnormalizado para melhorar performance de leituras. Refletir se endpoints de consulta realmente devem recorrer ao domínio, (Controllers, UseCases, Gateway...) ou se podem consumir diretamente a própria camada de infraestrutura com modelos otimizados | Uso estratégico da arquitetura |
| **WebSocket** | A API de `WebSocket` dos navegadores não permitem o envio de `Headers HTTP` customizados durante handshake inicial, apenas do nativo `Sec-WebSocket-Protocol`. Portanto, uma vez que o `APIM`, para endpoints `WebSocket,` disponibiliza uma única `Operation (onHandshake)`, migrar o envio do token `JWT` de autenticação nestes casos para um `Cookie HTTP Only` (em consonãncia com o débito técnico descrito no repositório videocore-frontend), ao invés do header `Sec-WebSocket-Protocol` | Segurança Crítica |
| **Implementar BDD** | Utilizar abordagem BDD para desenvolvimento de testes de integração em fluxos críticos | Testabilidade |

</details>

---

<h2 id="limitacoesqt">📉 Limitações de Quota (Azure for Students)</h2>

<details>
<summary>Expandir para mais detalhes</summary>

A assinatura **Azure for Students** impõe as seguintes restrições:

- **Região**: com base em uma policy específica de assinatura;

- **Quota de VMs**: Apenas **2 instâncias** do SKU utilizado para o node pool do AKS, tendo um impacto direto na escalabilidade do cluster. Quando o limite é atingido, novos nós não podem ser criados e dão erro no provisionamento de workloads.

### Erro no CD dos Microsserviços

Durante o deploy dos microsserviços, Pods podem ficar com status **Pending** e o seguinte erro pode aparecer:

![Error Quota CLI](docs/images/error-quota-cli.jpeg)
![Error Quota UI](docs/images/error-quota-ui.jpeg)

**Causa**: O cluster atingiu o limite máximo de VMs permitido pela quota e não há recursos computacionais (CPU/memória) disponíveis nos nós existentes.

**Solução**: Aguardar a liberação de recursos de outros pods e reexecutar CI + CD.

</details>

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

### Autenticação das Pipelines

- **Azure:**
  - **OIDC**: Token emitido pelo GitHub
  - **Azure AD Federation**: Confia no emissor GitHub
  - **Service Principal**: Autentica sem secret

### Proteções

- Branch `main` protegida
- Nenhum push direto permitido
- Todos os checks devem passar

### Ordem de Provisionamento

```text
1. videocore-infra          (AKS, VNET, APIM, etc)
2. videocore-db             (Cosmos DB)
3. videocore-auth           (Microsserviço de autenticação)
4. videocore-reports        (Microsserviço de relatórios)
5. videocore-worker         (Microsserviço de processamento)
6. videocore-notification   (Microsserviço de notificações)
7. videocore-frontend       (Aplicação SPA Web)
```

</details>

---

<h2 id="instalacao">🚀 Instalação e Uso</h2>

<details>
<summary>Expandir para mais detalhes</summary>

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

### Postman

### 🔗 Workspace: https://www.postman.com/pedroferrarezzo-2950189/workspace/fiap-soat-tech-team-8

### ❓ Como preencher variáveis:

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

> ⚠️ Para testes locais, ao invés de enviar um `Headar Authorization`, enviar um `Header Auth-Subject` contendo o ID do usuário autenticado. Isto é exatamente o que acontece em produção, na interação `APIM` -> `Azure Function Authorizer` -> `AKS`.

### Documentação da API / Health Check

Após iniciar a aplicação:
- **Swagger UI**: http://localhost:${SERVER_PORT}/swagger-ui.html
- **OpenAPI Spec**: http://localhost:${SERVER_PORT}/v3/api-docs
- **Actuator**: http://localhost:${SERVER_PORT}/actuator/health

</details>

---

<h2 id="contribuicao">🤝 Contribuição</h2>

<details>
<summary>Expandir para mais detalhes</summary>

### Fluxo de Contribuição

1. Crie uma branch a partir de `main`
2. Implemente suas alterações
3. Execute os testes: `./gradlew test`
4. Abra um Pull Request
5. Aguarde aprovação de um CODEOWNER

### Licença

Este projeto está licenciado sob a MIT License.

</details>

---

<div align="center">
  <strong>FIAP - Pós-graduação em Arquitetura de Software</strong><br>
  Hackaton (Tech Challenge 5)
</div>