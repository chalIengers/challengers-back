package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dao.*;
import org.knulikelion.challengers_backend.data.dto.request.ProjectCrewRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ProjectLinkRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ProjectRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ProjectTechStackRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.*;
import org.knulikelion.challengers_backend.data.entity.*;
import org.knulikelion.challengers_backend.data.enums.ProjectStatus;
import org.knulikelion.challengers_backend.data.repository.MonthlyViewsRepository;
import org.knulikelion.challengers_backend.data.repository.ProjectRepository;
import org.knulikelion.challengers_backend.data.repository.ProjectTechStackRepository;
import org.knulikelion.challengers_backend.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
    private final ProjectDAO projectDAO;
    private final ProjectCrewDAO projectCrewDAO;
    private final ProjectLinkDAO projectLinkDAO;
    private final ProjectTechStackDAO projectTechStackDAO;
    private final JwtTokenProvider jwtTokenProvider;
    private final ClubDAO clubDAO;
    private final UserDAO userDAO;
    private final MonthlyViewsRepository monthlyViewsRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTechStackRepository projectTechStackRepository;

    @Autowired
    public ProjectServiceImpl(
            ProjectDAO projectDAO,
            ProjectCrewDAO projectCrewDAO,
            ProjectLinkDAO projectLinkDAO,
            ProjectTechStackDAO projectTechStackDAO,
            JwtTokenProvider jwtTokenProvider,
            ClubDAO clubDAO,
            UserDAO userDAO,
            MonthlyViewsRepository monthlyViewsRepository, ProjectRepository projectRepository, ProjectTechStackRepository projectTechStackRepository) {
        this.projectDAO = projectDAO;
        this.projectCrewDAO = projectCrewDAO;
        this.projectLinkDAO = projectLinkDAO;
        this.projectTechStackDAO = projectTechStackDAO;
        this.jwtTokenProvider = jwtTokenProvider;
        this.clubDAO = clubDAO;
        this.userDAO = userDAO;
        this.monthlyViewsRepository = monthlyViewsRepository;
        this.projectRepository = projectRepository;
        this.projectTechStackRepository = projectTechStackRepository;
    }

    @Override
    public Object getProjectById(Long id) {
        if (projectDAO.selectProjectById(id).isEmpty()) {
            BaseResponseDto baseResponseDto = new BaseResponseDto();

            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("프로젝트가 존재하지 않음");
            logger.info("[Log] 프로젝트가 존재하지 않음, ID:" + id);

            return baseResponseDto;
        } else {
//            대상 프로젝트 선택
            Project selectedProject = projectDAO.selectProjectById(id).get();
            ProjectResponseDto projectResponseDto = new ProjectResponseDto();
            projectResponseDto.setId(selectedProject.getId());
            projectResponseDto.setProjectName(selectedProject.getProjectName());
            projectResponseDto.setProjectDescription(selectedProject.getProjectDescription());
            projectResponseDto.setProjectDetail(selectedProject.getProjectDetail());
            projectResponseDto.setImageUrl(selectedProject.getImageUrl());

            if(selectedProject.getStatus() == ProjectStatus.ACTIVE) {
                projectResponseDto.setProjectStatus("활성");
            } else if(selectedProject.getStatus() == ProjectStatus.INACTIVE) {
                projectResponseDto.setProjectStatus("종료");
            } else if(selectedProject.getStatus() == ProjectStatus.MAINTENCE) {
                projectResponseDto.setProjectStatus("점검");
            } else {
                projectResponseDto.setProjectStatus("점검");
            }

            projectResponseDto.setProjectPeriod(selectedProject.getProjectPeriod());
            projectResponseDto.setProjectCategory(selectedProject.getProjectCategory());
            projectResponseDto.setCreatedAt(String.valueOf(selectedProject.getCreatedAt()));
            projectResponseDto.setUpdatedAt(String.valueOf(selectedProject.getUpdatedAt()));
            projectResponseDto.setUploadedUserId(selectedProject.getUser().getId().intValue());
            if (selectedProject.getClub() != null) {
                projectResponseDto.setBelongedClubId(selectedProject.getClub().getId().intValue());
                projectResponseDto.setBelongedClubName(selectedProject.getClub().getClubName());
            } else {
                logger.info("[Log] 클럽이 존재하지 않음");
                projectResponseDto.setBelongedClubId(null);
                projectResponseDto.setBelongedClubName(null);
            }

//            프로젝트에 포함된 기술 스텍 불러옴
            List<ProjectTechStackResponseDto> techStacks = projectTechStackDAO.getTechStack(projectResponseDto.getId());
            projectResponseDto.setProjectTechStack(techStacks);

//            프로젝트에 포함된 링크 불러옴
            List<ProjectLinkResponseDto> projectLinks = projectLinkDAO.getLink(projectResponseDto.getId());
            projectResponseDto.setProjectLink(projectLinks);

//            프로젝트에 포함된 크루 불러옴
            Map<String,List<ProjectCrewResponseDto>> projectCrews = projectCrewDAO.getCrews(projectResponseDto.getId());
            projectResponseDto.setProjectCrew(projectCrews);
            /*List<ProjectCrewResponseDto> projectCrews = projectCrewDAO.getCrew(projectResponseDto.getId());
            projectResponseDto.setProjectCrew(projectCrews);*/

//            프로젝트 조회수 증가.
            projectDAO.incrementViewCount(id);

//            결과 반환
            return projectResponseDto;
        }
    }

    @Override
    public Page<AllProjectResponseDto> getAllProject(int page, int size, String categories, String sort, List<String> techStacks) {
        String category = getCategory(categories);
        String sortValue = sort.toUpperCase();
        Page<Project> projects;

        if (sortValue.equals("POPULAR")) {
            Page<MonthlyViews> monthlyViews;
            if (!category.equals("ALL") || !techStacks.isEmpty()) {
                Specification<MonthlyViews> spec = (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (!category.equals("ALL")) {
                        predicates.add(cb.equal(root.get("project").get("projectCategory"), category));
                    }

                    Join<Project, ProjectTechStack> techJoin = root.join("project").join("techStacks");
                    for (String tech : techStacks) {
                        predicates.add(cb.equal(techJoin.get("techStackName"), tech));
                    }

                    return cb.and(predicates.toArray(new Predicate[0]));
                };
                monthlyViews = monthlyViewsRepository.findAll(spec,
                        PageRequest.of(page, size,
                                Sort.by(Sort.Direction.DESC,"viewCount")));
            } else {
                monthlyViews = monthlyViewsRepository.findAll(
                        PageRequest.of(page,size,
                                Sort.by(Sort.Direction.DESC,"viewCount")));
            }

            return monthlyViews.getContent().stream()
                    .map(monthlyView -> mapToAllProjectResponseDto(monthlyView.getProject()))
                    .distinct()
                    .collect(Collectors.collectingAndThen(Collectors.toList(),
                            list -> new PageImpl<>(list, monthlyViews.getPageable(), list.size())));

        } else {

            if (!category.equals("ALL") || !techStacks.isEmpty()) {
                Specification<Project> spec = (root, query, cb) ->{
                    List<Predicate> predicates = new ArrayList<>();

                    if (!category.equals("ALL")) {
                        predicates.add(cb.equal(root.get("projectCategory"), category));
                    }

                    Predicate[] techPredicates = new Predicate[techStacks.size()];
                    int i = 0;

                    for(String tech : techStacks){
                        Join<Project , ProjectTechStack > techJoin= root.join( "techStacks" );
                        techPredicates[i++] = cb.equal(techJoin.get( "techStackName" ), tech);
                    }

                    predicates.add(cb.or(techPredicates));

                    return cb.and(predicates.toArray(new Predicate[0]));
                };

                projects= projectRepository.findAll(spec,
                        PageRequest.of(page,size,
                                Sort.by(Sort.Direction.DESC,"createdAt")));

            } else {

                projects= projectRepository.findAll(
                        PageRequest.of(page,size,
                                Sort.by(Sort.Direction.DESC,"createdAt")));

            }

            return projects.getContent().stream()
                    .map(this::mapToAllProjectResponseDto)
                    .distinct()
                    .collect(Collectors.collectingAndThen(Collectors.toList(),
                            list -> new PageImpl<>(list, projects.getPageable(), list.size())));
        }
    }

    private AllProjectResponseDto mapToAllProjectResponseDto(Project temp) {
        AllProjectResponseDto allProjectResponseDto = new AllProjectResponseDto();

        allProjectResponseDto.setId(temp.getId());
        allProjectResponseDto.setProjectName(temp.getProjectName());
        allProjectResponseDto.setProjectDescription(temp.getProjectDescription());
        allProjectResponseDto.setImageUrl(temp.getImageUrl());
        allProjectResponseDto.setProjectCategory(temp.getProjectCategory());

        if (temp.getClub() != null) {
            allProjectResponseDto.setBelongedClubName(temp.getClub().getClubName());
        } else {
            logger.info("[Log] 클럽이 존재하지 않음");
            allProjectResponseDto.setBelongedClubName(null);
        }

        return allProjectResponseDto;
    }

    private String getCategory(String categories) {
        if (categories == null || categories.trim().isEmpty()) {
            return "ALL";
        }

        switch (categories.toUpperCase()) {
            case "WEB":
                return "웹 서비스";
            case "APP":
                return "앱 서비스";
            case "ETC":
                return "기타 서비스";
            case "ALL":
                return "ALL";
            default:
                throw new IllegalArgumentException("잘못된 분류 값입니다: "+ categories);
        }
    }

    @Override
    public BaseResponseDto removeProject(Long id) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        Optional<Project> projectList = projectRepository.findById(id);

        if (projectList.isEmpty()) {
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("프로젝트가 존재하지 않음");
        } else {
            Project project = projectList.get();

//            프로젝트에 포함된 기술 스텍 모두 삭제
            logger.info("[Log] 포함된 기술 스텍 전체 삭제");
            projectTechStackDAO.removeTechStack(id);

//            프로젝트에 포함된 링크 모두 삭제
            logger.info("[Log] 포함된 링크 전체 삭제");
            projectLinkDAO.removeLink(id);

//            프로젝트에 포함된 크루 모두 삭제
            logger.info("[Log] 포함된 크루 전체 삭제");
            projectCrewDAO.removeCrew(id);

            logger.info("[Log] MonthlyViews 삭제");
            if (monthlyViewsRepository.findByProject(project) != null) {
                MonthlyViews monthlyViews = monthlyViewsRepository.findByProject(project);
                monthlyViewsRepository.delete(monthlyViews);
            }

            if(project.getUser()!=null) {
                logger.info("[Log] 프로젝트 생성자 삭제");
                project.setUser(null);
            }

            if(project.getClub()!=null) {
                logger.info("[Log] 소속 클럽 삭제");
                project.setClub(null);
            }

//            프로젝트 삭제
            logger.info("[Log] 포함된 프로젝트 삭제");
            projectDAO.removeProject(id);

//            결과 값 반환
            logger.info("[Log] 프로젝트 삭제 완료");
            baseResponseDto.setSuccess(true);
            baseResponseDto.setMsg("프로젝트 삭제가 완료되었습니다.");
        }

        return baseResponseDto;
    }

    @Override
    public BaseResponseDto createProject(ProjectRequestDto projectRequestDto, String token) {
        Project project = new Project();
//        사용자를 찾을 수 없다면, 프로젝트 생성을 거부함
        User founduser = userDAO.getByEmail(jwtTokenProvider.getUserEmail(token));
        if (founduser == null) {
            BaseResponseDto baseResponseDto = new BaseResponseDto();

            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("존재하지 않는 사용자");

            return baseResponseDto;
//         프로젝트 생성 프로세스 진행
        } else {
            project.setProjectName(projectRequestDto.getProjectName());
            project.setImageUrl(projectRequestDto.getImageUrl());
            project.setProjectDescription(projectRequestDto.getProjectDescription());
            project.setProjectDetail(projectRequestDto.getProjectDetail());

//            프로젝트 상태 설정
            if(projectRequestDto.getStatus() == null) {
                project.setStatus(ProjectStatus.MAINTENCE);
            } else {
                project.setStatus(projectRequestDto.getStatus());
            }

            project.setProjectPeriod(projectRequestDto.getProjectPeriod());
            project.setProjectCategory(projectRequestDto.getProjectCategory());
            project.setCreatedAt(LocalDateTime.now());
            project.setUpdatedAt(LocalDateTime.now());
//            클럽이 존재하지 않으면, 엔티티의 Club 값을 null로 처리함
            if (projectRequestDto.getBelongedClubId() == 0) {
                logger.info("[Log] 클럽 정보를 조회하지 못 함, ID:" + projectRequestDto.getBelongedClubId());
                project.setClub(null);
            } else {
                logger.info("[Log] 클럽 정보 조회 완료, ID:" + projectRequestDto.getBelongedClubId());
                project.setClub(clubDAO.selectClubById(projectRequestDto.getBelongedClubId()).get());
            }
            project.setUser(founduser);
//            프로젝트 생성
            Project createdProject = projectDAO.createProject(project);
            logger.info("[Log] 프로젝트 생성 됨, ID:" + createdProject.getId());

//            프로젝트 크루 생성
            for (ProjectCrewRequestDto projectCrewRequestDto : projectRequestDto.getProjectCrew()) {
                ProjectCrew projectCrew = new ProjectCrew();
                projectCrew.setProject(createdProject);
                projectCrew.setProjectCrewName(projectCrewRequestDto.getName());
                projectCrew.setProjectCrewPosition(projectCrewRequestDto.getPosition());
                projectCrew.setProjectCrewRole(projectCrewRequestDto.getRole());
                projectCrew.setCreatedAt(LocalDateTime.now());
                projectCrew.setUpdatedAt(LocalDateTime.now());

                projectCrewDAO.createCrew(projectCrew);
                logger.info("[Log] 새로운 크루 생성됨:" + projectCrewRequestDto.getName() + "," + projectCrewRequestDto.getPosition() + "," + projectCrewRequestDto.getRole());
            }

//            프로젝트 링크 생성
            for (ProjectLinkRequestDto projectLinkRequestDto : projectRequestDto.getProjectLink()) {
                ProjectLink projectLink = new ProjectLink();
                projectLink.setLinkName(projectLinkRequestDto.getName());
                projectLink.setLinkUrl(projectLinkRequestDto.getLinkUrl());
                projectLink.setProject(createdProject);

                projectLinkDAO.createLink(projectLink);
                logger.info("[Log] 새로운 프로젝트 링크 생성됨:" + projectLinkRequestDto.getName() + "," + projectLinkRequestDto.getLinkUrl());
            }

//            프로젝트에 포함된 기술 스텍 생성
            for (ProjectTechStackRequestDto projectTechStackRequestDto : projectRequestDto.getProjectTechStack()) {
                ProjectTechStack projectTechStack = new ProjectTechStack();
                projectTechStack.setTechStackName(projectTechStackRequestDto.getName());
                projectTechStack.setProject(createdProject);

                projectTechStackDAO.createTechStack(projectTechStack);
                logger.info("[Log] 새로운 프로젝트 기술 스텍 생성됨:" + projectTechStackRequestDto.getName());
            }
//            프로젝트 월간 조회수 생성
            MonthlyViews monthlyView = new MonthlyViews();
            monthlyView.setMonth(YearMonth.now());
            monthlyView.setProject(createdProject);
            monthlyView.setViewCount(0);
            monthlyViewsRepository.save(monthlyView);

//            프로젝트 생성 프로세스 완료
            BaseResponseDto baseResponseDto = BaseResponseDto.builder()
                    .success(true)
                    .msg("프로젝트 생성 완료")
                    .build();

            return baseResponseDto;
        }
    }

    @Override
    public BaseResponseDto updateProject(Long projectId, ProjectRequestDto projectRequestDto, String token) {
        Optional<Project> projectOptional = projectDAO.selectProjectById(projectId);
        User foundUser = userDAO.getByEmail(jwtTokenProvider.getUserEmail(token));
        if (!projectOptional.isPresent()) {
            BaseResponseDto baseResponseDto = BaseResponseDto.builder()
                    .success(false)
                    .msg("프로젝트가 존재하지 않습니다.")
                    .build();
            logger.info("[Log] 프로젝트가 존재하지 않음, ID:" + projectId);
            return baseResponseDto;
        }

        Project project = projectOptional.get();
        project.setId(projectId);
        project.setProjectName(projectRequestDto.getProjectName());
        project.setImageUrl(projectRequestDto.getImageUrl());
        project.setProjectDescription(projectRequestDto.getProjectDescription());
        project.setProjectDetail(projectRequestDto.getProjectDetail());
        project.setStatus(projectRequestDto.getStatus());
        project.setProjectPeriod(projectRequestDto.getProjectPeriod());
        project.setProjectCategory(projectRequestDto.getProjectCategory());
//        최종 수정일 업데이트
        project.setUpdatedAt(LocalDateTime.now());

        if (projectRequestDto.getBelongedClubId() == 0) {
            project.setClub(null);
            logger.info("[Log] 클럽 정보를 조회하지 못 함, ID:" + projectRequestDto.getBelongedClubId());
        } else {
            project.setClub(clubDAO.selectClubById(projectRequestDto.getBelongedClubId()).orElse(null));
        }

        project.setUser(foundUser);
        logger.info("[Log] 프로젝트 업데이트 됨");
        projectDAO.updateProject(project);

        logger.info("[Log] 프로젝트에 포함된 기술 스텍 전체 삭제");
        projectTechStackDAO.removeTechStack(projectId);
        logger.info("[Log] 프로젝트에 포함된 링크 전체 삭제");
        projectLinkDAO.removeLink(projectId);
        logger.info("[Log] 프로젝트에 포함된 크루 전체 삭제");
        projectCrewDAO.removeCrew(projectId);

        for (ProjectCrewRequestDto projectCrewRequestDto : projectRequestDto.getProjectCrew()) {
            ProjectCrew projectCrew = new ProjectCrew();
            projectCrew.setProject(project);
            projectCrew.setProjectCrewName(projectCrewRequestDto.getName());
            projectCrew.setProjectCrewPosition(projectCrewRequestDto.getPosition());
            projectCrew.setProjectCrewRole(projectCrewRequestDto.getRole());
            projectCrew.setCreatedAt(LocalDateTime.now());
            projectCrew.setUpdatedAt(LocalDateTime.now());

            projectCrewDAO.createCrew(projectCrew);

            logger.info("[Log] 새로운 크루 생성됨:" + projectCrewRequestDto.getName() + "," + projectCrewRequestDto.getPosition() + "," + projectCrewRequestDto.getRole());
        }

        for (ProjectLinkRequestDto projectLinkRequestDto : projectRequestDto.getProjectLink()) {
            ProjectLink projectLink = new ProjectLink();
            projectLink.setLinkName(projectLinkRequestDto.getName());
            projectLink.setLinkUrl(projectLinkRequestDto.getLinkUrl());
            projectLink.setProject(project);

            projectLinkDAO.createLink(projectLink);

            logger.info("[Log] 새로운 프로젝트 링크 생성됨:" + projectLinkRequestDto.getName() + "," + projectLinkRequestDto.getLinkUrl());
        }

        for (ProjectTechStackRequestDto projectTechStackRequestDto : projectRequestDto.getProjectTechStack()) {
            ProjectTechStack projectTechStack = new ProjectTechStack();
            projectTechStack.setTechStackName(projectTechStackRequestDto.getName());
            projectTechStack.setProject(project);

            projectTechStackDAO.createTechStack(projectTechStack);

            logger.info("[Log] 새로운 프로젝트 기술 스텍 생성됨:" + projectTechStackRequestDto.getName());
        }

        BaseResponseDto baseResponseDto = BaseResponseDto.builder()
                .success(true)
                .msg("프로젝트 업데이트가 완료되었습니다.")
                .build();

        return baseResponseDto;
    }

    @Override
    public Page<AllProjectResponseDto> getProjectsInMonth(YearMonth yearMonth, Pageable pageable) {
        Page<MonthlyViews> monthlyViews = monthlyViewsRepository.findByMonthOrderByViewCountDesc(yearMonth,pageable);


        return monthlyViews.map(monthlyView ->{
            Project temp = monthlyView.getProject();
            AllProjectResponseDto allProjectResponseDto = new AllProjectResponseDto();

            allProjectResponseDto.setId(temp.getId());
            allProjectResponseDto.setProjectName(temp.getProjectName());
            allProjectResponseDto.setProjectDescription(temp.getProjectDescription());
            allProjectResponseDto.setImageUrl(temp.getImageUrl());
            allProjectResponseDto.setProjectCategory(temp.getProjectCategory());

            if(temp.getClub()!=null) {
                allProjectResponseDto.setBelongedClubName(temp.getClub().getClubName());
            }else {
                logger.info("[Log]클럽이 존재하지 않음");
                allProjectResponseDto.setBelongedClubName(null);
            }
            return allProjectResponseDto;
        });
    }

    @Override
    public List<AllProjectTechStacksResponseDto> getProjectTechStacks() {
        List<ProjectTechStack> techStacks = projectTechStackRepository.findAll();
        return techStacks.stream()
                .map(tech -> tech.getTechStackName())
                .distinct()
                .map(AllProjectTechStacksResponseDto::new)
                .collect(Collectors.toList());
    }
}
