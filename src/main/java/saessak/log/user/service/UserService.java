package saessak.log.user.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saessak.log.jwt.TokenProvider;
import saessak.log.jwt.dto.TokenDto;
import saessak.log.reaction.Reaction;
import saessak.log.reaction.repository.ReactionRepository;
import saessak.log.subscription.Subscription;
import saessak.log.subscription.repository.SubscriptionRepository;
import saessak.log.user.User;
import saessak.log.user.dto.*;
import saessak.log.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final TokenProvider tokenProvider;
    private final ReactionRepository reactionRepository;
    private final SubscriptionRepository subscriptionRepository;

    // 회원가입
    @Transactional
    public Long join(UserJoinDto userJoinDto) {
        duplicateEmail(userJoinDto);
        if (userJoinDto.getPassword().equals(userJoinDto.getPasswordCheck())) {
            User user = User.builder()
                    .profileId(userJoinDto.getProfileId())
                    .email(userJoinDto.getEmail())
                    .name(userJoinDto.getName())
                    .password(encoder.encode(userJoinDto.getPassword()))
                    .build();
            userRepository.save(user);
            return user.getId();
        }
        throw new RuntimeException("입력하신 password 가 일치하지 않습니다.");

    }

    // email 중복검사
    private void duplicateEmail(UserJoinDto userJoinDto) {
        userRepository.findByEmail(userJoinDto.getEmail())
                .ifPresent(e ->{
                    throw new RuntimeException("중복된 이메일입니다.");
                });
    }

    // profileId 중복검사
    public void duplicateUser(UserDuplicateDto userDuplicateDto) {
        userRepository.findOptionalByProfileId(userDuplicateDto.getProfileId())
                .ifPresent(u -> {
                    throw new RuntimeException("중복된 아이디입니다.");
                });
    }

    // 로그인
    public TokenDto login(UserLoginDto userLoginDto) {
        User findUser = userRepository.findOptionalByProfileId(userLoginDto.getProfileId())
                .orElseThrow(() -> {
                    throw new IllegalStateException("등록되지 않은 회원입니다.");
                });

        if (!encoder.matches(userLoginDto.getPassword(), findUser.getPassword())) {
            throw new IllegalStateException("비밀번호가 잘못되었습니다.");
        }

        return tokenProvider.createToken(findUser.getId(), findUser.getProfileId());
    }

    // 아이디 찾기
    public ResponseFindIdDto findProfileId(UserFindIdDto userFindIdDto) {
        User findUser = userRepository
                .findByEmailAndName(userFindIdDto.getEmail(), userFindIdDto.getName())
                .orElseThrow(() ->
                        new IllegalStateException("등록되지 않은 회원입니다.")
                );
        ResponseFindIdDto responseFindIdDto = new ResponseFindIdDto();
        responseFindIdDto.setProfileId(findUser.getProfileId());

        return responseFindIdDto;
    }

    // 비밀번호 찾기
    @Transactional
    public ResponseResetPasswordDto findPassword(UserFindPasswordDto userFindPasswordDto) {
        User findUser = userRepository.findByUserInfo(
                        userFindPasswordDto.getEmail(),
                        userFindPasswordDto.getName(),
                        userFindPasswordDto.getProfileId())
                .orElseThrow(() ->
                        new IllegalStateException("등록되지 않은 회원입니다."));
        String resetPassword = RandomStringUtils.randomAlphabetic(8);
        findUser.changeTempPassword(encoder.encode(resetPassword));
        ResponseResetPasswordDto responseResetPasswordDto = new ResponseResetPasswordDto();
        responseResetPasswordDto.setResetPassword(resetPassword);

        return responseResetPasswordDto;
    }

    // 비밀번호 변경
    @Transactional
    public void updatePassword(String profileId, ChangePasswordDto changePasswordDto) {

        if (changePasswordDto.getPassword().equals(changePasswordDto.getPasswordCheck())) {
            User findUser = userRepository.findOptionalByProfileId(profileId)
                    .orElseThrow(() ->
                            new IllegalStateException("등록되지 않은 회원입니다."));
             findUser.changeTempPassword(encoder.encode(changePasswordDto.getPassword()));
        } else {
            new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }

    // 마이페이지 유저정보
    public ResponseUserInformationDto userInformation(String profileId) {
        User findUser = userRepository.findByProfileId(profileId);

        List<Reaction> reactions = reactionRepository.findByUserIdx(findUser.getId());
        List<Subscription> subscriptions = subscriptionRepository.findByFromUserID(findUser.getId());

        ResponseUserInformationDto userInformationDto = new ResponseUserInformationDto();

        List<Long> reactionPostId = reactions.stream().map(reaction -> reaction.getPost().getId()).collect(Collectors.toList());
        List<Long> subscriptionToUserId = subscriptions.stream().map(user -> user.getToUserId().getId()).collect(Collectors.toList());
        userInformationDto.setSubscriptionToUserId(subscriptionToUserId);
        userInformationDto.setReactionPostId(reactionPostId);
        userInformationDto.setUserId(findUser.getId());
        userInformationDto.setProfileId(findUser.getProfileId());
        userInformationDto.setEmail(findUser.getEmail());
        userInformationDto.setName(findUser.getName());
        return userInformationDto;
    }

}
