package com.lifecity.felux;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.lifecity.felux.items.Item;

/**
 * A fragment representing a single Scene detail screen.
 * on handsets.
 */
abstract class ItemDetailFragment<T> extends Fragment implements ActionMode.Callback {
    protected int layout;
    protected ItemDetailCallbacks<Item> detailCallbacks;
    protected ActionMode actionMode;
    protected FeluxManager manager;

    /**
     * The dummy content this fragment is presenting.
     */
    protected T item;

    public ItemDetailFragment(int layout) {
        this.layout = layout;
    }

    public void setDetailCallbacks(ItemDetailCallbacks<Item> callbacks) {
        this.detailCallbacks = callbacks;
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
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        updateItemView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(layout, container, false);
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        Log.i(this.getClass().getSimpleName(), "setItem");
        this.item = item;
        if (getView() != null)
            updateItemView();
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
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        /*
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.fragment_light_scene_menu, menu);
        onItemBeginEdit();
        return true;
        */
        return false;
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
        actionMode = null;
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
    }

    public void onItemBeginEdit() {
    }

    public void onItemEndEdit() {
    }

    abstract public void updateItemView();
}
