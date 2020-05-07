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
        
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        
        Objective obj = board.getObjective(args[1]);
        
        List<String> entries = new ArrayList<>(board.getEntries());
        List<String> printLns = new ArrayList<>();
        
        if (args[0].equals("list")) {
            if (args.length != 2 && args.length != 4) {
                sender.sendMessage("Incorrectly formatted command");
            }
            
            for(String entry : entries) {
                Set<Score> scores = board.getScores(entry);
                for(Score score : scores) {
                    if (score.getObjective​().equals(obj) && score.getScore() > 0) {
                        printLns.add(String.format("%04d", score.getScore()) + " " + entry);
                    }
                }
            }
            
            Collections.sort(printLns);
            
            int i = 0;
            int j = printLns.size();
            System.out.println(j);
            
            if (args.length == 4) {
                i = Integer.parseInt(args[2]);
                j = Integer.parseInt(args[3]);
            }
            
            while(i < j) {
                sender.sendMessage(printLns.get(i));
                i++;
            }
        }
        if (args[0].equals("clear")) {
            if (args.length != 2) {
                sender.sendMessage("Incorrectly formatted command");
            }
            
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