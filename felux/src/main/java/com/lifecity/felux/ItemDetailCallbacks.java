package com.lifecity.felux;

public interface ItemDetailCallbacks<T> {
    /**
     * Callback for when an item has been selected.
     */
    public void onItemStartUpdate(T item);
    public void onItemEndUpdate(T item);
    public void onItemDetailAdded(T item);
}
