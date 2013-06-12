package com.lifecity.felux;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * A fragment representing a single Scene detail screen.
 * on handsets.
 */
public class LightSceneDetailFragment extends ItemDetailFragment<LightScene> implements ColorLightDialogFragment.ColorLightDialogListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, ItemChangedListener, View.OnFocusChangeListener, AddLightSceneDialogFragment.AddLightSceneDialogListener {
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
        adapter.clear();
        for (Light light: item.getLights()) {
            adapter.add(light);
        }
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

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        adapter = new LightSceneLightListAdapter(
                getActivity(),
                R.layout.light_scene_light_list_row,
                item.getLights()
        );

        adapter.setItemChangedListener(this);

        return rootView;
    }
    */

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        nameEdit = (EditText)view.findViewById(R.id.light_scene_detail_name_edit);
        nameEdit.setOnFocusChangeListener(this);

        adapter = new LightSceneLightListAdapter(
                getActivity(),
                R.layout.light_scene_light_list_row,
                item.getLights()
        );

        adapter.setItemChangedListener(this);

        lightListView = (ListView)view.findViewById(R.id.light_scene_detail_light_list);
        TextView nameTextView = (TextView)view.findViewById(R.id.light_scene_detail_name_edit);
        selectAll =(CheckBox)view.findViewById(R.id.light_scene_detail_lights_select_all);
        selectAll.setOnCheckedChangeListener(this);
        nameTextView.setText(item.getName());
        lightListView.setAdapter(adapter);
        lightListView.setItemsCanFocus(false);
        lightListView.setOnItemClickListener(this);

        setControlsEnabled(false);

        super.onViewCreated(view, savedInstanceState);
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
    public void onLightSelected(Light light) {
        item.addLight(light);
        selectAll.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
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
        if (adapter.getCount() > 0) {
            selectAll.setVisibility(View.VISIBLE);
        }
        setControlsEnabled(true);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_item_add:
                List<Light> newLights = new ArrayList<Light>(manager.getLights().size());
                for (Light light: manager.getLights()) {
                    newLights.add(light);
                }
                ListIterator<Light> iterator = newLights.listIterator();
                while (iterator.hasNext()) {
                    if (item.hasLight(iterator.next())) {
                        iterator.remove();
                    }
                }
                if (newLights.size() > 0) {
                    AddLightSceneDialogFragment dialog = new AddLightSceneDialogFragment(this, newLights);
                    dialog.show(getActivity().getSupportFragmentManager(), "add_light_dialog_tag");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.light_scene_detail_light_already_added)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, null)
                    .show();
                }
                break;
            case R.id.action_item_remove:
                adapter.removeChecked();
                if (adapter.getCount() == 0) {
                    selectAll.setVisibility(View.GONE);
                }
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
