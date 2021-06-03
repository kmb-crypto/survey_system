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
&nbsp;&nbsp;&nbsp;&nbsp;"id": 1,<br>
&nbsp;&nbsp;&nbsp;&nbsp;"count": 3,<br>
&nbsp;&nbsp;&nbsp;&nbsp;"questions": [<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"id": 1,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"text": "Первый вопрос 1 опроса",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"type": "MULTIPLE_CHOICE",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"amount_of_items": 4<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"id": 2,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"text": "Второй вопрос 1 опроса",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"type": "SINGLE_CHOICE",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"amount_of_items": 3<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"id": 3,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"text": "Третий вопрос 1 опроса",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"type": "TEXT"<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>
&nbsp;&nbsp;&nbsp;&nbsp;]<br>
}

mount_of_items - поле, в котором фиксируется максимальное количество выбираемых ответов данного вопроса с типом, где выбираюся варианты

далее выполняются запросы
<h3>POST /api/answers</h3>
со следующим телом:<br>
для текста<br>
{<br>
&nbsp;&nbsp;&nbsp;&nbsp;"question_id": 3,<br>
&nbsp;&nbsp;&nbsp;&nbsp;"text": "ответ",<br>
&nbsp;&nbsp;&nbsp;&nbsp;"question_type": "TEXT"<br>
}<br>

для выборного ответа:<br>
{<br>
&nbsp;&nbsp;&nbsp;&nbsp;"question_id": 1,<br>
&nbsp;&nbsp;&nbsp;&nbsp;"items": [1,2],<br>
&nbsp;&nbsp;&nbsp;&nbsp;"question_type": "MULTIPLE_CHOICE"<br>
}
ответ привязан либо к авторизованному пользователю (тогда поле id в запросе игнорируется), либо к анониму, id которго передано через request

<h3>получение списка пройденных опросов<br>
GET /api/surveys/complete
</h3>
как и выше - получаем список либо для анонима с id в request, либо для авторизованного пользователя

успешный ответ - статус 200: <br>
{
&nbsp;&nbsp;&nbsp;&nbsp;"count": 2,<br>
&nbsp;&nbsp;&nbsp;&nbsp;"surveys": [<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"id": 1,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"title": "Опрос № 1"<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"id": 3,<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"title": "Опрос № 3"<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>
&nbsp;&nbsp;&nbsp;&nbsp;]<br>
}

<h3>получение списка вопрос-ответ
GET /api/surveys/questions_and_answers</h3>
формат ответа:<br>
{<br>
&nbsp;&nbsp;&nbsp;&nbsp;"count": 3,<br>
&nbsp;&nbsp;&nbsp;&nbsp;"survey_id": 1,<br>
&nbsp;&nbsp;&nbsp;&nbsp;"questions_and_answers":<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"items": [
1,
3,
4
],<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"question_text": "Первый вопрос 1 опроса",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"question_type": "MULTIPLE_CHOICE"<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"items": [
2
],<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"question_text": "Второй вопрос 1 опроса",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"question_type": "SINGLE_CHOICE"<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;},<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"question_text": "Третий вопрос 1 опроса",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"question_type": "TEXT",<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"answer_text": "user 1 ответ на 3 вопрос 1 опроса"<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;]<br>
}


