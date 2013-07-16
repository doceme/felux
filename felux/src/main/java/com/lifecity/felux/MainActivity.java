package com.lifecity.felux;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.*;
import android.util.Log;
import android.view.WindowManager;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import com.lifecity.felux.cues.Cue;
import com.lifecity.felux.items.Item;
import com.lifecity.felux.lights.*;
import com.lifecity.felux.scenes.LightScene;
import com.lifecity.felux.scenes.MidiScene;
import com.lifecity.felux.scenes.Scene;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


/**
 * MainActivity class
 */
public class MainActivity extends FragmentActivity implements ItemListCallbacks<Item> {
    /*
    public static final String ACTIVE_TAB = "active_tab";
    public static final String ACTIVE_LIGHT = "active_light";
    public static final String ACTIVE_SCENE = "active_scene";
    */
    private static final String ACTION_USB_PERMISSION = "com.lifecity.felux.USB_PERMISSION";
    private static final String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private FeluxManager feluxManager;
    private FtdiUartFileDescriptor uartFileDescriptor;
    private SimpleHdlcOutputStreamWriter feluxWriter;
    private UsbAccessory accessory;

    private static Map<String, String> itemToDetailFragment = new LinkedHashMap<String, String>();

    static {
        itemToDetailFragment.put(Cue.class.getCanonicalName(), CueDetailFragment.class.getCanonicalName());
        itemToDetailFragment.put(LightScene.class.getCanonicalName(), LightSceneDetailFragment.class.getCanonicalName());
        itemToDetailFragment.put(MidiScene.class.getCanonicalName(), MidiSceneDetailFragment.class.getCanonicalName());
        itemToDetailFragment.put(DmxGroupLight.class.getCanonicalName(), GroupLightDetailFragment.class.getCanonicalName());
        itemToDetailFragment.put(DmxColorLight.class.getCanonicalName(), ColorLightDetailFragment.class.getCanonicalName());
        itemToDetailFragment.put(DmxSwitchLight.class.getCanonicalName(), SwitchLightDetailFragment.class.getCanonicalName());
        itemToDetailFragment.put(DmxLight.class.getCanonicalName(), BasicLightDetailFragment.class.getCanonicalName());
    }

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action))
            {
                Log.d(TAG, "accessory detached");
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feluxManager = new FeluxManager(getPreferences(0));
        setContentView(R.layout.main_activity);
        registerReceiver(usbReceiver, new IntentFilter(UsbManager.ACTION_USB_ACCESSORY_DETACHED));

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        fragmentManager = getSupportFragmentManager();

        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayOptions(actionBar.getDisplayOptions() & ~ActionBar.DISPLAY_SHOW_TITLE);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        if (findViewById(R.id.fragment_secondary) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            actionBar.addTab(actionBar
                    .newTab()
                    .setText(R.string.title_cue_list)
                    .setTag(CueListFragment.class.getCanonicalName())
                    .setTabListener(
                            new TabListener(this)
                    ));

            actionBar.addTab(actionBar
                    .newTab()
                    .setText(R.string.title_scene_list)
                    .setTag(SceneListFragment.class.getCanonicalName())
                    .setTabListener(
                            new TabListener(this)
                    ));

            actionBar.addTab(actionBar
                    .newTab()
                    .setText(R.string.title_light_list)
                    .setTag(LightListFragment.class.getCanonicalName())
                    .setTabListener(
                            new TabListener(this)
                    ));

            /*
            if (savedInstanceState != null) {
            }
            */
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(usbReceiver);
        closeUsbAccessory();
    }

    private UsbAccessory getAccessory(UsbManager manager) {
        manager = (UsbManager)getSystemService(Context.USB_SERVICE);
        UsbAccessory[] accessories = manager.getAccessoryList();
        return accessories == null ? null : accessories[0];

        /* TODO: Loop through all accessories */
    }

    private void openUsbAccessory() {
        UsbManager manager = (UsbManager)getSystemService(Context.USB_SERVICE);
        accessory = getAccessory(manager);
        String accessoryString = accessory == null ? "null" : accessory.toString();

        if (accessory == null) {
            /*
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please connect a felux light board")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .show();
             */
        } else {
            try {
                uartFileDescriptor = new FtdiUartFileDescriptor(manager.openAccessory(accessory));
                FtdiUartFileDescriptor.FtdiUartOutputStream ftdiOutputStream = new FtdiUartFileDescriptor.FtdiUartOutputStream(uartFileDescriptor);
                if (getIntent().getParcelableExtra(UsbManager.EXTRA_ACCESSORY) != null) {
                    Log.d(TAG, "setConfig");
                    ftdiOutputStream.setConfig(921600,
                            FtdiUartFileDescriptor.DATA_BITS_8,
                            FtdiUartFileDescriptor.STOP_BITS_1,
                            FtdiUartFileDescriptor.PARITY_NONE,
                            FtdiUartFileDescriptor.FLOW_CONTROL_NONE);
                }
                Log.d(TAG, "write");
                feluxWriter = new SimpleHdlcOutputStreamWriter(ftdiOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        feluxManager.setFeluxWriter(feluxWriter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeUsbAccessory();
    }

    private void closeUsbAccessory() {
        try {
            if (feluxManager != null) {
                feluxManager.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        feluxWriter = null;
        uartFileDescriptor = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        openUsbAccessory();
        feluxManager.open();

        List<Light> lights = feluxManager.getLights();
        if (lights.size() == 0) {
            lights.add(new DmxColorLight("Screen", 0, 1, Color.RED));
            lights.add(new DmxColorLight("Side", 0, 5, Color.BLUE));
            lights.add(new DmxColorLight("Ceiling", 0, 9, Color.GREEN));
            lights.add(new DmxGroupLight("Stage", 1, 13, 16));
            lights.add(new DmxSwitchLight("House", 2, 1));
        }

        List<Scene> scenes = feluxManager.getScenes();
        if (scenes.size() == 0) {
            LightScene lightScene = new LightScene("Song Lights");
            lightScene.addLight((Light)lights.get(0).copy());
            lightScene.addLight((Light)lights.get(1).copy());
            lightScene.addLight((Light) lights.get(2).copy());
            lightScene.addLight((Light) lights.get(3).copy());
            lightScene.addLight((Light) lights.get(4).copy());
            lightScene.setFade(0.5f);
            MidiScene firstSlideScene = new MidiScene("First Slide", 0, 19, 1);
            firstSlideScene.setHold(2.0f);
            MidiScene logoScene = new MidiScene("Logo", 0, 5, 127);
            MidiScene playListScene = new MidiScene("Playlist", 0, 18, 1);
            scenes.add(lightScene);
            scenes.add(firstSlideScene);
            scenes.add(playListScene);
            scenes.add(logoScene);

            feluxManager.saveLights();
            feluxManager.saveScenes();
            feluxManager.saveCues();
        }

        ItemListFragment listFragment = getItemListFragment();
        if (listFragment != null) {
            listFragment.onItemsLoaded(feluxManager);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("unchecked")
    private ItemListFragment getItemListFragment() {
        return (ItemListFragment)fragmentManager.findFragmentByTag(getActionBar().getSelectedTab().getTag().toString());
    }

    @SuppressWarnings("unchecked")
    private ItemDetailFragment getItemDetailFragment(Item item) {
        FragmentManager fm = fragmentManager;
        FragmentTransaction ft = fm.beginTransaction();
        ItemDetailFragment itemDetailFragment = null;
        String tag = null;

        ItemDetailFragment oldDetailFragment = (ItemDetailFragment) fm.findFragmentById(R.id.fragment_secondary);

        if (item instanceof Item) {
            tag = itemToDetailFragment.get(item.getClass().getCanonicalName());
            if (tag == null) {
                throw new IllegalStateException("Invalid item");
            }
        } else if (item == null) {
            if (oldDetailFragment != null) {
                ft.detach(oldDetailFragment);
                ft.commit();
            }
            return oldDetailFragment;
        }

        if (tag != null && !tag.isEmpty()) {
            itemDetailFragment = (ItemDetailFragment) fm.findFragmentByTag(tag);
            if (itemDetailFragment != oldDetailFragment) {
                ft.detach(oldDetailFragment);
            }
            if (itemDetailFragment == null) {
                try {
                    ItemListFragment listFragment = getItemListFragment();
                    itemDetailFragment = (ItemDetailFragment)Class.forName(tag).newInstance();
                    itemDetailFragment.setItemDetailCallbacks(listFragment);
                    itemDetailFragment.setFeluxManager(feluxManager);
                    ft.replace(R.id.fragment_secondary, itemDetailFragment, tag);
                } catch (Exception ex) {
                    throw new IllegalStateException("Invalid item");
                }
            } else {
                if (itemDetailFragment.isDetached() || (itemDetailFragment != oldDetailFragment)) {
                    ft.attach(itemDetailFragment);
                }
            }

            if (itemDetailFragment == null) {
                throw new IllegalStateException("Invalid item");
            }
        } else {
            throw new IllegalStateException("Invalid item");
        }

        ft.commit();

        return itemDetailFragment;
    }
    /**
     * Callback method from {@link ItemListCallbacks}
     * indicating that the item with the given index was selected.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void onListItemSelected(int position, Item item) {
        ItemDetailFragment detailFragment = getItemDetailFragment(item);
        if (detailFragment != null) {
            detailFragment.setItem(item);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onListItemAdded(Item item) {
        ItemDetailFragment detailFragment = getItemDetailFragment(item);
        if (detailFragment != null) {
            detailFragment.onItemAdded(item);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onListItemUpdated(Item item) {
        if (item instanceof Light) {
            Light updatedLight = (Light)item;

            /* Update lights in scenes */
            for (Scene scene: feluxManager.getScenes()) {
                if (scene instanceof LightScene) {
                    for (Light light: ((LightScene)scene).getLights()) {
                        if (light.getUuid().equals(updatedLight.getUuid())) {
                            light.updateProperties(updatedLight);
                        }
                    }
                }
            }

            /* Update lights in scenes that are part of cues */
            for (Cue cue: feluxManager.getCues()) {
                for (Scene scene: cue.getScenes()) {
                    if (scene instanceof LightScene) {
                        for (Light light: ((LightScene)scene).getLights()) {
                            if (light.getUuid().equals(updatedLight.getUuid())) {
                                light.updateProperties(updatedLight);
                            }
                        }
                    }
                }
            }
        } else if (item instanceof LightScene) {
            LightScene updatedLightScene = (LightScene)item;

            for (Cue cue: feluxManager.getCues()) {
                for (Scene scene: cue.getScenes()) {
                    if (scene instanceof LightScene) {
                        if (scene.getUuid().equals(updatedLightScene.getUuid())) {
                            scene.updateProperties(updatedLightScene);
                        }
                    }
                }
            }
        } else if (item instanceof MidiScene) {
            MidiScene updatedMidiScene = (MidiScene)item;

            for (Cue cue: feluxManager.getCues()) {
                for (Scene scene: cue.getScenes()) {
                    if (scene instanceof MidiScene) {
                        if (scene.getUuid().equals(updatedMidiScene.getUuid())) {
                            scene.updateProperties(updatedMidiScene);
                        }
                    }
                }
            }
        }

        if (item instanceof Light) {
            feluxManager.saveLights();
            feluxManager.saveScenes();
            feluxManager.saveCues();
        } else if (item instanceof Scene) {
            feluxManager.saveScenes();
            feluxManager.saveCues();
        } else if (item instanceof Cue) {
            feluxManager.saveCues();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onListItemRemoved(Item item) {
        if (item instanceof Light) {
            Light updatedLight = (Light)item;

            /* Update lights in scenes */
            for (Scene scene: feluxManager.getScenes()) {
                if (scene instanceof LightScene) {
                    ListIterator<Light> lightIterator = ((LightScene)scene).getLights().listIterator();
                    while (lightIterator.hasNext()) {
                        if (lightIterator.next().getUuid().equals(updatedLight.getUuid())) {
                            lightIterator.remove();
                        }
                    }
                }
            }

            /* Update lights in scenes that are part of cues */
            for (Cue cue: feluxManager.getCues()) {
                for (Scene scene: cue.getScenes()) {
                    if (scene instanceof LightScene) {
                        ListIterator<Light> lightIterator = ((LightScene)scene).getLights().listIterator();
                        while (lightIterator.hasNext()) {
                            if (lightIterator.next().getUuid().equals(updatedLight.getUuid())) {
                                lightIterator.remove();
                            }
                        }
                    }
                }
            }
        } else if (item instanceof LightScene) {
            LightScene updatedLightScene = (LightScene)item;

            for (Cue cue: feluxManager.getCues()) {
                ListIterator<Scene> sceneIterator = cue.getScenes().listIterator();
                while (sceneIterator.hasNext()) {
                    Scene scene = sceneIterator.next();
                    if (scene instanceof LightScene) {
                        if (scene.getUuid().equals(updatedLightScene.getUuid())) {
                            sceneIterator.remove();
                        }
                    }
                }
            }
        } else if (item instanceof MidiScene) {
            MidiScene updatedMidiScene = (MidiScene)item;

            for (Cue cue: feluxManager.getCues()) {
                ListIterator<Scene> sceneIterator = cue.getScenes().listIterator();
                while (sceneIterator.hasNext()) {
                    Scene scene = sceneIterator.next();
                    if (scene instanceof MidiScene) {
                        if (scene.getUuid().equals(updatedMidiScene.getUuid())) {
                            sceneIterator.remove();
                        }
                    }
                }
            }
        }

        if (item instanceof Light) {
            feluxManager.saveLights();
            feluxManager.saveScenes();
            feluxManager.saveCues();
        } else if (item instanceof Scene) {
            feluxManager.saveScenes();
            feluxManager.saveCues();
        } else if (item instanceof Cue) {
            feluxManager.saveCues();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onListItemEndUpdate(Item item) {
        ItemDetailFragment detailFragment = (ItemDetailFragment)fragmentManager.findFragmentById(R.id.fragment_secondary);
        if (detailFragment != null) {
            detailFragment.onItemUpdated(item);
        }
    }

    private class TabListener implements ActionBar.TabListener {
        private FragmentActivity activity;

        public TabListener(FragmentActivity activity) {
            this.activity = activity;
        }
        public void onTabSelected(ActionBar.Tab tab,
                                  android.app.FragmentTransaction unused) {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            String listTag = tab.getTag().toString();
            ItemListFragment listFragment = (ItemListFragment)fm.findFragmentByTag(listTag);

            if (listFragment instanceof ItemListFragment) {
                ft.attach(listFragment);
            } else if (listFragment == null) {
                try {
                    listFragment = (ItemListFragment)Class.forName(listTag).newInstance();
                    listFragment.setFeluxManager(feluxManager);
                    ft.replace(R.id.fragment_primary, listFragment, listTag);
                } catch (Exception ex) {
                    throw new IllegalStateException("Invalid tab");
                }
            } else {
                throw new IllegalStateException("Invalid tab");
            }

            ft.commit();
        }

        public void onTabUnselected(ActionBar.Tab tab,
                                    android.app.FragmentTransaction unused) {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            String listTag = tab.getTag().toString();
            ItemListFragment listFragment = (ItemListFragment)fm.findFragmentByTag(listTag);

            if (listFragment instanceof ItemListFragment) {
                ft.detach(listFragment);
            } else {
                throw new IllegalStateException("Invalid tab");
            }

            ft.commit();
        }

        public void onTabReselected(ActionBar.Tab tab,
                                    android.app.FragmentTransaction unused) {
        }
    }
}
