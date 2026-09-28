package com.example.thymeleaftutorialtwo.repository;

import com.example.thymeleaftutorialtwo.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long>{

}
