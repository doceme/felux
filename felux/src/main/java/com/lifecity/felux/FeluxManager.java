package com.lifecity.felux;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lifecity.felux.items.Item;
import com.lifecity.felux.items.ItemAdapter;
import com.lifecity.felux.lights.*;
import com.lifecity.felux.scenes.Scene;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by scaudle on 6/7/13.
 */
public class FeluxManager {
    private SharedPreferences preferences;
    private List<Light> lights = new ArrayList<Light>();
    private List<Scene> scenes = new ArrayList<Scene>();
    private Gson gson;
    private static final String TAG = "FeluxManager";
    private static final String PREF_LIGHTS = "lights";
    private static final String PREF_SCENES = "scenes";
    private static final Type lightListType = new TypeToken<List<Light>>() {}.getType();
    private static final Type sceneListType = new TypeToken<List<Scene>>() {}.getType();
    private SimpleHdlcOutputStreamWriter feluxWriter;

    public FeluxManager(SharedPreferences preferences) {
        if (preferences == null) {
            throw new NullPointerException("Preferences must not be null");
        }

        this.preferences = preferences;

        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Light.class, new ItemAdapter<Light>());
        gsonBuilder.registerTypeAdapter(Scene.class, new ItemAdapter<Scene>());
        gson = gsonBuilder.create();


        //preferences.edit().clear().commit();
    }

    public void open() {
        loadLights();
        loadScenes();
    }

    public void setFeluxWriter(SimpleHdlcOutputStreamWriter feluxWriter) {
        this.feluxWriter = feluxWriter;
    }

    public void close() throws IOException {
        if (feluxWriter != null) {
            feluxWriter.close();
        }
        saveLights();
        saveScenes();
    }

    public void loadLights() {
        String lightsJson = preferences.getString(PREF_LIGHTS, null);

        if (lightsJson == null) {
            lights.clear();
        } else {
            lights = gson.fromJson(lightsJson, lightListType);
        }
    }

    public void loadScenes() {
        String scenesJson = preferences.getString(PREF_SCENES, null);

        if (scenesJson == null) {
            scenes.clear();
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
        //Log.d(TAG, "Saving scenes: " + scenesJson);
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
