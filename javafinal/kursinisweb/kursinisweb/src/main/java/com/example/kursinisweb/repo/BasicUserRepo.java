package com.example.kursinisweb.repo;

import com.example.kursinisweb.model.BasicUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicUserRepo  extends JpaRepository<BasicUser, Integer> {
}
