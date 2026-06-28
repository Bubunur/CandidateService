# Candidate Service

Микросервис управления кандидатами для рекрутеров.  
Принимает данные из Kafka (`cv.parsed`), предоставляет REST API для работы с кандидатами и публикует события изменения статуса (`candidate.status.changed`).

---

## Стек технологий

- **Java 21**, Spring Boot 3.4.5
- **PostgreSQL 15** — основное хранилище
- **Apache Kafka 3.6** (KRaft-режим, без ZooKeeper)
- **Liquibase** — миграции схемы БД
- **Springdoc OpenAPI** — автоматическая документация API
- **Testcontainers** — интеграционные тесты

---

## Быстрый старт

### Требования

| Инструмент     | Версия |
|----------------|--------|
| Java           | 21+    |
| Docker         | 20+    |
| Docker Compose | v2+    |

### 1. Запуск инфраструктуры

```bash
docker-compose up -d
```

Поднимаются два контейнера:

| Контейнер     | Порт | Описание                     |
|---------------|------|------------------------------|
| `cv-postgres` | 5432 | PostgreSQL, БД `cv_scan`     |
| `cv-kafka`    | 9092 | Kafka (KRaft, без ZooKeeper) |

### 2. Запуск приложения

```bash
./gradlew bootRun
```

Либо собрать jar и запустить:

```bash
./gradlew build -x test
java -jar build/libs/candidate-service-0.0.1-SNAPSHOT.jar
```

Приложение стартует на **http://localhost:8080**

### 3. Swagger UI

```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```
http://localhost:8080/api-docs
```

---

## REST API

Базовый путь: `/api/v1/candidates`

| Метод  | Путь                   | Описание                                     |
|--------|------------------------|----------------------------------------------|
| GET    | `/`                    | Список кандидатов с фильтрацией и пагинацией |
| GET    | `/{id}`                | Получить кандидата по ID                     |
| POST   | `/`                    | Создать кандидата                            |
| PUT    | `/{id}`                | Обновить кандидата (статус не меняется)      |
| DELETE | `/{id}`                | Удалить кандидата                            |
| PATCH  | `/{id}/status`         | Сменить статус кандидата                     |
| GET    | `/{id}/status-history` | История изменений статуса                    |

### Параметры фильтрации (`GET /`)

| Параметр   | Тип                              | Описание                            |
|------------|----------------------------------|-------------------------------------|
| `verdict`  | `FIT \| PARTIAL \| UNFIT`        | Фильтр по вердикту                  |
| `status`   | `CandidateStatus`                | Фильтр по статусу                   |
| `position` | `string`                         | Точное совпадение должности         |
| `search`   | `string`                         | Поиск по имени (`LIKE`)             |
| `page`     | `int` (default: `0`)             | Номер страницы                      |
| `size`     | `int` (default: `10`)            | Размер страницы                     |
| `sort`     | `string` (default: `createdAt,desc`) | Поле и направление сортировки   |

---

## Жизненный цикл статуса

```
NEW → IN_REVIEW → INVITED → APPROVED
                └→ REJECTED
         └→ REJECTED
```

Недопустимые переходы возвращают `422 Unprocessable Entity`.

---

## Kafka-интеграция

### Консьюмер — `cv.parsed`

Слушает входящие события `CvParsedEvent` и автоматически создаёт запись кандидата.  
**Идемпотентность**: дубликаты отсекаются по паре `(candidateId, parsedAt)`.  
При ошибке — 3 повторные попытки с интервалом 1 с (`FixedBackOff`).

### Продюсер — `candidate.status.changed`

Публикует `StatusChangedEvent` при каждой успешной смене статуса.  
Продюсер настроен с `acks=all` и `enable.idempotence=true`.

---

## Запуск тестов

```bash
./gradlew test
```

> Для интеграционных тестов нужен запущенный Docker — Testcontainers поднимает PostgreSQL и Kafka автоматически.

### Настройка Docker Desktop (macOS, одноразово)

Testcontainers подключается через стандартный сокет `/var/run/docker.sock`.  
В Docker Desktop он по умолчанию отключён — его нужно включить один раз:

**Docker Desktop → Settings → Advanced → ✅ Allow the default Docker socket to be used**

После этого тесты запустятся без дополнительных настроек на любой машине.

### Состав тестов

| Класс                        | Тип                              | Что проверяет                                        |
|------------------------------|----------------------------------|------------------------------------------------------|
| `CvParsedConsumerTest`       | Unit (Mockito)                   | Идемпотентность `createFromKafka`, маппинг полей     |
| `StatusServiceTests`         | Unit (Mockito)                   | Все разрешённые и запрещённые переходы статусов      |
| `CandidateApiIntegrationTest`| Integration (Testcontainers + RestAssured) | CRUD, смена статуса, история через HTTP |
| `KafkaIntegrationTest`       | Integration (Testcontainers)     | Создание кандидата из Kafka, защита от дублей        |

---

## Принятые решения

### ID кандидата — slug из email
ID генерируется из локальной части email (до `@`) с заменой спецсимволов на `-`.  
При коллизии добавляется числовой суффикс (`-2`, `-3`, …).  
Это даёт читаемые и воспроизводимые идентификаторы без UUID.

### Машина состояний в enum
Разрешённые переходы закодированы прямо в `CandidateStatus` через статическую карту `allowedTransitions`.  
Метод `validateTransition()` бросает `InvalidStatusTransitionException` при нарушении — логика изолирована в модели.

### Идемпотентный консьюмер Kafka
Дубль определяется по паре `(candidateId, parsedAt)` — не требует отдельной таблицы обработанных eventId.

### Фильтрация через JPA Specification
Динамические фильтры собираются через `Specification.and()` — без JPQL-конкатенации строк и ветвлений в репозитории.

### JSONB для сложных полей
`criteria`, `experience`, `questions` хранятся как `jsonb`-колонки.  
Схема гибкая — AI-сервис может добавлять новые поля без миграции БД.

### Явная конфигурация Kafka
Вместо автоконфигурации Spring Boot — явные `@Bean` для `ConsumerFactory` и `ProducerFactory`.  
Консьюмер: `RECORD`-режим коммитов. Продюсер: `acks=all` + идемпотентность.

---

## Что не успели и почему

### MapStruct-маппер
Зависимость `mapstruct-processor` добавлена в `build.gradle`, но маппер так и не написан — маппинг выполняется вручную в `CandidateServiceImpl`. Рефакторинг на `@Mapper` улучшил бы читаемость, но не менял бы поведение.

### Dead Letter Topic (DLT)
`DefaultErrorHandler` с `FixedBackOff(1000, 3)` логирует ошибки, но не отправляет сообщения в DLT. При продакшн-нагрузке это означает возможную потерю событий после 3 попыток.
