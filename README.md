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