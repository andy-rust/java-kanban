package test;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

class InMemoryTaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    public void setUp() {
        manager = Managers.getDefault();
    }

    @Test
    void shouldAddAndRetrieveTaskById() {
        Task task = new Task("Задача", "Описание задачи");
        Task addedTask = manager.addTask(task);
        assertTrue(addedTask.getId() > 0, "ID задачи должен быть больше 0 после добавления");
        Task retrievedTask = manager.getTask(addedTask.getId());
        assertEquals(addedTask, retrievedTask, "Извлеченная задача должна соответствовать добавленной");
    }

    @Test
    void shouldAddAndRetrieveEpicById() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Epic addedEpic = manager.addEpics(epic);
        assertTrue(addedEpic.getId() > 0, "ID эпика должен быть больше 0 после добавления");
        Epic retrievedEpic = manager.getEpic(addedEpic.getId());
        assertEquals(addedEpic, retrievedEpic, "Извлеченный эпик должен соответствовать добавленному");
    }

    @Test
    void shouldAddAndRetrieveSubtaskById() {
        Epic epic = new Epic("Эпик для подзадачи", "Описание эпика");
        Epic addedEpic = manager.addEpics(epic);
        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", addedEpic.getId());
        Subtask addedSubtask = manager.addSubTask(subtask);
        assertTrue(addedSubtask.getId() > 0, "ID подзадачи должен быть больше 0 после добавления");
        Subtask retrievedSubtask = manager.getSubtask(addedSubtask.getId());
        assertEquals(addedSubtask, retrievedSubtask,
                "Извлеченная подзадача должна соответствовать добавленной");
    }

    @Test
    void shouldNotConflictGeneratedIdAndGivenId() {
        Task taskWithGeneratedId = new Task("Тестовая задача", "Описание тестовой задачи");
        Task addedTaskWithGeneratedId = manager.addTask(taskWithGeneratedId);

        Task taskWithGivenId = new Task("Другая задача", "Описание другой задачи");
        int customId = 100;
        taskWithGivenId.setId(customId);
        manager.addTask(taskWithGivenId);

        assertNotNull(manager.getTask(addedTaskWithGeneratedId.getId()),
                "Задача с генерируемым ID должна быть найдена");
    }

    @Test
    void shouldRemainImmutableAfterAdding() {
        Task originalTask = new Task("Название задачи", "Описание задачи");
        Task addedTask = manager.addTask(originalTask);

        assertEquals(originalTask.getTitle(), addedTask.getTitle(),
                "Название задачи должно остаться неизменным после добавления");
        assertEquals(originalTask.getDescription(), addedTask.getDescription(),
                "Описание задачи должно остаться неизменным после добавления");
    }

    @Test
    void shouldUpdateTask() {
        Task originalTask = new Task("Название задачи", "Описание задачи");
        Task addedTask = manager.addTask(originalTask);
        addedTask.setTitle("Обновление названия");
        addedTask.setDescription("Обновление описания");
        manager.updateTask(addedTask);
        Task updatedTask = manager.getTask(addedTask.getId());
        assertEquals(updatedTask.getTitle(),"Обновление названия",
                "Название задачи должно быть обновлено");
        assertEquals(updatedTask.getDescription(),"Обновление описания",
                "Описание задачи должно быть обновлено");
    }

    @Test
    void shouldDeleteTask() {
        Task task = new Task("Задача", "Описание задачи");
        Task addedTask = manager.addTask(task);
        assertNotNull(manager.getTask(addedTask.getId()), "Задача должна существовать до удаления");
        manager.deleteTask(addedTask.getId());
        assertNull(manager.getTask(addedTask.getId()), "Задача должна быть удалена");
    }

    @Test
    void shouldUpdateEpic() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Epic addedEpic = manager.addEpics(epic);
        addedEpic.setTitle("Обновленный эпик");
        addedEpic.setDescription("Обновленное описание эпика");
        manager.updateEpic(addedEpic);
        Epic updatedEpic = manager.getEpic(addedEpic.getId());
        assertEquals(updatedEpic.getTitle(), "Обновленный эпик",
                "Название эпика должно быть обновлено");
        assertEquals( updatedEpic.getDescription(), "Обновленное описание эпика",
                "Описание эпика должно быть обновлено");
    }

    @Test
    void shouldDeleteEpic() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Epic addedEpic = manager.addEpics(epic);
        assertNotNull(manager.getEpic(addedEpic.getId()), "Эпик должен существовать до удаления");
        manager.deleteEpic(addedEpic.getId());
        assertNull(manager.getEpic(addedEpic.getId()), "Эпик должен быть удален");
    }

    @Test
    void shouldUpdateSubtask() {
        Epic epic = new Epic("Эпик для подзадач", "Описание эпика");
        Epic addedEpic = manager.addEpics(epic);
        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", addedEpic.getId());
        Subtask addedSubtask = manager.addSubTask(subtask);
        addedSubtask.setTitle("Обновленная подзадача");
        addedSubtask.setDescription("Обновленное описание подзадачи");
        manager.updateSubtask(addedSubtask);
        Subtask updatedSubtask = manager.getSubtask(addedSubtask.getId());
        assertEquals(updatedSubtask.getTitle(), "Обновленная подзадача",
                "Название подзадачи должно быть обновлено");
        assertEquals(updatedSubtask.getDescription(), "Обновленное описание подзадачи",
                "Описание подзадачи должно быть обновлено");
    }

    @Test
    void shouldDeleteSubtask() {
        Epic epic = new Epic("Эпик для подзадач", "Описание эпика");
        Epic addedEpic = manager.addEpics(epic);
        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", addedEpic.getId());
        Subtask addedSubtask = manager.addSubTask(subtask);
        assertNotNull(manager.getSubtask(addedSubtask.getId()), "Подзадача должна существовать до удаления");
        manager.deleteSubtasks(addedSubtask.getId());
        assertNull(manager.getSubtask(addedSubtask.getId()), "Подзадача должна быть удалена");
    }
}