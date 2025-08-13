import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Simple ToDo GUI Application
 * Uses JFrame, JButton, JTextField for Add/Delete functionality
 */
public class TodoGUI extends JFrame {
    
    // GUI Components
    private JTextField taskField;
    private JButton addButton;
    private JButton deleteButton;
    private JList<String> taskList;
    private DefaultListModel<String> listModel;
    private JLabel countLabel;
    
    // Data storage
    private ArrayList<String> tasks;
    
    public TodoGUI() {
        tasks = new ArrayList<>();
        setupGUI();
    }
    
    private void setupGUI() {
        // Frame setup
        setTitle("ToDo App");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Top panel for input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Task"));
        
        taskField = new JTextField(20);
        addButton = new JButton("Add Task");
        
        inputPanel.add(new JLabel("Task: "));
        inputPanel.add(taskField);
        inputPanel.add(addButton);
        
        // Center panel for task list
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tasks"));
        
        // Bottom panel for delete and info
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        
        deleteButton = new JButton("Delete Selected");
        countLabel = new JLabel("Tasks: 0");
        
        bottomPanel.add(deleteButton);
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
        
        // Allow Enter key to add task
        taskField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addTask();
                }
            }
        });
        
        updateCount();
    }
    
    private void addTask() {
        String task = taskField.getText().trim();
        
        if (task.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a task!");
            return;
        }
        
        if (tasks.contains(task)) {
            JOptionPane.showMessageDialog(this, "Task already exists!");
            return;
        }
        
        tasks.add(task);
        listModel.addElement(task);
        taskField.setText("");
        taskField.requestFocus();
        updateCount();
        
        JOptionPane.showMessageDialog(this, "Task added successfully!");
    }
    
    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task to delete!");
            return;
        }
        
        String selectedTask = listModel.getElementAt(selectedIndex);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Delete task: " + selectedTask + "?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            tasks.remove(selectedTask);
            listModel.removeElementAt(selectedIndex);
            updateCount();
            JOptionPane.showMessageDialog(this, "Task deleted successfully!");
        }
    }
    
    private void updateCount() {
        countLabel.setText("Tasks: " + tasks.size());
        deleteButton.setEnabled(tasks.size() > 0);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TodoGUI().setVisible(true);
            }
        });
    }
}
