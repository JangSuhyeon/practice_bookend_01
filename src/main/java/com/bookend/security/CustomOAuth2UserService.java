package com.bookend.security;

import com.bookend.login.domain.Role;
import com.bookend.security.dto.OAuthAttributes;
import com.bookend.security.dto.SessionUser;
import com.bookend.login.domain.entity.User;
import com.bookend.login.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2 로그인 이후 반환된 사용자 정보를 처리
 */
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService(); // DefaultOAuth2UserService는 OAuth2UserService 구현체로서, 이 변수를 이용하여 기본 OAuth2 작업을 수행할 수 있음.
        OAuth2User oAuth2User = delegate.loadUser(userRequest); // 사용자 정보를 가져옴, userRequest는 OAuth2 로그인 요청과 관련된 정보를 포함하고 있음.

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 현재 로그인 진행 중인 서비스를 구분하는 코드 (google)
        String userNameAttributeName = userRequest.getClientRegistration() // sub
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName(); // OAuth2 로그인 진행 시 키가 되는 필드값
        System.out.println("registrationId : " + registrationId);
        System.out.println("userNameAttributeName : " + userNameAttributeName);
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes()); // userRequest 안에 담긴 사용자 정보를 OAuthAttributes로 변환

        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user)); // 세션에 사용자 정보 저장

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())), // 사용자 Role의 key를 가져와 권한 설정
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 사용자 정보 저장 및 업데이트
    private User saveOrUpdate(OAuthAttributes attributes) {
        // 기존에 있는 사용자 이면 정보 업데이트
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }

    // 게스트 로그인
    public OAuth2User guestLogin() {
        System.out.println("게스트 로그인");
        // 게스트 이름 생성
        String name = "GUEST" + String.format("%02d", userRepository.countByRole(Role.GUEST));

        // 게스트 속성 생성
        Map<String, Object> guestAttributes = new HashMap<>();
        guestAttributes.put("name", name);
        guestAttributes.put("email", "GUEST");
        guestAttributes.put("picture", "GUEST");

        OAuthAttributes attributes = OAuthAttributes.of("guest", "email", guestAttributes);

        User user = guestSave(attributes);
        httpSession.setAttribute("user", new SessionUser(user)); // 세션에 사용자 정보 저장

        OAuth2User guestUser = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())), // 사용자 Role의 key를 가져와 권한 설정
                attributes.getAttributes(),
                attributes.getNameAttributeKey());

        // Spring Security에 사용자 인증 정보를 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(guestUser, null, guestUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return guestUser;
    }

    // 게스트 정보 저장
    private User guestSave(OAuthAttributes attributes) {
        User user = attributes.toGuestEntity();
        return userRepository.save(user);
    }
}
