package com.lifecity.felux;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lifecity.felux.colorpicker.ColorPicker;
import com.lifecity.felux.colorpicker.SVBar;
import com.lifecity.felux.lights.Light;

/**
 * Created by scaudle on 6/7/13.
 */
public class ColorLightDialogFragment extends DialogFragment implements ColorPicker.OnColorChangedListener {
    private int color;
    private ColorLightDialogListener listener;

    @Override
    public void onColorChanged(int color) {
        this.color = color;
    }

    public interface ColorLightDialogListener {
        public void onColorSelected(int color);
        public void onCanceled();
    }

    public ColorLightDialogFragment(ColorLightDialogListener listener) {
        this.listener = listener;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_color_light, null);
        builder.setView(view);
        builder.setTitle(R.string.light_colorpicker_title);

        SVBar svBar = (SVBar)view.findViewById(R.id.svbar);
        ColorPicker picker = (ColorPicker)view.findViewById(R.id.picker);
        picker.addSVBar(svBar);
        picker.setColor(color);
        picker.setNewCenterColor(color);
        picker.setOldCenterColor(color);
        picker.setOnColorChangedListener(this);

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (listener != null) {
                    listener.onColorSelected(color);
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
}
