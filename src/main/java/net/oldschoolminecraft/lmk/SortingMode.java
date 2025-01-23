package net.oldschoolminecraft.lmk;

import org.bukkit.ChatColor;

public enum SortingMode
{
    DEFAULT(ChatColor.DARK_GRAY + "Unordered"),
    ALPHABETICAL(ChatColor.GRAY + "Sort by: " + ChatColor.DARK_GRAY + "A-Z"),
    VISITS(ChatColor.GRAY + "Sort by: " + ChatColor.DARK_GRAY + "Most visits");

    SortingMode()
    {
        msg = ChatColor.GRAY + "Sorting: " + ChatColor.DARK_GRAY + this;
    }

    SortingMode(String msg)
    {
        this.msg = msg;
    }

    private final String msg;

    public String getMessage()
    {
        return msg;
    }
}
