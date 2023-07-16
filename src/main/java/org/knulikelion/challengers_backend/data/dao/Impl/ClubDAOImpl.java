package org.knulikelion.challengers_backend.data.dao.Impl;

import org.knulikelion.challengers_backend.data.dao.ClubDAO;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.repository.ClubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClubDAOImpl implements ClubDAO {
    private static final Logger logger = LoggerFactory.getLogger(ClubDAOImpl.class);

    private final ClubRepository clubRepository;
    @Autowired
    public ClubDAOImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public Optional<Club> selectClubById(Long id) {
        logger.info("select club id:"+id);
        return clubRepository.findById(id);
    }

    @Override
    public Club createClub(Club club) {
        logger.info(club.toString());
        return clubRepository.save(club);
    }

    @Override
    public void removeClub(Long id) {
        logger.info("delete club id:"+id);
        clubRepository.deleteById(id);
    }
}
