package com.lifecity.felux;

public interface ItemListCallbacks<T> {
    /**
     * Callback for when an item has been selected.
     */
    public void onItemSelected(int position, T item);
    public void onItemAdded(T item);
    public void onItemUpdated(T item);
}
