package com.spurposting.plugin;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

import org.bukkit.command.*;
import org.bukkit.scoreboard.*;

import org.bukkit.Bukkit;

public class ScoreboardFlip implements CommandExecutor {
    
    public LinkedHashMap<String, Integer> sortHashMapByValues(HashMap<String, Integer> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);
    
        LinkedHashMap<String, Integer> sortedMap =
            new LinkedHashMap<>();
    
        Iterator<Integer> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Integer val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();
    
            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Integer comp1 = passedMap.get(key);
                Integer comp2 = val;
    
                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (true) {

            sender.sendMessage(String.format("Flipping %s and writing to %s", args[0], args[1]));

            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard board = manager.getMainScoreboard();
            Objective timing = board.getObjective(args[0]);
            Set<String> entries = board.getEntries();
            
            HashMap<String, Integer> timingScores = new HashMap<String, Integer>();
            
            for(String entry : entries) {
                Set<Score> scores = board.getScores(entry);
                for(Score score : scores) {
                    if (score.getObjective​().equals(timing) && score.getScore() > 0) {
                        timingScores.put(entry, score.getScore());
                    }
                }
            }

            
            HashMap<String, Integer> timingScoresSorted = sortHashMapByValues(timingScores);
            List<String> scoreList = new ArrayList<String>();
            
            Objective newBoard = board.getObjective(args[1]);
            if (newBoard != null) {
                for(String entry : entries) {
                    if (Character.isDigit(entry.trim().charAt(0))) {
                        Set<Score> scores = board.getScores(entry);
                        for(Score score : scores) {
                            if (score.getObjective​().equals(newBoard) && score.getScore() == 0) {
                                board.resetScores(entry);
                            }
                        }
                    }
                }
                board.getObjective(args[1]).unregister​();
            }
            
            Objective sorted = board.registerNewObjective(args[1], "dummy");
            sorted.setDisplayName("Top Times Leaderboard");
            DisplaySlot display = DisplaySlot.SIDEBAR;
            sorted.setDisplaySlot(display);
            
            int longestUser = 0;
            int longestTime = 0;

            for(HashMap.Entry<String, Integer> result : timingScoresSorted.entrySet()) {
                String user = result.getKey();
                String time = Integer.toString(result.getValue());
                
                if (user.length() > longestUser) {
                    longestUser = user.length();
                }
                if (time.length() > longestTime) {
                    longestTime = user.length();
                }
            }
            
            for(HashMap.Entry<String, Integer> result : timingScoresSorted.entrySet()) {
                String user = result.getKey();
                String time = Integer.toString(result.getValue());
                
                int padding = 1;
                if (longestUser > 14) {
                    padding = longestUser - 14;
                }
                
                Score score = sorted.getScore("");
                

                if (longestTime - time.length() == 0) {
                    String entry = time + "_" + user;
                    score = sorted.getScore(entry);
                } else {
                    String entry =
                    String.format("%" + (longestTime - time.length()) + "d", Integer.parseInt(time))
                    + "_" + user;
        
                    score = sorted.getScore(entry);
                }
                score.setScore(0);
            }
        }
        return true;
    }
}