package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

    // Добавление задачи, эпика и подзадачи
    @Override
    public Task addTask(Task task) {
        int taskId = generatedId();
        task.setId(taskId);
        tasks.put(taskId, task);
        return task;
    }

    @Override
    public Subtask addSubTask(Subtask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            return null;
        }

        subTask.setId(generatedId());
        subTasks.put(subTask.getId(), subTask);
        epic.addSubtaskId(subTask.getId());
        updateEpicStatusBasedOnSubtasks(epic.getId());
        return subTask;
    }

    @Override
    public Epic addEpics(Epic epic) {
        int epicId = generatedId();
        epic.setId(epicId);
        epics.put(epicId, epic);
        return epic;
    }

    // Получение списка всех задач, эпиков и подзадач
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subTasks.values());
    }

    // Получение конкретной задачи, подзадачи и эпика
    @Override
    public Task getTask(int taskId) {
        Task task = tasks.get(taskId);
        historyManager.add(task); // Регистрация задачи в истории просмотров
        return task;
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = epics.get(epicId);
        historyManager.add(epic); // Регистрация эпика в истории просмотров
        return epic;
    }

    @Override
    public Subtask getSubtask(int subTaskId) {
        Subtask subtask = subTasks.get(subTaskId);
        historyManager.add(subtask); // Регистрация подзадачи в истории просмотров
        return subtask;
    }

    // Удаление всех задач
    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasksIds().clear();
            epic.setStatus(TaskStatus.NEW);
        }
    }

    // Удаление задачи, эпика и подзадачи по ID
    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
        historyManager.remove(taskId); // Удаление из истории
    }

    @Override
    public void deleteEpic(int epicId) {
        Epic epic = epics.remove(epicId);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtasksIds()) {
                subTasks.remove(subtaskId);
                historyManager.remove(subtaskId); // Удаление подзадач из истории
            }
            historyManager.remove(epicId); // Удаление эпика из истории
        }
    }

    @Override
    public void deleteSubtasks(int subTaskId) {
        Subtask subTask = subTasks.get(subTaskId);
        if (subTask == null) {
            return;
        }
        subTasks.remove(subTaskId);
        historyManager.remove(subTaskId); // Удаление из истории
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            epic.getSubtasksIds().remove(Integer.valueOf(subTaskId));
            updateEpicStatusBasedOnSubtasks(epic.getId());
        }
    }

    // Обновление задачи
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        int epicId = epic.getId();
        if (epics.containsKey(epicId)) {
            epics.put(epicId, epic);
            updateEpicStatusBasedOnSubtasks(epicId);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int subTaskId = subtask.getId();
        if (subTasks.containsKey(subTaskId)) {
            subTasks.put(subTaskId, subtask);
            updateEpicStatusBasedOnSubtasks(subtask.getEpicId());
        }
    }

    // Генерация ID
    private int generatedId() {
        return ++id;
    }

    // Реализация метода getHistory из интерфейса TaskManager
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    // Метод по управлению статусом эпика
    private void updateEpicStatusBasedOnSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null && epic.getSubtasksIds().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        } else if (epic == null) {
            return;
        }

        boolean allDone = true;
        boolean atLeastOneInProgress = false;

        for (Integer subtaskId : epic.getSubtasksIds()) {
            TaskStatus subtaskStatus = subTasks.get(subtaskId).getStatus();
            if (subtaskStatus != TaskStatus.DONE) {
                allDone = false;
            }
            if (subtaskStatus == TaskStatus.IN_PROGRESS) {
                atLeastOneInProgress = true;
            }
        }

        if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else if (atLeastOneInProgress) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
    }
}