import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

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

public class ToDoListApp {
    private ArrayList<Task> tasks;
    private Scanner scanner;

    public ToDoListApp() {
        tasks = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("\n=== To-Do List Manager ===");
        System.out.println("1. Add Task");
        System.out.println("2. Remove Task");
        System.out.println("3. View Tasks");
        System.out.println("4. Mark Task as Completed");
        System.out.println("5. Save Tasks to File");
        System.out.println("6. Load Tasks from File");
        System.out.println("7. Search Task");
        System.out.println("8. Exit");
        System.out.print("Choose an option: ");
    }

    public void addTask() {
        System.out.print("Enter the task description: ");
        String description = scanner.nextLine();
        System.out.print("Enter the task priority (1-5): ");
        int priority = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        tasks.add(new Task(description, priority));
        System.out.println("Task added: " + description);
    }

    public void removeTask() {
        System.out.println("Current Tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }

        System.out.print("Enter the task number to remove: ");
        int taskNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (taskNumber > 0 && taskNumber <= tasks.size()) {
            Task removedTask = tasks.remove(taskNumber - 1);
            System.out.println("Task removed: " + removedTask.getDescription());
        } else {
            System.out.println("Invalid task number.");
        }
    }

    public void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            System.out.println("Current Tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
    }

    public void markTaskCompleted() {
        System.out.println("Current Tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }

        System.out.print("Enter the task number to mark as completed: ");
        int taskNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (taskNumber > 0 && taskNumber <= tasks.size()) {
            tasks.get(taskNumber - 1).markCompleted();
            System.out.println("Task marked as completed: " + tasks.get(taskNumber - 1).getDescription());
        } else {
            System.out.println("Invalid task number.");
        }
    }

    public void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"))) {
            for (Task task : tasks) {
                writer.write(task.getDescription() + "|" + task.isCompleted() + "|" + task.getPriority());
                writer.newLine();
            }
            System.out.println("Tasks saved to tasks.txt");
        } catch (IOException e) {
            System.out.println("An error occurred while saving tasks: " + e.getMessage());
        }
    }

    public void loadTasksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"))) {
            String line;
            while ((line = reader.readLine()) != null ) {
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
            System.out.println("Tasks loaded from tasks.txt");
        } catch (IOException e) {
            System.out.println("An error occurred while loading tasks: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error in task data format: " + e.getMessage());
        }
    }

    public void searchTask() {
        System.out.print("Enter the keyword to search for: ");
        String keyword = scanner.nextLine();
        boolean found = false;
        System.out.println("Search Results:");
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(task);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No tasks found with the keyword: " + keyword);
        }
    }

    public void run() {
        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    removeTask();
                    break;
                case 3:
                    viewTasks();
                    break;
                case 4:
                    markTaskCompleted();
                    break;
                case 5:
                    saveTasksToFile();
                    break;
                case 6:
                    loadTasksFromFile();
                    break;
                case 7:
                    searchTask();
                    break;
                case 8:
                    System.out.println("Exiting the application.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        ToDoListApp app = new ToDoListApp();
        app.run();
    }
}