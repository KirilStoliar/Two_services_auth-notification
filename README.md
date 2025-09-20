# Two_services_auth-notification

## 📌 Описание проекта

Приложение состоит из двух микросервисов:

1. **Auth Service**  

   Отвечает за аутентификацию и авторизацию пользователей, управление ролями и CRUD-операции над пользователями. 
    
   - Роли: `USER`, `ADMIN`  
       - `USER` может управлять только своим аккаунтом  
       - `ADMIN` может управлять любыми пользователями  
   - Используется **JWT** для авторизации  
   - Открытые эндпоинты: регистрация и логин  
   - Все остальные действия требуют токена  

2. **Notification Service**  

   Отвечает за отправку email-уведомлений.  

   - При удалении пользователя администратором рассылается уведомление всем администраторам  
   - Используется Spring MailSender + SMTP  
   - Email-адреса администраторов настраиваются в `application.yml`  

---

## ⚙️ Стек технологий

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Security, JWT**
- **Spring Data JPA, Hibernate**
- **Spring Mail**
- **PostgreSQL**
- **Liquibase** (миграции базы данных)
- **Docker & Docker Compose**
- **Maven**
- **Spring-dotenv** (для работы с `.env` в локальной среде)
- **OpenAPI / Swagger** (документация API)
- **Lombok**

---

## 📂 Структура проекта

```
project-root/
│── auth-microservice/ # сервис аутентификации и управления пользователями
│ └── src/main/java/...
│ └── docker-compose.yml 
│ └── Dockerfile
│ └── pom.xml 
│── notification-microservice/ # сервис уведомлений
│ └── src/main/java/...
│ └── .env 
│ └── docker-compose.yml 
│ └── Dockerfile
│ └── pom.xml 
│── docker-compose.yml
│── pom.xml
│── README.md
```

---

## 🚀 Запуск проекта

### 1. Клонирование репозитория

```bash
git clone https://github.com/KirilStoliar/Two_services_auth-notification
cd Two_services_auth-notification
```

2. Настройка окружения

Создайте файл .env в корне notification-microservice (указано в структуре проекта) и пропишите переменные:
```env
# SMTP
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your.email@gmail.com
SPRING_MAIL_PASSWORD=your_app_password
```

3. Пропишите свои данные к базе данных

- docker-compose в auth

```
    environment:
      POSTGRES_DB: usersdb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres

    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://auth-db:5432/usersdb
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
```

- application.yml в auth

```
spring:
  datasource:
    url: jdbc:postgresql://auth-db:5432/usersdb
    username: postgres
    password: postgres
```

4. Сборка сервисов

```bash
mvn clean
mvn install
```

5. Запуск через Docker Compose

```bash
docker-compose up --build
```

После запуска будут доступны:

- Auth Service → http://localhost:8080/swagger-ui.html
- Notification Service → http://localhost:8081/swagger-ui.html
- Postgres → localhost:5432

### 🔑 Основные эндпоинты

Auth Service (http://localhost:8080)

+ POST /api/auth/register → регистрация пользователя

+ POST /api/auth/login → вход, получение JWT

+ GET /api/users/{id} → получить пользователя (User сам себя (Owner) или ADMIN)

+ PUT /api/users/{id} → обновить данные пользователя (Owner или ADMIN)

+ DELETE /api/users/{id} → удалить пользователя (Owner или ADMIN)

+ GET /api/users → получить список пользователей (только ADMIN)

Notification Service (http://localhost:8081)

+ POST /api/notifications/send → отправить уведомление вручную

+ Автоматическая отправка уведомлений при создании, обновлении или удалении пользователя

### 🛡️ Безопасность

+ Авторизация через JWT

+ Пароли хранятся в БД только в захэшированном виде (BCrypt)

+ Доступ к CRUD операциям ограничен ролями

### 📖 Swagger документация

Каждый сервис имеет встроенную Swagger UI документацию:

+ Auth Service → http://localhost:8080/swagger-ui.html

+ Notification Service → http://localhost:8081/swagger-ui.html

### 🐳 Работа с Docker

+ Пересобрать сервисы:

```bash
docker-compose build
```

+ Посмотреть логи конкретного сервиса:

```bash
docker-compose logs -f auth-service
docker-compose logs -f notification-service
```

+ Остановить сервисы:

```bash
docker-compose down
```

