package com.spurposting.plugin;

import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.command.*;

import org.bukkit.Bukkit;

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

public class StartClock implements CommandExecutor {
    
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        LocalDateTime now = LocalDateTime.now();
                
        if (args[0].equals("set") || args[0].equals("create")) {
            String name = args[1];
            String createCommand = "hd create " + name + " New StartClock";
            
            Bukkit.dispatchCommand(console, createCommand);
            
        }

        if (args[0].equals("update")) {
            
            String name = args[1];
            String updateCommand = "hd setline " + name + " 1 " + dtf.format(now);
            sender.sendMessage(updateCommand);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), updateCommand);
        }

        if (args[0].equals("remove")) {
            
            String name = args[1];
            String remove = "hd remove " + name;
            sender.sendMessage(remove);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), remove);
        }
        
        
        return true;
    }
}