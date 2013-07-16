package com.lifecity.felux;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.lifecity.felux.items.Item;
import com.lifecity.felux.lights.DmxColorLight;
import com.lifecity.felux.lights.DmxLight;
import com.lifecity.felux.lights.Light;
import com.lifecity.felux.scenes.LightScene;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * A fragment representing a single Scene detail screen.
 * on handsets.
 */
public class LightSceneDetailFragment extends ItemDetailFragment<LightScene> implements ColorLightDialogFragment.ColorLightDialogListener, LightDialogFragment.LightDialogListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, ItemChangedListener, View.OnFocusChangeListener, AddLightSceneDialogFragment.AddLightSceneDialogListener, View.OnClickListener {
    private LightSceneLightListAdapter adapter;
    private Light currentLight;
    private CheckBox selectAll;
    private MenuItem removeLight;
    private MenuItem moveUpScene;
    private MenuItem moveDownScene;
    private ListView lightListView;
    private EditText nameEdit;
    private EditText holdEdit;
    private EditText fadeEdit;
    private Button previewButton;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LightSceneDetailFragment() {
        super(R.layout.fragment_light_scene_detail);
    }

    private void updateLights() {
        adapter.clear();
        for (Light light: item.getLights()) {
            adapter.add(light);
        }
    }

    @Override
    public void updateItemView() {
        if (nameEdit != null && holdEdit != null && fadeEdit != null && adapter != null && item != null) {
            nameEdit.setText(item.getName());
            holdEdit.setText(String.valueOf(item.getHold()));
            fadeEdit.setText(String.valueOf(item.getFade()));
            updateLights();
            adapter.notifyDataSetChanged();
        }
    }

    private void setControlsEnabled(boolean enabled) {
        if (!enabled) {
            if (nameEdit.hasFocus()) {
                nameEdit.clearFocus();
            } else if (holdEdit.hasFocus()) {
                holdEdit.clearFocus();
            } else if (fadeEdit.hasFocus()) {
                fadeEdit.clearFocus();
            }
        }

        nameEdit.setFocusable(enabled);
        nameEdit.setFocusableInTouchMode(enabled);
        holdEdit.setFocusable(enabled);
        holdEdit.setFocusableInTouchMode(enabled);
        fadeEdit.setFocusable(enabled);
        fadeEdit.setFocusableInTouchMode(enabled);
        lightListView.setEnabled(enabled);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        showHideKeyboard(view, hasFocus);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        nameEdit = (EditText)view.findViewById(R.id.light_scene_detail_name_edit);
        nameEdit.setOnFocusChangeListener(this);
        holdEdit = (EditText)view.findViewById(R.id.light_scene_detail_hold_edit);
        fadeEdit = (EditText)view.findViewById(R.id.light_scene_detail_fade_edit);
        previewButton = (Button)view.findViewById(R.id.light_scene_detail_preview_button);
        List<Light> adapterLights = new ArrayList<Light>(item.getLights().size());
        for (Light light: item.getLights()) {
            adapterLights.add(light);
        }

        adapter = new LightSceneLightListAdapter(
                getActivity(),
                R.layout.light_scene_light_list_row,
                adapterLights
        );

        adapter.setItemChangedListener(this);

        previewButton.setOnClickListener(this);

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
            dialog.setLight(dmxColorLight);
            dialog.setFeluxManager(manager);
            dialog.show(getActivity().getSupportFragmentManager(), "add_light_dialog_tag");
        } else if (currentLight instanceof DmxLight) {
            DmxLight dmxLight = (DmxLight)currentLight;
            LightDialogFragment dialog = new LightDialogFragment(this);
            dialog.setValue(dmxLight.getValue());
            dialog.setLight(dmxLight);
            dialog.setFeluxManager(manager);
            dialog.show(getActivity().getSupportFragmentManager(), "add_light_dialog_tag");
        }
    }

    @Override
    public void onColorSelected(int color) {
        if (currentLight != null) {
            ((DmxColorLight) currentLight).setColor(color);
            manager.showLight((DmxLight)currentLight);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onValueSelected(int value) {
        if (currentLight != null) {
            currentLight.setValue(value);
            manager.showLight((DmxLight)currentLight);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLightsSelected(List<Light> lights) {
        for (Light light: lights) {
            item.addLight(light);
            adapter.add(light);
        }
        //itemDetailCallbacks.onDetailItemUpdated(item);
        selectAll.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCanceled() {

    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.fragment_add_remove_move_menu, menu);
        menu.findItem(R.id.action_item_add).setTitle(R.string.action_item_add_light_label);
        moveUpScene = menu.findItem(R.id.action_item_moveup);
        moveDownScene = menu.findItem(R.id.action_item_movedown);
        removeLight = menu.findItem(R.id.action_item_remove);
        removeLight.setVisible(adapter.areAnyItemsChecked());
        setControlsEnabled(true);
        boolean result = super.onCreateActionMode(actionMode, menu);
        //updateLights();
        adapter.startEditMode();
        if (adapter.getCount() > 0) {
            selectAll.setVisibility(View.VISIBLE);
        }
        updateMoveIcons();
        return result;
    }

    public void moveLight(Light light, boolean down) {
        int oldPosition = adapter.getSingleCheckedItemPosition();
        int newPosition;

        if (oldPosition >= 0) {
            if (down) {
                if (oldPosition < (lightListView.getCount() - 1)) {
                    newPosition = oldPosition + 1;
                }  else {
                    return;
                }
            } else {
                if (oldPosition > 0) {
                    newPosition = oldPosition - 1;
                } else {
                    return;
                }
            }

            List<Light> lights = item.getLights();
            Light temp = lights.get(newPosition);
            lights.set(newPosition, light);
            lights.set(oldPosition, temp);
            updateLights();
            updateMoveIcons();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        ListIterator<Light> iterator;
        int position = adapter.getSingleCheckedItemPosition();
        switch (menuItem.getItemId()) {
            case R.id.action_item_add:
                List<Light> newLights = new ArrayList<Light>(manager.getLights().size());
                for (Light light: manager.getLights()) {
                    newLights.add((Light)light.copy());
                }
                iterator = newLights.listIterator();
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
            case R.id.action_item_moveup:
                moveLight(this.item.getLights().get(position), false);
                return true;
            case R.id.action_item_movedown:
                moveLight(this.item.getLights().get(position), true);
                return true;
            case R.id.action_item_remove:
                adapter.removeChecked();
                iterator = item.getLights().listIterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getChecked()) {
                        iterator.remove();
                    }
                }
                itemDetailCallbacks.onDetailItemUpdated(item);
                if (adapter.getCount() == 0) {
                    selectAll.setVisibility(View.GONE);
                }
                removeLight.setVisible(adapter.areAnyItemsChecked());
                return true;
            case R.id.action_item_cancel:
                actionMode.setTag("cancelled");
                actionMode.finish();
            default:
                break;
        }
        return super.onActionItemClicked(actionMode, menuItem);
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        boolean cancelled = actionMode.getTag() != null && actionMode.equals("cancelled");
        if (!cancelled) {
            if (!nameEdit.getText().toString().isEmpty()) {
                item.setName(nameEdit.getText().toString());
            }
            if (!holdEdit.getText().toString().isEmpty()) {
                item.setHold(Float.valueOf(holdEdit.getText().toString()));
            }
            if (!fadeEdit.getText().toString().isEmpty()) {
                item.setFade(Float.valueOf(fadeEdit.getText().toString()));
            }
        }
        adapter.stopEditMode();
        selectAll.setVisibility(View.GONE);
        setControlsEnabled(false);
        super.onDestroyActionMode(actionMode);
    }

    private void updateMoveIcons() {
        int position = adapter.getSingleCheckedItemPosition();
        if (position >= 0) {
            int size = lightListView.getCount();

            if (position >= 0 && size > 0) {
                moveUpScene.setVisible(position > 0);
                moveDownScene.setVisible(position < (size - 1));
            } else {
                moveUpScene.setVisible(false);
                moveDownScene.setVisible(false);
            }
        } else {
            moveUpScene.setVisible(false);
            moveDownScene.setVisible(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        adapter.selectAll(checked);
        removeLight.setVisible(checked);
    }

    @Override
    public void onItemCheckedChanged(Item item) {
        updateMoveIcons();
        removeLight.setVisible(adapter.areAnyItemsChecked());
    }

    @Override
    public void onClick(View view) {
        if (view == previewButton && manager != null) {
            manager.showScene(item);
        }
    }
}
