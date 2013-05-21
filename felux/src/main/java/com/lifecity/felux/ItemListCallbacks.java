package com.lifecity.felux;

public interface ItemListCallbacks<T> {
    /**
     * Callback for when an item has been selected.
     */
    public void onItemSelected(T item);
}
