package com.lifecity.felux;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.lifecity.felux.items.Item;
import com.lifecity.felux.items.LightSceneLightListAdapter;
import com.lifecity.felux.lights.DmxColorLight;
import com.lifecity.felux.scenes.LightScene;
import com.lifecity.felux.scenes.Scene;

/**
 * A fragment representing a single Scene detail screen.
 * on handsets.
 */
public class LightSceneDetailFragment extends ItemDetailFragment<LightScene> {
    private LightSceneLightListAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LightSceneDetailFragment() {
        super(R.layout.fragment_light_scene_detail);
    }

    @Override
    public void updateItemView() {
        // TODO: Update light list
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        item.addLight(new DmxColorLight("Test", 0x1, Color.WHITE));

        adapter = new LightSceneLightListAdapter(
                getActivity(),
                R.layout.light_scene_light_list_row,
                item.getLights()
        );

        ListView lightListView = (ListView)rootView.findViewById(R.id.light_scene_detail_light_list);
        TextView nameTextView = (TextView)rootView.findViewById(R.id.light_scene_detail_name_edit);
        nameTextView.setText(item.getName());
        lightListView.setAdapter(adapter);

        return rootView;
    }
}
