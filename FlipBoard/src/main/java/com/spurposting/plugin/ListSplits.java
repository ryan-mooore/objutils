package com.spurposting.plugin;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.command.*;
import org.bukkit.scoreboard.*;

import org.bukkit.Bukkit;



public class ListSplits implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        Objective splits = board.getObjective(args[0]);
        Set<String> entries = board.getEntries();

        List<String> entriesSorted = new ArrayList<>(entries);
        Collections.sort(entriesSorted);

        for(String entry : entriesSorted) {
            Set<Score> scores = board.getScores(entry);
            for(Score score : scores) {
                if (score.getObjective​().equals(splits)) {
                    sender.sendMessage(score.getScore() + " " + entry);
                }
            }
        }
        return true;
    }
}