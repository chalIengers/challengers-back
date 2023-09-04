package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.data.dto.response.MyPageResponseDto;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.entity.UserClub;
import org.knulikelion.challengers_backend.data.repository.UserClubRepository;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.MyPageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyPageServiceImpl implements MyPageService {
    private final UserRepository userRepository;
    private final UserClubRepository userClubRepository;

    public MyPageServiceImpl(UserRepository userRepository, UserClubRepository userClubRepository) {
        this.userRepository = userRepository;
        this.userClubRepository = userClubRepository;
    }

    @Override
    public MyPageResponseDto getInfo(String email) {
        User user = userRepository.getByEmail(email);
        List<UserClub> userClub = userClubRepository.findByUserId(user.getId());

        MyPageResponseDto myPageResponseDto = new MyPageResponseDto();

        myPageResponseDto.setName(user.getUserName());
        myPageResponseDto.setEmail(user.getUsername());

        List<String> clubs = new ArrayList<>();

        for (UserClub temp : userClub) {
            clubs.add(temp.getClub().getClubName());
        }

        myPageResponseDto.setClubs(clubs);

        return myPageResponseDto;
    }
}
