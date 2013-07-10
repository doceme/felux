package com.lifecity.felux;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.lifecity.felux.scenes.Scene;

import java.util.ArrayList;
import java.util.List;

public class AddCueSceneDialogFragment extends DialogFragment {
    private Scene scene;
    private List<Scene> scenes;
    private List<Scene> selectedScenes;
    private AddCueSceneDialogListener listener;
    private ArrayAdapter<Scene> adapter;
    boolean[] checkedScenes;

    public interface AddCueSceneDialogListener {
        public void onScenesSelected(List<Scene> scenes);
        public void onCanceled();
    }

    public AddCueSceneDialogFragment(AddCueSceneDialogListener listener, List<Scene> scenes) {
        this.listener = listener;
        this.scenes = scenes;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] sceneNames = new String[scenes.size()];
        checkedScenes = new boolean[scenes.size()];

        /* Initialize scene names */
        for (int i = 0; i < scenes.size(); i++) {
            sceneNames[i] = scenes.get(i).getName();
            checkedScenes[i] = false;
        }

        builder.setTitle(R.string.select_scene_label)
                .setMultiChoiceItems(sceneNames, checkedScenes, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                        checkedScenes[which] = isChecked;
                    }
                })
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (listener != null) {
                            List<Scene> selectedScenes = new ArrayList<Scene>();
                            for (int i = 0; i < scenes.size(); i++) {
                                if (checkedScenes[i]) {
                                    selectedScenes.add((Scene) scenes.get(i).copy());
                                }
                            }
                            if (selectedScenes.size() > 0) {
                                listener.onScenesSelected(selectedScenes);
                            } else {
                                Toast.makeText(getActivity(), "No scenes selected", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        scene = null;
                        if (listener != null) {
                            listener.onCanceled();
                        }
                    }
                });

        return builder.create();
    }
}
