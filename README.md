API

авторизация
POST /api/auth/login

запрос:
{
    "name" : "username",
    "password" : "password"
}

ответ при успешной авторизации:
{
    "result":  true,
    "user": {
        "id": 111,
        "name": "Mike",
        "moderation": true,
    }
}

ответ при ошибке
{
    "result": false
}