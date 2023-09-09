package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.ClubJoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ClubJoinRepository extends JpaRepository<ClubJoin, Long> {
    List<ClubJoin> findByClub(Club club);
    ClubJoin findByClubIdAndUserId(Long clubId, Long userId);
    List<ClubJoin> findAllByUserId(Long userId);
}
