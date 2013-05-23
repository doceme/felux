package com.lifecity.felux;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.lifecity.felux.lights.DmxColorLight;
import com.lifecity.felux.lights.DmxGroupLight;
import com.lifecity.felux.lights.DmxLight;
import com.lifecity.felux.lights.Light;

public class AddLightDialogFragment extends DialogFragment {
    private Light light;
    private AddLightDialogListener listener;

    public interface AddLightDialogListener {
        public void onTypeSelected(Light light);
        public void onCanceled();
    }

    public AddLightDialogFragment(AddLightDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.light_types_label)
                .setItems(R.array.light_types, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            light = new DmxLight();
                        } else if (which == 1) {
                            light = new DmxGroupLight();
                        } else if (which == 2) {
                            light = new DmxColorLight();
                        }
                        listener.onTypeSelected(light);
                    }
                });

                builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        light = null;
                        if (listener != null) {
                            listener.onCanceled();
                        }
                    }
                });

        return builder.create();
    }
}
