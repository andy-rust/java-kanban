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

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

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

    @Override
    public Task getTask(int taskId) {
        Task task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = epics.get(epicId);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int subTaskId) {
        Subtask subtask = subTasks.get(subTaskId);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
            for (int subtaskId : epic.getSubtasksIds()) {
                historyManager.remove(subtaskId);
            }
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Subtask subtask : subTasks.values()) {
            historyManager.remove(subtask.getId());
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasksIds().clear();
            epic.setStatus(TaskStatus.NEW);
        }
    }

    @Override
    public void deleteTask(int taskId) {
        Task task = tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpic(int epicId) {
        Epic epic = epics.remove(epicId);
        historyManager.remove(epicId);
        for (Integer subtaskId : epic.getSubtasksIds()) {
            subTasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
    }

    @Override
    public void deleteSubtasks(int subTaskId) {
        Subtask subTask = subTasks.remove(subTaskId);
        historyManager.remove(subTaskId);
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            epic.getSubtasksIds().remove(Integer.valueOf(subTaskId));
            updateEpicStatusBasedOnSubtasks(epic.getId());
        }
    }

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

    private int generatedId() {
        return ++id;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

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
