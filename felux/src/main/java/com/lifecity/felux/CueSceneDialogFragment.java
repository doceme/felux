package com.lifecity.felux;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by scaudle on 6/7/13.
 */
public class CueSceneDialogFragment extends DialogFragment {
    private float hold;
    private float fade;
    private boolean isLightScene;
    private CueSceneDialogListener listener;

    public interface CueSceneDialogListener {
        public void onCueChanged(float hold, float fade, boolean isLightScene);
        public void onCanceled();
    }

    public CueSceneDialogFragment(CueSceneDialogListener listener, boolean isLightScene) {
        this.listener = listener;
        this.isLightScene = isLightScene;
    }

    public void setHold(float hold) {
        this.hold = hold;
    }

    public void setFade(float fade) {
        this.fade = fade;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_cue_scene, null);
        builder.setView(view);
        builder.setTitle(R.string.cue_scene_update_title);

        final EditText holdEdit = (EditText)view.findViewById(R.id.cue_scene_hold);
        final EditText fadeEdit = (EditText)view.findViewById(R.id.cue_scene_fade);
        final TextView fadeLabel = (TextView)view.findViewById(R.id.cue_scene_fade_label);

        holdEdit.setText(String.valueOf(hold));
        int fadeVisibility = isLightScene ? View.VISIBLE : View.GONE;
        fadeLabel.setVisibility(fadeVisibility);
        fadeEdit.setVisibility(fadeVisibility);

        if (isLightScene) {
            fadeEdit.setText(String.valueOf(fade));
        }

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (listener != null) {
                    hold = Float.valueOf(holdEdit.getText().toString());
                    fade = isLightScene ? Float.valueOf(fadeEdit.getText().toString()) : 0;
                    listener.onCueChanged(hold, fade, isLightScene);
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
