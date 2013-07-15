package com.lifecity.felux;

public interface ItemListCallbacks<T> {
    /**
     * Callback for when an item has been selected.
     */
    public void onListItemSelected(int position, T item);
    public void onListItemAdded(T item);
    public void onListItemEndUpdate(T item);
    public void onListItemUpdated(T item);
    public void onListItemRemoved(T item);
}
