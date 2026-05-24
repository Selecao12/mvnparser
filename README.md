# MVN Parser

Spring Boot приложение на Kotlin для парсинга зависимостей Maven из репозитория [search.maven.org](https://search.maven.org/).

## Описание

Приложение предоставляет REST API для получения списка транзитивных зависимостей (implementation) для указанных Maven-координат (groupId, artifactId, version).

## Технологии

- **Kotlin** 1.6.21
- **Spring Boot** 2.7.5
- **Spring WebFlux** (реактивный веб-стек)
- **OpenFeign** (HTTP-клиент для взаимодействия с внешними API)
- **Springdoc OpenAPI** (документация API)
- **Jackson** (сериализация JSON/XML)

## Требования

- Java 17+
- Gradle

## Сборка и запуск

### Сборка проекта

```bash
./gradlew build
```

### Запуск приложения

```bash
./gradlew bootRun
```

После запуска приложение будет доступно по адресу: `http://localhost:8080`

## API Endpoints

### GET /v1/parse

Парсит зависимости для указанной Maven-зависимости.

**Параметры запроса:**
- `groupId` - groupId Maven-зависимости (обязательный)
- `artifactId` - artifactId Maven-зависимости (обязательный)
- `version` - версия Maven-зависимости (обязательный)

**Пример запроса:**
```bash
curl "http://localhost:8080/v1/parse?groupId=org.springframework.boot&artifactId=spring-boot-starter-web&version=2.7.5"
```

**Ответ:** Список строк с координатами зависимостей в формате `groupId:artifactId:version`.

---

### POST /v1/parse-all

Парсит зависимости для списка Maven-зависимостей.

**Тело запроса:** JSON массив строк с зависимостями в формате `groupId:artifactId:version`.

**Параметры запроса:**
- `returnRequestImplementations` (опциональный, по умолчанию `true`) - включать ли запрошенные зависимости в ответ

**Пример запроса:**
```bash
curl -X POST "http://localhost:8080/v1/parse-all?returnRequestImplementations=true" \
  -H "Content-Type: application/json" \
  -d '[
    "org.springframework.boot:spring-boot-starter-web:2.7.5",
    "com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4"
  ]'
```

**Ответ:** Уникальный набор всех найденных зависимостей.

## Конфигурация

Приложение использует следующие конфигурационные свойства (префикс `mvnparse`):

| Свойство | Описание | Значение по умолчанию |
|----------|----------|----------------------|
| `mvnparse.api-url` | URL API для парсинга | - |

Для настройки создайте файл `application.yml` или `application.properties` в директории `src/main/resources/`.

**Пример application.yml:**
```yaml
mvnparse:
  api-url: https://search.maven.org/

server:
  port: 8080
```

## Документация API

После запуска приложения документация Swagger UI доступна по адресу:

```
http://localhost:8080/swagger-ui.html
```

Также доступен OpenAPI спецификация в формате JSON:

```
http://localhost:8080/v3/api-docs
```

## Структура проекта

```
src/
├── main/
│   ├── kotlin/
│   │   └── com/example/mvnparser/
│   │       ├── MvnparserApplication.kt    # Точка входа Spring Boot
│   │       ├── config/                    # Конфигурация приложения
│   │       ├── controller/                # REST контроллеры
│   │       ├── model/                     # Модели данных
│   │       └── service/                   # Бизнес-логика и сервисы
│   └── resources/                         # Ресурсы приложения
└── test/
    └── kotlin/
        └── com/example/mvnparser/         # Тесты
```

## Тестирование

Запуск тестов:

```bash
./gradlew test
```

## Лицензия

[Укажите лицензию вашего проекта]

## Контакты

[Укажите контактную информацию]
