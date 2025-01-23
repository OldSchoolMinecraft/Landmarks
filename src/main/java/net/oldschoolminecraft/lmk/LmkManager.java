package net.oldschoolminecraft.lmk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.oldschoolminecraft.lmk.ex.DuplicateException;
import net.oldschoolminecraft.lmk.ex.NotFoundException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class LmkManager
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_PATH = "landmarks.v2.json";
    private ArrayList<LandmarkData> landmarks;

    public LmkManager()
    {
        landmarks = new ArrayList<>();
    }

    public void reload()
    {
        File file = new File(FILE_PATH);
        if (!file.exists())
        {
            System.out.println("No landmarks file found. Creating a new one on save.");
            landmarks = new ArrayList<>();
            return;
        }

        try (FileReader reader = new FileReader(file))
        {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<LandmarkData>>() {}.getType();
            landmarks = gson.fromJson(reader, listType);

            if (landmarks == null)
                landmarks = new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Failed to reload landmarks: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public void save()
    {
        try (FileWriter writer = new FileWriter(FILE_PATH))
        {
            gson.toJson(landmarks, writer);
        } catch (IOException e) {
            System.err.println("Failed to save landmarks: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public void addLandmark(LandmarkData landmark) throws DuplicateException
    {
        if (findLandmark(landmark.name) != null) throw new DuplicateException();
        landmarks.add(landmark);
        save();
    }

    public void removeLandmark(String name) throws NotFoundException
    {
        if (findLandmark(name) == null) throw new NotFoundException();
        landmarks.removeIf(lmk -> lmk.name.equalsIgnoreCase(name));
        save();
    }

    public ArrayList<LandmarkData> getLandmarks()
    {
        return landmarks;
    }

    public LandmarkData findLandmark(String name)
    {
        for (LandmarkData lmk : landmarks)
        {
            if (lmk.name.equalsIgnoreCase(name))
                return lmk;
        }
        return null;
    }
}
