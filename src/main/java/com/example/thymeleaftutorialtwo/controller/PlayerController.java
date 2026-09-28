package com.example.thymeleaftutorialtwo.controller;

import com.example.thymeleaftutorialtwo.dto.PlayerRequest;
import com.example.thymeleaftutorialtwo.service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;
    public PlayerController(PlayerService playerService){
        this.playerService=playerService;
    }
    @GetMapping
    public String getAllStudent(Model model){
        model.addAttribute("players",playerService.getAllPlayers());
        return "players";
    }
    @GetMapping("/addPlayer")
    public String addPlayer(Model model){
        model.addAttribute("player",new PlayerRequest());
        return "playerForm";
    }
    @PostMapping("/addPlayer")
    public String addPlayer(@ModelAttribute("player") PlayerRequest playerRequest){
        playerService.savePlayer(playerRequest);
        return "redirect:/players";
    }
    @GetMapping("/updatePlayer/{id}")
    public String updatePlayer(Model model, @PathVariable Long id){
        model.addAttribute("player",playerService.getPlayerById(id));
        return "playerForm";
    }
    @PostMapping("/updatePlayer/{id}")
    public String updatePlayersById(@PathVariable Long id,@ModelAttribute("player") PlayerRequest playerRequest){
        playerService.updatePlayerById(id,playerRequest);//id must be placed as first
        return "redirect:/players";
    }
    @GetMapping("/deletePlayer/{id}")
    public String deletePlayer(@PathVariable Long id){
        playerService.delete(id);
        return "redirect:/players";
    }

}
