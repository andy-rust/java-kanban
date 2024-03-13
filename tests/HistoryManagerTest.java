import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void shouldMaintainTaskHistory() {
        // Создаем тестовые задачи
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");

        // Добавляем задачи в историю
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
    void shouldNotExceedMaxHistorySize() {
        for (int i = 0; i < 12; i++) {
            Task task = new Task("Задача " + i, "Описание задачи " + i);
            historyManager.add(task);
        }

        // Получаем историю задач
        List<Task> history = historyManager.getHistory();

        // Проверяем, что размер истории не превышает максимально допустимый
        assertEquals(10, history.size(), "Размер истории должен быть ограничен 10 задачами");

    }

}
