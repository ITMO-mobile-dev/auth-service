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



