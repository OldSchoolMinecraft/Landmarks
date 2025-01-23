package net.oldschoolminecraft.lmk;

import net.oldschoolminecraft.lmk.cmd.Admin;
import net.oldschoolminecraft.lmk.cmd.ListAndTP;
import org.bukkit.plugin.java.JavaPlugin;

public class Landmarks extends JavaPlugin
{
    private LmkManager lmkManager;

    public void onEnable()
    {
        lmkManager = new LmkManager();
        lmkManager.reload();

        getCommand("landmarks").setExecutor(new ListAndTP(this));
        getCommand("lmkadmin").setExecutor(new Admin(this));

        System.out.println("Landmarks enabled");
    }

    public void onDisable()
    {
        System.out.println("Landmarks disabled");
    }

    public LmkManager getLmkManager()
    {
        return lmkManager;
    }
}
