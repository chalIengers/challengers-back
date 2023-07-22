package org.knulikelion.challengers_backend.data.dao.Impl;

import org.knulikelion.challengers_backend.data.dao.ClubDAO;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.repository.ClubRepository;

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

    private final ClubRepository clubRepository;
    @Autowired
    public ClubDAOImpl(ClubRepository clubRepository) {
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
            clubInfo.setClubApproved(club.getClubApproved());

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
        Club selectedClub = clubRepository.findById(id).get();
        selectedClub.setUsers(null);
        clubRepository.delete(selectedClub);
    }

    @Override
    public List<Long> getUsersByClubId(Long id) {
        logger.info("Get Users By Club_Id:"+id);
        Club selectedClub = clubRepository.getById(id);
        List<User> members = selectedClub.getUsers();
        List<Long> ans = new ArrayList<>();
        for(User user : members){
            ans.add(user.getId());
        }
        return ans;
    }
    @Override
    public Club updateUsers(Long id, User selectedUser, User addUser) {
        Club selectedClub = clubRepository.getById(id);
        logger.info("Update Users Of Club_Id:"+id);

        List<User> users = selectedClub.getUsers();
        List<User> newList = new ArrayList<>();

        if (selectedClub.getUsers() == null){
            newList.add(addUser);
            selectedClub.setUsers(newList);
        }else {
            for(User user : users){
                if(!user.getId().equals(selectedUser.getId())){
                    newList.add(user);
                }
            }newList.add(addUser);
            selectedClub.setUsers(newList);
        }
        return selectedClub;
    }

    @Override
    public Club removeClubUsers(Long id, User userId) {
        Club selectedClub = clubRepository.findById(id).get();
        List<User> users = selectedClub.getUsers();
        List<User> newList = new ArrayList<>();
        for(User temp : users){
            if (!temp.getId().equals(userId.getId())){
                newList.add(temp);
            }
        }
        selectedClub.setUsers(newList);
        return selectedClub;
    }
}

