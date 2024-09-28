package manager;

import manager.exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public static void main(String[] args) {
        File tempFile = new File("tasks.csv");

        if (tempFile.exists()) {
            tempFile.delete();
        }

        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        Task task1 = new Task("Task 1", "Description for Task 1");
        Task task2 = new Task("Task 2", "Description for Task 2");
        manager.addTask(task1);
        manager.addTask(task2);

        Epic epic1 = new Epic("Epic 1", "Description for Epic 1");
        Epic epic2 = new Epic("Epic 2", "Description for Epic 2");
        manager.addEpics(epic1);
        manager.addEpics(epic2);

        Subtask subtask1 = new Subtask("Subtask 1", "Description for Subtask 1", epic1.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description for Subtask 2", epic1.getId());
        Subtask subtask3 = new Subtask("Subtask 3", "Description for Subtask 3", epic2.getId());
        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);
        manager.addSubTask(subtask3);

        manager.getTask(task1.getId());
        manager.getEpic(epic1.getId());
        manager.getSubtask(subtask1.getId());

        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        boolean allTasksLoaded = loadedManager.getAllTasks().size() == manager.getAllTasks().size();
        boolean allEpicsLoaded = loadedManager.getAllEpics().size() == manager.getAllEpics().size();
        boolean allSubtasksLoaded = loadedManager.getAllSubtasks().size() == manager.getAllSubtasks().size();
        boolean historyLoaded = loadedManager.getHistory().equals(manager.getHistory());

        System.out.println("=== Проверка загрузки из файла ===");
        System.out.println("Задачи загружены корректно: " + allTasksLoaded);
        System.out.println("Эпики загружены корректно: " + allEpicsLoaded);
        System.out.println("Подзадачи загружены корректно: " + allSubtasksLoaded);
        System.out.println("История загружена корректно: " + historyLoaded);

        System.out.println("\n=== Все задачи из загруженного менеджера ===");
        for (Task task : loadedManager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("\n=== Все эпики из загруженного менеджера ===");
        for (Epic epic : loadedManager.getAllEpics()) {
            System.out.println(epic);
        }

        System.out.println("\n=== Все подзадачи из загруженного менеджера ===");
        for (Subtask subtask : loadedManager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("\n=== История из загруженного менеджера ===");
        for (Task historyTask : loadedManager.getHistory()) {
            System.out.println(historyTask);
        }
    }

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();

            for (Task task : getAllTasks()) {
                writer.write(task.toString());
                writer.newLine();
            }

            for (Epic epic : getAllEpics()) {
                writer.write(epic.toString());
                writer.newLine();
            }

            for (Subtask subtask : getAllSubtasks()) {
                writer.write(subtask.toString());
                writer.newLine();
            }

            List<Task> history = getHistory();
            if (!history.isEmpty()) {
                writer.newLine();
                StringBuilder historyLine = new StringBuilder();
                for (Task task : history) {
                    historyLine.append(task.getId()).append(",");
                }
                historyLine.deleteCharAt(historyLine.length() - 1);
                writer.write(historyLine.toString());
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении менеджера в файл", e);
        }
    }


    @Override
    public Task addTask(Task task) {
        Task addedTask = super.addTask(task);
        save();
        return addedTask;
    }

    @Override
    public Epic addEpics(Epic epic) {
        Epic addedEpic = super.addEpics(epic);
        save();
        return addedEpic;
    }

    @Override
    public Subtask addSubTask(Subtask subTask) {
        Subtask addedSubtask = super.addSubTask(subTask);
        save();
        return addedSubtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public void deleteSubtasks(int subtaskId) {
        super.deleteSubtasks(subtaskId);
        save();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null || !line.equals("id,type,name,status,description,epic")) {
                throw new ManagerSaveException("Некорректный формат файла");
            }

            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                Task task = parseTask(line);
                switch (task.getType()) {
                    case TASK:
                        manager.tasks.put(task.getId(), task);
                        break;
                    case EPIC:
                        manager.epics.put(task.getId(), (Epic) task);
                        break;
                    case SUBTASK:
                        Subtask subtask = (Subtask) task;
                        manager.subTasks.put(subtask.getId(), subtask);
                        Epic epic = manager.epics.get(subtask.getEpicId());
                        if (epic != null) {
                            epic.addSubtaskId(subtask.getId());
                        } else {
                            throw new ManagerSaveException("Эпик с id " + subtask.getEpicId() +
                                    " не найден для подзадачи " + subtask.getId());
                        }
                        break;
                }

                if (task.getId() > manager.id) {
                    manager.id = task.getId();
                }
            }

            if ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] historyIds = line.split(",");
                for (String idStr : historyIds) {
                    if (!idStr.isEmpty()) {
                        int id = Integer.parseInt(idStr);
                        Task task = manager.tasks.get(id);
                        if (task == null) {
                            task = manager.epics.get(id);
                        }
                        if (task == null) {
                            task = manager.subTasks.get(id);
                        }
                        if (task != null) {
                            manager.historyManager.add(task);
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке менеджера из файла", e);
        }
        return manager;
    }

    private static Task parseTask(String line) {
        String[] fields = line.split(",", -1);
        if (fields.length != 6) {
            throw new ManagerSaveException("Некорректный формат строки задачи: " + line);
        }
        return Task.fromString(fields);
    }
}
