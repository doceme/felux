package com.lifecity.felux;

import android.os.Bundle;
import android.view.*;
import android.widget.EditText;
import android.widget.NumberPicker;
import com.lifecity.felux.colorpicker.ColorPicker;
import com.lifecity.felux.colorpicker.SVBar;
import com.lifecity.felux.lights.DmxColorLight;
import com.lifecity.felux.lights.Light;

/**
 * A fragment representing a single Light detail screen.
 * on handsets.
 */
public class ColorLightDetailFragment extends ItemDetailFragment<Light> implements View.OnFocusChangeListener, ColorPicker.OnColorChangedListener {
    private EditText nameEdit;
    private NumberPicker addrEdit;
    private ColorPicker colorPicker;
    private SVBar svBar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ColorLightDetailFragment() {
        super(R.layout.fragment_color_light_detail);
    }

    private void setControlsEnabled(boolean enabled) {
        if (!enabled) {
            if (nameEdit.hasFocus()) {
                nameEdit.clearFocus();
            } else if (addrEdit.hasFocus()) {
                addrEdit.clearFocus();
            }
        }

        nameEdit.setFocusable(enabled);
        nameEdit.setFocusableInTouchMode(enabled);
        addrEdit.setFocusable(enabled);
        addrEdit.setFocusableInTouchMode(enabled);
        addrEdit.setEnabled(enabled);
        colorPicker.setEnabled(enabled);
        svBar.setEnabled(enabled);

    }

    /*
    @Override
    public void onItemAdded(Light light) {
        super.onItemAdded(light);
        updateItemView(true);
        startActionMode();
    }
    */

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        showHideKeyboard(view, hasFocus);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        nameEdit = (EditText)view.findViewById(R.id.light_detail_name_edit);
        addrEdit = (NumberPicker)view.findViewById(R.id.light_detail_addr_picker);
        colorPicker = (ColorPicker)view.findViewById(R.id.light_detail_color_picker);
        svBar = (SVBar)view.findViewById(R.id.light_detail_color_svbar);

        nameEdit.setOnFocusChangeListener(this);
        addrEdit.setOnFocusChangeListener(this);
        addrEdit.setMinValue(1);
        addrEdit.setMaxValue(512);

        colorPicker.addSVBar(svBar);
        colorPicker.setOnColorChangedListener(this);

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
            DmxColorLight light = (DmxColorLight)item;
            light.setAddress(addrEdit.getValue());
            light.setColor(colorPicker.getColor());
        }
        setControlsEnabled(false);
        super.onDestroyActionMode(actionMode);
    }

    public void updateItemView(boolean updateValues) {
        if (updateValues) {
            if (nameEdit != null) {
                nameEdit.setText(item != null ? item.getName() : "");
            }

            if (addrEdit != null && item != null) {
                DmxColorLight light = (DmxColorLight)item;
                int address = light.getAddress();
                int color = light.getColor();
                addrEdit.setValue(address > 0 ? address : 1);
                colorPicker.setColor(color);
                colorPicker.setNewCenterColor(color);
                colorPicker.setOldCenterColor(color);
            }
        }
    }

    @Override
    public void onColorChanged(int color) {
        /* TODO: Update light hardware */
    }
}
