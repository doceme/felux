package com.lifecity.felux;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
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
        public void onItemSelected(Item item) {
        }
        public void onItemAdded(Item item) {
        }
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // Restore the previously serialized activated item position.
        if (savedInstanceState == null) {
            if (items.size() > 0) {
                setActivatedPosition(0);
            }
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
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        itemListCallbacks = emptyItemListCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        itemListCallbacks.onItemSelected(items.get(position));
        getActivity().invalidateOptionsMenu();
    }

    public void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
            itemListCallbacks.onItemSelected(null);
        } else {
            getListView().setItemChecked(position, true);
            itemListCallbacks.onItemSelected(items.get(position));
        }

        getActivity().invalidateOptionsMenu();
        mActivatedPosition = position;
    }

    public void addItem(T item) {
        items.add(item);
        adapter.notifyDataSetChanged();
        setActivatedPosition(items.size() - 1);
        itemListCallbacks.onItemAdded(item);
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
        MenuItem upItem = menu.findItem(R.id.moveup);
        MenuItem downItem = menu.findItem(R.id.movedown);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.moveup:
            moveItem(selectedItem(), false);
            return true;
        case R.id.movedown:
            moveItem(selectedItem(), true);
            return true;
            /*
        case R.id.add:
            addItem(new T("Scene " + Integer.toString(items.size() + 1)));
            return true;
            */
        case R.id.remove:
            removeItem(selectedItem());
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
