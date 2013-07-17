package com.lifecity.felux;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import com.lifecity.felux.items.Item;

/**
 * A fragment representing a single Scene detail screen.
 * on handsets.
 */
abstract class ItemDetailFragment<T> extends Fragment implements ActionMode.Callback {
    protected int layout;
    protected ItemDetailCallbacks<T> itemDetailCallbacks;
    protected ActionMode actionMode;
    protected FeluxManager manager;
    protected boolean itemAdded;

    /**
     * The dummy content this fragment is presenting.
     */
    protected T item;
    protected T itemBeforeEdit;

    public ItemDetailFragment(int layout) {
        setRetainInstance(true);
        this.layout = layout;
    }

    public void setItemDetailCallbacks(ItemDetailCallbacks<T> callbacks) {
        this.itemDetailCallbacks = callbacks;
    }

    public void setFeluxManager(FeluxManager manager) {
        this.manager = manager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        updateItemView();

        if (itemAdded) {
            itemAdded = false;
            startActionMode();
            itemDetailCallbacks.onDetailItemAdded(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(layout, container, false);
    }

    public T getItem() {
        return item;
    }

    public ItemDetailFragment<T> setItem(T item) {
        if (this.item != item) {
            this.item = item;
            if (getView() != null)
                updateItemView();
        }

        return this;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_edit:
                startActionMode();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void startActionMode() {
        actionMode = getActivity().startActionMode(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        itemDetailCallbacks.onDetailItemStartUpdate(item);
        T temp = item;
        itemBeforeEdit = item;
        temp = (T)((Item)item).copy();
        item = temp;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_item_cancel:
                actionMode.setTag(R.string.cancelled);
                actionMode.finish();
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        Object tag = actionMode.getTag();
        if (tag != null && tag.equals(R.string.cancelled)) {
            item = itemBeforeEdit;
        } else {
            Item temp = (Item)itemBeforeEdit;
            temp.update((Item)item);
            item = itemBeforeEdit;
            itemBeforeEdit = null;
            itemDetailCallbacks.onDetailItemUpdated(item);
        }
        itemDetailCallbacks.onDetailItemEndUpdate(item);
        this.actionMode = null;
    }

    public void showHideKeyboard(View view, boolean show) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.dispatchWindowFocusChanged(show);
        if (show) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } else {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onItemAdded(T item) {
        if (this.item != null && this.item.getClass().equals(item.getClass())) {
            startActionMode();
            itemDetailCallbacks.onDetailItemAdded(item);
        } else {
                itemAdded = true;
        }

        setItem(item);
    }

    public void onItemUpdated(T item) {
        updateItemView();
    }

    abstract public void updateItemView();
}
