package com.lifecity.felux;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lifecity.felux.scene.Scenes;

/**
 * A fragment representing a single Scene detail screen.
 * on handsets.
 */
public class SceneDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_INDEX = "scene_index";

    /**
     * The dummy content this fragment is presenting.
     */
    private Scenes.Scene mScene;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SceneDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ARG_INDEX)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mScene = Scenes.SCENE_MAP.get(arguments.getInt(ARG_INDEX));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scene_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mScene != null) {
            ((TextView) rootView.findViewById(R.id.scene_detail)).setText(mScene.name);
        }

        return rootView;
    }

    public Scenes.Scene getScene() {
        return mScene;
    }
}
