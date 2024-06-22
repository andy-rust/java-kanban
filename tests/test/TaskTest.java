package test;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task("Task 1", "Описание для Task 1");
        Task task2 = new Task("Task 2", "Описание для Task 2");
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1, task2, "Задачи с одинаковым идентификатором должны быть равны");
    }
    @Test
    void epicsWithSameIdShouldBeEqual() {
        Epic epic1 = new Epic("Epic 1", "Описание для Epic 1");
        Epic epic2 = new Epic("Epic 2", "Описание для Epic 2");
        epic1.setId(1);
        epic2.setId(1);
        assertEquals(epic1, epic2, "Эпики с одинаковым идентификатором должны быть равны");
    }

    @Test
    void subtasksWithSameIdShouldBeEqual() {
        Epic epic = new Epic("Epic для подзадач", "Описание Epic");
        epic.setId(1); // Устанавливаем ID для Epic, так как подзадачи связаны с Epic
        Subtask subtask1 = new Subtask("Subtask 1", "Описание для Subtask 1", 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Описание для Subtask 2", 1);
        subtask1.setId(2);
        subtask2.setId(2);
        assertEquals(subtask1, subtask2, "Подзадачи с одинаковым идентификатором должны быть равны");
    }
}