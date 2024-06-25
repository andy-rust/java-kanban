package manager;

import tasks.Task;
import tasks.Epic;
import tasks.Subtask;
import java.util.List;

public interface TaskManager {
    Task addTask(Task task);

    Subtask addSubTask(Subtask subTask);

    Epic addEpics(Epic epic);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    Task getTask(int taskId);

    Epic getEpic(int epicId);

    Subtask getSubtask(int subTaskId);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    void deleteTask(int taskId);

    void deleteEpic(int epicId);

    void deleteSubtasks(int subtaskId);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    List<Task> getHistory();
}
