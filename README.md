# Asteroids Game

Классическая аркадная игра "Asteroids" на Java с использованием **LibGDX** и **архитектуры ECS (Entity Component System)**.

## Основные возможности
- Управление кораблем с физикой инерции и трения
- Стрельба лазерами по астероидам
- Разбиение больших астероидов на более мелкие
- Столкновения с астероидами и система жизней
- Телепортация объектов при выходе за границы экрана
- Тряска камеры при получении урона
- Уведомления через **EventBus**
- UI: отображение счета, количества жизней, FPS и размера экрана

## Структура проекта
<pre>
asteroids
 ├── core
 │   ├── src/main/java/op/javagame/asteroids
 │   │   ├── ecs
 │   │   │   ├── components   // Компоненты ECS
 │   │   │   ├── systems      // Системы ECS
 │   │   │   ├── factories    // Фабрики сущностей
 │   │   ├── events          // Шина событий (EventBus)
 │   │   ├── screens         // Экранные состояния игры
 │   ├── assets
 │   │   ├── textures        // Спрайты и фон
 │   │   ├── ui              // Скины UI
 │   ├── build.gradle        // Сборка проекта
 ├── lwjgl3                 // Запуск через LWJGL3
 ├── gradlew                // Gradle Wrapper
 ├── README.md              // Описание проекта
</pre>

## Инструкция по запуску
### 1. Установка зависимостей
Перед запуском убедитесь, что у вас установлен **JDK 8+** и **Gradle**.

### 2. Запуск через Gradle
```sh
./gradlew lwjgl3:run

Или в IntelliJ IDEA:
	•	Открыть java/op/javagame/asteroids/lwjgl3/Lwjgl3Launcher.java
	(asteroids/lwjgl3/src/main/java/op/javagame/asteroids/lwjgl3/Lwjgl3Launcher.java)
	•	Запустить метод main()

3. Сборка JAR

./gradlew lwjgl3:dist

Готовый JAR-файл появится в lwjgl3/build/libs/.

Реализованные алгоритмы

1. Алгоритмы движения
	•	Интеграция Эйлера для плавного перемещения:

position.position.add(velocity.cpy().scl(deltaTime));


	•	Векторное ускорение с трением:

velocity.scl(FRICTION);



2. Коллизии и столкновения
	•	Евклидово расстояние для обнаружения столкновений:

float distance = entity1.position.dst(entity2.position);
if (distance < collider1.radius + collider2.radius) { handleCollision(); }



3. Разбиение астероидов
	•	Процедурное разбиение больших объектов:

Vector2 velocity1 = direction.cpy().rotateDeg(MathUtils.random(-20, -10)).scl(splitSpeed);



4. Генерация случайных объектов
	•	Спавн за пределами экрана:

int side = MathUtils.random(3);



5. Телепортация объектов
	•	Зацикливание мира:

if (position.x < -margin) position.x = screenWidth + margin;



6. Реактивное программирование (EventBus)
	•	Паттерн “Observer”:

EventBus.INSTANCE.addListener(event -> { /* обработка событий */ });



Используемые паттерны проектирования

Паттерн	Применение
Entity Component System (ECS)	Разделение данных и логики на компоненты и системы
Factory Method	Создание игровых объектов через GameEntityFactory
Singleton	EventBus и GameScreen
Observer	Реализация шины событий EventBus
State Machine	Управление экранами через ScreenManager
Flyweight	Использование TextureRegion для оптимизации спрайтов

Технологии
	•	Java (JDK 8+)
	•	LibGDX (OpenGL + Box2D)
	•	Gradle (управление зависимостями)
	•	LWJGL3 (рендеринг и запуск)
	•	Ashley ECS (Entity Component System)

Этот `README.md` содержит:
- Описание проекта
- Структуру файлов
- Инструкции по запуску и сборке
- Реализованные алгоритмы
- Используемые паттерны проектирования
- Список технологий
