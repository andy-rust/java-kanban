import manager.InMemoryTaskManager;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class Main {
        public static void main(String[] args) {
                TaskManager manager = new InMemoryTaskManager();

                // Создание и добавление задач
                Task task1 = new Task("Задача 1", "Описание для задачи 1");
                Task task2 = new Task("Задача 2", "Описание для задачи 2");
                manager.addTask(task1);
                manager.addTask(task2);

                // Создание и добавление эпиков
                Epic epic1 = new Epic("Эпик 1", "Описание для эпика 1");
                Epic epic2 = new Epic("Эпик 2", "Описание для эпика 2");
                manager.addEpics(epic1);
                manager.addEpics(epic2);

                // Создание и добавление подзадач
                Subtask subtask1 = new Subtask("Подзадача 1", "Описание для подзадачи 1", epic1.getId());
                Subtask subtask2 = new Subtask("Подзадача 2", "Описание для подзадачи 2", epic1.getId());
                Subtask subtask3 = new Subtask("Подзадача 3", "Описание для подзадачи 3", epic2.getId());
                manager.addSubTask(subtask1);
                manager.addSubTask(subtask2);
                manager.addSubTask(subtask3);

                // Распечатка списков задач, эпиков и подзадач
                System.out.println("Все задачи: " + manager.getAllTasks());
                System.out.println("Все эпики: " + manager.getAllEpics());
                System.out.println("Все подзадачи: " + manager.getAllSubtasks());

                // Изменение статусов и проверка обновлений
                subtask1.setStatus(TaskStatus.DONE);
                subtask2.setStatus(TaskStatus.DONE);
                subtask3.setStatus(TaskStatus.IN_PROGRESS);
                manager.updateSubtask(subtask1);
                manager.updateSubtask(subtask2);
                manager.updateSubtask(subtask3);

                // Повторная распечатка для проверки обновленных статусов
                System.out.println("\nОбновление статусов:");
                System.out.println("Все задачи: " + manager.getAllTasks());
                System.out.println("Все эпики: " + manager.getAllEpics());
                System.out.println("Все подзадачи: " + manager.getAllSubtasks());

                // Удаление задачи и эпика
                manager.deleteTask(task1.getId());
                manager.deleteEpic(epic1.getId());

                // Повторная распечатка после удаления
                System.out.println("\nПосле удаления:");
                System.out.println("Все задачи: " + manager.getAllTasks());
                System.out.println("Все эпики: " + manager.getAllEpics());
                System.out.println();
        }
}