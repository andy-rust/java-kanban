import java.io.File;
import java.util.List;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.TaskStatus;

public class Main {

        public static void main(String[] args) {

                File file = new File("tasks.csv");
                TaskManager manager = Managers.getFileBackedManager(file);

                Task task1 = new Task("Задача 1", "Описание для задачи 1");
                Task task2 = new Task("Задача 2", "Описание для задачи 2");
                manager.addTask(task1);
                manager.addTask(task2);

                Epic epic1 = new Epic("Эпик 1", "Описание для эпика 1");
                Epic epic2 = new Epic("Эпик 2", "Описание для эпика 2");
                manager.addEpics(epic1);
                manager.addEpics(epic2);

                Subtask subtask1 = new Subtask("Подзадача 1", "Описание для подзадачи 1", epic1.getId());
                Subtask subtask2 = new Subtask("Подзадача 2", "Описание для подзадачи 2", epic1.getId());
                Subtask subtask3 = new Subtask("Подзадача 3", "Описание для подзадачи 3", epic2.getId());
                manager.addSubTask(subtask1);
                manager.addSubTask(subtask2);
                manager.addSubTask(subtask3);

                System.out.println("Все задачи: " + manager.getAllTasks());
                System.out.println("Все эпики: " + manager.getAllEpics());
                System.out.println("Все подзадачи: " + manager.getAllSubtasks());

                subtask1.setStatus(TaskStatus.DONE);
                subtask2.setStatus(TaskStatus.DONE);
                subtask3.setStatus(TaskStatus.IN_PROGRESS);
                manager.updateSubtask(subtask1);
                manager.updateSubtask(subtask2);
                manager.updateSubtask(subtask3);

                System.out.println("\nОбновление статусов:");
                System.out.println("Все задачи: " + manager.getAllTasks());
                System.out.println("Все эпики: " + manager.getAllEpics());
                System.out.println("Все подзадачи: " + manager.getAllSubtasks());

                manager.getTask(task1.getId());
                manager.getEpic(epic2.getId());
                manager.getTask(task2.getId());
                manager.getSubtask(subtask1.getId());
                manager.getEpic(epic1.getId());
                manager.getSubtask(subtask2.getId());
                manager.getSubtask(subtask3.getId());
                manager.getTask(task1.getId());

                printHistory(manager.getHistory());

                manager.deleteTask(task1.getId());

                printHistory(manager.getHistory());

                manager.deleteEpic(epic1.getId());

                printHistory(manager.getHistory());

                System.out.println("\nПосле удаления:");
                System.out.println("Все задачи: " + manager.getAllTasks());
                System.out.println("Все эпики: " + manager.getAllEpics());
                System.out.println("Все подзадачи: " + manager.getAllSubtasks());
        }

        private static void printHistory(List<Task> history) {
                System.out.println("История:");
                for (Task task : history) {
                        System.out.println(task);
                }
                System.out.println();
        }
}