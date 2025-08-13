import java.io.Serializable;

/**
 * Task class represents a single task in the ToDo application
 * Follows OOP principles with encapsulation and proper data management
 */
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private String description;
    private boolean completed;
    private String priority;
    private String dueDate;
    private String category;
    
    // Constructor
    public Task(int id, String title, String description, String priority, String dueDate, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = false;
        this.priority = priority;
        this.dueDate = dueDate;
        this.category = category;
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public String getDueDate() {
        return dueDate;
    }
    
    public String getCategory() {
        return category;
    }
    
    // Setters
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    // Toggle completion status
    public void toggleCompleted() {
        this.completed = !this.completed;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%s) - Due: %s", 
                           completed ? "✓" : " ", 
                           title, 
                           priority, 
                           category, 
                           dueDate);
    }
    
    // Method to get formatted display text for GUI
    public String getDisplayText() {
        String status = completed ? "✓ " : "○ ";
        String priorityIcon = getPriorityIcon();
        return String.format("%s%s %s - %s", status, priorityIcon, title, category);
    }
    
    private String getPriorityIcon() {
        switch (priority.toLowerCase()) {
            case "high": return "🔴";
            case "medium": return "🟡";
            case "low": return "🟢";
            default: return "⚪";
        }
    }
}
