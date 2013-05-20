package com.lifecity.felux;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.lifecity.felux.scene.Scenes;

/**
 * A list fragment representing a list of Lights. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link SceneDetailFragment}.
 * <p>
 * interface.
 */
public class SceneListFragment extends ListFragment {
    private ArrayAdapter<Scenes.Scene> adapter;
    private Menu menu;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    public static final String TAG = "scene_list_tag";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private SceneListCallbacks mListCallbacks = sEmptySceneListCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private static class EmptyListCallbacks implements SceneListCallbacks {
        @Override
        public void onSceneSelected(Scenes.Scene scene) {
        }
    }

    /**
     */
    private static SceneListCallbacks sEmptySceneListCallbacks = new EmptyListCallbacks();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SceneListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayAdapter<Scenes.Scene>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                Scenes.SCENES
        );
        // TODO: replace with a real list adapter.
        setListAdapter(adapter);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scene_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        } else {
            if (Scenes.SCENES.size() > 0) {
                setActivatedPosition(0);
            }
        }

        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof SceneListCallbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mListCallbacks = (SceneListCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mListCallbacks = sEmptySceneListCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mListCallbacks.onSceneSelected(Scenes.SCENES.get(position));
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    public void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
            mListCallbacks.onSceneSelected(null);
        } else {
            getListView().setItemChecked(position, true);
            mListCallbacks.onSceneSelected(Scenes.SCENES.get(position));
        }

        if (menu != null) {
            MenuItem item;
            if (position == 0) {
                item = menu.findItem(R.id.moveup_scene);
                if (item != null) {
                    item.setVisible(false);
                }
            } else if (position == (Scenes.SCENES.size() - 1)) {
                item = menu.findItem(R.id.movedown_scene);
                if (item != null) {
                    item.setVisible(false);
                }
            }
        }

        getActivity().invalidateOptionsMenu();
        mActivatedPosition = position;
    }

    public void addScene(Scenes.Scene scene) {
        Scenes.SCENES.add(scene);
        adapter.notifyDataSetChanged();
        setActivatedPosition(Scenes.SCENES.size() - 1);
    }

    public void removeScene(Scenes.Scene scene) {
        int oldPosition = selectedPosition();
        Scenes.SCENES.remove(scene);
        adapter.notifyDataSetChanged();
        setActivatedPosition(oldPosition - 1);
    }

    public void moveScene(Scenes.Scene scene, boolean down) {
        int oldPosition = selectedPosition();
        int newPosition;

        if (down) {
            if (oldPosition < (Scenes.SCENES.size() - 1)) {
                newPosition = oldPosition + 1;
            }  else {
                return;
            }
        } else {
            if (oldPosition > 0) {
                newPosition = oldPosition - 1;
            } else {
                return;
            }
        }

        Scenes.Scene temp = Scenes.SCENES.get(newPosition);
        Scenes.SCENES.set(newPosition, scene);
        Scenes.SCENES.set(oldPosition, temp);
        adapter.notifyDataSetChanged();
        setActivatedPosition(newPosition);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.fragment_scene_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        int position = selectedPosition();
        MenuItem upItem = menu.findItem(R.id.moveup_scene);
        MenuItem downItem = menu.findItem(R.id.movedown_scene);

        if (position >= 0 && Scenes.SCENES.size() > 0) {
            if (upItem != null) {
                upItem.setVisible(position > 0);
            }
            if (downItem != null) {
                downItem.setVisible(position < (Scenes.SCENES.size() - 1));
            }
        } else {
            if (upItem != null) {
                upItem.setVisible(false);
            }
            if (downItem != null) {
                downItem.setVisible(false);
            }
        }
    }

    public int selectedPosition() {
        return getListView().getCheckedItemPosition();
    }

    public Scenes.Scene selectedScene() {
        return Scenes.SCENES.get(selectedPosition());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.moveup_scene:
            moveScene(selectedScene(), false);
            return true;
        case R.id.movedown_scene:
            moveScene(selectedScene(), true);
            return true;
        case R.id.add_scene:
            addScene(new Scenes.Scene("Scene " + Integer.toString(Scenes.SCENES.size() + 1)));
            return true;
        case R.id.remove_scene:
            removeScene(selectedScene());
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
  }
}
