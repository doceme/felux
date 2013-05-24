package com.lifecity.felux;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
public class LightDetailFragment extends ItemDetailFragment<Light> {
    public static final String TAG = "light_detail_tag";
    private EditText nameEdit;
    private EditText addrEdit;
    private TextView typeText;
    private TextView addrLabel;
    private TextView endAddrLabel;
    private EditText endAddrEdit;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LightDetailFragment(ItemDetailCallbacks<Item> detailCallbacks) {
        super(R.layout.fragment_light_detail, detailCallbacks);
    }

    private void setControlsEnabled(boolean enabled) {
        if (!enabled) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (nameEdit.hasFocus()) {
                nameEdit.clearFocus();
                nameEdit.dispatchWindowFocusChanged(false);
                imm.hideSoftInputFromWindow(nameEdit.getWindowToken(), 0);
            } else if (addrEdit.hasFocus()) {
                addrEdit.clearFocus();
                addrEdit.dispatchWindowFocusChanged(false);
                imm.hideSoftInputFromWindow(addrEdit.getWindowToken(), 0);
            } else if (endAddrEdit.hasFocus()) {
                endAddrEdit.clearFocus();
                endAddrEdit.dispatchWindowFocusChanged(false);
                imm.hideSoftInputFromWindow(endAddrEdit.getWindowToken(), 0);
            }
        }

        nameEdit.setFocusable(enabled);
        nameEdit.setFocusableInTouchMode(enabled);
        addrEdit.setFocusable(enabled);
        addrEdit.setFocusableInTouchMode(enabled);
        endAddrEdit.setFocusable(enabled);
        endAddrEdit.setFocusableInTouchMode(enabled);

    }

    private void beginEdit() {
        nameEdit.requestFocus();
        nameEdit.dispatchWindowFocusChanged(true);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(nameEdit, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onItemAdded(Light light) {
        super.onItemAdded(light);
        nameEdit.setText(light.getName());
        setControlsEnabled(true);
        beginEdit();
        updateItemView(false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        nameEdit = (EditText)getView().findViewById(R.id.light_detail_name_edit);
        nameEdit.setSelectAllOnFocus(true);
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

        typeText = (TextView)getView().findViewById(R.id.light_detail_type_text);
        addrLabel = (TextView)getView().findViewById(R.id.light_detail_addr_label);
        endAddrLabel = (TextView)getView().findViewById(R.id.light_detail_end_addr_label);
        addrEdit = (EditText)getView().findViewById(R.id.light_detail_addr_edit);
        endAddrEdit = (EditText)getView().findViewById(R.id.light_detail_end_addr_edit);

        typeText.setText("");
        addrEdit.setText("");
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
    public void onItemBeginEdit() {
        setControlsEnabled(true);
        beginEdit();
    }

    @Override
    public void onItemEndEdit() {
        super.onItemEndEdit();
        if (!nameEdit.getText().toString().isEmpty()) {
            item.setName(nameEdit.getText().toString());
        }
        if (item instanceof DmxLight && !addrEdit.toString().isEmpty()) {
            DmxLight light = (DmxLight)item;
            try {
                int addr = Integer.valueOf(addrEdit.getText().toString());
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
        setControlsEnabled(false);
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
                        addrEdit.setText(address > 0 ? Integer.toString(address) : "");
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
                        addrEdit.setText(startAddress > 0 ? Integer.toString(startAddress) : "");
                        endAddrEdit.setText(endAddress > 0 ? Integer.toString(endAddress) : "");
                    }
                } else if (item instanceof DmxLight) {
                    typeText.setText(R.string.light_type_basic);
                    addrLabel.setText(R.string.light_detail_addr_label);
                    addrEdit.setText(Integer.toString(((DmxLight) item).getAddress()));
                    endAddrLabel.setVisibility(View.INVISIBLE);
                    endAddrEdit.setVisibility(View.INVISIBLE);
                    if (updateValues) {
                        int address = ((DmxLight) item).getAddress();
                        addrEdit.setText(address > 0 ? Integer.toString(address) : "");
                    }
                }
            }
        }
    }
}
