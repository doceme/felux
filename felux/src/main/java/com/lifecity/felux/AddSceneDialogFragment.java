package com.lifecity.felux;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.lifecity.felux.scenes.Scene;
import com.lifecity.felux.scenes.LightScene;
import com.lifecity.felux.scenes.DelayScene;
import com.lifecity.felux.scenes.MidiScene;

public class AddSceneDialogFragment extends DialogFragment {
    private Scene scene;
    private AddSceneDialogListener listener;

    public interface AddSceneDialogListener {
        public void onTypeSelected(Scene scene);
        public void onCanceled();
    }

    public AddSceneDialogFragment(AddSceneDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.scene_types_label)
                .setItems(R.array.scene_types, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            scene = new LightScene();
                        } else if (which == 1) {
                            scene = new DelayScene();
                        } else if (which == 1) {
                            scene = new MidiScene();
                        } else {
                            throw new IllegalStateException("Invalid scene type");
                        }

                        listener.onTypeSelected(scene);
                    }
                });

                builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
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
