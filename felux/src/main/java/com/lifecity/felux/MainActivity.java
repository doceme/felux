package com.lifecity.felux;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


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
    public static final String ACTIVE_TAB = "active_tab";
    public static final String ACTIVE_LIGHT = "active_light";
    public static final String ACTIVE_SCENE = "active_scene";
    private FragmentManager mFragmentManager;
    private SceneListFragment sceneListFragment;
    private LightListFragment lightListFragment;
    private SceneDetailFragment sceneDetailFragment;
    private LightDetailFragment lightDetailFragment;

    public enum TabType {
        SCENE,
        LIGHT
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

        mFragmentManager = getSupportFragmentManager();

        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayOptions(actionBar.getDisplayOptions() & ~ActionBar.DISPLAY_SHOW_TITLE);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_HOME_AS_UP |
                ActionBar.DISPLAY_SHOW_TITLE
        );
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
                    .setTabListener(
                            new TabListener(this, TabType.SCENE)
                    ));

            actionBar.addTab(actionBar
                    .newTab()
                    .setText(R.string.title_light_list)
                    .setTabListener(
                            new TabListener(this, TabType.LIGHT)
                    ));

            if (savedInstanceState != null) {
                actionBar.setSelectedNavigationItem(savedInstanceState.getInt(ACTIVE_TAB));
                //sceneListFragment.setActivatedPosition(savedInstanceState.getInt(ACTIVE_SCENE));
                //lightListFragment.setActivatedPosition(savedInstanceState.getInt(ACTIVE_LIGHT));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ACTIVE_TAB, getActionBar().getSelectedNavigationIndex());
        //outState.putInt(ACTIVE_LIGHT, lightListFragment.getSelectedItemPosition());
        //outState.putInt(ACTIVE_SCENE, sceneListFragment.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    /**
     * Callback method from {@link ItemListCallbacks}
     * indicating that the item with the given index was selected.
     */
    @Override
    public void onItemSelected(Item item) {
        String tag;
        if (item instanceof Scene) {
            tag = SceneDetailFragment.TAG;
        } else if (item instanceof Light) {
            tag = LightDetailFragment.TAG;
        } else {
            throw new IllegalStateException("Invalid selected item");
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ItemDetailFragment itemDetailFragment = (ItemDetailFragment) fm.findFragmentByTag(tag);
        if (itemDetailFragment != null) {
            itemDetailFragment.setItem(item);
        }
    }

    private class TabListener implements ActionBar.TabListener {
        private FragmentActivity activity;
        private SceneListFragment sceneListFragment;
        private LightListFragment lightListFragment;
        private SceneDetailFragment sceneDetailFragment;
        private LightDetailFragment lightDetailFragment;

        private TabType tabType;

        public TabListener(FragmentActivity activity, TabType type) {
            this.activity = activity;
            this.tabType = type;

            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            sceneListFragment = (SceneListFragment) fm.findFragmentByTag(SceneListFragment.TAG);
            if (sceneListFragment != null && !sceneListFragment.isDetached()) {
                ft.detach(sceneListFragment);
            }
            lightListFragment = (LightListFragment) fm.findFragmentByTag(LightListFragment.TAG);
            if (lightListFragment != null && !lightListFragment.isDetached()) {
                ft.detach(lightListFragment);
            }
            sceneDetailFragment = (SceneDetailFragment) fm.findFragmentByTag(SceneDetailFragment.TAG);
            if (sceneDetailFragment != null && !sceneDetailFragment.isDetached()) {
                ft.detach(sceneDetailFragment);
            }
            lightDetailFragment = (LightDetailFragment) fm.findFragmentByTag(LightDetailFragment.TAG);
            if (lightDetailFragment != null && !lightDetailFragment.isDetached()) {
                ft.detach(lightDetailFragment);
            }
            ft.commit();

        }
        public void onTabSelected(ActionBar.Tab tab,
                                  android.app.FragmentTransaction unused) {
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            if (tabType == TabType.SCENE) {
                if (sceneListFragment == null) {
                    sceneListFragment = new SceneListFragment();
                    ft.add(R.id.fragment_primary, sceneListFragment, SceneListFragment.TAG);
                } else {
                    ft.attach(sceneListFragment);
                }
                if (sceneDetailFragment == null) {
                    sceneDetailFragment = new SceneDetailFragment();
                    ft.add(R.id.fragment_secondary, sceneDetailFragment, SceneDetailFragment.TAG);
                } else {
                    ft.attach(sceneDetailFragment);
                }
            } else if (tabType == TabType.LIGHT) {
                if (lightListFragment == null) {
                    lightListFragment = new LightListFragment();
                    ft.add(R.id.fragment_primary, lightListFragment, LightListFragment.TAG);
                } else {
                    ft.attach(lightListFragment);
                }
                if (lightDetailFragment == null) {
                    lightDetailFragment = new LightDetailFragment();
                    ft.add(R.id.fragment_secondary, lightDetailFragment, LightDetailFragment.TAG);
                } else {
                    ft.attach(lightDetailFragment);
                }
            }

            ft.commit();
        }

        public void onTabUnselected(ActionBar.Tab tab,
                                    android.app.FragmentTransaction unused) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();

            if (tabType == TabType.SCENE) {
                if (sceneListFragment != null) {
                    ft.detach(sceneListFragment);
                }
                if (sceneDetailFragment != null) {
                    ft.detach(sceneDetailFragment);
                }
            } else if (tabType == TabType.LIGHT) {
                if (lightListFragment != null) {
                    ft.detach(lightListFragment);
                }
                if (lightDetailFragment != null) {
                    ft.detach(lightDetailFragment);
                }
            }

            ft.commit();
        }

        public void onTabReselected(ActionBar.Tab tab,
                                    android.app.FragmentTransaction unused) {
        }
    }
}
