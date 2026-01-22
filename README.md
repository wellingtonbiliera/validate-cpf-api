# API Validação de CPF

API REST para validação de CPF usando Spring Boot, Oracle Database e Kafka.

## O que faz

A API valida CPF de duas formas:
- Validação local: verifica formato e dígitos verificadores
- Consulta externa: busca dados na ReceitaWS (nome, data de nascimento, situação)

Todas as validações são salvas no banco e eventos são publicados no Kafka.

## Tecnologias

- Java 17
- Spring Boot 3.2.0
- Oracle Database
- Apache Kafka
- Spring Data JPA
- Spring Cloud OpenFeign
- Swagger/OpenAPI

## Pré-requisitos

- Java 17+
- Docker e Docker Compose
- Gradle (wrapper incluído)

## Como executar

### 1. Iniciar serviços

```bash
docker compose up -d
```

Aguarde 1-2 minutos para o Oracle ficar pronto.

### 2. Inicializar banco de dados

```bash
docker exec -i valida-cpf-oracle sqlplus system/Oracle123@XE < scripts/init-database.sql
```

### 3. Executar aplicação

```bash
./gradlew bootRun
```

A aplicação estará em `http://localhost:8080`

## Endpoints

### POST /api/validar-cpf

Valida um CPF. Aceita com ou sem formatação.

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
