package com.lifecity.felux;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.*;
import android.view.WindowManager;
import com.lifecity.felux.items.Item;
import com.lifecity.felux.lights.DmxColorLight;
import com.lifecity.felux.lights.DmxGroupLight;
import com.lifecity.felux.lights.DmxLight;
import com.lifecity.felux.lights.Light;
import com.lifecity.felux.scenes.DelayScene;
import com.lifecity.felux.scenes.LightScene;
import com.lifecity.felux.scenes.MidiScene;
import com.lifecity.felux.scenes.Scene;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * An activity representing a list of Lights. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link SceneListFragment} and the item details
 * (if present) is a {@link SceneDetailFragment}.
 * <p>
 * This activity also implements the required
 * to listen for item selections.
 */
public class MainActivity extends FragmentActivity implements ItemListCallbacks<Item> {
    /*
    public static final String ACTIVE_TAB = "active_tab";
    public static final String ACTIVE_LIGHT = "active_light";
    public static final String ACTIVE_SCENE = "active_scene";
    */
    private FragmentManager mFragmentManager;
    private LightManager lightManager = new LightManager();

    private static Map<String, String> itemToDetailFragment = new LinkedHashMap<String, String>();
    //private static Map<String, Integer> listPositions = new LinkedHashMap<String, Integer>();

    static {
        //Map<String, String> detailFragments = new LinkedHashMap<String, String>();
        itemToDetailFragment.put(LightScene.class.getCanonicalName(), LightSceneDetailFragment.class.getCanonicalName());
        itemToDetailFragment.put(DelayScene.class.getCanonicalName(), SceneDetailFragment.class.getCanonicalName());
        itemToDetailFragment.put(MidiScene.class.getCanonicalName(), SceneDetailFragment.class.getCanonicalName());
        itemToDetailFragment.put(MidiScene.class.getCanonicalName(), SceneDetailFragment.class.getCanonicalName());
        itemToDetailFragment.put(DmxLight.class.getCanonicalName(), LightDetailFragment.class.getCanonicalName());
        itemToDetailFragment.put(DmxGroupLight.class.getCanonicalName(), GroupLightDetailFragment.class.getCanonicalName());
        itemToDetailFragment.put(DmxColorLight.class.getCanonicalName(), ColorLightDetailFragment.class.getCanonicalName());
        //itemToDetailFragment = Collections.unmodifiableMap(detailFragments);
    }

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        List<Light> lights = lightManager.getLights();
        lights.add(new DmxColorLight("Screen", 1, Color.RED));
        lights.add(new DmxColorLight("Side", 5, Color.BLUE));
        lights.add(new DmxColorLight("Ceiling", 9, Color.GREEN));
        lights.add(new DmxGroupLight("Stage", 13, 16));

        List<Scene> scenes = lightManager.getScenes();
        scenes.add(new LightScene("Scene 1"));

        mFragmentManager = getSupportFragmentManager();
        //listPositions.put(SceneListFragment.class.getCanonicalName(), -1);
        //listPositions.put(LightListFragment.class.getCanonicalName(), -1);

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
                actionBar.setSelectedNavigationItem(savedInstanceState.getInt(ACTIVE_TAB));
            }
            */
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //outState.putInt(ACTIVE_TAB, getActionBar().getSelectedNavigationIndex());
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("unchecked")
    private ItemDetailFragment getItemFragment(Item item) {
        FragmentManager fm = mFragmentManager;
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
            ft.detach(oldDetailFragment);
            ft.commit();
            return oldDetailFragment;
        }

        if (tag != null && !tag.isEmpty()) {
            itemDetailFragment = (ItemDetailFragment) fm.findFragmentByTag(tag);
            if (itemDetailFragment != oldDetailFragment) {
                ft.detach(oldDetailFragment);
            }
            if (itemDetailFragment == null) {
                try {
                    ItemListFragment listFragment = (ItemListFragment)fm.findFragmentByTag(getActionBar().getSelectedTab().getTag().toString());
                    itemDetailFragment = (ItemDetailFragment)Class.forName(tag).newInstance();
                    itemDetailFragment.setDetailCallbacks(listFragment);
                    itemDetailFragment.setFeluxManager(lightManager);
                    ft.add(R.id.fragment_secondary, itemDetailFragment, tag);
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
    public void onItemSelected(int position, Item item) {
        getItemFragment(item).setItem(item);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onItemAdded(Item item) {
        getItemFragment(item).onItemAdded(item);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onItemBeginEdit(Item item) {
        getItemFragment(item).onItemBeginEdit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onItemEndEdit(Item item) {
        getItemFragment(item).onItemEndEdit();
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
                    listFragment.setFeluxManager(lightManager);
                    ft.add(R.id.fragment_primary, listFragment, listTag);
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
