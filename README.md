# Auth-service

## Настройки проекта

### Общие настройки
  - Версия JVM: `17`
  - Проект запускается по адресу http://localhost:8080

### Как запустить

```
docker compose build
docker compose up -d
```


### Рабочие URL

  - GET: `http://localhost:8080/health` - возвращает OK (защищенный запрос, требуется jwt)

  - POST: `http://localhost:8080/register` - создание нового пользователя (при отправке запроса необходимо передать параметры `login` и `password`, советую использовать Postman)

  - POST: `http://localhost:8080/login` - авторизация, проверка на соотвествие логина и пароля (при отправке запроса необходимо передать параметры `login` и `password`, советую использовать Postman)


### 1. PostgreSQL
  - Ссылка для подключения к Postgres:
  `jdbc:postgresql://localhost:5432/ITMO-mobile`
  - Название БД: `ITMO-mobile`
  - Порт: `5432`
  - Имя пользователя: `postgres`
  - Пароль: `123`

### 2. Redis
  - ссылка для подключения к Redis: `redis://localhost:6379` (используется внутри программы)

### 3. Kafka
  - порт Zookeeper\`a: `2181`
  - порт консьюмера и продюсера: `9092`






