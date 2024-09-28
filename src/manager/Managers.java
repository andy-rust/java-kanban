package manager;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getFileBackedManager(File file) {
        if (file.exists() && file.length() > 0) {
            return FileBackedTaskManager.loadFromFile(file);
        } else {
            return new FileBackedTaskManager(file);
        }
    }
}
