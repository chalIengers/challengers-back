package org.knulikelion.challengers_backend.data.dao.Impl;

import org.knulikelion.challengers_backend.data.dao.ClubDAO;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.entity.UserClub;
import org.knulikelion.challengers_backend.data.repository.ClubRepository;

import org.knulikelion.challengers_backend.data.repository.UserClubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ClubDAOImpl implements ClubDAO {
    private static final Logger logger = LoggerFactory.getLogger(ClubDAOImpl.class);
    private final UserClubRepository userClubRepository;
    private final ClubRepository clubRepository;
    @Autowired
    public ClubDAOImpl(UserClubRepository userClubRepository, ClubRepository clubRepository) {
        this.userClubRepository = userClubRepository;
        this.clubRepository = clubRepository;
    }

    @Override
    public Optional<Club> selectClubById(Long id) {
        logger.info("Select Club Id:"+id);
        return clubRepository.findById(id);
    }

    @Override
    public Club updateClub(Long id, Club club) throws Exception {
        Optional<Club> selectedClub = clubRepository.findById(id);
        Club updatedClub;
        if (selectedClub.isPresent()){
            Club clubInfo = selectedClub.get();
            clubInfo.setClubName(club.getClubName());
            clubInfo.setLogoUrl(club.getLogoUrl());
            clubInfo.setClubDescription(club.getClubDescription());
            clubInfo.setClubForm(club.getClubForm());
            clubInfo.setClubApproved(club.isClubApproved());

            updatedClub = clubRepository.save(clubInfo);
        }else{
            throw new Exception();
        }
        return updatedClub;
    }

    @Override
    public Club createClub(Club club) {
        logger.info("Create Club Info:"+club.toString());
        return clubRepository.save(club);
    }

    @Override
    public void removeClub(Long id) {
        logger.info("Delete Club Id:"+id);
        clubRepository.deleteById(id);
    }

    @Override
    public List<User> getUsersByClubId(Long id) {
        logger.info("Get Users By Club_Id:" + id);

        List<UserClub> userClubList = userClubRepository.findAll();
        List<User> userList = new ArrayList<>();

        for (UserClub userClub : userClubList) {
            if (userClub.getClub().getId().equals(id)) {
                userList.add(userClub.getUser());
            }
        }
        return userList;
    }

    @Override
    public List<Club> getAllClub(int page, int size) {
        return clubRepository.findAll();
    }

    @Override
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }
}

