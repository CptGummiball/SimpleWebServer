package org.cptgum.simplewebserver;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.cptgum.simplewebserver.WebServerManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public final class main extends JavaPlugin {

    private WebServerManager webServerManager;
    private static final Logger logger = LoggerFactory.getLogger("SimpleWebServer");

    @Override
    public void onEnable() {
        // Plugin startup logic
        WebServerManager webServerManager = new WebServerManager(this);
        loadConfig();
        copyIndexHtml();
        registerCommands();
        webServerManager.jettyStart();
        logger.info("Plugin enabled!");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (webServerManager != null) {
            webServerManager.jettyStop();
        }
        logger.info("Plugin disabled!");
    }

    private void loadConfig() {
        // Load config
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    private class SimpleWebServerCommands implements CommandExecutor, TabCompleter {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to use this command!");
                return true;
            }

            Player player = (Player) sender;

            // Check the main permission
            if (!player.hasPermission("sws.use")) {
                player.sendMessage("You do not have permission to use this command!");
                return true;
            }

            if (args.length == 0) {
                player.sendMessage("/sws <start|stop|restart|link>");
                return true;
            }

            // Check the first argument (suffix) and call the corresponding method
            switch (args[0].toLowerCase()) {
                case "stop":
                    if (player.hasPermission("sws.admin")){
                        webServerManager.jettyStop();

                    } else {
                        player.sendMessage("You do not have permission to use this command!");
                    }
                    break;
                case "start":
                    if (player.hasPermission("sws.admin")){
                        webServerManager.jettyStart();

                    } else {
                        player.sendMessage("You do not have permission to use this command!");
                    }
                    break;
                case "restart":
                    if (player.hasPermission("sws.admin")){
                        webServerManager.jettyRestart();

                    } else {
                        player.sendMessage("You do not have permission to use this command!");
                    }
                    break;
                case "link":
                    if (player.hasPermission("sws.link")) {
                        String serverIP = Bukkit.getServer().getIp();
                        int webServerPort = getConfig().getInt("web-server-port");

                        String url = "http://" + serverIP + ":" + webServerPort;

                        String clickMessage = "Click here to open the web server.";

                        TextComponent clickableUrl = new TextComponent(clickMessage);
                        clickableUrl.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));

                        player.spigot().sendMessage(clickableUrl);
                    } else {
                        player.sendMessage("You do not have permission to use this command!");
                    }
                    break;
                default:
                    player.sendMessage("Invalid argument. Use /sws <start|stop|restart|link>");
                    break;
            }

            return true;
        }
        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, String[] args) {
            List<String> completions = new ArrayList<>();

            if (args.length == 1) {
                // Provide tab completion for the first argument
                String partialCommand = args[0].toLowerCase();

                // Add your command names here
                List<String> commandNames = List.of("start", "stop", "restart", "link");

                for (String command : commandNames) {
                    if (command.startsWith(partialCommand)) {
                        completions.add(command);
                    }
                }
            }

            // You can add more tab completion logic for additional arguments if needed

            return completions;
        }
    }


    private void registerCommands() {
        getCommand("sws").setExecutor(new SimpleWebServerCommands());
    }

    private void copyIndexHtml() {
        Path pluginFolder = getDataFolder().toPath();
        Path indexPath = pluginFolder.resolve("web/index.html");

        if (!Files.exists(indexPath)) {
            // The file does not exist, so copy it from resources
            try {
                Files.createDirectories(indexPath.getParent());
                Files.copy(getResource("web/index.html"), indexPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                logger.error("Error copying index.html: " + e.getMessage());
            }
        }
    }
}
