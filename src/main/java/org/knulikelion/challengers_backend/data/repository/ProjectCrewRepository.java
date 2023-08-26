package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.ProjectCrew;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectCrewRepository extends JpaRepository<ProjectCrew, Long> {
    List<ProjectCrew> findAllByProjectId(Long projectId);
}
