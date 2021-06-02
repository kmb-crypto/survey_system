<H1>API</H1>

<h3>авторизация<br>
POST /api/auth/login</h3>

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


<h3>создание опроса<br>
POST /api/surveys/</h3>
