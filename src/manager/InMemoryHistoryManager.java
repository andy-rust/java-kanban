package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> historyMap = new HashMap<>();
    private final CustomLinkedList historyList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            Node newNode = new Node(task);
            historyList.linkLast(newNode);
            historyMap.put(task.getId(), newNode);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    @Override
    public void remove(int id) {
        Node node = historyMap.remove(id);
        if (node != null) {
            historyList.removeNode(node);
        }
    }
}