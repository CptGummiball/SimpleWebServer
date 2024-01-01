# SimpleWebServer
A simple internal web server for Spigot servers

## Description
SimpleWebServer is a Spigot plugin that provides a basic web server functionality for Minecraft servers. It allows server administrators to start, stop, restart the embedded Jetty web server and provides a command to display the link to the web server.

## Usage
### Commands
- `/sws start`: Starts the web server.
- `/sws stop`: Stops the web server.
- `/sws restart`: Restarts the web server.
- `/sws link`: Displays the link to access the web server.

### Permissions
- `sws.use`: Allows general access to the /sws command.
- `sws.admin`: Access to administrative commands (start, stop, restart).
- `sws.link`: Show the link to the web server.

## Configuration
In the config.yml file, you can specify the port for the web server.
````
# Port of the webserver
web-server-port: 8080
````

## Installation
1. Download the latest release from the [releases page](https://github.com/CptGummiball/SimpleWebServer/releases).
2. Place the JAR file in the plugins folder of your Spigot server.
3. Start or restart your server.
4. Place your webfiles in `plugins/SimpleWebServer/web`

## Note
- Ensure that the specified web server port is open and not conflicting with other services.
- Access the web server link using the /sws link command in-game.

## Credits
Author: CptGummiball

## License
This plugin is licensed under the MIT License - see the [LICENSE](https://github.com/CptGummiball/SimpleWebServer/blob/main/LICENSE) file for details.
