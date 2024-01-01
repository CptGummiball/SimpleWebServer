package org.cptgum.simplewebserver;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServerManager {

    private static final Logger logger = LoggerFactory.getLogger("SimpleWebServer");
    private final JavaPlugin plugin;
    private static final int DELAY_SECONDS = 30; // 30 seconds delay

    public WebServerManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void jettyStart() {
        FileConfiguration config = plugin.getConfig();
        int port = config.getInt("webServerPort", 8080);
        try {
            JettyServer.start(port);
        } catch (Exception e) {
            logger.error("Failed to start Web Server", e);
        }
    }

    public void jettyStop() {
        try {
            JettyServer.stop();
        } catch (Exception e) {
            logger.error("Failed to stop Web Server", e);
        }
    }

    public void jettyRestart() {
        // Stop Jetty Server after a delay
        new BukkitRunnable() {
            @Override
            public void run() {
                jettyStop();
                // Start Jetty Server after another delay
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        jettyStart();
                    }
                }.runTaskLater(plugin, DELAY_SECONDS * 20); // 20 ticks per second
            }
        }.runTaskLater(plugin, DELAY_SECONDS * 20); // 20 ticks per second
    }
}
