package net.oldschoolminecraft.lmk.cmd;

import net.oldschoolminecraft.lmk.LandmarkData;
import net.oldschoolminecraft.lmk.Landmarks;
import net.oldschoolminecraft.lmk.ex.DuplicateException;
import net.oldschoolminecraft.lmk.ex.NotFoundException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Admin implements CommandExecutor
{
    private Landmarks plugin;

    public Admin(Landmarks plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender.hasPermission("landmarks.admin")))
        {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        if (args.length < 2)
        {
            sender.sendMessage(ChatColor.RED + "Usage: /lmkadmin <create/delete> <name>");
            return true;
        }

        String subcommand = args[0];
        String name = args[1];

        switch (subcommand.toLowerCase())
        {
            default:
                sender.sendMessage(ChatColor.RED + "Unknown sub-command: " + ChatColor.GRAY + subcommand);
                break;
            case "create":
                if (!(sender instanceof Player))
                {
                    sender.sendMessage(ChatColor.RED + "Only players can create landmarks!");
                    return true;
                }

                Player ply = (Player) sender;
                Location plyLoc = ply.getLocation();

                LandmarkData newLmk = new LandmarkData();
                newLmk.name = name;
                newLmk.worldName = ply.getWorld().getName();
                newLmk.x = plyLoc.getX();
                newLmk.y = plyLoc.getY();
                newLmk.z = plyLoc.getZ();
                newLmk.yaw = plyLoc.getYaw();
                newLmk.pitch = plyLoc.getPitch();
                try
                {
                    plugin.getLmkManager().addLandmark(newLmk);
                    sender.sendMessage(ChatColor.GREEN + "Successfully created new landmark '" + name + "'!");
                } catch (DuplicateException e) {
                    sender.sendMessage(ChatColor.RED + "A landmark with that name already exists!");
                    return true;
                }
                break;
            case "delete":
                try
                {
                    plugin.getLmkManager().removeLandmark(name);
                    sender.sendMessage(ChatColor.GREEN + "Successfully removed landmark '" + name + "'!");
                } catch (NotFoundException e) {
                    sender.sendMessage(ChatColor.RED + "No landmark called '" + ChatColor.DARK_GRAY + name + ChatColor.RED + "' found!");
                    return true;
                }
                break;
            case "move":
                if (!(sender instanceof Player))
                {
                    sender.sendMessage(ChatColor.RED + "Only players can move landmarks!");
                    return true;
                }

                try
                {
                    plugin.getLmkManager().moveCurrentLandmark(name, ply);
                    sender.sendMessage(ChatColor.GREEN + "Successfully moved landmark '" + name + "'!");
                } catch (NotFoundException e) {
                    sender.sendMessage(ChatColor.RED + "No landmark called '" + ChatColor.DARK_GRAY + name + ChatColor.RED + "' found!");
                    return true;
                }
                break;
        }

        return true;
    }
}
