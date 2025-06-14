# Практическая работа №5

## Глава 1. Аппаратные датчики мобильного устройства

В первой главе изучена работа с сенсорным оборудованием смартфона.
В проекте активирована безопасная привязка разметки к коду (View Binding). 
Затем получен системный диспетчер датчиков, сформирован перечень всех доступных аппаратных и 
виртуальных сенсоров и выведен на экран в виде списка, где для каждого устройства показаны его название и рабочий диапазон.

### Задание — список датчиков

Пользователь видит полный перечень сенсоров своего телефона: акселерометр, гироскоп, датчик освещённости, 
барометр и т. д. Реализации с меньшим энергопотреблением располагаются в начале списка, виртуальные — в конце.

![image_2025-05-29_23-44-33](https://github.com/user-attachments/assets/07fcfb7d-13a1-470a-ac82-266bcebfb1c4)

## Глава 2. Показания акселерометра

Создан отдельный модуль, который в реальном времени отображает три значения ускорения по осям X, Y, Z. 
При наклоне или встряхивании телефона цифры мгновенно обновляются, позволяя наблюдать физические отклонения устройства.

![image_2025-05-29_23-49-22](https://github.com/user-attachments/assets/69cab981-84c9-48ed-b52e-df3384471dca)

---

## Глава 3. Механизм разрешений Android

Третья глава посвящена динамическому управлению «опасными» разрешениями.
В манифест проекта добавлены разрешения на использование камеры, микрофона и внешней памяти, а в самих активностях реализована трёхшаговая логика:
  1. Проверка текущего статуса разрешения.
  2. Запрос у пользователя, если разрешение ещё не выдано.
  3. Обработка ответа и обновление интерфейса в зависимости от решения.
При первом обращении к ресурсу система показывает стандартный диалог, где можно согласиться, дать однократный доступ или отказать. Решения пользователя сохраняются и проверяются при каждом запуске.

---

## Глава 4. Работа с камерой

Создан модуль Camera. При нажатии на изображение запускается штатное приложение-камера, а снимок автоматически сохраняется во 
внутреннюю папку Pictures текущего приложения. Для безопасной передачи файла используется компонент FileProvider, 
благодаря которому другой программе передаётся защищённый URI без прямого доступа к файловой системе. Полученная фотография тут же отображается на экране.

![image_2025-05-30_00-11-38](https://github.com/user-attachments/assets/a681980a-2a15-41ed-b65b-4d67a859eb79)
![image_2025-05-30_00-11-51](https://github.com/user-attachments/assets/faceb84d-73a1-491a-ab9f-941265b7c419)
![image_2025-05-30_00-18-05](https://github.com/user-attachments/assets/85f97f9b-16a9-4aab-bcf7-3811f1c03f48)

---

## Глава 5. Диктофон на MediaRecorder

В пятой главе разработан диктофон. Интерфейс состоит из двух кнопок — «Начать запись» (с подписью группы и номера в списке) и «Воспроизвести». 
При нажатии первой начинается запись звука во внутреннюю память в формате 3GP, при повторном — запись останавливается и вторая кнопка становится активной. 
Во время воспроизведения первой кнопкой пользоваться нельзя, что исключает одновременную запись и прослушивание.

![image_2025-05-30_00-21-07](https://github.com/user-attachments/assets/93662ae9-57da-4e18-a848-068cb6616e45)
![image_2025-05-30_00-21-16](https://github.com/user-attachments/assets/c4771cbb-c87f-4667-bb33-330747aef173)
![image_2025-05-30_00-21-24](https://github.com/user-attachments/assets/233010c2-d617-4430-8309-49983b415df4)
![image_2025-05-30_00-21-33](https://github.com/user-attachments/assets/6936e21b-5ece-4157-8148-aff1b467ad9c)
![image_2025-05-30_00-22-00](https://github.com/user-attachments/assets/820c7d6e-8b84-4d42-9560-011dd12a95af)

---

## Глава 6. Контрольное задание — MireaProject: Hardware Hub

К существующему приложению MireaProject добавлен фрагмент Hardware Hub, объединяющий весь функционал работы с аппаратной частью: компас, шумомер и фото-заметка.

![image_2025-05-30_00-56-25](https://github.com/user-attachments/assets/cd7fa36e-c723-4825-b059-cf177d3c73fc)
![image_2025-05-30_00-59-26](https://github.com/user-attachments/assets/8bf4ef96-8e69-4ebe-a7ab-80c190dab5c6)
![image_2025-05-30_01-01-58](https://github.com/user-attachments/assets/e435494a-ef92-4489-b690-6a23274a1abb)

---

## Вывод

В ходе работы закреплены навыки:
  безопасное подключение макетов без методов поиска представлений;
  получение и визуализация потоковых данных с датчиков;
  объединение информации нескольких сенсоров для решения прикладных задач;
  корректное использование механизма динамических разрешений Android 6+;
  вызов системной камеры с последующим сохранением и отображением снимка;
  запись и воспроизведение аудио с учётом ограничений многозадачности;
  интеграция всех модулей в единое приложение, демонстрирующее комплексное использование аппаратных возможностей современного смартфона.

---

**Выполнил**: Мусин М.Р.  
**Группа**: БСБО-09-22
