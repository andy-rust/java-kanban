package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();
    private static final int HISTORY_CAPACITY = 10;

    @Override
    public void add(Task task) {
        if (task != null) {
            history.add(task);
            if (history.size() > HISTORY_CAPACITY) {
                history.remove(0);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}