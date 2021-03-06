package com.spurposting.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {

    @Override
    public void onEnable() {

        getLogger().info("Loading Plugin: FLIPBOARD");
        this.getCommand("flip").setExecutor(new ScoreboardFlip());
        this.getCommand("obj").setExecutor(new SBUtils());
        
    }
    @Override
    public void onDisable() {
        getLogger().info("Unloading Plugin: FLIPBOARD");
    }
}