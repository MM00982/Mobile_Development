# Практическая работа №2

## Глава 1

В 1 главе мы изучили основные инструменты отладки в Android Studio:

- Запуск приложения в режиме **Debug** с установкой точек останова для пошагового выполнения кода и проверки значений переменных.  
- Подключение отладчика к уже запущенному процессу через «Attach debugger to Android process».  
- Использование **Android Profiler** для мониторинга загрузки CPU, памяти и сетевой активности в реальном времени.  
- Инструмент **LogCat** и класс `android.util.Log` для вывода и фильтрации сообщений (уровни: error, warning, info, debug, verbose, wtf), особенно полезный при работе с сетью и асинхронными потоками.

---

## Глава 2

В 2 главе мы изучили жизненный цикл `Activity` в Android:

- Понятие задачи (`Task`) и стека `Activity` (Back Stack): как система добавляет и удаляет экраны по принципу «последним вошёл – первым вышел».

![image](https://github.com/user-attachments/assets/e4ce51a9-cf82-4d5c-b990-db377e866596)

- Основные колбэки жизненного цикла и их назначение:  
  - `onCreate` – инициализация UI и восстановление состояния из `Bundle`.  
  - `onStart`/`onPostCreate` – подготовка к показу (UI уже готов, но ещё невидим).  
  - `onResume`/`onPostResume` – `Activity` на переднем плане, готова к взаимодействию.  
  - `onPause` – освобождение лёгких ресурсов при потере фокуса.  
  - `onSaveInstanceState`/`onRestoreInstanceState` – сохранение и восстановление динамического состояния.  
  - `onStop` – освобождение тяжёлых ресурсов, `Activity` остаётся в памяти.  
  - `onRestart` – повторный запуск после `onStop`.  
  - `onDestroy` – финальное уничтожение экземпляра.  
- Сравнение навигации между `Activity` и браузером: `Intent` как ссылка, `Task` как вкладка.

Ответы на вопросы:

1. *Будет ли вызван метод «onCreate» после нажатия на кнопку «Home» и возврата в приложение?*
Нет. При нажатии Home текущая Activity только приостанавливается (onPause, onStop). При повторном входе вызываются onRestart → onStart → onResume; onCreate не запускается, т.к. экземпляр уже существует.

2. *Изменится ли значение поля «EditText» после нажатия на кнопку «Home» и возврата в приложение?*
Не изменится. Интерфейс остаётся в памяти; пользователь увидит тот же введённый текст.

3. *Изменится ли значение поля «EditText» после нажатия на кнопку «Back» и возврата в приложение?*
Да, изменится. Кнопка Back уничтожает Activity (onDestroy). При повторном запуске onCreate создаёт новый экземпляр. Если вы не сохраняете текст в onSaveInstanceState, поле будет пустым (или примет значение по умолчанию).

---

## Глава 3

В 3 главе мы изучили:

- Понятие **Intent** как механизма связи между экранами и компонентами:  
  - **явные** Intent’ы (`new Intent(this, SecondActivity.class)`) для перехода внутри приложения;
  
  ![image](https://github.com/user-attachments/assets/8d719252-b8fb-471e-842d-0404b6e062cf)
  
  ![image](https://github.com/user-attachments/assets/f3b7853f-c8c7-4856-9fbf-4b4eeb86997f)
  
  - **неявные** Intent’ы (с параметрами `action`, `data`, `category`) для вызова внешних функций и выбора пользователя.
  
  ![image](https://github.com/user-attachments/assets/420404bd-488a-4ba0-b776-3d8189965d39)
  
  ![image](https://github.com/user-attachments/assets/e910054f-1dc7-45b6-8283-1a6379e79610)
  
  ![image](https://github.com/user-attachments/assets/cf118a90-184e-43a2-996f-0d64c55307aa)
  
- Роль **Intent Filter** в манифесте для обработки неявных Intent’ов и возможность указать `exported="false"` для ограничения доступа.  
- Передачу данных между Activity через `putExtra`/`getSerializableExtra` (или другие `putXXX`/`getXXX`).  
- Создание многоэкранного приложения: добавление кнопок в разметке, генерация новых Activity через «File → New → Activity», обновление манифеста.  

---

## Глава 4

В 4 главе мы рассмотрели три вида диалоговых окон в Android:

- **Toast** – простые всплывающие уведомления без реакции пользователя (методы `Toast.makeText(...).show()`, возможна настройка позиции через `setGravity`).

![image](https://github.com/user-attachments/assets/fbd163a3-16b7-4521-9e63-6e3563494d1f)
  
- **Notification** – системные уведомления в шторке: создание через `NotificationCompat.Builder`, управление каналами (`NotificationChannel` для Android 8+) и показ через `NotificationManager.notify()`. Не забывать запрашивать разрешение `POST_NOTIFICATIONS` на Android 13+.  

![image](https://github.com/user-attachments/assets/3f995197-56e9-4f25-8099-0ec4bed55a79)

- **Dialog** – модальные окна на базе `DialogFragment`/`AlertDialog`: задаются заголовок, сообщение и до трёх кнопок (`setPositiveButton`, `setNeutralButton`, `setNegativeButton`), реагирующих колбэками в Activity.

![image](https://github.com/user-attachments/assets/f766be84-4536-45f5-9063-5da9ea42e06a)

![image](https://github.com/user-attachments/assets/e807b431-9288-4e42-ba1d-735ecac1ee95)

В подразделениях:
1. Реализуется подсчёт символов и показ `Toast` по нажатию кнопки.  
2. Создаются уведомления в строке состояния с иконкой, заголовком и текстом, а также канал для управления ими.  
3. Пишутся `DialogFragment`‑классы для отображения `AlertDialog` и передачи событий кнопок в Activity.  

---

## Самостоятельная работа

В самостоятельной работе мы освоили и применили `Snackbar`, `TimePickerDialog`, `DatePickerDialog` и `ProgressDialog`. Был создан модуль `Independent`.

![image](https://github.com/user-attachments/assets/3b02834a-7df6-4f5d-9f5b-61c5b91ed11f)

![image](https://github.com/user-attachments/assets/24f1d1fa-df09-4903-b09c-043782d3dc0f)

![image](https://github.com/user-attachments/assets/c71003be-6963-4849-928f-b8550d7ed09c)

![image](https://github.com/user-attachments/assets/da94901a-29c2-4881-b426-4d14a2525365)

![image](https://github.com/user-attachments/assets/6d6d2a45-803a-4016-aafa-0f0370e652f3)

---

## Вывод

Мы изучили, как эффективно отлаживать Android‑приложения с помощью точек останова, LogCat и профайлеров, получать детальную информацию о работе CPU, памяти и сетевой активности; мы познакомились с полным жизненным циклом Activity — от создания и восстановления состояния до остановки и уничтожения — и поняли, как устроен Back Stack; мы освоили механизм навигации между экранами и взаимодействия компонентов через явные и неявные Intent‑ы, передачу данных через Bundle и настройку Intent Filter в манифесте; мы научились уведомлять пользователя через Toast, системные уведомления с каналами и строить модальные диалоги на базе DialogFragment, а также рассмотрели дополнительные элементы взаимодействия — Snackbar, TimePickerDialog, DatePickerDialog и ProgressDialog.

---

**Выполнил**: Мусин М.Р.  
**Группа**: БСБО-09-22
