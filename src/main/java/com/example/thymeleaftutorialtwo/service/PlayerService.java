package com.example.thymeleaftutorialtwo.service;

import com.example.thymeleaftutorialtwo.dto.PlayerRequest;
import com.example.thymeleaftutorialtwo.model.Player;
import com.example.thymeleaftutorialtwo.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    public PlayerService(PlayerRepository playerRepository){
        this.playerRepository=playerRepository;
    }
    public PlayerRequest toDTO(Player player){//When you call a constructor;you use getters
        return new PlayerRequest(player.getId(),player.getName(),player.getJersey());
    }
    public Player toEntity(PlayerRequest playerRequest){
        Player player=new Player();//Remember this as Angulu taught you;calling a constructor
        player.setId(playerRequest.getId());
        player.setName(playerRequest.getName());
        player.setJersey(playerRequest.getJersey());
        return player;
    }
    public List<PlayerRequest> getAllPlayers(){
        return playerRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    public PlayerRequest getPlayerById(Long id){
        Player p=playerRepository.findById(id).orElseThrow(()->new RuntimeException("Player with id not found"));
        return toDTO(p);
    }
    public PlayerRequest savePlayer(PlayerRequest playerRequest){
        Player p=toEntity(playerRequest);//Don't ever make a mistake of starting with an empty dto;null will be saved
        Player saved=playerRepository.save(p);
        return toDTO(saved);
    }
    public PlayerRequest updatePlayer(PlayerRequest playerRequest,Long id){
        Player p=new Player();
        p=playerRepository.findById(id).orElseThrow(()->new RuntimeException("Player already exists"));
        p.setId(playerRequest.getId());
        p.setName(playerRequest.getName());
        p.setJersey(playerRequest.getJersey());
        Player saved=playerRepository.save(p);
        return toDTO(saved);
    }
    public PlayerRequest updatePlayerById(Long id,PlayerRequest playerRequest){
        Player p=playerRepository.findById(id).orElseThrow(()->new RuntimeException("Player not found"));
        //You can only update an existing player
        p.setId(playerRequest.getId());
        p.setName(playerRequest.getName());
        p.setJersey(playerRequest.getJersey());
        //you must save it again after update
        Player updated=playerRepository.save(p);
        return toDTO(updated);
        }
    public void delete(Long id){
        playerRepository.deleteById(id);
    }
}
