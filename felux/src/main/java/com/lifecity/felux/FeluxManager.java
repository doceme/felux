package com.lifecity.felux;

import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lifecity.felux.items.ItemAdapter;
import com.lifecity.felux.lights.*;
import com.lifecity.felux.scenes.Scene;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by scaudle on 6/7/13.
 */
public class FeluxManager {
    private SharedPreferences preferences;
    private List<Light> lights;
    private List<Scene> scenes;
    private Gson gson;
    static final private String PREF_LIGHTS = "lights";
    static final private String PREF_SCENES = "scenes";
    static final private Type lightListType = new TypeToken<List<Light>>() {}.getType();
    static final private Type sceneListType = new TypeToken<List<Scene>>() {}.getType();

    public FeluxManager(SharedPreferences preferences) {
        if (preferences == null) {
            throw new NullPointerException("Preferences must not be null");
        }

        this.preferences = preferences;

        String lightsJson = preferences.getString(PREF_LIGHTS, null);
        String scenesJson = preferences.getString(PREF_SCENES, null);
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Light.class, new ItemAdapter<Light>());
        gsonBuilder.registerTypeAdapter(Scene.class, new ItemAdapter<Scene>());
        gson = gsonBuilder.create();

        //preferences.edit().clear().commit();

        loadLights();
        loadScenes();
    }

    public void loadLights() {
        String lightsJson = preferences.getString(PREF_LIGHTS, null);

        if (lightsJson == null) {
            lights = new ArrayList<Light>();
        } else {
            lights = gson.fromJson(lightsJson, lightListType);
        }
    }

    public void loadScenes() {
        String scenesJson = preferences.getString(PREF_SCENES, null);

        if (scenesJson == null) {
            scenes = new ArrayList<Scene>();
        } else {
            scenes = gson.fromJson(scenesJson, sceneListType);
        }
    }

    public void saveLights() {
        SharedPreferences.Editor editor = preferences.edit();
        String lightsJson = gson.toJson(lights, lightListType);
        editor.putString(PREF_LIGHTS, lightsJson);
        editor.commit();
    }

    public void saveScenes() {
        SharedPreferences.Editor editor = preferences.edit();
        String scenesJson = gson.toJson(scenes, sceneListType);
        editor.putString(PREF_SCENES, scenesJson);
        editor.commit();
    }

    public List<Light> getLights() {
        return lights;
    }

    public List<Scene> getScenes() {
        return scenes;
    }
}
