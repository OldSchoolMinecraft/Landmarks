package net.oldschoolminecraft.lmk.cmd;

import net.oldschoolminecraft.lmk.Landmarks;
import net.oldschoolminecraft.lmk.PlayerConf;
import net.oldschoolminecraft.lmk.SortingMode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChangeSortMode implements CommandExecutor
{
    private Landmarks plugin;

    public ChangeSortMode(Landmarks plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
        {
            sender.sendMessage(ChatColor.RED + "Usage: /lmksort <default,alphabetical,visits>");
            return true;
        }

        try
        {
            SortingMode mode = SortingMode.valueOf(args[0].toUpperCase());
            PlayerConf playerConf = plugin.getPlayerConfig(sender.getName());
            playerConf.setProperty("sorting.mode", mode.toString());
            playerConf.save();
            sender.sendMessage(ChatColor.GREEN + "Successfully updated sorting mode!");
        } catch (IllegalArgumentException ex) {
            sender.sendMessage(ChatColor.RED + "Invalid sorting mode! Use default, alphabetical, or visits!");
            return true;
        }

        return true;
    }
}
