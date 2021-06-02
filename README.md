<H1>API</H1>

<h3>авторизация<br>
POST /api/auth/login</h3>

запрос:
{
    "name" : "username",
    "password" : "password"
}

ответ при успешной авторизации - статус 200
{
    "result":  true,
    "user": {
        "id": 111,
        "name": "Mike",
        "moderation": true,
    }
}

ответ при ошибке - статус 200
{
    "result": false
}


<h3>создание опроса<br>
POST /api/surveys/</h3>
ответ при успешном создании - статус 200
{
    "result": true
}

ответ при ошибке - статус 200
{
    "result": false
    "errors":{
        "some error":"description"    
    }
}

<h3>удаление<br>
DELETE /api/surveys/ID</h3>
ответ при успешном удалении - статус 200
{   
    "result": true
}
ответ при ошибке - статус 404
{   
"result": false
}
