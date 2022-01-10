# Стажировка <a href="https://github.com/JavaWebinar/topjava">Topjava</a>

## Миграция на Spring Boot
За основу взят [финальный код проекта BootJava (без Spring Data Rest)](https://javaops.ru/view/bootjava/lesson07)  
Вычекайте в отдельную папку (как отдельный проект) ветку `spring_boot`:
```
git clone --branch spring_boot --single-branch https://github.com/JavaWebinar/topjava.git topjava_boot
```  
Если будете его менять, [настройте `git remote`](https://javaops.ru/view/bootjava/lesson01#project)

---
Если захотите сами накатить патчи, сделайте ветку от `initial` и в корне создайте каталог `src\test`  
[Патчи](https://drive.google.com/drive/u/1/folders/1ZsPX879m6x4Va0Wy3D1EQIBsnZUOOvao)

----

#### Apply 12_1_init_boot_java
- оставил как в TopJava:
    - название приложения  `Calories Management`
    - имя базы `topjava`
    - пользователей:  `user@yandex.ru` и `admin@gmail.com`
- имя таблицы `user_role` взял из BootJava (вместо `user_roles`, делаем в единственном числе)

#### Apply 12_2_add_calories_meals

Добавил:
- Еду, кэширование, калории
- Таблицу `meals` переименовал в `meal`
- Общие вещи (пусть небольшие) вынес в сервис: `MealService`
- Проверку принадлежности еды делаю в `MealRepository.checkBelong` с исключением `DataConflictException` (не зависит от `org.springframework.web`)
- Вместо своих конверторов использую `@DateTimeFormat`
- Мигрировал все тесты контроллеров. В тестовом проекте столько тестов не обязательно. Достаточно нескольких, на основные юзкейсы.
- Кэширование в выпускном желательно. 7 раз подумайте, что будете кэшировать! **Максимально просто, самые частые запросы, которые редко изменяются**.
- **Добавьте в свой выпускной OpenApi/Swagger - это будет большим плюсом и избавит от необходимости писать документацию**.   
  Не забудьте ссылку `http://localhost:8080/swagger-ui.html` в `readme.md`

### За основу выпускного предлагаю взять этот код миграции, сделав выпускной МАКСИМАЛЬНО в этом стиле.