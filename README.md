# SkillTrack — Система отслеживания и развития профессиональных навыков сотрудников

Микросервисное приложение для учёта навыков, построения треков развития, учёта прогресса и отправки уведомлений (в текущей реализации — только Telegram).

# Архитектура 
В проекте реализованы следующие сервисы:
- **User Service (8081)** — пользователи, роли, аутентификация (JWT), хранение telegramId.
- **Skill Service (8082)** — справочник навыков и уровней.
- **Profile Service (8083)** — текущие навыки сотрудников (user ↔ skill).
- **Track Service (8084)** — индивидуальные планы развития (tracks).
- **Progress Service (8085)** — фиксация прогресса.
- **Notification Service (8086)** — подписывается на Kafka-события от всех микросервисов и **отправляет уведомления в Telegram**.


# Технологии
- Java 21
- Spring Boot 3 (Web, Data JPA, Kafka, Cloud OpenFeign)
- Spring Kafka (consumer)
- PostgreSQL (реляционные данные)
- Kafka (событийная шина)
- JWT (User Service выдаёт токены: `POST /api/auth/login`)
- Docker / Docker Compose 
- Swagger / OpenAPI (Springdoc)
- Telegram Bot API (для отправки уведомлений)

# Безопасность и авторизация
- Аутентификация пользователей: **JWT**, выдаваемый `User Service`

# Конфигурация отправки в Telegram
telegram.bot.token — необходимо указать токен бота.