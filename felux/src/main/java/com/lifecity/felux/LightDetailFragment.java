package com.lifecity.felux;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

    @Override
    public void itemAdded(Light light) {
        super.itemAdded(light);
        nameEdit.setText(light.getName());
        nameEdit.requestFocus();
        nameEdit.dispatchWindowFocusChanged(true);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(nameEdit, InputMethodManager.SHOW_IMPLICIT);
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
                    item.setName(editable.toString());
                    detailCallbacks.onItemNameChanged(item);
                }
            }
        });

        typeText = (TextView)getView().findViewById(R.id.light_detail_type_text);
        addrLabel = (TextView)getView().findViewById(R.id.light_detail_addr_label);
        endAddrLabel = (TextView)getView().findViewById(R.id.light_detail_end_addr_label);
        addrEdit = (EditText)getView().findViewById(R.id.light_detail_addr_edit);
        endAddrEdit = (EditText)getView().findViewById(R.id.light_detail_end_addr_edit);

        addrEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    DmxLight light = (DmxLight)item;
                    light.setAddress(Integer.valueOf(editable.toString()));
                }
            }
        });

        endAddrEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (item instanceof DmxGroupLight && !editable.toString().isEmpty()) {
                    DmxGroupLight light = (DmxGroupLight)item;
                    light.setEndAddress(Integer.valueOf(editable.toString()));
                }
            }
        });

        typeText.setText("");
        addrEdit.setText("");
        endAddrEdit.setText("");
        endAddrEdit.setVisibility(View.INVISIBLE);
        endAddrLabel.setVisibility(View.INVISIBLE);

        super.onViewCreated(view, savedInstanceState);
    }

    public void updateItemView() {
        updateItemView(true);
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
