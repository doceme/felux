package com.lifecity.felux;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;

import android.widget.Toast;
import com.lifecity.felux.lights.DmxColorLight;
import com.lifecity.felux.lights.DmxGroupLight;
import com.lifecity.felux.lights.DmxLight;
import com.lifecity.felux.lights.Light;

import java.util.ArrayList;
import java.util.List;

public class AddLightSceneDialogFragment extends DialogFragment {
    private Light light;
    private List<Light> lights;
    private List<Light> selectedLights;
    private AddLightSceneDialogListener listener;
    private ArrayAdapter<Light> adapter;
    boolean[] checkedLights;

    public interface AddLightSceneDialogListener {
        public void onLightsSelected(List<Light> lights);
        public void onCanceled();
    }

    public AddLightSceneDialogFragment(AddLightSceneDialogListener listener, List<Light> lights) {
        this.listener = listener;
        this.lights = lights;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] lightNames = new String[lights.size()];
        checkedLights = new boolean[lights.size()];

        /* Initialize light names */
        for (int i = 0; i < lights.size(); i++) {
            lightNames[i] = lights.get(i).getName();
            checkedLights[i] = false;
        }

        builder.setTitle(R.string.select_light_label)
                .setMultiChoiceItems(lightNames, checkedLights, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                        checkedLights[which] = isChecked;
                    }
                })
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (listener != null) {
                            List<Light> selectedLights = new ArrayList<Light>();
                            for (int i = 0; i < lights.size(); i++) {
                                if (checkedLights[i]) {
                                    selectedLights.add((Light) lights.get(i).copy());
                                }
                            }
                            if (selectedLights.size() > 0) {
                                listener.onLightsSelected(selectedLights);
                            } else {
                                Toast.makeText(getActivity(), "No lights selected", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
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
