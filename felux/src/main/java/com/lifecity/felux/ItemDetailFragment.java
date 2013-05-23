package com.lifecity.felux;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lifecity.felux.items.Item;

/**
 * A fragment representing a single Scene detail screen.
 * on handsets.
 */
abstract class ItemDetailFragment<T> extends Fragment {
    public String TAG;
    protected int layout;
    protected ItemDetailCallbacks<Item> detailCallbacks;

    /**
     * The dummy content this fragment is presenting.
     */
    protected T item;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment(int layout, ItemDetailCallbacks<Item> detailCallbacks) {
        this.layout = layout;
        this.detailCallbacks = detailCallbacks;
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

    public void itemAdded(T item) {
    }

    abstract public void updateItemView();
}
