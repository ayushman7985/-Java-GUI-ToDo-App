import java.util.*;
import java.io.*;

/**
 * TaskManager class handles all task operations and data persistence
 * Implements business logic for the ToDo application
 */
public class TaskManager {
    private ArrayList<Task> tasks;
    private int nextId;
    private final String DATA_FILE = "tasks.dat";
    
    public TaskManager() {
        tasks = new ArrayList<>();
        nextId = 1;
        loadTasks();
    }
    
    // Add a new task
    public void addTask(String title, String description, String priority, String dueDate, String category) {
        Task task = new Task(nextId++, title, description, priority, dueDate, category);
        tasks.add(task);
        saveTasks();
    }
    
    // Remove a task by ID
    public boolean removeTask(int id) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == id) {
                tasks.remove(i);
                saveTasks();
                return true;
            }
        }
        return false;
    }
    
    // Toggle task completion
    public boolean toggleTaskCompletion(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.toggleCompleted();
                saveTasks();
                return true;
            }
        }
        return false;
    }
    
    // Update a task
    public boolean updateTask(int id, String title, String description, String priority, String dueDate, String category) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.setTitle(title);
                task.setDescription(description);
                task.setPriority(priority);
                task.setDueDate(dueDate);
                task.setCategory(category);
                saveTasks();
                return true;
            }
        }
        return false;
    }
    
    // Get all tasks
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
    
    // Get tasks by completion status
    public ArrayList<Task> getTasksByStatus(boolean completed) {
        ArrayList<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isCompleted() == completed) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }
    
    // Get tasks by category
    public ArrayList<Task> getTasksByCategory(String category) {
        ArrayList<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getCategory().equalsIgnoreCase(category)) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }
    
    // Get tasks by priority
    public ArrayList<Task> getTasksByPriority(String priority) {
        ArrayList<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getPriority().equalsIgnoreCase(priority)) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }
    
    // Search tasks by title or description
    public ArrayList<Task> searchTasks(String searchTerm) {
        ArrayList<Task> results = new ArrayList<>();
        String lowerSearchTerm = searchTerm.toLowerCase();
        
        for (Task task : tasks) {
            if (task.getTitle().toLowerCase().contains(lowerSearchTerm) ||
                task.getDescription().toLowerCase().contains(lowerSearchTerm)) {
                results.add(task);
            }
        }
        return results;
    }
    
    // Get task by ID
    public Task getTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }
    
    // Get task statistics
    public Map<String, Integer> getTaskStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        int total = tasks.size();
        int completed = 0;
        int pending = 0;
        int high = 0, medium = 0, low = 0;
        
        for (Task task : tasks) {
            if (task.isCompleted()) {
                completed++;
            } else {
                pending++;
            }
            
            switch (task.getPriority().toLowerCase()) {
                case "high": high++; break;
                case "medium": medium++; break;
                case "low": low++; break;
            }
        }
        
        stats.put("total", total);
        stats.put("completed", completed);
        stats.put("pending", pending);
        stats.put("high", high);
        stats.put("medium", medium);
        stats.put("low", low);
        
        return stats;
    }
    
    // Save tasks to file
    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(tasks);
            oos.writeInt(nextId);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }
    
    // Load tasks from file
    @SuppressWarnings("unchecked")
    private void loadTasks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            tasks = (ArrayList<Task>) ois.readObject();
            nextId = ois.readInt();
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, start with empty list
            tasks = new ArrayList<>();
            nextId = 1;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            tasks = new ArrayList<>();
            nextId = 1;
        }
    }
    
    // Clear all tasks
    public void clearAllTasks() {
        tasks.clear();
        saveTasks();
    }
    
    // Get unique categories
    public Set<String> getCategories() {
        Set<String> categories = new HashSet<>();
        for (Task task : tasks) {
            categories.add(task.getCategory());
        }
        return categories;
    }
}
