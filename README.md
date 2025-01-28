# successToFile

`successToFile` — это Kotlin-приложение, которое обрабатывает список URL-адресов, проверяет их доступность и записывает успешные URL в файл.

## Функционал

- Проверка списка URL-адресов на доступность.
- Обработка статусов HTTP:
    - **Успешные запросы** (2xx): Запись URL в файл.
    - **Редиректы** (3xx): Переход по новому адресу из заголовка `Location` и проверка доступности.
    - **Ошибки** (4xx, 5xx): Логирование ошибки.
- Возможность задания входного и выходного файлов через параметры.

## Структура проекта

### Основные компоненты

1. **`HttpClientHelper`**  
   Обеспечивает создание и отправку HTTP-запросов, обработку ответа.
    - `getHttpClient()`: Возвращает HTTP-клиент.
    - `getHttpRequest(url: String)`: Создает HTTP-запрос.
    - `getHttpResponse(url: String)`: Выполняет запрос и возвращает ответ.

2. **`HttpStatusHandler`**  
   Обрабатывает статусы HTTP-ответов.
    - `handleSuccessStatus(statusCode: Int, url: String, writer: BufferedWriter)`: Логирует и записывает успешные URL.
    - `handleRedirectStatus(statusCode: Int, url: String, writer: BufferedWriter, httpClientHelper: HttpClientHelper)`: Обрабатывает редиректы.
    - `handleErrorStatus(statusCode: Int, url: String)`: Логирует ошибки.

3. **`CheckUrlsCore`**  
   Основной класс, обрабатывающий входной файл и выполняющий проверку URL.
    - Читает список URL из файла.
    - Обрабатывает HTTP-ответы.
    - Записывает успешные URL в выходной файл.

4. **`CheckUrlsCommand`**  
   Обеспечивает интерфейс командной строки. Принимает:
    - `--input`: Путь к входному файлу с URL.
    - `--output`: Путь к выходному файлу для успешных URL.

## Установка и использование

1. **Клонируйте репозиторий:**
   ```bash
   git clone https://github.com/panisimov812/successToFile.git
   cd successToFile

## УСТАНОВКА
 - Соберите JAR-файл:
 - `./gradlew clean fatJar`

## Запуск приложения
- `java -jar build/libs/successToFile.jar --input input.txt --output output.txt;` 