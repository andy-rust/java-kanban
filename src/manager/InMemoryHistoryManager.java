package manager;

import tasks.Task;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList historyList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (task != null) {
            historyList.remove(task.getId());
            CustomLinkedList.Node newNode = historyList.createNode(task);
            historyList.linkLast(newNode);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    @Override
    public void remove(int id) {
        historyList.remove(id);
    }
}
