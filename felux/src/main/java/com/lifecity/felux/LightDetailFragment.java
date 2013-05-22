package com.lifecity.felux;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.lifecity.felux.items.Item;
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
        nameEdit.requestFocus();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        nameEdit = (EditText)getView().findViewById(R.id.light_detail_name_edit);
        nameEdit.setSelectAllOnFocus(true);
        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                item.name = editable.toString();
                detailCallbacks.onItemNameChanged(item);
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

        super.onViewCreated(view, savedInstanceState);
    }

    public void updateItemView() {
        if (nameEdit != null) {
            nameEdit.setText(item != null ? item.name : "");
        }
        if (typeText != null) {
            if (item == null) {
                typeText.setText("");
            } else {
            }
        }
    }
}
