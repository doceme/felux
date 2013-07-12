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
import java.util.Timer;
import java.util.TimerTask;

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

    private final byte CMD_DMX_SET_VALUE = (byte)0x80;
    private final byte CMD_DMX_SET_GROUP = (byte)0x81;
    private final byte CMD_DMX_SET_RANGE = (byte)0x82;
    private final byte CMD_DMX_FADE_SET_VALUE = (byte)0x90;
    private final byte CMD_DMX_FADE_SET_GROUP = (byte)0x91;
    private final byte CMD_DMX_FADE_SET_RANGE = (byte)0x92;
    private final byte CMD_DMX_FADE_START = (byte)0x9A;
    private final byte CMD_DMX_FADE_STOP = (byte)0x9B;
    private final byte CMD_DMX_SET_BLACKOUT = (byte)0xB0;
    private final byte CMD_MIDI_NOTE_OFF = (byte)0xA0;
    private final byte CMD_MIDI_NOTE_ON = (byte)0xA1;
    private final byte CMD_HOUSE_LIGHTS = (byte)0xC0;

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

    public Light getBaseLight(Light light) {
        int index = lights.indexOf(light);

        if (index >= 0) {
            return lights.get(index);
        }

        return null;
    }

    public void showLight(DmxLight light, boolean fade) {
        if (light instanceof DmxColorLight) {
            showColorLight((DmxColorLight)light, fade);
        } else if (light instanceof DmxGroupLight) {
            showGroupLight((DmxGroupLight)light, fade);
        } else {
            showBasicLight(light, fade);
        }
    }

    public void showLight(DmxLight light) {
        showLight(light, false);
    }

    public void showLight(DmxLight light, int value) {
        DmxLight baseLight = (DmxLight)getBaseLight(light);
        if (baseLight != null) {
            baseLight.setValue(value);
            showLight(baseLight);
        }
    }

    public void showBasicLight(DmxLight light, boolean fade) {
        if (feluxWriter != null) {
            int address = light.getAddress();
            byte[] data = {
                    fade ? CMD_DMX_FADE_SET_VALUE : CMD_DMX_SET_VALUE,
                    (byte)light.getUniverse(),
                    (byte)(address << 16), (byte)(address & 0xff),
                    (byte)light.getValue()
            };
            try {
                feluxWriter.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showBasicLight(DmxLight light) {
        showBasicLight(light, false);
    }

    public void showBasicLight(DmxLight light, int value) {
        DmxLight baseLight = (DmxLight)getBaseLight(light);
        if (baseLight != null) {
            baseLight.setValue(value);
            showBasicLight(baseLight);
        }
    }

    public void showGroupLight(DmxGroupLight light, boolean fade) {
        if (feluxWriter != null) {
            int startAddress = light.getStartAddress();
            int endAddress = light.getEndAddress();
            byte[] data = {
                    fade ? CMD_DMX_FADE_SET_GROUP : CMD_DMX_SET_GROUP,
                    (byte)light.getUniverse(),
                    (byte)(startAddress << 16), (byte)(startAddress & 0xff),
                    (byte)(endAddress - startAddress + 1),
                    (byte)(light.getValue())
            };
            try {
                feluxWriter.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showGroupLight(DmxGroupLight light) {
        showGroupLight(light, false);
    }

    public void showGroupLight(DmxGroupLight light, int value) {
        DmxGroupLight baseLight = (DmxGroupLight)getBaseLight(light);
        if (baseLight != null) {
            baseLight.setValue(value);
            showGroupLight(baseLight);
        }
    }

    public void showColorLight(DmxColorLight light, boolean fade) {
        if (feluxWriter != null) {
            int address = light.getAddress();
            int color = light.getColor();
            byte[] data = {
                    fade ? CMD_DMX_FADE_SET_RANGE : CMD_DMX_SET_RANGE,
                    (byte)light.getUniverse(),
                    (byte)(address >> 16), (byte)(address & 0xff),
                    (byte)4,
                    (byte) Color.red(color),
                    (byte) Color.green(color),
                    (byte) Color.blue(color),
                    (byte) 0xff,
            };
            try {
                feluxWriter.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startFade(int fadeDuration) {
        if (feluxWriter != null) {
            byte[] data = {
                    CMD_DMX_FADE_START,
                    (byte)(fadeDuration >> 24),
                    (byte)(fadeDuration >> 16),
                    (byte)(fadeDuration >> 8),
                    (byte)(fadeDuration),
            };
            try {
                feluxWriter.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopFade() {
        if (feluxWriter != null) {
            byte[] data = {
                    CMD_DMX_FADE_STOP,
            };
            try {
                feluxWriter.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showColorLight(DmxColorLight light) {
        showColorLight(light, false);
    }

    public void showColorLight(DmxColorLight light, int color) {
        DmxColorLight baseLight = (DmxColorLight)getBaseLight(light);
        if (baseLight != null) {
            baseLight.setColor(color);
            showColorLight(baseLight);
        }
    }

    public void showLightScene(LightScene scene) {
        if (feluxWriter != null) {
            boolean shouldFade = (scene.getFade() >= 0.002);
            for (Light light: scene.getLights()) {
                if (light instanceof DmxColorLight) {
                    showColorLight((DmxColorLight)light, shouldFade);
                } else if (light instanceof DmxGroupLight) {
                    showGroupLight((DmxGroupLight)light, shouldFade);
                } else if (light instanceof DmxLight) {
                    showLight((DmxLight)light, shouldFade);
                }
            }

            if (shouldFade) {
                startFade((int)(scene.getFade() * 1000));
            }
        }
    }

    /*
    public void fadeColorLight(DmxColorLight light, int color) {
        DmxColorLight baseLight = (DmxColorLight)getBaseLight(light);
        if (baseLight != null) {
            if (baseLight.getColor() != color) {
                //baseLight.setColor(color);
                //showColorLight(baseLight);
            }
        }
    }
    */

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

    private class LightSceneFadeTimerTask extends TimerTask {
        private int count = 0;
        private int fade;
        private List<Light> lights;
        private List<Light> preFadelights;

        private LightSceneFadeTimerTask(FeluxManager manager, LightScene scene) {
            this.lights = scene.getLights();

            /* Get fade in milliseconds */
            fade = (int)(scene.getFade() * 1000);

            int size = lights.size();
            preFadelights = new ArrayList<Light>(size);
            for (int i = 0; i < lights.size(); i++) {
                preFadelights.add((Light)lights.get(i).copy());
            }
        }

        @Override
        public void run() {
            for (int i = 0; i < lights.size(); i++) {
                Light oldLight = preFadelights.get(i);
                Light newLight = lights.get(i);

                if (oldLight instanceof DmxColorLight && newLight instanceof DmxColorLight) {
                    DmxColorLight oldDmxColorLight = (DmxColorLight)oldLight;
                    DmxColorLight newDmxColorLight = (DmxColorLight)newLight;
                    int oldRed = Color.red(oldDmxColorLight.getColor());
                    int oldGreen = Color.green(oldDmxColorLight.getColor());
                    int oldBlue = Color.blue(oldDmxColorLight.getColor());

                    int newRed = Color.red(newDmxColorLight.getColor());
                    int newGreen = Color.green(newDmxColorLight.getColor());
                    int newBlue = Color.blue(newDmxColorLight.getColor());

                    float scaleFactor = (float)count / (float)fade;

                    int red = oldRed + (int)((float)(newRed - oldRed) * scaleFactor);
                    int green = oldGreen + (int)((float)(newGreen - oldGreen) * scaleFactor);
                    int blue = oldBlue + (int)((float)(newBlue - oldBlue) * scaleFactor);

                    int color = Color.rgb(red, green, blue);

                    showColorLight(newDmxColorLight, color);
                } else if (oldLight instanceof DmxGroupLight && newLight instanceof DmxGroupLight) {
                    DmxGroupLight oldDmxGroupLight = (DmxGroupLight)oldLight;
                    DmxGroupLight newDmxGroupLight = (DmxGroupLight)newLight;
                    int oldValue = oldDmxGroupLight.getValue();
                    int newValue = newDmxGroupLight.getValue();

                    float scaleFactor = (float)count / (float)fade;

                    int value = oldValue + (int)((float)(newValue - oldValue) * scaleFactor);

                    showGroupLight(newDmxGroupLight, value);
                } else if (oldLight instanceof DmxGroupLight && newLight instanceof DmxGroupLight) {
                    DmxLight oldDmxLight = (DmxLight)oldLight;
                    DmxLight newDmxLight = (DmxLight)newLight;
                    int oldValue = oldDmxLight.getValue();
                    int newValue = newDmxLight.getValue();

                    float scaleFactor = (float)count / (float)fade;

                    int value = oldValue + (int)((float)(newValue - oldValue) * scaleFactor);

                    showLight(newDmxLight, value);
                }
            }

            if (++count >= fade) {
                cancel();
            }
        }
    }
}
