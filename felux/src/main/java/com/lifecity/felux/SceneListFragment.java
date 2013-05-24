package com.lifecity.felux;

import android.view.MenuItem;
import com.lifecity.felux.scenes.LightScene;
import com.lifecity.felux.scenes.Scene;

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
        items.add(new LightScene("Scene 1"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.item_add:
            AddSceneDialogFragment dialog = new AddSceneDialogFragment(new AddSceneDialogFragment.AddSceneDialogListener() {
                @Override
                public void onTypeSelected(Scene scene) {
                    if (scene != null) {
                        scene.setName("Scene " + Integer.toString(items.size() + 1));
                        addItem(scene);
                    }
                }

                @Override
                public void onCanceled() {

                }
            });
            dialog.show(getActivity().getSupportFragmentManager(), "add_scene_dialog_tag");
            return super.onOptionsItemSelected(item);
        default:
            return super.onOptionsItemSelected(item);
    }
  }
}
