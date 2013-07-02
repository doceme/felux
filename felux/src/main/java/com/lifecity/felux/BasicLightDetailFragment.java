package com.lifecity.felux;

import android.os.Bundle;
import android.view.*;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import com.lifecity.felux.lights.DmxLight;
import com.lifecity.felux.lights.Light;

/**
 * A fragment representing a single Light detail screen.
 * on handsets.
 */
public class BasicLightDetailFragment extends ItemDetailFragment<Light> implements View.OnFocusChangeListener, SeekBar.OnSeekBarChangeListener {
    private EditText nameEdit;
    private NumberPicker addrEdit;
    private TextView valueLabel;
    private SeekBar valueSeekBar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BasicLightDetailFragment() {
        super(R.layout.fragment_basic_light_detail);
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
        addrEdit.setEnabled(enabled);
        addrEdit.setFocusable(enabled);
        addrEdit.setFocusableInTouchMode(enabled);
        valueSeekBar.setEnabled(enabled);
        valueSeekBar.setFocusable(enabled);
        valueSeekBar.setFocusableInTouchMode(enabled);
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

        addrEdit = (NumberPicker)getView().findViewById(R.id.light_detail_addr_picker);
        valueLabel = (TextView)getView().findViewById(R.id.light_detail_light_value_label);
        valueSeekBar = (SeekBar)getView().findViewById(R.id.light_detail_light_value);

        addrEdit.setOnFocusChangeListener(this);
        addrEdit.setMinValue(1);
        addrEdit.setMaxValue(512);

        valueSeekBar.setMax(255);
        valueSeekBar.setOnSeekBarChangeListener(this);

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

            DmxLight light = (DmxLight)item;
            light.setAddress(addrEdit.getValue());
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
                DmxLight light = (DmxLight)item;
                int startAddress = light.getAddress();
                addrEdit.setValue(startAddress > 0 ? startAddress : 1);
                valueLabel.setText(String.valueOf(light.getPercent()) + "%");
                valueSeekBar.setProgress(light.getValue());
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        item.setValue(progress);
        valueLabel.setText(String.valueOf(item.getPercent()) + "%");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

