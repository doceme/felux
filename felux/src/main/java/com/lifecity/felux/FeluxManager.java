package com.lifecity.felux;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lifecity.felux.cues.Cue;
import com.lifecity.felux.items.Item;
import com.lifecity.felux.items.ItemAdapter;
import com.lifecity.felux.lights.*;
import com.lifecity.felux.scenes.LightScene;
import com.lifecity.felux.scenes.MidiScene;
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
    private List<Cue> cues = new ArrayList<Cue>();
    private Gson gson;
    private static final String TAG = "FeluxManager";
    private static final String PREF_LIGHTS = "lights";
    private static final String PREF_SCENES = "scenes";
    private static final String PREF_CUES = "cues";
    private static final Type lightListType = new TypeToken<List<Light>>() {}.getType();
    private static final Type sceneListType = new TypeToken<List<Scene>>() {}.getType();
    private static final Type cueListType = new TypeToken<List<Cue>>() {}.getType();
    private SimpleHdlcOutputStreamWriter feluxWriter;

    private final byte CMD_DMX_SET_VALUE = (byte)0x91;
    private final byte CMD_DMX_SET_GROUP = (byte)0x92;
    private final byte CMD_DMX_SET_RANGE = (byte)0x93;
    private final byte CMD_DMX_SET_BLACKOUT = (byte)0x99;
    private final byte CMD_MIDI_NOTE_ON = (byte)0xA1;
    private final byte CMD_MIDI_NOTE_OFF = (byte)0xA2;
    private final byte CMD_HOUSE_LIGHTS = (byte)0xB0;

    public FeluxManager(SharedPreferences preferences) {
        if (preferences == null) {
            throw new NullPointerException("Preferences must not be null");
        }

        this.preferences = preferences;

        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Light.class, new ItemAdapter<Light>());
        gsonBuilder.registerTypeAdapter(Scene.class, new ItemAdapter<Scene>());
        //gsonBuilder.registerTypeAdapter(Cue.class, new ItemAdapter<Cue>());
        gson = gsonBuilder.create();


        //preferences.edit().clear().commit();
    }

    public void open() {
        loadLights();
        loadScenes();
        loadCues();
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
        saveCues();
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

    public void loadCues() {
        String cuesJson = preferences.getString(PREF_CUES, null);

        if (cuesJson == null) {
            cues.clear();
        } else {
            cues = gson.fromJson(cuesJson, cueListType);
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

    public void saveCues() {
        SharedPreferences.Editor editor = preferences.edit();
        String cuesJson = gson.toJson(cues, cueListType);
        //Log.d(TAG, "Saving cues: " + cuesJson);
        editor.putString(PREF_CUES, cuesJson);
        editor.commit();
    }

    public List<Light> getLights() {
        return lights;
    }

    public List<Scene> getScenes() {
        return scenes;
    }

    public List<Cue> getCues() {
        return cues;
    }

    public void showLight(int universe, int address, int value) {
        if (feluxWriter != null) {
            byte[] data = {CMD_DMX_SET_VALUE,
                    (byte)universe,
                    (byte)(address << 16), (byte)(address & 0xff),
                    (byte)value};
            try {
                feluxWriter.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showLight(DmxLight light) {
        showLight(light.getUniverse(), light.getAddress(), light.getValue());
    }

    public void showGroupLight(int universe, int startAddress, int endAddress, int value) {
        if (feluxWriter != null) {
            byte[] data = {CMD_DMX_SET_GROUP,
                    (byte)universe,
                    (byte)(startAddress << 16), (byte)(startAddress & 0xff),
                    (byte)(endAddress - startAddress + 1),
                    (byte)(value)};
            try {
                feluxWriter.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showGroupLight(DmxGroupLight light) {
        showGroupLight(light.getUniverse(), light.getStartAddress(), light.getEndAddress(), light.getValue());
    }

    public void showColorLight(int universe, int address, int color) {
        if (feluxWriter != null) {
            byte[] data = {CMD_DMX_SET_RANGE,
                    (byte)universe,
                    (byte)(address << 16), (byte)(address & 0xff),
                    (byte)4,
                    (byte) Color.red(color),
                    (byte) Color.green(color),
                    (byte) Color.blue(color),
                    (byte) 0xff};
            try {
                feluxWriter.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showColorLight(DmxColorLight light) {
        showColorLight(light.getUniverse(), light.getAddress(), light.getColor());
    }

    public void showLightScene(LightScene scene) {
        if (feluxWriter != null) {
            for (Light light: scene.getLights()) {
                if (light instanceof DmxColorLight) {
                    showColorLight((DmxColorLight)light);
                } else if (light instanceof DmxGroupLight) {
                    showGroupLight((DmxGroupLight)light);
                } else if (light instanceof DmxLight) {
                    showLight((DmxLight)light);
                }
            }
        }
    }

    public void showMidiScene(int channel, int note, int velocity, boolean isEventOn) {
        if (feluxWriter != null) {
            byte cmd = isEventOn ? CMD_MIDI_NOTE_ON : CMD_MIDI_NOTE_OFF;
            byte[] data = {cmd,
                    (byte)channel,
                    (byte)note,
                    (byte)velocity};
            try {
                feluxWriter.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showMidiScene(MidiScene scene) {
        showMidiScene(scene.getChannel(), scene.getNote(), scene.getVelocity(), scene.getEventOn());
    }
}
