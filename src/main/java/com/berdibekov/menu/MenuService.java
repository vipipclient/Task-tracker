package com.berdibekov.menu;


import com.berdibekov.domain.Project;
import com.berdibekov.domain.Task;
import com.berdibekov.domain.User;
import com.berdibekov.repository.NodeRepository;
import com.berdibekov.repository.ProjectRepository;
import com.berdibekov.repository.TaskRepository;
import com.berdibekov.repository.UserRepository;
import com.berdibekov.writer.console_writer.ReportPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MenuService {

    private static boolean isExited = false;
    private List<MenuAction> menuActions = new ArrayList<>();
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskService taskService;
    private final ReportPrinter reportPrinter;
    private final NodeRepository nodeRepository;

    @Autowired
    public MenuService(UserRepository userRepository,
                       TaskRepository taskRepository,
                       ProjectRepository projectRepository, TaskService taskService, ReportPrinter reportPrinter, NodeRepository nodeRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.taskService = taskService;
        this.reportPrinter = reportPrinter;
        this.nodeRepository = nodeRepository;

        menuActions.add(new MenuAction("Test.", this::test));
        menuActions.add(new MenuAction("Show all tasks.", this::showAllTasks));
        menuActions.add(new MenuAction("Add new Task.", this::addNewTask));
        menuActions.add(new MenuAction("Assignee task to user.", this::assigneeTaskToUser));
        menuActions.add(new MenuAction("Show all task related to specified user and project.", this::showTasks));
        menuActions.add(new MenuAction("Delete task by id.", this::deleteTaskById));
        menuActions.add(new MenuAction("Close task by id.", this::closeTaskById));
        menuActions.add(new MenuAction("Show all users.", this::showAllUsers));
        menuActions.add(new MenuAction("Add new user.", this::addNewUser));
        menuActions.add(new MenuAction("Delete user by ID.", this::deleteUserById));
        menuActions.add(new MenuAction("Show all projects.", this::showAllProjects));
        menuActions.add(new MenuAction("Add a user to the project.", this::addUserToProject));


        menuActions.add(new MenuAction("Exit.", this::exit));
    }



    private void test() {
        nodeRepository.deleteById(0L);
    }

    private void closeTaskById() {
        try {
            System.out.println("Enter task ID to delete :");
            Scanner input = new Scanner(System.in);
            long taskId = input.nextLong();
            taskService.closeTaskById(taskId);
        } catch (InputMismatchException e) {
            System.err.println("Input should be positive integer number.");
        }
        System.out.println();
    }

    private void deleteTaskById() {
        try {
            System.out.println("Enter task ID to delete :");
            Scanner input = new Scanner(System.in);
            long taskId = input.nextLong();
            taskService.deleteTaskWithSubTask(taskId);

        } catch (InputMismatchException e) {
            System.err.println("Input should be positive integer number.");
        }
        System.out.println();
    }

    private void showAllTasks() {
        Iterable<Task> tasks = taskRepository.findAll();
        if (!tasks.iterator().hasNext()) {
            System.out.println("There are no tasks yet");
            return;
        }
        reportPrinter.writeHeader();
        reportPrinter.printTasks(tasks);
    }

    private void showTasks() {
        try {
            Long projectId;
            Long userId;

            Scanner input = new Scanner(System.in);
            System.out.println("Enter project Id or type 'any': ");
            String projectIdString = input.next();
            if (projectIdString.equals("any")) {
                projectId = null;
            } else {
                projectId = Long.parseLong(projectIdString);
            }
            System.out.println("Enter project Id or type 'any': ");
            String userIdString = input.next();
            if (projectIdString.equals("any")) {
                userId = null;
            } else {
                userId = Long.parseLong(userIdString);
            }

            Iterable<Task> tasks = taskService.getTasksRelatedToUserAndProject(userId, projectId);
            reportPrinter.writeHeader();
            reportPrinter.printTasks(tasks);
        } catch (InputMismatchException e) {
            System.err.println("Input should be positive integer number.");
        }
        System.out.println();
    }

    private void addNewTask() {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter task name : ");
            String taskName = input.nextLine();
            System.out.println("Enter task description: ");
            String description = input.nextLine();
            System.out.println("Enter project Id: ");
            long projectId = input.nextLong();
            Task task = taskService.addTask(taskName, description, projectId);
            System.out.println("new task with ID = " + task.getId() + " was added.");
        } catch (InputMismatchException e) {
            System.err.println("Input should be positive integer number.");
        }
        System.out.println();
    }

    public void lunchUserInterface() {
        while (!isExited()) {
            try {
                showMenu();
                Scanner input = new Scanner(System.in);
                int selection = input.nextInt();
                execute(selection);
            } catch (Exception e) {
                System.err.println("Input should be positive integer number.");
                e.printStackTrace();
            }
        }
    }

    void execute(int choice) {
        if (choice > menuActions.size()) {
            return;
        }
        System.out.println(menuActions.get(choice - 1).description);
        menuActions.get(choice - 1).action.run();
    }

    void showAllUsers() {
        Iterable<User> users = userRepository.findAll();
        users.forEach(System.out::println);
    }


    void addUserToProject() {
        try {
            Iterable<Project> projects = projectRepository.findAll();
            Scanner input = new Scanner(System.in);
            System.out.println("Enter user ID :");
            long userID = input.nextLong();
            projects.forEach(project -> System.out.println(project.getId() + " " + project.getName()));
            System.out.println("Enter Project ID :");
            long projectID = input.nextLong();

            taskService.addUserToProject(userID, projectID);
            System.out.println("User was assigned to project");
        } catch (InputMismatchException e) {
            System.err.println("Input should be positive integer number.");
        }
        System.out.println();
    }

    void assigneeTaskToUser() {
        try {
            Iterable<Project> projects = projectRepository.findAll();
            Scanner input = new Scanner(System.in);
            System.out.println("Enter user ID :");
            long userID = input.nextLong();
            projects.forEach(project -> System.out.println(project.getId() + " " + project.getName()));
            System.out.println("Enter task ID :");
            long taskId = input.nextLong();

            Optional<Task> p = taskRepository.findById(taskId);
            if (p.isPresent()) {
                Task task = p.get();
                Optional<User> user = userRepository.findById(userID);
                if (user.isPresent()) {
                    task.setUser(user.get());
                    taskRepository.save(task);
                } else {
                    System.out.println("User with id : " + userID + " not found.");
                }
                System.out.println("Task was assigned");
            } else {
                System.out.println("Task with id : " + taskId + " not found.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Input should be positive integer number.");
        }
        System.out.println();
    }

    public void deleteUserById() {
        try {
            System.out.println("Enter user ID to delete him/her :");
            Scanner input = new Scanner(System.in);
            long userID = input.nextLong();
            if (userRepository.existsById(userID)) {
                userRepository.deleteById(userID);
                System.out.println("User was deleted");
            } else {
                System.out.println("User with id : " + userID + " not found.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Input should be positive integer number.");
        }
        System.out.println();
    }

    void showMenu() {
        System.out.println("Enter : ");
        for (int i = 0; i < menuActions.size(); i++) {
            System.out.println(i + 1 + ". " + menuActions.get(i).description);
        }
    }


    void showAllProjects() {
        Iterable<Project> projects = projectRepository.findAll();
        projects.forEach(System.out::println);
    }

    void addNewUser() {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter user name : ");
            String firstName = input.nextLine();
            System.out.println("Enter user last name: ");
            String lastName = input.nextLine();
            System.out.println("Enter user project Id: ");

            User user = taskService.addUser(firstName, lastName);
            System.out.println("new user with ID = " + user.getId() + " was added.");
        } catch (Exception e) {
            System.err.println("Input should be positive integer number.");
        }
        System.out.println();
    }

    public void setMenuActions(List<MenuAction> menuActions) {
        this.menuActions = menuActions;
    }

    public boolean isExited() {
        return isExited;
    }

    private void exit() {
        isExited = true;
    }
}
