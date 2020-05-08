package com.spurposting.plugin;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;

import org.bukkit.command.*;
import org.bukkit.scoreboard.*;



public class SBUtils implements CommandExecutor {

    int TICKRATE = 20;


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
                    if (score.getObjective().equals(obj) && score.getScore() > 0) {
                        int time = score.getScore();
                        int timeSeconds = (int)Math.floor(time / TICKRATE);
                        int timeMinutes = (int)Math.floor(timeSeconds / 60);
                        int ticks = (time % 20) / 2;
                        int seconds = timeSeconds % 60;
                        printLns.add(
                            String.format("%02d", timeMinutes)
                            + ":" + String.format("%02d", seconds)
                            + "." + ticks
                            + " " + entry);
                    }
                }
            }

            Collections.sort(printLns);

            int i = 0;
            int j = printLns.size();

            if (args.length == 4) {
                if (Integer.parseInt(args[2]) >= printLns.size()) {
                    sender.sendMessage("No Results Yet");
                    return true;
                } else {
                    i = Integer.parseInt(args[2]);
                }
                if (Integer.parseInt(args[3]) > printLns.size()) {
                    j = printLns.size();
                } else {
                    j = Integer.parseInt(args[3]);
                }
            }

            while (i < j) {
                if (i < 9) {
                    sender.sendMessage("  " + (i + 1) + ". " + printLns.get(i));
                } else {
                    sender.sendMessage((i + 1) + ". " + printLns.get(i));
                }
                i++;
            }
        }
        if (args[0].equals("clear")) {
            if (args.length != 2) {
                sender.sendMessage("Incorrectly formatted command");
            }

            for (String entry : entries) {
                Set<Score> scores = board.getScores(entry);
                for (Score score : scores) {
                    if (score.getObjective().equals(obj) && score.getScore() > 0) {
                        board.resetScores(entry);
                    }
                }
            }
            sender.sendMessage(String.format(
                "Cleared Objective %s (display name %s)",
                obj.getName(), obj.getDisplayName()));
        }
        if (args[0].equals("double")) {
            if (args.length != 2) {
                sender.sendMessage("Incorrectly formatted command");
            }

            for (String entry : entries) {
                Set<Score> scores = board.getScores(entry);
                for (Score score : scores) {
                    if (score.getObjective().equals(obj) && score.getScore() > 0) {
                        score.setScore(score.getScore() * 2);
                    }
                }
            }
            sender.sendMessage(String.format(
                "Doubled Objective %s (display name %s)",
                obj.getName(), obj.getDisplayName()));
        }
        if (args[0].equals("copy")) {
            if (args.length != 3) {
                sender.sendMessage("Incorrectly formatted command");
            }
            Objective newObj = board.getObjective(args[2]);
            for (String entry : entries) {
                Set<Score> scores = board.getScores(entry);
                for (Score score : scores) {
                    if (score.getObjective().equals(obj) && score.getScore() > 0) {
                        Score newScore = newObj.getScore(entry);
                        newScore.setScore(score.getScore());
                    }
                }
            }
            
            sender.sendMessage(String.format(
                "Copied Objective %s to %s",
                obj.getName(), newObj.getName()));
        }
        return true;
    }
}