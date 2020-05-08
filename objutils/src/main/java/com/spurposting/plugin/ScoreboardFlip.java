package com.spurposting.plugin;

import java.util.Set;
import java.util.HashSet;

import org.bukkit.Bukkit;

import org.bukkit.command.*;
import org.bukkit.scoreboard.*;





public class ScoreboardFlip implements CommandExecutor {

    int TICKRATE = 20;


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 && args.length != 3) {
            sender.sendMessage("Incorrectly formatted command");
        }
        
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();

        Objective oldObj = board.getObjective(args[0]);
        Objective newObj = board.getObjective(args[1]);
        Objective isActive = null;
        
        if (args.length == 3) {
            isActive = board.getObjective(args[2]);
        }

        Set<String> entries = board.getEntries();

        Set<String> oldEntries = new HashSet<>();
        Set<String> newEntries = new HashSet<>();
        
        entryLoop:
        for(String entry : entries) {
            Set<Score> scores = board.getScores(entry);
            for(Score score : scores) {
                if (score.getObjective().equals(oldObj) && score.getScore() > 0) {
                    oldEntries.add(entry);
                }
                if (score.getObjective().equals(newObj) && score.getScore() > 0) {
                    newEntries.add(entry);
                }
            }

            for(Score score : scores) {
                if (score.getScore() > 0) {
                    continue entryLoop;
                }
            }
            board.resetScores(entry);
        }
        if (newObj != null && oldObj != null) {
            outer:
            for(String oldEntry : oldEntries) {
                int time = oldObj.getScore(oldEntry).getScore();
                int timeSeconds = (time / TICKRATE);
                int timeMinutes = (int)Math.floor(timeSeconds / 60);
                int seconds = timeSeconds % 60;
                int ticks = (time % 20) / 2;
                String oldEntryFormatted = String.format("%02d", timeMinutes)
                 + ":" + String.format("%02d", seconds)
                 + "." + ticks
                 + " " + oldEntry;
                
                for (String newEntry : newEntries) {
                    if (newEntry.contains(oldEntry)) {
                        if (newEntry.equals(oldEntryFormatted)) {
                            // if nothing has changed
                            continue outer;
                        } else {
                            // else
                            // remove old entry
                            Set<Score> scores = board.getScores(oldEntry);
                            for(Score score : scores) {
                                // if timing is active
                                if (score.getObjective().equals(isActive) && score.getScore() > 0) {
                                    board.resetScores(newEntry);
                                    // add new entry
                                    newEntry = oldEntryFormatted;
                                    Score newScore = newObj.getScore(newEntry);
                                    newScore.setScore(1);
                                    sender.sendMessage(String.format("updated score for %s", newEntry));
                                    continue outer;
                                }
                            }
                            
                            // if timing is not active but new score is less
                            if (oldEntryFormatted.compareTo(newEntry) < 0) {
                                board.resetScores(newEntry);
                                // add new entry
                                newEntry = oldEntryFormatted;
                                Score score = newObj.getScore(newEntry);
                                score.setScore(1);
                                sender.sendMessage(String.format("updated score for %s", newEntry));
                            }

                            // else do nothing
                            continue outer;
                        }
                    }
                }
                String newEntry = oldEntryFormatted;
                Score score = newObj.getScore(newEntry);
                score.setScore(1);
            }
            sender.sendMessage(String.format("Flipped %s and wrote to %s", args[0], args[1]));
        } else {
            if (oldObj == null) {
                sender.sendMessage(String.format("Objective %s not found", args[0]));
            }
            if (newObj == null) {
                sender.sendMessage(String.format("Objective %s not found", args[1]));
            }
        }
        
    return true;
    }
}