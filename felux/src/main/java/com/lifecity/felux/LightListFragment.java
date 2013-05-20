package com.lifecity.felux;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lifecity.felux.light.Lights;

/**
 * A list fragment representing a list of Lights. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link LightDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link ListCallbacks}
 * interface.
 */
public class LightListFragment extends ListFragment {
    private ArrayAdapter<Lights.Light> adapter;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    public static final String TAG = "light_list_tag";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private LightListCallbacks mListCallbacks = sEmptyLightListCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private static class EmptyListCallbacks implements LightListCallbacks {
        @Override
        public void onLightSelected(Lights.Light light) {
        }
    }

    /**
     */
    private static LightListCallbacks sEmptyLightListCallbacks = new EmptyListCallbacks();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LightListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayAdapter<Lights.Light>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                Lights.LIGHTS
        );
        // TODO: replace with a real list adapter.
        setListAdapter(adapter);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_light_list, container, false);
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
            if (Lights.LIGHTS.size() > 0) {
                setActivatedPosition(0);
            }
        }

        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof LightListCallbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mListCallbacks = (LightListCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mListCallbacks = sEmptyLightListCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mListCallbacks.onLightSelected(Lights.LIGHTS.get(position));
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
            mListCallbacks.onLightSelected(null);
        } else {
            getListView().setItemChecked(position, true);
            mListCallbacks.onLightSelected(Lights.LIGHTS.get(position));
        }

        getActivity().invalidateOptionsMenu();
        mActivatedPosition = position;
    }

    public void addLight(Lights.Light light) {
        Lights.LIGHTS.add(light);
        adapter.notifyDataSetChanged();
        setActivatedPosition(Lights.LIGHTS.size() - 1);
    }

    public void removeLight(Lights.Light light) {
        int oldPosition = selectedPosition();
        Lights.LIGHTS.remove(light);
        adapter.notifyDataSetChanged();
        setActivatedPosition(oldPosition - 1);
    }

    public void moveLight(Lights.Light light, boolean down) {
        int oldPosition = selectedPosition();
        int newPosition;
        if (down) {
            if (oldPosition < (Lights.LIGHTS.size() - 1)) {
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

        Lights.Light temp = Lights.LIGHTS.get(newPosition);
        Lights.LIGHTS.set(newPosition, light);
        Lights.LIGHTS.set(oldPosition, temp);
        adapter.notifyDataSetChanged();
        setActivatedPosition(newPosition);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_light_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        int position = selectedPosition();
        MenuItem upItem = menu.findItem(R.id.moveup_light);
        MenuItem downItem = menu.findItem(R.id.movedown_light);

        if (position >= 0 && Lights.LIGHTS.size() > 0) {
            if (upItem != null) {
                upItem.setVisible(position > 0);
            }
            if (downItem != null) {
                downItem.setVisible(position < (Lights.LIGHTS.size() - 1));
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

    public Lights.Light selectedLight() {
        return Lights.LIGHTS.get(selectedPosition());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.moveup_light:
            moveLight(selectedLight(), false);
            return true;
        case R.id.movedown_light:
            moveLight(selectedLight(), true);
            return true;
        case R.id.add_light:
            addLight(new Lights.DmxLight(0, "Light " + Integer.toString(Lights.LIGHTS.size() + 1)));
            return true;
        case R.id.remove_light:
            removeLight(selectedLight());
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
  }
}
