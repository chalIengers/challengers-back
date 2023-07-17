package org.knulikelion.challengers_backend.data.dao;

import org.knulikelion.challengers_backend.data.entity.ProjectCrew;

import java.util.Optional;


public interface ProjectCrewDAO {

    Optional<ProjectCrew> selectById(Long id);
    ProjectCrew createCrew(ProjectCrew projectCrew);
    ProjectCrew updateCrew(ProjectCrew projectCrew);
    void removeCrew(Long id);
}

