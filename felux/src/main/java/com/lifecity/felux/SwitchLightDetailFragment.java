package com.lifecity.felux;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.lifecity.felux.lights.DmxLight;
import com.lifecity.felux.lights.DmxSwitchLight;
import com.lifecity.felux.lights.Light;

/**
 * A fragment representing a single Light detail screen.
 * on handsets.
 */
public class SwitchLightDetailFragment extends ItemDetailFragment<Light> implements View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener {
    private EditText nameEdit;
    private NumberPicker univEdit;
    private NumberPicker addrEdit;
    private ToggleButton stateToggle;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SwitchLightDetailFragment() {
        super(R.layout.fragment_switch_light_detail);
    }

    private void setControlsEnabled(boolean enabled) {
        if (!enabled) {
            if (nameEdit.hasFocus()) {
                nameEdit.clearFocus();
            } else if (univEdit.hasFocus()) {
                univEdit.clearFocus();
            } else if (addrEdit.hasFocus()) {
                addrEdit.clearFocus();
            }
        }

        nameEdit.setFocusable(enabled);
        nameEdit.setFocusableInTouchMode(enabled);
        univEdit.setEnabled(enabled);
        univEdit.setFocusable(enabled);
        univEdit.setFocusableInTouchMode(enabled);
        addrEdit.setEnabled(enabled);
        addrEdit.setFocusable(enabled);
        addrEdit.setFocusableInTouchMode(enabled);
        stateToggle.setEnabled(enabled);
    }

    @Override
    public void onItemAdded(Light light) {
        super.onItemAdded(light);
        if (nameEdit != null) {
            nameEdit.setText(light.getName());
            setControlsEnabled(true);
        }
        updateItemView(false);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        showHideKeyboard(view, hasFocus);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        nameEdit = (EditText)getView().findViewById(R.id.light_detail_name_edit);
        nameEdit.setOnFocusChangeListener(this);

        univEdit = (NumberPicker)getView().findViewById(R.id.light_detail_universe_picker);
        addrEdit = (NumberPicker)getView().findViewById(R.id.light_detail_addr_picker);
        stateToggle = (ToggleButton)getView().findViewById(R.id.light_detail_light_switch);

        univEdit.setOnFocusChangeListener(this);
        univEdit.setMinValue(0);
        univEdit.setMaxValue(2);

        addrEdit.setOnFocusChangeListener(this);
        addrEdit.setMinValue(1);
        addrEdit.setMaxValue(512);

        stateToggle.setOnCheckedChangeListener(this);

        setControlsEnabled(false);

        super.onViewCreated(view, savedInstanceState);
    }

    public void updateItemView() {
        updateItemView(true);
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.fragment_action_cancel, menu);
        setControlsEnabled(true);
        return super.onCreateActionMode(actionMode, menu);
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_item_cancel:
                actionMode.setTag("cancelled");
                actionMode.finish();
                return true;
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

            DmxSwitchLight light = (DmxSwitchLight)item;
            light.setUniverse(univEdit.getValue());
            light.setAddress(addrEdit.getValue());
            light.set(stateToggle.isChecked());
        }
        setControlsEnabled(false);
        super.onDestroyActionMode(actionMode);
    }

    public void updateItemView(boolean updateValues) {
        if (updateValues) {
            if (nameEdit != null) {
                nameEdit.setText(item != null ? item.getName() : "");
            }

            if (univEdit != null && addrEdit != null && item != null) {
                DmxSwitchLight light = (DmxSwitchLight)item;
                int address = light.getAddress();
                int universe = light.getUniverse();
                univEdit.setValue(universe < 0 ? 0 : universe);
                addrEdit.setValue(address > 0 ? address : 1);
                stateToggle.setChecked(light.isOn());
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (isResumed()) {
            int value = checked ? DmxLight.MAX_VALUE : DmxLight.MIN_VALUE;
                if (item.getValue() != value) {
                item.setValue(value);
                if (manager != null) {
                    manager.showBaseLight((DmxSwitchLight) item, value);
                }
            }
        }
    }
}

