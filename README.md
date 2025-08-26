# RestBook - CRUD Application for Book Management

Полноценное CRUD-приложение для управления книгами, построенное с использованием Spring Boot, Spring Data JPA и Redis кеширования.

## 🎯 Что было реализовано

Полноценное CRUD-приложение для управления книгами с использованием современных технологий Spring Boot, Spring Data JPA и Redis кеширования.

## 🏗️ Технологии

- **Spring Boot 3.2.0** - основной фреймворк
- **Spring Data JPA** - для работы с базой данных
- **Spring Boot Redis** - для кеширования
- **PostgreSQL** - основная база данных
- **Redis** - кеширование
- **Docker & Docker Compose** - контейнеризация инфраструктуры
- **Maven** - система сборки

## 🏗️ Архитектура

### Сущности

1. **Book** - сущность книги
   - id (Long)
   - title (String) - название книги
   - author (String) - автор
   - isbn (String) - ISBN
   - publicationYear (Integer) - год публикации
   - description (String) - описание
   - category (Category) - категория (связь ManyToOne)
   - createdAt (LocalDateTime) - дата создания
   - updatedAt (LocalDateTime) - дата обновления

2. **Category** - сущность категории
   - id (Long)
   - name (String) - название категории
   - books (List<Book>) - список книг (связь OneToMany)

### Связи между сущностями
- Связь "многие к одному" (ManyToOne) между Book и Category
- Категории создаются автоматически при создании книги
- Если категория уже существует, она переиспользуется

### Кеширование

Приложение использует Redis для кеширования:

1. **Кеш "books"** - для поиска книг по названию и автору
   - Ключ: `title + '_' + author`
   - TTL: 10 минут

2. **Кеш "booksByCategory"** - для поиска книг по категории
   - Ключ: `categoryName`
   - TTL: 10 минут

### Инвалидация кеша

Кеши инвалидируются при:
- Создании новой книги
- Обновлении книги
- Удалении книги

Используется `@CacheEvict` с `beforeInvocation = true` для инвалидации до выполнения операции.

## 🚀 API Endpoints

### 1. Получить список всех книг
```
GET /api/books
```

### 2. Поиск книги по названию и автору
```
GET /api/books/search?title={title}&author={author}
```

### 3. Поиск книг по категории
```
GET /api/books/category/{categoryName}
```

### 4. Создание книги
```
POST /api/books
Content-Type: application/json

{
  "title": "Название книги",
  "author": "Автор",
  "categoryName": "Категория",
  "isbn": "978-1234567890",
  "publicationYear": 2023,
  "description": "Описание книги"
}
```

### 5. Обновление книги
```
PUT /api/books/{id}
Content-Type: application/json

{
  "title": "Новое название",
  "author": "Новый автор",
  "categoryName": "Новая категория",
  "isbn": "978-0987654321",
  "publicationYear": 2024,
  "description": "Новое описание"
}
```

### 6. Удаление книги
```
DELETE /api/books/{id}
```

## 🐳 Docker Setup

### Предварительные требования

1. **Docker Desktop** - скачайте с [docker.com](https://www.docker.com/products/docker-desktop/)
2. **Docker Compose** - обычно включен в Docker Desktop

### Установка Docker Desktop

#### Windows
1. Скачайте Docker Desktop для Windows с официального сайта
2. Установите Docker Desktop
3. Запустите Docker Desktop
4. Дождитесь, пока Docker Engine полностью загрузится

### Проверка установки
```bash
docker --version
docker-compose --version
```

### Docker Compose
- **PostgreSQL 15** - основная база данных
- **Redis 7** - кеширование
- **Health checks** - проверка состояния сервисов
- **Volumes** - персистентность данных

### Скрипты автоматизации
- `start-infrastructure.ps1` - запуск инфраструктуры
- `stop-infrastructure.ps1` - остановка инфраструктуры

## 🚀 Запуск приложения

### Предварительные требования

1. **Java 17** или выше
2. **Maven 3.6** или выше
3. **Docker Desktop** (для PostgreSQL и Redis)
4. **Docker Compose** (обычно включен в Docker Desktop)

### Запуск инфраструктуры

#### Автоматический запуск (рекомендуется):

1. **Запустите инфраструктуру:**
```powershell
# Windows
.\start-infrastructure.ps1

# Linux/macOS
docker-compose up -d
```

2. **Остановите инфраструктуру:**
```powershell
# Windows
.\stop-infrastructure.ps1

# Linux/macOS
docker-compose down
```

#### Ручной запуск:

1. **Запустите PostgreSQL и Redis:**
```bash
docker-compose up -d
```

2. **Проверьте статус сервисов:**
```bash
docker-compose ps
```

3. **Остановите сервисы:**
```bash
docker-compose down
```

### Запуск приложения

1. **Клонируйте репозиторий:**
```bash
git clone <repository-url>
cd RestBook
```

2. **Соберите проект:**
```bash
mvn clean install
```

3. **Запустите инфраструктуру:**
```powershell
# Windows
.\start-infrastructure.ps1

# Linux/macOS
docker-compose up -d
```

4. **Запустите приложение:**
```bash
# Windows PowerShell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-24"; .\mvnw.cmd spring-boot:run

# Linux/macOS
./mvnw spring-boot:run
```

### Подключение к базе данных

#### PostgreSQL
- **Host**: `localhost:5432`
- **Database**: `restbook`
- **Username**: `postgres`
- **Password**: `postgres`

#### Redis
- **Host**: `localhost:6379`

## 🗄️ Управление контейнерами

### Просмотр запущенных контейнеров
```bash
docker ps
```

### Просмотр логов PostgreSQL
```bash
docker logs restbook-postgres
```

### Просмотр логов Redis
```bash
docker logs restbook-redis
```

### Подключение к PostgreSQL
```bash
docker exec -it restbook-postgres psql -U postgres -d restbook
```

### Подключение к Redis
```bash
docker exec -it restbook-redis redis-cli
```

## 🧪 Интерактивное тестирование API

Приложение включает удобный веб-интерфейс для тестирования всех API endpoints прямо из браузера!

### Доступ к интерфейсу:
- **Главная страница**: http://localhost:8080/
- **Прямое тестирование API**: http://localhost:8080/ (нажмите кнопки "Тестировать")

### Доступные функции:
- **📖 Получить все книги** - мгновенное получение списка всех книг
- **🔍 Поиск книги** - поиск по названию и автору с модальным окном
- **📂 Поиск по категории** - поиск книг в определенной категории
- **➕ Создание книги** - создание новой книги с полной формой
- **✏️ Обновление книги** - редактирование существующей книги
- **🗑️ Удаление книги** - удаление книги по ID с подтверждением

### Особенности интерфейса:
- ✅ Красивый современный дизайн
- ✅ Модальные окна для ввода данных
- ✅ Валидация форм
- ✅ Отображение результатов в JSON формате
- ✅ Цветовая индикация успеха/ошибки
- ✅ Подтверждение для критических операций
- ✅ Адаптивный дизайн для мобильных устройств

## 🧪 Тестирование API

### Примеры запросов с curl

1. **Получение всех книг:**
```bash
curl "http://localhost:8080/api/books"
```

2. **Поиск книги:**
```bash
curl "http://localhost:8080/api/books/search?title=The%20Great%20Gatsby&author=F.%20Scott%20Fitzgerald"
```

3. **Поиск книг по категории:**
```bash
curl "http://localhost:8080/api/books/category/Fiction"
```

4. **Создание книги:**
```bash
curl -X POST "http://localhost:8080/api/books" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "New Book",
    "author": "New Author",
    "categoryName": "Fiction",
    "isbn": "978-1234567890",
    "publicationYear": 2023,
    "description": "A new book description"
  }'
```

5. **Обновление книги:**
```bash
curl -X PUT "http://localhost:8080/api/books/1" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated Book Title",
    "author": "Updated Author",
    "categoryName": "Non-Fiction",
    "isbn": "978-0987654321",
    "publicationYear": 2024,
    "description": "Updated description"
  }'
```

6. **Удаление книги:**
```bash
curl -X DELETE "http://localhost:8080/api/books/1"
```

## 🔧 Конфигурация

### Профили Spring Boot
- **dev** - для разработки с PostgreSQL и Redis

### Настройки кеширования
- TTL: 10 минут
- JSON сериализация
- String сериализация для ключей

## 📁 Структура проекта

```
RestBook/
├── src/
│   ├── main/
│   │   ├── java/com/example/restbook/
│   │   │   ├── config/
│   │   │   │   ├── RedisConfig.java
│   │   │   │   ├── JacksonConfig.java
│   │   │   │   └── DataInitializer.java
│   │   │   ├── controller/
│   │   │   │   └── BookController.java
│   │   │   ├── dto/
│   │   │   │   └── BookDto.java
│   │   │   ├── entity/
│   │   │   │   ├── Book.java
│   │   │   │   └── Category.java
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── repository/
│   │   │   │   ├── BookRepository.java
│   │   │   │   └── CategoryRepository.java
│   │   │   ├── service/
│   │   │   │   └── BookService.java
│   │   │   └── RestBookApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── static/
│   │           └── index.html
│   └── test/
├── docker-compose.yml
├── start-infrastructure.ps1
├── stop-infrastructure.ps1
├── docker.env
├── pom.xml
└── README.md
```

## 🎉 Особенности реализации

### Кеширование
- ✅ Гибкие ключи кеша с составными значениями
- ✅ Инвалидация по ключу с `beforeInvocation = true`
- ✅ Настраиваемый TTL для кешей
- ✅ JSON сериализация для сложных объектов
- ✅ Поддержка Java 8 date/time типов (LocalDateTime)

### Управление категориями
- ✅ Автоматическое создание категорий при создании книги
- ✅ Переиспользование существующих категорий
- ✅ Связь "многие к одному" между Book и Category

### Валидация и обработка ошибок
- ✅ Bean Validation для входных данных
- ✅ Глобальный обработчик исключений
- ✅ Единообразная обработка ошибок

### Производительность
- ✅ Redis кеширование для ускорения запросов
- ✅ Lazy loading для связей между сущностями
- ✅ Оптимизированные JPQL запросы

## 🐳 Очистка Docker

### Остановка и удаление контейнеров
```bash
docker-compose down
```

### Удаление контейнеров и томов
```bash
docker-compose down -v
```

### Полная очистка (контейнеры, тома, образы)
```bash
docker-compose down -v --rmi all
```

## 🔧 Устранение неполадок

### Проблема: Порт уже занят
Если порты 5432 или 6379 уже заняты, измените их в `docker-compose.yml`:

```yaml
ports:
  - "5433:5432"  # PostgreSQL на порту 5433
  - "6380:6379"  # Redis на порту 6380
```

### Проблема: Docker не запущен
Убедитесь, что Docker Desktop запущен и работает.

### Проблема: Недостаточно памяти
Увеличьте лимиты памяти в Docker Desktop (Settings -> Resources -> Memory).

### Проблема: LocalDateTime сериализация
Если возникает ошибка с сериализацией LocalDateTime, убедитесь что:
- В `RedisConfig.java` используется кастомный `ObjectMapper` с `JavaTimeModule`
- В `JacksonConfig.java` зарегистрирован `JavaTimeModule`

## 📊 Мониторинг

### Проверка здоровья сервисов
```bash
docker-compose ps
```

### Просмотр использования ресурсов
```bash
docker stats
```

### Просмотр сетей
```bash
docker network ls
```

Приложение логирует:
- Операции с кешем (DEBUG уровень)
- SQL запросы (включены в конфигурации)
- Общие операции приложения

## 🚀 Готово к использованию!

Приложение полностью функционально и включает:
- ✅ Все требуемые CRUD операции
- ✅ Redis кеширование с инвалидацией
- ✅ Поддержка PostgreSQL
- ✅ Docker контейнеризация
- ✅ Автоматическая инициализация данных
- ✅ Валидация и обработка ошибок
- ✅ Подробная документация
- ✅ Веб-интерфейс для тестирования API
- ✅ Поддержка Java 8 date/time типов
