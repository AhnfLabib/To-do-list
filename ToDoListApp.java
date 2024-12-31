import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

class Task {
    private String description;
    private boolean completed;
    private int priority;

    public Task(String description, int priority) {
        this.description = description;
        this.completed = false;
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        this.completed = true;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        String status = completed ? "[Completed]" : "[Pending]";
        return String.format("%s %s (Priority: %d)", status, description, priority);
    }
}

public class ToDoListApp extends JFrame {
    private ArrayList<Task> tasks;
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JTextField descriptionField;
    private JComboBox<Integer> priorityCombo;
    
    public ToDoListApp() {
        tasks = new ArrayList<>();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("To-Do List Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create the table
        String[] columns = {"Status", "Description", "Priority"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        taskTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(taskTable);
        
        // Create input panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        descriptionField = new JTextField(20);
        priorityCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        JButton addButton = new JButton("Add Task");
        
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Priority:"));
        inputPanel.add(priorityCombo);
        inputPanel.add(addButton);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton removeButton = new JButton("Remove Task");
        JButton completeButton = new JButton("Mark Completed");
        JButton saveButton = new JButton("Save Tasks");
        JButton loadButton = new JButton("Load Tasks");
        JButton searchButton = new JButton("Search");
        
        buttonPanel.add(removeButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(searchButton);
        
        // Add components to frame
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Add button listeners
        addButton.addActionListener(e -> addTask());
        removeButton.addActionListener(e -> removeTask());
        completeButton.addActionListener(e -> markTaskCompleted());
        saveButton.addActionListener(e -> saveTasksToFile());
        loadButton.addActionListener(e -> loadTasksFromFile());
        searchButton.addActionListener(e -> searchTask());
        
        // Set frame properties
        setSize(800, 500);
        setLocationRelativeTo(null);
    }

    private void addTask() {
        String description = descriptionField.getText().trim();
        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a task description");
            return;
        }
        
        int priority = (Integer) priorityCombo.getSelectedItem();
        Task task = new Task(description, priority);
        tasks.add(task);
        updateTaskTable();
        descriptionField.setText("");
    }

    private void removeTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            tasks.remove(selectedRow);
            updateTaskTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to remove");
        }
    }

    private void markTaskCompleted() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            tasks.get(selectedRow).markCompleted();
            updateTaskTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to mark as completed");
        }
    }

    private void updateTaskTable() {
        tableModel.setRowCount(0);
        for (Task task : tasks) {
            String status = task.isCompleted() ? "✓" : "○";
            tableModel.addRow(new Object[]{
                status,
                task.getDescription(),
                task.getPriority()
            });
        }
    }

    private void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"))) {
            for (Task task : tasks) {
                writer.write(task.getDescription() + "|" + task.isCompleted() + "|" + task.getPriority());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Tasks saved successfully");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving tasks: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTasksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"))) {
            tasks.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String description = parts[0];
                boolean completed = Boolean.parseBoolean(parts[1]);
                int priority = Integer.parseInt(parts[2]);
                Task task = new Task(description, priority);
                if (completed) {
                    task.markCompleted();
                }
                tasks.add(task);
            }
            updateTaskTable();
            JOptionPane.showMessageDialog(this, "Tasks loaded successfully");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading tasks: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchTask() {
        String keyword = JOptionPane.showInputDialog(this, "Enter search keyword:");
        if (keyword != null && !keyword.trim().isEmpty()) {
            ArrayList<Task> searchResults = new ArrayList<>();
            for (Task task : tasks) {
                if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                    searchResults.add(task);
                }
            }
            
            if (searchResults.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No tasks found with the keyword: " + keyword);
            } else {
                StringBuilder result = new StringBuilder("Search Results:\n\n");
                for (Task task : searchResults) {
                    result.append(task.toString()).append("\n");
                }
                JOptionPane.showMessageDialog(this, result.toString());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ToDoListApp().setVisible(true);
        });
    }
}