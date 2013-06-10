package com.lifecity.felux;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lifecity.felux.lights.DmxColorLight;
import com.lifecity.felux.lights.Light;
import com.lifecity.felux.scenes.LightScene;

/**
 * A fragment representing a single Scene detail screen.
 * on handsets.
 */
public class LightSceneDetailFragment extends ItemDetailFragment<LightScene> implements ColorLightDialogFragment.ColorLightDialogListener, AdapterView.OnItemClickListener {
    private LightSceneLightListAdapter adapter;
    private Light currentLight;

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

        adapter = new LightSceneLightListAdapter(
                getActivity(),
                R.layout.light_scene_light_list_row,
                manager.getLights()
        );

        ListView lightListView = (ListView)rootView.findViewById(R.id.light_scene_detail_light_list);
        TextView nameTextView = (TextView)rootView.findViewById(R.id.light_scene_detail_name_edit);
        nameTextView.setText(item.getName());
        lightListView.setAdapter(adapter);
        lightListView.setItemsCanFocus(false);
        lightListView.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        currentLight = (Light)adapterView.getItemAtPosition(i);
        if (currentLight instanceof DmxColorLight) {
            DmxColorLight dmxColorLight = (DmxColorLight)currentLight;
            ColorLightDialogFragment dialog = new ColorLightDialogFragment(this);
            dialog.setColor(dmxColorLight.getColor());
            dialog.show(getActivity().getSupportFragmentManager(), "add_light_dialog_tag");
        }
    }

    @Override
    public void onColorSelected(int color) {
        if (currentLight != null) {
            ((DmxColorLight) currentLight).setColor(color);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCanceled() {

    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.fragment_light_scene_menu, menu);
        onItemBeginEdit();
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_item_add:
                return true;
            default:
                break;
        }
        return super.onActionItemClicked(actionMode, menuItem);
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        super.onDestroyActionMode(actionMode);
    }
}
