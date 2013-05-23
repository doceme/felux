package com.lifecity.felux;

import android.view.View;
import android.widget.TextView;
import com.lifecity.felux.items.Item;
import com.lifecity.felux.scenes.Scene;

/**
 * A fragment representing a single Scene detail screen.
 * on handsets.
 */
public class SceneDetailFragment extends ItemDetailFragment<Scene> {
    public static final String TAG = "scene_detail_tag";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SceneDetailFragment(ItemDetailCallbacks<Item> detailCallbacks) {
        super(R.layout.fragment_scene_detail, detailCallbacks);
    }

    public void updateItemView() {
        View rootView = getView();
        if (rootView != null) {
            TextView textView = (TextView)rootView.findViewById(R.id.scene_detail);
            if (textView != null) {
                if (item != null) {
                    textView.setText(item.getName());
                } else {
                    textView.setText("");
                }
            }
        }
    }
}
