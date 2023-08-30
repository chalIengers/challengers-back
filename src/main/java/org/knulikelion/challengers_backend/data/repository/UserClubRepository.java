package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.UserClub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserClubRepository extends JpaRepository<UserClub,Long> {
    List<UserClub> findAllByClubId(Long clubId);
    UserClub findByUserIdAndClubId(Long userId, Long ClubId);
    List<UserClub> findByClubId(Long clubId);
    List<UserClub> findByUserId(Long userId);
}
