package com.lifecity.felux;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.lifecity.felux.items.Item;
import com.lifecity.felux.lights.DmxColorLight;
import com.lifecity.felux.lights.DmxGroupLight;
import com.lifecity.felux.lights.DmxLight;
import com.lifecity.felux.lights.Light;

/**
 * A fragment representing a single Light detail screen.
 * on handsets.
 */
public class LightDetailFragment extends ItemDetailFragment<Light> implements View.OnFocusChangeListener {
    private EditText nameEdit;
    private NumberPicker addrEdit;
    private TextView typeText;
    private TextView addrLabel;
    private TextView endAddrLabel;
    private EditText endAddrEdit;
    private boolean actionCancelled;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LightDetailFragment() {
        super(R.layout.fragment_light_detail);
    }

    private void setControlsEnabled(boolean enabled) {
        if (!enabled) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (nameEdit.hasFocus()) {
                nameEdit.clearFocus();
                /*
                nameEdit.dispatchWindowFocusChanged(false);
                imm.hideSoftInputFromWindow(nameEdit.getWindowToken(), 0);
                */
            } else if (endAddrEdit.hasFocus()) {
                endAddrEdit.clearFocus();
                endAddrEdit.dispatchWindowFocusChanged(false);
                imm.hideSoftInputFromWindow(endAddrEdit.getWindowToken(), 0);
            } else if (addrEdit.hasFocus()) {
                addrEdit.clearFocus();
                /*
                addrEdit.dispatchWindowFocusChanged(false);
                imm.hideSoftInputFromWindow(addrEdit.getWindowToken(), 0);
                */
            }
        }

        nameEdit.setFocusable(enabled);
        nameEdit.setFocusableInTouchMode(enabled);
        addrEdit.setFocusable(enabled);
        addrEdit.setFocusableInTouchMode(enabled);
        addrEdit.setEnabled(enabled);
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
        if (hasFocus) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            view.dispatchWindowFocusChanged(true);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

        } else {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            view.dispatchWindowFocusChanged(false);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        nameEdit = (EditText)getView().findViewById(R.id.light_detail_name_edit);
        nameEdit.setOnFocusChangeListener(this);
        /*
        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    detailCallbacks.onItemNameChanged(item);
                }
            }
        });
        */

        typeText = (TextView)getView().findViewById(R.id.light_detail_type_text);
        addrLabel = (TextView)getView().findViewById(R.id.light_detail_addr_label);
        endAddrLabel = (TextView)getView().findViewById(R.id.light_detail_end_addr_label);
        addrEdit = (NumberPicker)getView().findViewById(R.id.light_detail_addr_picker);
        endAddrEdit = (EditText)getView().findViewById(R.id.light_detail_end_addr_edit);

        addrEdit.setOnFocusChangeListener(this);
        addrEdit.setMinValue(1);
        addrEdit.setMaxValue(512);

        typeText.setText("");
        endAddrEdit.setText("");
        endAddrEdit.setVisibility(View.INVISIBLE);
        endAddrLabel.setVisibility(View.INVISIBLE);
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
        if (!actionCancelled) {
            if (!nameEdit.getText().toString().isEmpty()) {
                item.setName(nameEdit.getText().toString());
                detailCallbacks.onItemNameChanged(item);
            }
            if (item instanceof DmxLight && !addrEdit.toString().isEmpty()) {
                DmxLight light = (DmxLight)item;
                try {
                    int addr = addrEdit.getValue();
                    light.setAddress(addr);
                } catch (NumberFormatException e) {}
            }
            if (item instanceof DmxGroupLight && !endAddrEdit.toString().isEmpty()) {
                DmxGroupLight light = (DmxGroupLight)item;
                try {
                    int addr = Integer.valueOf(endAddrEdit.getText().toString());
                    light.setEndAddress(addr);
                } catch (NumberFormatException e) {}
            }
        } else {
            updateItemView(true);
        }
        setControlsEnabled(false);
        super.onDestroyActionMode(actionMode);
    }

    public void updateItemView(boolean updateValues) {
        if (updateValues) {
            if (nameEdit != null) {
                nameEdit.setText(item != null ? item.getName() : "");
            }
        }
        if (typeText != null) {
            if (item == null) {
                typeText.setText("");
            } else {
                if (item instanceof DmxColorLight) {
                    typeText.setText(R.string.light_type_color);
                    addrLabel.setText(R.string.light_detail_addr_label);
                    endAddrLabel.setVisibility(View.INVISIBLE);
                    endAddrEdit.setVisibility(View.INVISIBLE);
                    if (updateValues) {
                        int address = ((DmxLight) item).getAddress();
                        addrEdit.setValue(address > 0 ? address : 1);
                    }
                } else if (item instanceof DmxGroupLight) {
                    typeText.setText(R.string.light_type_group);
                    addrLabel.setText(R.string.light_detail_start_addr_label);
                    endAddrLabel.setText(R.string.light_detail_end_addr_label);
                    endAddrLabel.setVisibility(View.VISIBLE);
                    endAddrEdit.setVisibility(View.VISIBLE);
                    if (updateValues) {
                        int startAddress = ((DmxGroupLight) item).getAddress();
                        int endAddress = ((DmxGroupLight) item).getEndAddress();
                        addrEdit.setValue(startAddress > 0 ? startAddress : 1);
                        endAddrEdit.setText(endAddress > 0 ? Integer.toString(endAddress) : "");
                    }
                } else if (item instanceof DmxLight) {
                    typeText.setText(R.string.light_type_basic);
                    addrLabel.setText(R.string.light_detail_addr_label);
                    addrEdit.setValue(((DmxLight) item).getAddress());
                    endAddrLabel.setVisibility(View.INVISIBLE);
                    endAddrEdit.setVisibility(View.INVISIBLE);
                    if (updateValues) {
                        int address = ((DmxLight) item).getAddress();
                        addrEdit.setValue(address > 0 ? address : 1);
                    }
                }
            }
        }
    }
}
