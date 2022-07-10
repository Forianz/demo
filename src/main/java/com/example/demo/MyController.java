package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class MyController {

    int team = 0;
    Random random = new Random();

    LocalDateTime time = null;

    int points = 0;

    Map<String, Integer> teamPoints = new HashMap<>();

    private List<String> cards = new ArrayList<>();
    private List<String> removed = new ArrayList<>();

    @GetMapping("/get")
    public String test() {
        return "test";
    }

    @GetMapping("/game")
    public String game(Model model) {
        if (time == null)
            time = LocalDateTime.now();
        modelsetup(model);
        return "game";
    }

    @GetMapping("/next")
    public String nextTeam(Model model) {
        time = LocalDateTime.now();
        modelsetup(model);
        return "game";
    }

    @GetMapping("/done")
    public String nextCard(Model model, String value) {
        removed.add(value);
        cards.remove(value);
        points++;
        if (cards.isEmpty())
        {
            cards.addAll(removed);
            removed = new ArrayList<>();
            model.addAttribute("last", true);
            model.addAttribute("nextround", "The next round will start after this");
            model.addAttribute("point", points);
            points--;
            return "point";
        }
        modelsetup(model);
        return "game";
    }

    @GetMapping("/fin")
    public String showPoints(Model model, String value) {
        cards.remove(value);
        removed.add(value);
        if (cards.isEmpty()){
            cards.addAll(removed);
            removed = new ArrayList<>();
            model.addAttribute("nextround", "");
        }
        time = null;
        model.addAttribute("point", points);
        System.out.println(teamPoints);
        return "point";
    }

    @GetMapping("/tmax")
    public String maxPoint(Model model) {
        if (!teamPoints.containsKey(getTeam()))
            teamPoints.put(getTeam(),0);
        teamPoints.put(getTeam(), teamPoints.get(getTeam()) + points + 1);
        team++;
        time = LocalDateTime.now();
        modelsetup(model);
        points = 0;
        return "game";
    }

    @GetMapping("/tother")
    public String stolen(Model model) {
        if (!teamPoints.containsKey(getTeam()))
            teamPoints.put(getTeam(),0);
        teamPoints.put(getTeam(), teamPoints.get(getTeam()) + points);
        team++;
        if (!teamPoints.containsKey(getTeam()))
            teamPoints.put(getTeam(),0);
        teamPoints.put(getTeam(), teamPoints.get(getTeam()) + 1);
        time = LocalDateTime.now();
        modelsetup(model);
        points = 0;
        return "game";
    }

    @PostMapping("/save")
    public String save(String card) {
        cards.add(card);
        return "test";
    }

    private void modelsetup(Model model) {
        model.addAttribute("time", time.plusSeconds(10).toString());
        model.addAttribute("team", getTeam());
        model.addAttribute("message", cards.get(random.nextInt(cards.size())));
        model.addAttribute("team", getTeam());
    }

    private String getTeam() {
        if (team % 2 == 0)
            return "Team1";
        else
            return "Team2";
    }

}
