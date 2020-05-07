package com.spurposting.plugin;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;

import org.bukkit.command.*;
import org.bukkit.scoreboard.*;



public class SBUtils implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("Incorrectly formatted command");
        }
        
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        
        Objective obj = board.getObjective(args[1]);

        Set<String> entries = board.getEntries();
        List<String> entriesSorted = new ArrayList<>();
        
        if (args[0].equals("list")) {
            
            for(String entry : entries) {
                Set<Score> scores = board.getScores(entry);
                for(Score score : scores) {
                    if (score.getObjective​().equals(obj) && score.getScore() > 0) {
                        entriesSorted.add(String.format("%04d", score.getScore()) + " " + entry);
                    }
                }
            }
            
            Collections.sort(entriesSorted);
            
            for(String entry : entriesSorted) {
                sender.sendMessage(entry);
            }
        }
        if (args[0].equals("clear")) {
            for(String entry : entries) {
                Set<Score> scores = board.getScores(entry);
                for(Score score : scores) {
                    if (score.getObjective​().equals(obj) && score.getScore() > 0) {
                        board.resetScores(entry);
                    }
                }
            }
            sender.sendMessage(String.format(
                "Cleared Objective %s (display name %s)",
                obj.getName​(), obj.getDisplayName​()));
        }
        return true;
    }
}