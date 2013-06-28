package com.lifecity.felux;

import android.os.Bundle;
import android.view.*;
import android.widget.EditText;
import android.widget.NumberPicker;
import com.lifecity.felux.lights.DmxGroupLight;
import com.lifecity.felux.lights.Light;

/**
 * A fragment representing a single Light detail screen.
 * on handsets.
 */
public class GroupLightDetailFragment extends ItemDetailFragment<Light> implements View.OnFocusChangeListener {
    private EditText nameEdit;
    private NumberPicker startAddrEdit;
    private NumberPicker endAddrEdit;
    private boolean actionCancelled;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupLightDetailFragment() {
        super(R.layout.fragment_group_light_detail);
    }

    private void setControlsEnabled(boolean enabled) {
        if (!enabled) {
            if (nameEdit.hasFocus()) {
                nameEdit.clearFocus();
            } else if (startAddrEdit.hasFocus()) {
                startAddrEdit.clearFocus();
            } else if (endAddrEdit.hasFocus()) {
                endAddrEdit.clearFocus();
            }
            startAddrEdit.clearFocus();
            endAddrEdit.clearFocus();
        }

        nameEdit.setFocusable(enabled);
        nameEdit.setFocusableInTouchMode(enabled);
        startAddrEdit.setEnabled(enabled);
        startAddrEdit.setFocusable(enabled);
        startAddrEdit.setFocusableInTouchMode(enabled);
        endAddrEdit.setEnabled(enabled);
        endAddrEdit.setFocusable(enabled);
        endAddrEdit.setFocusableInTouchMode(enabled);
    }

    @Override
    public void onItemAdded(Light light) {
        super.onItemAdded(light);
        nameEdit.setText(light.getName());
        setControlsEnabled(true);
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

        startAddrEdit = (NumberPicker)getView().findViewById(R.id.light_detail_addr_picker);
        endAddrEdit = (NumberPicker)getView().findViewById(R.id.light_detail_end_addr_picker);

        startAddrEdit.setOnFocusChangeListener(this);
        startAddrEdit.setMinValue(1);
        startAddrEdit.setMaxValue(512);

        endAddrEdit.setOnFocusChangeListener(this);
        endAddrEdit.setMinValue(1);
        endAddrEdit.setMaxValue(512);

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
        actionCancelled = false;
        setControlsEnabled(true);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_item_cancel:
                actionCancelled = true;
                if (actionMode != null) {
                    actionMode.finish();
                }
                return true;
            default:
                break;
        }
        return super.onActionItemClicked(actionMode, menuItem);
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        setControlsEnabled(false);
        if (!actionCancelled) {
            if (!nameEdit.getText().toString().isEmpty()) {
                item.setName(nameEdit.getText().toString());
                detailCallbacks.onItemDetailUpdated(item);
            }

            DmxGroupLight light = (DmxGroupLight)item;
            light.setAddress(startAddrEdit.getValue());
            light.setEndAddress(endAddrEdit.getValue());
        } else {
            updateItemView(true);
        }
        super.onDestroyActionMode(actionMode);
    }

    public void updateItemView(boolean updateValues) {
        if (updateValues) {
            if (nameEdit != null) {
                nameEdit.setText(item != null ? item.getName() : "");
            }
        }
        if (updateValues && startAddrEdit != null && endAddrEdit != null && item != null) {
            int startAddress = ((DmxGroupLight) item).getAddress();
            int endAddress = ((DmxGroupLight) item).getEndAddress();
            startAddrEdit.setValue(startAddress > 0 ? startAddress : 1);
            endAddrEdit.setValue(endAddress > 0 ? endAddress : 1);
        }
    }
}
