package com.spurposting.plugin;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;

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
        
        if (!board.getObjective("resultsSorted").equals(null)) {
            board.getObjective("resultsSorted").unregister​();
        }
        
        Objective sorted = board.registerNewObjective("resultsSorted", "dummy");
        sorted.setDisplayName("TOP TIMES LEADERBOARD");
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
        
        int i = timingScoresSorted.size();
        for(HashMap.Entry<String, Integer> result : timingScoresSorted.entrySet()) {
            String user = result.getKey();
            String time = Integer.toString(result.getValue());

            String entry = 
            /*"[\"\",{\"text\":\""
             + String.format("%" + longestTime + "s", time)
             +  "\"\"},{\"text\":\""
             + String.format("%" + (30 - longestUser - longestTime) + "s", "")
             +  "\"\"},{\"text\":\""
             + String.format("%" + longestUser + "s", user)
             + "\"\",\"bold\":true,\"color\":\"gold\"}]";*/
            //String.format("%" + longestTime + "s", time)
            //+ String.format("%" + longestUser + "s", user)
            //+ String.format("%" + longestUser + "s", "");
            //+ "\\u00A7e["
            //+ "]";
            time + " " + user + " ";

             Score score = sorted.getScore(entry);
            score.setScore(i--);
        }
        return true;
    }
}