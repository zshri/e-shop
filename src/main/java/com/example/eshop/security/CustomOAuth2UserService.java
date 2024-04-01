package com.example.eshop.security;


import com.example.eshop.model.Role;
import com.example.eshop.model.User;
import com.example.eshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class  CustomOAuth2UserService extends DefaultOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getClientName();

        User user = userRepository.findByOauthProviderAndOauthId(provider, oAuth2User.getAttribute("id"));

        if (user == null) {
            user = new User();
            user.setUsername(oAuth2User.getAttribute("login"));
            user.setOauthId(oAuth2User.getAttribute("id"));
            user.setRoles(Collections.singleton(Role.ROLE_USER));
            user.setOauthProvider(provider);
            userRepository.save(user);
        }
        return user;
    }
//



}