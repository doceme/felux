package com.lifecity.felux;

import android.view.*;

/**
 *
 */
public class SceneListFragment extends ItemListFragment<Scene> {
    public static final String TAG = "scene_list_tag";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SceneListFragment() {
        items.add(new Scene("Scene 1"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.add:
            addItem(new Scene("Scene " + Integer.toString(items.size() + 1)));
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
  }
}
