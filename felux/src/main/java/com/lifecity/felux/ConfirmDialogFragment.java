package com.lifecity.felux;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ConfirmDialogFragment extends DialogFragment {
    private ConfirmDialogListener listener;
    private String title;
    private String message;

    public interface ConfirmDialogListener {
        public void onConfirm();
    }

    public ConfirmDialogFragment(ConfirmDialogListener listener) {
        this.listener = listener;
    }

    public ConfirmDialogFragment(String title, ConfirmDialogListener listener) {
        this.title = title;
        this.listener = listener;
    }

    public ConfirmDialogFragment(String title, String message, ConfirmDialogListener listener) {
        this.title = title;
        this.message = message;
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(android.R.drawable.ic_dialog_alert)
               .setCancelable(false)
               .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onConfirm();
                        }
                    }
               })
               .setNegativeButton(android.R.string.no, null);
        if (title != null) {
            builder.setTitle(title);
        }
        if (message != null) {
            builder.setMessage(message);
        }

        return builder.create();
    }
}
