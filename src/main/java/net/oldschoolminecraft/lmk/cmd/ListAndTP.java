package net.oldschoolminecraft.lmk.cmd;

import net.oldschoolminecraft.lmk.LandmarkData;
import net.oldschoolminecraft.lmk.Landmarks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListAndTP implements CommandExecutor
{
    private Landmarks plugin;
    private static final int LIST_OFFSET = 10;
    private static int MAX_PAGES;

    public ListAndTP(Landmarks plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender.hasPermission("landmarks.listandtp")))
        {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        int page = 1;
        if (args.length >= 1)
        {
            if (isNumber(args[0]))
            {
                // page selection
                page = Integer.parseInt(args[0]);
            } else {
                // teleporting
                if (!(sender instanceof Player))
                {
                    sender.sendMessage(ChatColor.RED + "Only players can teleport to landmarks!");
                    return true;
                }

                String lmkName = args[0];
                LandmarkData lmk = plugin.getLmkManager().findLandmark(lmkName);
                if (lmk == null)
                {
                    sender.sendMessage(ChatColor.GRAY + "There is no landmark called '" + ChatColor.DARK_GRAY + lmkName + "'" + ChatColor.GRAY + "!");
                    return true;
                }
                ((Player)sender).teleport(new Location(Bukkit.getWorld(lmk.worldName), lmk.x, lmk.y, lmk.z, lmk.yaw, lmk.pitch));
                lmk.registerVisit(((Player)sender).getName());
                asyncSave(); // make sure the visit is saved
                sender.sendMessage(ChatColor.GRAY + "You have been teleported to '" + ChatColor.DARK_GRAY + lmkName + "'" + ChatColor.GRAY + "!");
                return true;
            }
        }

        List<LandmarkData> lmkPage = getLandmarkPage(page);
        if (lmkPage == null)
        {
            sender.sendMessage(ChatColor.GRAY + "The page " + ChatColor.DARK_GRAY + "#" + page + ChatColor.GRAY + " doesn't exist!");
            return true;
        }

        sender.sendMessage(ChatColor.GRAY + "-- Landmarks (" + ChatColor.DARK_GRAY + "Page " + page + "/" + MAX_PAGES + ChatColor.GRAY + ") --");
        int pageStartNum = ((page - 1) * LIST_OFFSET) + 1;
        int index = 0;
        for (LandmarkData landmark : lmkPage)
        {
            String pageStr = (pageStartNum + index) + ". ";
            sender.sendMessage(ChatColor.DARK_GRAY + pageStr + ChatColor.GRAY + landmark.name);
            index++;
        }
        sender.sendMessage(ChatColor.GRAY + "-- Use " + ChatColor.DARK_GRAY + "/lmk #" + ChatColor.GRAY + " to show other pages --");

        return true;
    }

    private void asyncSave()
    {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, () -> plugin.getLmkManager().save(), 0L);
    }

    private boolean isNumber(String in)
    {
        try
        {
            Integer.parseInt(in);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private List<LandmarkData> getLandmarkPage(int page)
    {
        List<List<LandmarkData>> lmkPages = paginateLandmarks(plugin.getLmkManager().getLandmarks());
        MAX_PAGES = lmkPages.size();
        if (page < 1 || (page - 1) >= lmkPages.size())
            return null;
        return lmkPages.get(page - 1);
    }

    private List<List<LandmarkData>> paginateLandmarks(List<LandmarkData> landmarks) {
        List<List<LandmarkData>> chunks = new ArrayList<>();
        for (int i = 0; i < landmarks.size(); i += LIST_OFFSET)
        {
            chunks.add(landmarks.subList(i, Math.min(i + LIST_OFFSET, landmarks.size())));
        }
        return chunks;
    }
}
