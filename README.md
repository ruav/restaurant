# CRUD ресторанами

## сборка

mvn clean build package

## Запуск

Возможен запуск с 3 различными профилями: 
- postgres
- h2 
- подключение к heroku (по - умолчанию)


# Запуск из командной строки
`java  -jar target/restaurant-0.0.1-SNAPSHOT.jar` 

`java -Dspring.profiles.active=postgres -jar target/restaurant-0.0.1-SNAPSHOT.jar`
 
`java -Dspring.profiles.active=h2 -jar target/restaurant-0.0.1-SNAPSHOT.jar` 


# Отправка писем на сброс пароля 
Для отправки писем со сбросом пароля необходимо в файле MvcConfig
настроить логин и пароль:
- `mailSender.setUsername(""); //почтовый адрес, с которого отправляем письма `

- `mailSender.setPassword(""); // пароль для почтового ящика `

# Получение jwt - токена
 эндпоинт `/rest/authority/{restaurantId}` генерирует jwt - токен, и сохраняет его в куки. 
 А также, дополнительно, возвращает строкой 
 
# Раздел синхронизации через sse
Слушать по адресу `http://localhost:8090/rest/sync`

Для создания записей необходимо пройти авторизацию (сейчас отключено)
Пример создания хостес
`curl -X POST \
  http://localhost:8090/rest/create/hostes \
  -H 'Accept: */*' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: multipart/form-data;' \
  -F 'name=Маша'`
 
 Пример обновления хостес
 `curl -X POST \
   http://localhost:8090/rest/update/hostes \
   -H 'Accept: */*' \
   -H 'Cache-Control: no-cache' \
   -H 'Content-Type: multipart/form-data;' \
   -F 'id=1' \
   -F 'name=Маша'`
 
 пример создания клиента
 `curl -X POST \
   http://localhost:8090/rest/create/client \
   -H 'Accept: */*' \
   -H 'Cache-Control: no-cache' \
   -H 'Content-Type: multipart/form-data; ' \
   -F 'name=Маша' \
   -F 'phone=+12345678' \
   -F vip=false`
   
пример обновления клиента
`curl -X POST \
  http://localhost:8090/rest/update/client \
  -H 'Accept: */*' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: multipart/form-data; ' \
  -F 'id=1' \
  -F 'name=Маша' \
  -F 'phone=+12345678' \
  -F vip=false`