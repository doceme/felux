package com.lifecity.felux;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.lifecity.felux.items.Item;
import com.lifecity.felux.lights.DmxColorLight;
import com.lifecity.felux.lights.Light;
import com.lifecity.felux.scenes.LightScene;

/**
 * A fragment representing a single Scene detail screen.
 * on handsets.
 */
public class LightSceneDetailFragment extends ItemDetailFragment<LightScene> implements ColorLightDialogFragment.ColorLightDialogListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, ItemChangedListener, View.OnFocusChangeListener {
    private LightSceneLightListAdapter adapter;
    private Light currentLight;
    private CheckBox selectAll;
    private MenuItem removeLight;
    private ListView lightListView;
    private EditText nameEdit;

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

    private void setControlsEnabled(boolean enabled) {
        if (!enabled) {
            if (nameEdit.hasFocus()) {
                nameEdit.clearFocus();
            }
        }

        nameEdit.setFocusable(enabled);
        nameEdit.setFocusableInTouchMode(enabled);
        lightListView.setEnabled(enabled);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        showHideKeyboard(view, hasFocus);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        adapter = new LightSceneLightListAdapter(
                getActivity(),
                R.layout.light_scene_light_list_row,
                manager.getLights()
        );

        adapter.setItemChangedListener(this);

        lightListView = (ListView)rootView.findViewById(R.id.light_scene_detail_light_list);
        TextView nameTextView = (TextView)rootView.findViewById(R.id.light_scene_detail_name_edit);
        selectAll =(CheckBox)rootView.findViewById(R.id.light_scene_detail_lights_select_all);
        selectAll.setOnCheckedChangeListener(this);
        nameTextView.setText(item.getName());
        lightListView.setAdapter(adapter);
        lightListView.setItemsCanFocus(false);
        lightListView.setOnItemClickListener(this);

        nameEdit = (EditText)rootView.findViewById(R.id.light_scene_detail_name_edit);
        nameEdit.setOnFocusChangeListener(this);

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
        removeLight = (MenuItem)menu.findItem(R.id.action_item_remove);
        removeLight.setVisible(adapter.areAnyItemsChecked());
        adapter.startEditMode();
        selectAll.setVisibility(View.VISIBLE);
        onItemBeginEdit();
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_item_add:
                return true;
            case R.id.action_item_remove:
                adapter.removeChecked();
                removeLight.setVisible(adapter.areAnyItemsChecked());
                return true;
            default:
                break;
        }
        return super.onActionItemClicked(actionMode, menuItem);
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        super.onDestroyActionMode(actionMode);
        adapter.stopEditMode();
        selectAll.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        adapter.selectAll(checked);
        removeLight.setVisible(checked);
    }

    @Override
    public void onItemCheckedChanged(Item item) {
        removeLight.setVisible(adapter.areAnyItemsChecked());
    }
}
