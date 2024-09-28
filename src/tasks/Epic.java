package tasks;

import manager.exceptions.ManagerSaveException;
import tasks.enums.TaskType;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasksIds;

    public Epic(String title, String description) {
        super(title, description);
        this.subtasksIds = new ArrayList<>();
    }

    public List<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void addSubtaskId(int id) {
        subtasksIds.add(id);
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static Epic fromString(String[] value) {
        Task task = Task.fromString(value);
        if (!(task instanceof Epic)) {
            throw new ManagerSaveException("Строка не соответствует типу Epic: " + value);
        }
        return (Epic) task;
    }
}
