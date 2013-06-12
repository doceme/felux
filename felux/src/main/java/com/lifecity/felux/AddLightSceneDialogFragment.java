package com.lifecity.felux;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;

import com.lifecity.felux.lights.DmxColorLight;
import com.lifecity.felux.lights.DmxGroupLight;
import com.lifecity.felux.lights.DmxLight;
import com.lifecity.felux.lights.Light;

import java.util.List;

public class AddLightSceneDialogFragment extends DialogFragment {
    private Light light;
    private List<Light> lights;
    private AddLightSceneDialogListener listener;
    private ArrayAdapter<Light> adapter;

    public interface AddLightSceneDialogListener {
        public void onLightSelected(Light light);
        public void onCanceled();
    }

    public AddLightSceneDialogFragment(AddLightSceneDialogListener listener, List<Light> lights) {
        this.listener = listener;
        this.lights = lights;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        /*
        adapter = new LightSceneLightListAdapter(
                getActivity().getApplicationContext(),
                R.layout.light_scene_light_list_row,
                lights
        );
        */

        adapter = new ArrayAdapter<Light>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                lights
        );

        builder.setTitle(R.string.select_light_label)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        light = adapter.getItem(which);
                        listener.onLightSelected(light);
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
