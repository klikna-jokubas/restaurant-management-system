package com.example.kursinisweb.repo;

import com.example.kursinisweb.model.BasicUser;
import com.example.kursinisweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {

    User getUserByLogin(String login);

}
