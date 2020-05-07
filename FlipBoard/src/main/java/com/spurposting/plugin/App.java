package com.spurposting.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Loading Plugin: FLIPBOARD");
        this.getCommand("sbflip").setExecutor(new ScoreboardFlip());
        this.getCommand("splits").setExecutor(new ListSplits());
    }
    @Override
    public void onDisable() {
        getLogger().info("Unloading Plugin: FLIPBOARD");
    }
}