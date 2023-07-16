package org.knulikelion.challengers_backend.data.dao;

import org.knulikelion.challengers_backend.data.entity.Club;

import java.util.Optional;

public interface ClubDAO{
    Optional<Club> selectClubById(Long id);
    Club createClub(Club club);
    void removeClub(Long id);
}
