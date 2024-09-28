package tasks;

import manager.exceptions.ManagerSaveException;
import tasks.enums.TaskStatus;
import tasks.enums.TaskType;

import java.util.Objects;

public class Task {
    private String title;
    private String description;
    private int id;
    private TaskStatus status;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        if (this.id == 0) {
            this.id = id;
        }
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s", id, getType(), title, status, description, "");
    }

    public static Task fromString(String[] fields) {
        if (fields.length != 6) {
            throw new ManagerSaveException("Некорректное количество полей: " + String.join(",", fields));
        }

        try {
            int id = Integer.parseInt(fields[0]);
            TaskType type = TaskType.valueOf(fields[1]);
            String title = fields[2];
            TaskStatus status = TaskStatus.valueOf(fields[3]);
            String description = fields[4];
            String epicIdStr = fields[5];

            Task task;
            switch (type) {
                case TASK:
                    task = new Task(title, description);
                    break;
                case EPIC:
                    task = new Epic(title, description);
                    break;
                case SUBTASK:
                    if (epicIdStr.isEmpty()) {
                        throw new ManagerSaveException("Подзадача должна иметь связанный эпик: " + String.join(",", fields));
                    }
                    int epicId = Integer.parseInt(epicIdStr);
                    task = new Subtask(title, description, epicId);
                    break;
                default:
                    throw new ManagerSaveException("Неизвестный тип задачи: " + type);
            }
            task.setId(id);
            task.setStatus(status);
            return task;
        } catch (Exception e) {
            throw new ManagerSaveException("Ошибка при разборе строки задачи: " + String.join(",", fields), e);
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
