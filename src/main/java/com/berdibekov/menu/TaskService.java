package com.berdibekov.menu;

import com.berdibekov.domain.Project;
import com.berdibekov.domain.Task;
import com.berdibekov.domain.User;
import com.berdibekov.exception.ResourceNotFoundException;
import com.berdibekov.repository.ProjectRepository;
import com.berdibekov.repository.TaskRepository;
import com.berdibekov.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaskService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(ProjectRepository projectRepository,
                       TaskRepository taskRepository,
                       UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task addTask(String taskName, String description, long projectId) {
        Optional<Project> p = projectRepository.findById(projectId);
        if (p.isPresent()) {
            Project project = p.get();
            Task task = new Task();
            task.setName(taskName);
            task.setDescription(description);
            task.setProject(project);
            task = taskRepository.save(task);
            return task;
        }
        throw new ResourceNotFoundException("project with id : " + projectId + " not found.");
    }


    public void addUserToProject(long userID, long projectID) {
        Optional<Project> p = projectRepository.findById(projectID);
        if (p.isPresent()) {
            Project project = p.get();
            Optional<User> user = userRepository.findById(userID);
            if (user.isPresent()) {
                project.getProjectMembers().add(user.get());
            } else {
                throw new ResourceNotFoundException("User with id : " + userID + " not found.");
            }
        } else {
            throw new ResourceNotFoundException("Project with id : " + projectID + " not found.");
        }
    }

    public User addUser(String firstName, String lastName) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return userRepository.save(user);

    }

    public Iterable<Task> getTasksRelatedToUserAndProject(Long userId, Long projectId) {
        return taskRepository.findAllByUserAndProject(userId, projectId);
    }

    public void closeTaskById(long taskId) {
        Optional<Task> t = taskRepository.findById(taskId);
        if (t.isPresent()) {
            Task task = t.get();
            task.closeTaskAndAllSubTasks();
            taskRepository.save(task);
            task.getSubTasks().forEach(task1 -> closeTaskById(task1.getId()));
        } else {
            throw new ResourceNotFoundException("Task with id : " + taskId + " not found.");
        }
    }

    public void deleteTaskWithSubTask(long taskId) {
        Optional<Task> t = taskRepository.findById(taskId);
        if (t.isPresent()) {
            t.get().getSubTasks().forEach(task -> deleteTaskWithSubTask(task.getId()));
        } else {
            throw new ResourceNotFoundException("Task with id : " + taskId + " not found.");
        }
        taskRepository.deleteById(taskId);
    }
}
