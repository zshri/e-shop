package com.example.eshop.repository;

import com.example.eshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    User findByEmail(String email);

    User findByOauthProviderAndOauthId(String provider, Integer id);

}
