import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Enhanced ToDo GUI Application
 * Uses JFrame, JButton, JTextField for Add/Delete functionality
 * Integrates with TaskManager for robust task management
 */
public class EnhancedTodoGUI extends JFrame {
    
    // GUI Components
    private JTextField taskTitleField;
    private JTextField taskDescriptionField;
    private JComboBox<String> priorityCombo;
    private JTextField dueDateField;
    private JTextField categoryField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton toggleButton;
    private JList<String> taskList;
    private DefaultListModel<String> listModel;
    private JLabel countLabel;
    
    // Task management
    private TaskManager taskManager;
    
    public EnhancedTodoGUI() {
        taskManager = new TaskManager();
        setupGUI();
        refreshTaskList();
    }
    
    private void setupGUI() {
        // Frame setup
        setTitle("Enhanced ToDo App");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Top panel for input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Task"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Task title
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        taskTitleField = new JTextField(20);
        inputPanel.add(taskTitleField, gbc);
        
        // Task description
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        taskDescriptionField = new JTextField(20);
        inputPanel.add(taskDescriptionField, gbc);
        
        // Priority
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        inputPanel.add(new JLabel("Priority:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        priorityCombo = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        priorityCombo.setSelectedIndex(1); // Default to Medium
        inputPanel.add(priorityCombo, gbc);
        
        // Due date
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        inputPanel.add(new JLabel("Due Date:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        dueDateField = new JTextField("YYYY-MM-DD");
        inputPanel.add(dueDateField, gbc);
        
        // Category
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        inputPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        categoryField = new JTextField("General");
        inputPanel.add(categoryField, gbc);
        
        // Add button
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        addButton = new JButton("Add Task");
        addButton.setPreferredSize(new Dimension(120, 30));
        inputPanel.add(addButton, gbc);
        
        // Center panel for task list
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tasks"));
        scrollPane.setPreferredSize(new Dimension(580, 300));
        
        // Bottom panel for actions and info
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        
        deleteButton = new JButton("Delete Selected");
        toggleButton = new JButton("Toggle Complete");
        countLabel = new JLabel("Tasks: 0");
        
        bottomPanel.add(deleteButton);
        bottomPanel.add(toggleButton);
        bottomPanel.add(Box.createHorizontalStrut(20));
        bottomPanel.add(countLabel);
        
        // Add panels to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Event listeners
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteTask();
            }
        });
        
        toggleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleTaskCompletion();
            }
        });
        
        // Allow Enter key to add task
        taskTitleField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addTask();
                }
            }
        });
        
        // Clear placeholder text on focus
        dueDateField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (dueDateField.getText().equals("YYYY-MM-DD")) {
                    dueDateField.setText("");
                }
            }
            public void focusLost(FocusEvent e) {
                if (dueDateField.getText().trim().isEmpty()) {
                    dueDateField.setText("YYYY-MM-DD");
                }
            }
        });
        
        updateCount();
    }
    
    private void addTask() {
        String title = taskTitleField.getText().trim();
        String description = taskDescriptionField.getText().trim();
        String priority = (String) priorityCombo.getSelectedItem();
        String dueDate = dueDateField.getText().trim();
        String category = categoryField.getText().trim();
        
        // Validation
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a task title!");
            taskTitleField.requestFocus();
            return;
        }
        
        if (description.isEmpty()) {
            description = "No description";
        }
        
        if (dueDate.equals("YYYY-MM-DD") || dueDate.isEmpty()) {
            dueDate = "No due date";
        }
        
        if (category.isEmpty()) {
            category = "General";
        }
        
        // Add task using TaskManager
        taskManager.addTask(title, description, priority, dueDate, category);
        
        // Clear fields
        taskTitleField.setText("");
        taskDescriptionField.setText("");
        priorityCombo.setSelectedIndex(1);
        dueDateField.setText("YYYY-MM-DD");
        categoryField.setText("General");
        
        // Refresh display
        refreshTaskList();
        taskTitleField.requestFocus();
        
        JOptionPane.showMessageDialog(this, "Task added successfully!");
    }
    
    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task to delete!");
            return;
        }
        
        // Get all tasks and find the selected one
        ArrayList<Task> tasks = taskManager.getAllTasks();
        if (selectedIndex >= tasks.size()) {
            JOptionPane.showMessageDialog(this, "Invalid task selection!");
            return;
        }
        
        Task selectedTask = tasks.get(selectedIndex);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Delete task: " + selectedTask.getTitle() + "?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            taskManager.removeTask(selectedTask.getId());
            refreshTaskList();
            JOptionPane.showMessageDialog(this, "Task deleted successfully!");
        }
    }
    
    private void toggleTaskCompletion() {
        int selectedIndex = taskList.getSelectedIndex();
        
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task to toggle!");
            return;
        }
        
        // Get all tasks and find the selected one
        ArrayList<Task> tasks = taskManager.getAllTasks();
        if (selectedIndex >= tasks.size()) {
            JOptionPane.showMessageDialog(this, "Invalid task selection!");
            return;
        }
        
        Task selectedTask = tasks.get(selectedIndex);
        taskManager.toggleTaskCompletion(selectedTask.getId());
        refreshTaskList();
        
        String status = selectedTask.isCompleted() ? "completed" : "pending";
        JOptionPane.showMessageDialog(this, "Task marked as " + status + "!");
    }
    
    private void refreshTaskList() {
        listModel.clear();
        ArrayList<Task> tasks = taskManager.getAllTasks();
        
        for (Task task : tasks) {
            listModel.addElement(task.getDisplayText());
        }
        
        updateCount();
    }
    
    private void updateCount() {
        ArrayList<Task> tasks = taskManager.getAllTasks();
        int total = tasks.size();
        int completed = 0;
        
        for (Task task : tasks) {
            if (task.isCompleted()) {
                completed++;
            }
        }
        
        countLabel.setText(String.format("Tasks: %d (Completed: %d, Pending: %d)", 
                                        total, completed, total - completed));
        
        deleteButton.setEnabled(total > 0);
        toggleButton.setEnabled(total > 0);
    }
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            // Use default look and feel
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EnhancedTodoGUI().setVisible(true);
            }
        });
    }
}
