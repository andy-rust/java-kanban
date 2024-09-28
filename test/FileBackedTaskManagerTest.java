import manager.FileBackedTaskManager;
import manager.exceptions.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        tempFile.deleteOnExit();
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    public void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void shouldSaveAndLoadEmptyManager() {
        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getAllTasks().isEmpty(), "Задачи не должны быть загружены");
        assertTrue(loadedManager.getAllEpics().isEmpty(), "Эпики не должны быть загружены");
        assertTrue(loadedManager.getAllSubtasks().isEmpty(), "Подзадачи не должны быть загружены");
        assertTrue(loadedManager.getHistory().isEmpty(), "История должна быть пустой");
    }

    @Test
    public void shouldSaveMultipleTasks() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        manager.addTask(task1);
        manager.addTask(task2);

        manager.save();

        List<String> lines = null;
        try {
            lines = Files.readAllLines(tempFile.toPath());
        } catch (IOException e) {
            fail("Не удалось прочитать файл: " + e.getMessage());
        }

        assertNotNull(lines, "Содержимое файла не должно быть null");
        assertEquals(3, lines.size(), "Файл должен содержать заголовок и две задачи");
        assertEquals("id,type,name,status,description,epic", lines.get(0), "Заголовок файла некорректен");
        assertTrue(lines.get(1).contains("1,TASK,Task 1,NEW,Description 1,"), "Первая задача сохранена некорректно");
        assertTrue(lines.get(2).contains("2,TASK,Task 2,NEW,Description 2,"), "Вторая задача сохранена некорректно");
    }


    @Test
    public void shouldLoadMultipleTasks() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        manager.addTask(task1);
        manager.addTask(task2);

        Epic epic1 = new Epic("Epic 1", "Epic Description 1");
        manager.addEpics(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description 1", epic1.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description 2", epic1.getId());
        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);

        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        List<Task> loadedTasks = loadedManager.getAllTasks();
        assertEquals(2, loadedTasks.size(), "Должно быть загружено 2 задачи");
        assertTrue(loadedTasks.contains(task1), "Задача 1 не загружена");
        assertTrue(loadedTasks.contains(task2), "Задача 2 не загружена");

        List<Epic> loadedEpics = loadedManager.getAllEpics();
        assertEquals(1, loadedEpics.size(), "Должен быть загружен 1 эпик");
        assertTrue(loadedEpics.contains(epic1), "Эпик 1 не загружен");

        List<Subtask> loadedSubtasks = loadedManager.getAllSubtasks();
        assertEquals(2, loadedSubtasks.size(), "Должно быть загружено 2 подзадачи");
        assertTrue(loadedSubtasks.contains(subtask1), "Подзадача 1 не загружена");
        assertTrue(loadedSubtasks.contains(subtask2), "Подзадача 2 не загружена");

        Epic loadedEpic1 = loadedEpics.get(0);
        assertEquals(2, loadedEpic1.getSubtasksIds().size(), "Эпик должен иметь 2 подзадачи");
        assertTrue(loadedEpic1.getSubtasksIds().contains(subtask1.getId()), "Эпик не содержит Подзадачу 1");
        assertTrue(loadedEpic1.getSubtasksIds().contains(subtask2.getId()), "Эпик не содержит Подзадачу 2");
    }


    @Test
    public void shouldSaveAndLoadHistory() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        manager.addTask(task1);
        manager.addTask(task2);

        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getTask(task1.getId());

        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        List<Task> history = loadedManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать 2 уникальные задачи");
        assertEquals(task2, history.get(0), "Первая задача в истории должна быть Task 2");
        assertEquals(task1, history.get(1), "Вторая задача в истории должна быть Task 1");
    }


    @Test
    public void shouldSaveAndLoadEpicsWithSubtasks() {
        Epic epic1 = new Epic("Epic 1", "Epic Description 1");
        manager.addEpics(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description 1", epic1.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description 2", epic1.getId());
        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);

        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        List<Epic> loadedEpics = loadedManager.getAllEpics();
        assertEquals(1, loadedEpics.size(), "Должен быть загружен 1 эпик");
        Epic loadedEpic = loadedEpics.get(0);
        assertEquals(epic1.getId(), loadedEpic.getId(), "ID эпика должен совпадать");
        assertEquals(2, loadedEpic.getSubtasksIds().size(), "Эпик должен иметь 2 подзадачи");
        assertTrue(loadedEpic.getSubtasksIds().contains(subtask1.getId()), "Эпик не содержит Подзадачу 1");
        assertTrue(loadedEpic.getSubtasksIds().contains(subtask2.getId()), "Эпик не содержит Подзадачу 2");

        List<Subtask> loadedSubtasks = loadedManager.getAllSubtasks();
        assertEquals(2, loadedSubtasks.size(), "Должно быть загружено 2 подзадачи");
        assertTrue(loadedSubtasks.contains(subtask1), "Подзадача 1 не загружена");
        assertTrue(loadedSubtasks.contains(subtask2), "Подзадача 2 не загружена");
    }
}
