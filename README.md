Задача: спроектировать и разработать API для системы опросов пользователей.

Функционал для администратора системы:

- авторизация в системе (регистрация не нужна)
- добавление/изменение/удаление опросов. Атрибуты опроса: название, дата старта, дата окончания, описание. После
  создания поле "дата старта" у опроса менять нельзя
- добавление/изменение/удаление вопросов в опросе. Атрибуты вопросов: текст вопроса, тип вопроса (ответ текстом, ответ с
  выбором одного варианта, ответ с выбором нескольких вариантов)

Функционал для пользователей системы:

- получение списка активных опросов
- прохождение опроса: опросы можно проходить анонимно, в качестве идентификатора пользователя в API передаётся числовой
  ID, по которому сохраняются ответы пользователя на вопросы; один пользователь может участвовать в любом количестве
  опросов
- получение пройденных пользователем опросов с детализацией по ответам (что выбрано) по ID уникальному пользователя

Использовать следующие технологии: Spring Framework.

<H1>API</H1>

<h3>авторизация<br>
POST /api/auth/login</h3>

запрос:<br>
{<br>
&nbsp;&nbsp;&nbsp;&nbsp;"name" : "username",<br>
&nbsp;&nbsp;&nbsp;&nbsp;"password" : "password"<br>
}

ответ при успешной авторизации - статус 200 <br>
{<br>
&nbsp;&nbsp;&nbsp;&nbsp;"result":  true,<br>
&nbsp;&nbsp;&nbsp;&nbsp;"user": <br>
&nbsp;&nbsp;&nbsp;&nbsp;{<br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"id": 111,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"name": "Mike",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"moderation": true,<br>&nbsp;&nbsp;&nbsp;&nbsp; }<br> }

ответ при ошибке - статус 200<br> {<br>
&nbsp;&nbsp;&nbsp;&nbsp;"result": false<br>
}

<h3>работа с изменениями опросов доступна только модератору</h3>

<h3>создание опроса<br>
POST /api/surveys/</h3>
ответ при успешном создании - статус 200 <br>
{<br>
&nbsp;&nbsp;&nbsp;&nbsp;"result": true <br>}

ответ при ошибке - статус 200<br> {

&nbsp;&nbsp;&nbsp;&nbsp;"result": false<br>
&nbsp;&nbsp;&nbsp;&nbsp;"errors":<br>
&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"some error":"description"<br>&nbsp;&nbsp;&nbsp;&nbsp;} <br>}

<h3>удаление опроса<br>
DELETE /api/surveys/ID</h3>
ответ при успешном удалении - статус 200 <br>{<br>
&nbsp;&nbsp;&nbsp;&nbsp;"result": true<br> }<br>

ответ при ошибке - статус 404 <br>{<br>
&nbsp;&nbsp;&nbsp;&nbsp;"result": false <br>}

<h3>редактирование опроса<br>
PUT /api/surveys/ID</h3>
ответ при успешном редактировании - статус 200 <br>
{<br>
&nbsp;&nbsp;&nbsp;&nbsp;"result": true<br> }<br>

ответ при ошибке - статус 200<br>
{
&nbsp;&nbsp;&nbsp;&nbsp;"result": false<br>
&nbsp;&nbsp;&nbsp;&nbsp;"errors":<br>
&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"some error":"description"<br>&nbsp;&nbsp;&nbsp;&nbsp;} <br>}

ответ при отсутствии опроса - статус 404

<h3>Анонимный пользователь получает id, которого заведомо нет в базе. 
В момент генерации соответствующий user заноситься в базу (этот механизм не реализован).
Предполагается что фронт фиксирует этот id и заполняет соответсвующее поле anonymousId в request классах.</h3>

<h3>получение списка активных постов (активны те, чей период попадает в текущую дату)<br>
GET /api/surveys/active</h3>
успешный ответ - статус 200<br>
{<br>
&nbsp;&nbsp;&nbsp;&nbsp;"count": 2,<br>
&nbsp;&nbsp;&nbsp;&nbsp;"surveys": [<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"id": 1,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"title": "Опрос № 1"<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"id": 4,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"title": "Опрос № 4"<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>
&nbsp;&nbsp;&nbsp;&nbsp;]<br>
}<br>

ответ при отсутствии опроса - статус 404

<h3>Прохождение опроса. Предполагается, что предварительно получен сам опрос по запросу
/api/surveys/ID со следующим результатом:</h3>
{<br>
"id": 1,
"count": 3,
"questions": [
{
"id": 1,
"text": "Первый вопрос 1 опроса",
"type": "MULTIPLE_CHOICE",
"amount_of_items": 4
},
{
"id": 2,
"text": "Второй вопрос 1 опроса",
"type": "SINGLE_CHOICE",
"amount_of_items": 3
},
{
"id": 3,
"text": "Третий вопрос 1 опроса",
"type": "TEXT"
}
]
}