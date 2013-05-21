package com.lifecity.felux;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment representing a single Scene detail screen.
 * on handsets.
 */
abstract class ItemDetailFragment<T> extends Fragment {
    public String TAG;
    protected int layout;

    /**
     * The dummy content this fragment is presenting.
     */
    protected T item;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment(int layout) {
        this.layout = layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateItemView();
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
        updateItemView();
    }

    abstract public void updateItemView();
}
