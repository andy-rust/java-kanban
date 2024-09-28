package tasks;

import manager.exceptions.ManagerSaveException;
import tasks.enums.TaskStatus;
import tasks.enums.TaskType;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return super.toString() + epicId;
    }

    public static Subtask fromString(String value) {
        String[] fields = value.split(",");
        if (fields.length < 6) {
            throw new ManagerSaveException("Некорректный формат строки подзадачи: " + value);
        }
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String title = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];
        int epicId = Integer.parseInt(fields[5]);

        if (type != TaskType.SUBTASK) {
            throw new ManagerSaveException("Строка не соответствует типу Subtask: " + value);
        }

        Subtask subtask = new Subtask(title, description, epicId);
        subtask.setId(id);
        subtask.setStatus(status);
        return subtask;
    }
}
