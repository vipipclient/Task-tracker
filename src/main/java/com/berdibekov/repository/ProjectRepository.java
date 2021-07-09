package com.berdibekov.repository;

import com.berdibekov.domain.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project,Long> {
}
