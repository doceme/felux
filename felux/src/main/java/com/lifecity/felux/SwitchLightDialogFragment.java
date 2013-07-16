package com.lifecity.felux;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.lifecity.felux.lights.DmxLight;
import com.lifecity.felux.lights.DmxSwitchLight;

/**
 * Created by scaudle on 6/7/13.
 */
public class SwitchLightDialogFragment extends DialogFragment implements CompoundButton.OnCheckedChangeListener {
    private boolean state;
    private SwitchLightDialogListener listener;
    private Switch previewSwitch;
    private ToggleButton stateToggle;
    private DmxSwitchLight light;
    private FeluxManager manager;

    public interface SwitchLightDialogListener {
        public void onSwitchSelected(boolean state);
        public void onCanceled();
    }

    public SwitchLightDialogFragment(SwitchLightDialogListener listener) {
        this.listener = listener;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setLight(DmxSwitchLight light) {
        this.light = light;
    }

    public void setFeluxManager(FeluxManager manager) {
       this.manager = manager;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_switch_light_dialog, null);
        builder.setView(view);
        builder.setTitle(R.string.light_seekbar_title);

        previewSwitch = (Switch)view.findViewById(R.id.switch_light_preview);
        previewSwitch.setOnCheckedChangeListener(this);

        stateToggle = (ToggleButton)view.findViewById(R.id.switch_light_dialog_light_switch);
        stateToggle.setOnCheckedChangeListener(this);
        stateToggle.setChecked(state);

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (listener != null) {
                    listener.onSwitchSelected(state);
                    if (previewSwitch.isChecked() && manager != null && light != null) {
                        manager.showLight(light, state ? DmxLight.MAX_VALUE : DmxLight.MIN_VALUE);
                    }
                }
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (listener != null) {
                    listener.onCanceled();
                }
            }

        });

        return builder.create();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        state = stateToggle.isChecked();
        if (previewSwitch.isChecked() && manager != null && light != null) {
            manager.showLight(light, state ? DmxLight.MAX_VALUE : DmxLight.MIN_VALUE);
        }
    }
}
