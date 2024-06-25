import manager.Managers;
import manager.TaskManager;
import manager.HistoryManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ManagersTest {

    @Test
    void shouldReturnInitializedManagersAndCheckTheirFunctionality() {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(taskManager, "Экземпляр TaskManager не должен быть равен null.");
        assertNotNull(historyManager, "Экземпляр HistoryManager не должен быть равен null.");

        // Проверяем функциональность менеджеров
        // Для TaskManager
        assertTrue(taskManager.getAllTasks().isEmpty(), "Список задач должен быть пустым.");
        assertTrue(taskManager.getAllEpics().isEmpty(), "Список эпиков должен быть пустым.");
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Список подзадач должен быть пустым.");

        // Для HistoryManager
        assertTrue(taskManager.getHistory().isEmpty(), "История просмотров должна быть пустой.");
    }

}
