import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void shouldMaintainTaskHistory() {
        // Создаем тестовые задачи
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setId(1);
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        task2.setId(2);

        // Добавляем задачи в менеджер задач
        historyManager.add(task1);
        historyManager.add(task2);

        // Получаем историю задач
        List<Task> history = historyManager.getHistory();

        // Проверяем, что задачи добавились в историю
        assertTrue(history.contains(task1), "История должна содержать задачу 1");
        assertTrue(history.contains(task2), "История должна содержать задачу 2");

        // Проверяем порядок задач в истории
        assertEquals(task1, history.get(0), "Первая задача в истории должна быть задачей 1");
        assertEquals(task2, history.get(1), "Вторая задача в истории должна быть задачей 2");
    }

    @Test
    void shouldNotAllowDuplicateEntries() {
        // Создаем тестовую задачу
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setId(1);

        // Добавляем задачу в менеджер задач
        historyManager.add(task1);

        // Добавляем задачу несколько раз, чтобы она добавилась в историю
        historyManager.add(task1);
        historyManager.add(task1);
        historyManager.add(task1);

        // Получаем историю задач
        List<Task> history = historyManager.getHistory();

        // Проверяем, что задача только одна в истории
        assertEquals(1, history.size(), "История должна содержать только одну запись для каждой задачи");
        assertTrue(history.contains(task1), "История должна содержать задачу 1");
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        // Создаем тестовые задачи
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setId(1);
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        task2.setId(2);

        // Добавляем задачи в менеджер задач
        historyManager.add(task1);
        historyManager.add(task2);

        // Удаляем первую задачу из менеджера задач (это также удалит её из истории)
        historyManager.remove(1);

        // Получаем историю задач
        List<Task> history = historyManager.getHistory();

        // Проверяем, что история не содержит первую задачу
        assertFalse(history.contains(task1), "История не должна содержать задачу 1");
        assertTrue(history.contains(task2), "История должна содержать задачу 2");
    }


    @Test
    void shouldUpdateTaskHistoryOrder() {
        // Создаем тестовые задачи
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setId(1);
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        task2.setId(2);
        Task task3 = new Task("Задача 3", "Описание задачи 3");
        task3.setId(3);

        // Добавляем задачи в менеджер задач
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        // Повторно добавляем первую задачу
        historyManager.add(task1);

        // Получаем историю задач
        List<Task> history = historyManager.getHistory();

        // Проверяем, что порядок задач обновился
        assertEquals(task2, history.get(0), "Первая задача в истории должна быть задачей 2");
        assertEquals(task3, history.get(1), "Вторая задача в истории должна быть задачей 3");
        assertEquals(task1, history.get(2), "Третья задача в истории должна быть задачей 1");
    }

    @Test
    void shouldRemoveTaskFromBeginningOfHistory() {
        // Создаем тестовые задачи
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setId(1);
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        task2.setId(2);
        Task task3 = new Task("Задача 3", "Описание задачи 3");
        task3.setId(3);

        // Добавляем задачи в менеджер задач
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        // Удаляем первую задачу из менеджера задач
        historyManager.remove(1);

        // Получаем историю задач
        List<Task> history = historyManager.getHistory();

        // Проверяем порядок задач в истории после удаления
        assertEquals(2, history.size(), "История должна содержать две задачи");
        assertEquals(task2, history.get(0), "Первая задача в истории должна быть задачей 2");
        assertEquals(task3, history.get(1), "Вторая задача в истории должна быть задачей 3");
    }

    @Test
    void shouldRemoveTaskFromMiddleOfHistory() {
        // Создаем тестовые задачи
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setId(1);
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        task2.setId(2);
        Task task3 = new Task("Задача 3", "Описание задачи 3");
        task3.setId(3);

        // Добавляем задачи в менеджер задач
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        // Удаляем вторую задачу из менеджера задач
        historyManager.remove(2);

        // Получаем историю задач
        List<Task> history = historyManager.getHistory();

        // Проверяем порядок задач в истории после удаления
        assertEquals(2, history.size(), "История должна содержать две задачи");
        assertEquals(task1, history.get(0), "Первая задача в истории должна быть задачей 1");
        assertEquals(task3, history.get(1), "Вторая задача в истории должна быть задачей 3");
    }

    @Test
    void shouldRemoveTaskFromEndOfHistory() {
        // Создаем тестовые задачи
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setId(1);
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        task2.setId(2);
        Task task3 = new Task("Задача 3", "Описание задачи 3");
        task3.setId(3);

        // Добавляем задачи в менеджер задач
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        // Удаляем третью задачу из менеджера задач
        historyManager.remove(3);

        // Получаем историю задач
        List<Task> history = historyManager.getHistory();

        // Проверяем порядок задач в истории после удаления
        assertEquals(2, history.size(), "История должна содержать две задачи");
        assertEquals(task1, history.get(0), "Первая задача в истории должна быть задачей 1");
        assertEquals(task2, history.get(1), "Вторая задача в истории должна быть задачей 2");
    }
}
