# API Validação de CPF

API REST para validação de CPF.

## Funcionalidades

- Validação local de CPF (formato e dígitos)
- Consulta externa na ReceitaWS
- Verificar se CPF pode votar (user-info.herokuapp.com)
- Verificar se CPF já votou em uma pauta

## Tecnologias

- Java 17
- Spring Boot 3.2.0
- H2 Database
- Spring Data JPA
- Spring Cloud OpenFeign
- Swagger/OpenAPI

## Pré-requisitos

- Java 17+
- Docker e Docker Compose
- Gradle (wrapper incluído)

## Como executar

### Executar aplicação

```bash
./gradlew bootRun
```

A aplicação estará em `http://localhost:8080`

**Nota:** O banco H2 é em memória, então os dados são perdidos ao reiniciar a aplicação. Para persistência, configure um banco de dados externo.

## Endpoints

### POST /api/validacoes-cpf

Cria uma nova validação de CPF. Aceita CPF com ou sem formatação.

**Request:**
```json
{
  "cpf": "12345678901"
}
```

ou

```json
{
  "cpf": "123.456.789-01"
}
```

**Response (sucesso):**
```json
{
  "valido": true,
  "mensagem": "CPF válido",
  "cpf": "123.456.789-01",
  "dataValidacao": "2024-01-15T10:30:00",
  "detalhes": {
    "nome": "João da Silva",
    "dataNascimento": "1990-05-15",
    "situacao": "REGULAR"
  }
}
```

**Códigos de resposta:**
- 200: Validação realizada com sucesso
- 400: Formato inválido ou campo ausente
- 422: CPF inválido
- 503: Serviço externo indisponível

### GET /api/validacoes-cpf/{cpf}

Consulta a última validação realizada para um CPF. Aceita CPF com ou sem formatação.

**Exemplo:**
```
GET /api/validacoes-cpf/12345678901
GET /api/validacoes-cpf/123.456.789-01
```

**Response (sucesso):**
```json
{
  "valido": true,
  "mensagem": "CPF válido",
  "cpf": "123.456.789-01",
  "dataValidacao": "2024-01-15T10:30:00",
  "detalhes": {
    "nome": "João da Silva",
    "dataNascimento": "1990-05-15",
    "situacao": "REGULAR"
  }
}
```

**Códigos de resposta:**
- 200: Validação encontrada
- 400: Formato de CPF inválido
- 404: Validação não encontrada

### POST /api/validar-cpf

Valida um CPF (endpoint alternativo, mesma funcionalidade do POST /api/validacoes-cpf).

### Swagger

Documentação interativa disponível em:
- http://localhost:8080/swagger-ui.html

## Estrutura do projeto

```
src/main/java/com/validacpf/
├── adapter/          # Implementações (REST, JPA, Kafka, Feign)
├── application/      # Casos de uso
├── config/           # Configurações
└── domain/           # Lógica de negócio (modelos, value objects, portas)
```

O projeto usa Arquitetura Hexagonal, separando a lógica de negócio das implementações técnicas.

## Configuração

As configurações estão em `application.yml`. Para desenvolvimento:

```bash
SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun
```

**Portas:**
- API: 8080
- Kafka UI: 8081
- Mock ReceitaWS: 8082
- Oracle: 1521

## Testes

```bash
./gradlew test
```

Para ver cobertura:

```bash
./gradlew test jacocoTestReport
```

Relatório em: `build/reports/jacoco/test/html/index.html`

O projeto exige mínimo de 90% de cobertura.

## Teste de performance (JMeter)

Existe um plano de teste JMeter em `performance/jmeter/valida-cpf.jmx` para o endpoint `POST /api/validar-cpf`.

### Pré-requisitos

- API rodando em `http://localhost:8080` (e, opcionalmente, o mock ReceitaWS em `http://localhost:8082` via `docker compose up -d`)

### Executar via Docker (recomendado)

```bash
cd performance/jmeter
./run.sh
```

Isso gera:
- Relatório HTML em `performance/jmeter/results/report/`
- Resultados brutos em `performance/jmeter/results/results.jtl`

### Parametrização

Você pode ajustar as variáveis de ambiente antes de rodar:
- `JMETER_HOST` (default: `localhost`)
- `JMETER_PORT` (default: `8080`)
- `JMETER_THREADS` (default: `20`)
- `JMETER_RAMP_UP` (default: `10`)
- `JMETER_DURATION` (default: `60`)

## Build

```bash
./gradlew build
```

Para gerar o JAR:

```bash
./gradlew bootJar
```

## Health Check

```bash
curl http://localhost:8080/actuator/health
