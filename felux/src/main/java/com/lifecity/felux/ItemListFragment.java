package com.lifecity.felux;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.lifecity.felux.items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class ItemListFragment<T> extends ListFragment implements ItemDetailCallbacks<Item> {
    protected ArrayAdapter<T> adapter;
    protected List<T> items = new ArrayList<T>();
    protected ActionMode actionMode;
    protected ActionMode.Callback actionModeCallback;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    public static String TAG;

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    protected ItemListCallbacks itemListCallbacks = emptyItemListCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    protected int mActivatedPosition = ListView.INVALID_POSITION;

    protected static class EmptyItemListCallbacks implements ItemListCallbacks<Item> {
        @Override
        public void onItemSelected(int position, Item item) {
        }

        @Override
        public void onItemAdded(Item item) {
        }

        @Override
        public void onItemBeginEdit(Item item) {

        }

        @Override
        public void onItemEndEdit(Item item) {

        }
    }

    public int getNumItems() {
        return items.size();
    }

    public T getItemAt(int index) {
        return items.get(index);
    }

    /**
     */
    protected static ItemListCallbacks emptyItemListCallbacks = new EmptyItemListCallbacks();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayAdapter<T>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                items
        );

        setListAdapter(adapter);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        // Restore the previously serialized activated item position.
        ListView listView = getListView();
        int position = listView.getCheckedItemPosition();
        if (position < 0 && items.size() > 0) {
            position = 0;
            listView.setItemChecked(position, true);
        }

        if (position >= 0) {
            itemListCallbacks.onItemSelected(position, items.get(position));
        }

        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof ItemListCallbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        itemListCallbacks = (ItemListCallbacks) activity;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(this.getClass().getSimpleName(), "onPause");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        itemListCallbacks = emptyItemListCallbacks;
        Log.i(this.getClass().getSimpleName(), "onDetach");
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        itemListCallbacks.onItemSelected(0, items.get(position));
        getActivity().invalidateOptionsMenu();
    }

    public void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
            itemListCallbacks.onItemSelected(-1, null);
        } else {
            getListView().setItemChecked(position, true);
            itemListCallbacks.onItemSelected(position, items.get(position));
        }

        getActivity().invalidateOptionsMenu();
        mActivatedPosition = position;
    }

    public void addItem(T item) {
        items.add(item);
        adapter.notifyDataSetChanged();
        setActivatedPosition(items.size() - 1);
        itemListCallbacks.onItemAdded(item);
        startActionMode();
    }

    public void removeItem(T item) {
        int oldPosition = selectedPosition();
        items.remove(item);
        adapter.notifyDataSetChanged();
        setActivatedPosition(oldPosition - 1);
    }

    public void moveItem(T item, boolean down) {
        int oldPosition = selectedPosition();
        int newPosition;

        if (down) {
            if (oldPosition < (items.size() - 1)) {
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

        T temp = items.get(newPosition);
        items.set(newPosition, item);
        items.set(oldPosition, temp);
        adapter.notifyDataSetChanged();
        setActivatedPosition(newPosition);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_list_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        int position = selectedPosition();
        MenuItem upItem = menu.findItem(R.id.item_moveup);
        MenuItem downItem = menu.findItem(R.id.item_movedown);
        MenuItem editItem = menu.findItem(R.id.item_edit);
        MenuItem removeItem = menu.findItem(R.id.item_remove);

        editItem.setVisible(items.size() > 0);
        removeItem.setVisible(items.size() > 0);

        if (position >= 0 && items.size() > 0) {
            if (upItem != null) {
                upItem.setVisible(position > 0);
            }
            if (downItem != null) {
                downItem.setVisible(position < (items.size() - 1));
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

    public T selectedItem() {
        return items.get(selectedPosition());
    }

    private void startActionMode() {
        actionMode = getActivity().startActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                itemListCallbacks.onItemBeginEdit(selectedItem());
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                itemListCallbacks.onItemEndEdit(selectedItem());
                actionMode = null;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.item_moveup:
            moveItem(selectedItem(), false);
            return true;
        case R.id.item_movedown:
            moveItem(selectedItem(), true);
            return true;
        case R.id.item_remove:
            ConfirmDialogFragment dialog = new ConfirmDialogFragment("Delete", "Are you sure you want to delete this?", new ConfirmDialogFragment.ConfirmDialogListener() {
                @Override
                public void onConfirm() {
                    removeItem(selectedItem());
                    getActivity().invalidateOptionsMenu();
                }
            });
            dialog.show(getActivity().getSupportFragmentManager(), "confirm_remove_dialog_tag");
            return true;
        case R.id.item_edit:
            startActionMode();
            return true;
        case R.id.item_add:
            getActivity().invalidateOptionsMenu();
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
  }

    @Override
    public void onItemNameChanged(Item item) {
        adapter.notifyDataSetChanged();
    }
}
