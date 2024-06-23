package manager;

import tasks.Task;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList historyList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (task != null) {
            historyList.removeNode(task.getId());
            historyList.linkLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    @Override
    public void remove(int id) {
        historyList.removeNode(id);
    }
}
