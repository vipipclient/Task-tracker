package com.berdibekov.repository;

import com.berdibekov.domain.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task,Long> {
    @Query(value="select * from tasks where USER_ID = ? AND PROJECT_ID = ?", nativeQuery = true)
    Iterable<Task> findAllByUserAndProject(Long userId, Long projectId);
}
