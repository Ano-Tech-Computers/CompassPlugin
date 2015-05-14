package no.atc.floyd.bukkit.compass;


import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;


import java.util.logging.Logger;


/**
* Simple compass plugin for Bukkit
*
* @author FloydATC
*/
public class CompassPlugin extends JavaPlugin implements Listener {
//    public static Permissions Permissions = null;
//    private final ContainerPlayerListener playerListener = new ContainerPlayerListener(this);
//    private final ContainerBlockListener blockListener = new ContainerBlockListener(this);

    
    String baseDir = "plugins/CompassPlugin";

    private ConcurrentHashMap<String, String> locations = new ConcurrentHashMap<String, String>(); 
    private ConcurrentHashMap<String, String> modes = new ConcurrentHashMap<String, String>(); 
	public static final Logger logger = Logger.getLogger("Minecraft.CompassPlugin");
    
    public void onDisable() {
        // TODO: Place any custom disable code here
    	modes.clear();
    	locations.clear();

        // NOTE: All registered events are automatically unregistered when a plugin is disabled
    	
        // EXAMPLE: Custom code, here we just output some info so we can check all is well
    	PluginDescriptionFile pdfFile = this.getDescription();
		logger.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!" );
    }

    public void onEnable() {
    	
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
		logger.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }

    @EventHandler
    public boolean onLogin(PlayerLoginEvent event ) {
    	event.getPlayer().setCompassTarget( event.getPlayer().getLocation().add(-100,0,0) );
    	return true;
    }

    @EventHandler
    public boolean onMove(PlayerMoveEvent event ) {
    	event.getPlayer().setCompassTarget( event.getPlayer().getLocation().add(-100,0,0) );
    	return true;
    }

    @EventHandler
    public boolean onTeleport(PlayerTeleportEvent event ) {
    	event.getPlayer().setCompassTarget( event.getPlayer().getLocation().add(-100,0,0) );
    	return true;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args ) {
    	String cmdname = cmd.getName().toLowerCase();
        Player player = null;
        if (sender instanceof Player) {
        	player = (Player)sender;
        }
        if (player == null) {
        	logger.info("[Compass] You are facing the console.");
        	return true;
        }
        
        if (cmdname.equals("compass")) {
        	if (args.length == 0) {
        		showPlayerHeading(player);
        	} else {
        		setPlayerHeading(player, args[0]);
        	}
    		return true;
        }
        
        return false;
    }


    private void showPlayerHeading(Player player) {
    	Location loc = player.getLocation();
    	Float angle = loc.getYaw();
    	while (angle < 0) { angle = angle + 360; }
    	Float sector = (angle + 22.5f) / 45;
    	String heading = "(Unknown)";
    	if (sector < 1 || sector >= 8) {
    		heading = "west";
    	} else if ( sector < 2) {
    		heading = "northwest";
    	} else if ( sector < 3) {
    		heading = "north";
    	} else if ( sector < 4) {
    		heading = "northeast";
    	} else if ( sector < 5) {
    		heading = "east";
    	} else if ( sector < 6) {
    		heading = "southeast";
    	} else if ( sector < 7) {
    		heading = "south";
    	} else if ( sector < 8) {
    		heading = "southwest";
    	}
   		player.sendMessage("§7[§6Compass§7] §7You are now facing §6" + heading + "§7 in world '" + loc.getWorld().getName() + "'");
    }
    
    private void setPlayerHeading(Player player, String heading) {
    	Location loc = player.getLocation();
    	if (heading.equalsIgnoreCase("W") || heading.equalsIgnoreCase("west")) {
    		loc.setYaw(0);
    	} else if (heading.equalsIgnoreCase("NW") || heading.equalsIgnoreCase("northwest")) {
    		loc.setYaw(45);
    	} else if (heading.equalsIgnoreCase("N") || heading.equalsIgnoreCase("north")) {
    		loc.setYaw(90);
    	} else if (heading.equalsIgnoreCase("NE") || heading.equalsIgnoreCase("northeast")) {
    		loc.setYaw(135);
    	} else if (heading.equalsIgnoreCase("E") || heading.equalsIgnoreCase("east")) {
    		loc.setYaw(180);
    	} else if (heading.equalsIgnoreCase("SE") || heading.equalsIgnoreCase("southeast")) {
    		loc.setYaw(225);
    	} else if (heading.equalsIgnoreCase("S") || heading.equalsIgnoreCase("south")) {
    		loc.setYaw(270);
    	} else if (heading.equalsIgnoreCase("SW") || heading.equalsIgnoreCase("southwest")) {
    		loc.setYaw(315);
    	}
    	player.teleport(loc);
    	showPlayerHeading(player);
    }
    

}

