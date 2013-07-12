package com.lifecity.felux;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import com.lifecity.felux.lights.DmxLight;
import com.lifecity.felux.lights.Light;

/**
 * Created by scaudle on 6/7/13.
 */
public class LightDialogFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener {
    private int value;
    private LightDialogListener listener;
    private Switch previewSwitch;
    private DmxLight light;
    private FeluxManager manager;
    private TextView valueLabel;

    public interface LightDialogListener {
        public void onValueSelected(int value);
        public void onCanceled();
    }

    public LightDialogFragment(LightDialogListener listener) {
        this.listener = listener;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setLight(DmxLight light) {
        this.light = light;
    }

    public void setFeluxManager(FeluxManager manager) {
       this.manager = manager;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_basic_light_dialog, null);
        builder.setView(view);
        builder.setTitle(R.string.light_seekbar_title);

        previewSwitch = (Switch)view.findViewById(R.id.basic_light_preview);
        valueLabel = (TextView)view.findViewById(R.id.basic_light_dialog_value_label);

        SeekBar valueSeekBar = (SeekBar)view.findViewById(R.id.basic_light_dialog_value);
        valueSeekBar.setMax(255);
        valueSeekBar.setOnSeekBarChangeListener(this);

        valueLabel.setText(String.valueOf(light.getPercent()) + "%");
        valueSeekBar.setProgress(light.getValue());

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (listener != null) {
                    listener.onValueSelected(value);
                    if (previewSwitch.isChecked() && manager != null && light != null) {
                        manager.showLight(light, value);
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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        value = progress;
        valueLabel.setText(String.valueOf(DmxLight.getPercent(progress)) + "%");
        if (previewSwitch.isChecked() && manager != null && light != null) {
                manager.showLight(light, value);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
