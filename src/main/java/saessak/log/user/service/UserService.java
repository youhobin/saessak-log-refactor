package saessak.log.user.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saessak.log.jwt.TokenProvider;
import saessak.log.jwt.dto.TokenResponse;
import saessak.log.reaction.repository.ReactionRepository;
import saessak.log.subscription.Subscription;
import saessak.log.subscription.repository.SubscriptionRepository;
import saessak.log.user.User;
import saessak.log.user.dto.*;
import saessak.log.user.exception.DuplicateEmailException;
import saessak.log.user.exception.DuplicateLoginIdException;
import saessak.log.user.exception.NotMatchPasswordException;
import saessak.log.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

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
            User user = userJoinDto.toEntity();

            User savedUser = userRepository.save(user.passwordEncode(encoder));
            return savedUser.getId();
        }
        throw new NotMatchPasswordException("입력하신 password 가 일치하지 않습니다.");

    }

    // email 중복검사
    private void duplicateEmail(UserJoinDto userJoinDto) {
        userRepository.findByEmail(userJoinDto.getEmail())
                .ifPresent(e ->{
                    throw new DuplicateEmailException("중복된 이메일입니다.");
                });
    }

    // profileId 중복검사
    public void duplicateUser(ProfileIdDuplicateDto profileIdDuplicateDto) {
        userRepository.findOptionalByProfileId(profileIdDuplicateDto.getProfileId())
                .ifPresent(u -> {
                    throw new DuplicateLoginIdException("중복된 아이디입니다.");
                });
    }

    // 로그인
    public TokenResponse login(UserLoginDto userLoginDto) {
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
    public FindIdResponse findProfileId(UserFindIdRequest userFindIdRequest) {
        User findUser = userRepository
                .findByEmailAndName(userFindIdRequest.getEmail(), userFindIdRequest.getName())
                .orElseThrow(() ->
                        new IllegalStateException("등록되지 않은 회원입니다.")
                );

        return new FindIdResponse(findUser.getProfileId());
    }

    // 비밀번호 찾기
    @Transactional
    public ResetPasswordResponse findPassword(UserFindPasswordRequest userFindPasswordRequest) {
        User findUser = userRepository.findByUserInfo(
                        userFindPasswordRequest.getEmail(),
                        userFindPasswordRequest.getName(),
                        userFindPasswordRequest.getProfileId())
                .orElseThrow(() ->
                        new IllegalStateException("등록되지 않은 회원입니다."));

        String resetPassword = RandomStringUtils.randomAlphabetic(8);
        findUser.changeTempPassword(encoder.encode(resetPassword));

        return new ResetPasswordResponse(resetPassword);
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
            throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    // 마이페이지 유저정보
    public UserInformationResponse userInformation(String profileId) {

        //토큰에서 얻어온 profileId로
        User findUser = userRepository.findWithReactionsByProfileId(profileId);

        List<Subscription> subscriptions = subscriptionRepository.findByFromUserID(findUser.getId());

        List<Long> reactionPostId = findUser.getReactions().stream().map(reaction -> reaction.getPost().getId()).collect(Collectors.toList());
        List<Long> subscriptionToUserId = subscriptions.stream().map(user -> user.getToUserId().getId()).collect(Collectors.toList());

        UserInformationResponse userInformationResponse = UserInformationResponse.builder()
            .userId(findUser.getId())
            .profileId(findUser.getProfileId())
            .email(findUser.getEmail())
            .name(findUser.getName())
            .reactionPostId(reactionPostId)
            .subscriptionToUserId(subscriptionToUserId)
            .build();
        return userInformationResponse;
    }

}
