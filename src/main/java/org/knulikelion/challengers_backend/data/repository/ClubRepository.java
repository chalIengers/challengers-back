package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findAllByClubManager(User user);

    // clubApproved 0인 클럽 추출
    Page<Club> findAllByClubApprovedTrue(Pageable pageable);



    Club findByClubName(String clubName);

    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
