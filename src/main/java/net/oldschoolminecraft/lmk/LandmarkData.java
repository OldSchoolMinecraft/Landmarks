package net.oldschoolminecraft.lmk;

import java.util.ArrayList;

public class LandmarkData
{
    public String name;
    public String worldName;
    public double x, y, z;
    public float yaw, pitch;
    public ArrayList<String> visitors;

    public void registerVisit(String name)
    {
        if (visitors == null)
            visitors = new ArrayList<>();
        if (visitors.contains(name.toLowerCase())) return; // no duplicate visits
        visitors.add(name.toLowerCase());
    }
}
