## CURL Commands TopJava

### Get All Meals

curl -s http://localhost:8080/topjava/rest/profile/meals --user admin@gmail.com:admin

### Get Meal

curl -s http://localhost:8080/topjava/rest/profile/meals/100003 --user admin@gmail.com:admin

### Create Meal

curl -s -X POST -d "{\"dateTime\":\"2021-11-01T12:00\",\"description\":\"Burger\",\"calories\":500}" -H "Content-Type:
application/json;charset=UTF-8" http://localhost:8080/topjava/rest/profile/meals --user user@yandex.ru:password

### Update Meal

curl -s -X PUT -d "{\"dateTime\":\"2021-12-02T07:00\", \"description\":\"Salad\", \"calories\":200}" -H "Content-Type:
application/json" http://localhost:8080/topjava/rest/profile/meals/100011 --user user@yandex.ru:password

### Delete Meal

curl -s -X DELETE http://localhost:8080/topjava/rest/profile/meals/100011 --user user@yandex.ru:password 

###Filter Meals
curl
-s "http://localhost:8080/topjava/rest/profile/meals/between?startDateTime=2020-01-30T12:00:00&endDateTime=2021-04-30T20:00:00"
--user user@yandex.ru:password

### GetBetween Meals

curl
-s "http://localhost:8080/topjava/rest/profile/meals/filter?startDate=2020-01-30&endDate=2021-04-30&startTime=00:00&endTime=13:00"
--user user@yandex.ru:password