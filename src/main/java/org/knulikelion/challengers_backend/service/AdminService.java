package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.*;
import org.knulikelion.challengers_backend.data.dto.response.*;
import org.knulikelion.challengers_backend.data.enums.ProjectStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {
    SignResponseDto signIn(SignInRequestDto signInRequestDto) throws RuntimeException;
    BaseResponseDto assignAdministrator(AssignAdministratorRequestDto assignAdministratorRequestDto);
    BaseResponseDto postNoti(NoticeRequestDto noticeRequestDto, String email);
    List<NoticeResponseDto> getAllNoti();
    NoticeResponseDto getNotiDetail(Long id);
    BaseResponseDto deleteNoti(Long id);
    BaseResponseDto updateNoti(UpdateNoticeRequestDto updateNoticeRequestDto, String email);
    BaseResponseDto removeClubMember(Long clubId, List<Long> userId);
    BaseResponseDto changeName(String email, String name);
    BaseResponseDto changeProfile(String email, String url);
    BaseResponseDto changePw(ChangePasswordRequestDto changePasswordRequestDto, String email);
    BaseResponseDto changeRole(String email, String role);
    Page<AdminClubResponseDto> getAllClubs(int page, int size);
    BaseResponseDto changeClubStatus(Long clubId, String status);
    BaseResponseDto changeProjectStatus(Long projectId, ProjectStatus status);
    Page<AllProjectResponseDto> getAllProject(int page, int size);

//  대시 보드
    Long countUsers();
    Long countClubs();
    Long countProjects();

    Long countTodayAddProjects();

    Long countTodayAddClubs();

    Long countTodayAddUsers();

    Long countTodayDeletedProjects();

    Long countTodayDeletedClubs();

    Long countTodayDeletedUsers();
    Long countDeletedUsers();

//  홈 피드

    BaseResponseDto createHomeFeed(AdminHomeFeedRequestDto adminHomeFeedDto, String token);
    Object getHomeFeed(Long id);
    List<AdminHomeFeedResponseDto> getAllHomeFeed();
    BaseResponseDto updateHomeFeed(AdminHomeFeedRequestDto adminHomeFeedRequestDto,Long feedId);
    BaseResponseDto deleteHomeFeed(Long feedId);
    List<ProjectAuditResponseDto> getLatestCreatedProject();
    List<ProjectAuditResponseDto> getLatestDeletedProject();
    List<ClubAuditResponseDto> getLatestCreatedClub();
    List<ClubAuditResponseDto> getLatestDeletedClub();
    List<UserAuditResponseDto> getLatestCreatedUser();
    List<UserAuditResponseDto> getLatestDeletedUser();
}
