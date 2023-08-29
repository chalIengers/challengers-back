package org.knulikelion.challengers_backend.data.dao;

import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.User;

import java.util.List;
import java.util.Optional;

public interface ClubDAO{
    Optional<Club> selectClubById(Long id);
    Club updateClub(Long id, Club club) throws Exception;
    Club createClub(Club club);
    void removeClub(Long id);
    List<User> getUsersByClubId(Long id);
    List<Club> getAllClub(int page, int size);
    List<Club> getAllClubs();
}
