package com.lifecity.felux;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.lifecity.felux.lights.Light;
import com.lifecity.felux.scenes.MidiScene;

import java.util.ListIterator;

/**
 * A fragment representing a single Scene detail screen.
 * on handsets.
 */
public class MidiSceneDetailFragment extends ItemDetailFragment<MidiScene> implements View.OnFocusChangeListener, View.OnClickListener {
    private Button previewButton;
    private EditText nameEdit;
    private EditText holdEdit;
    private EditText channelEdit;
    private EditText noteEdit;
    private EditText velocityEdit;
    private Switch eventSwitch;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MidiSceneDetailFragment() {
        super(R.layout.fragment_midi_scene_detail);
    }

    @Override
    public void updateItemView() {
        if (nameEdit != null && holdEdit != null && item != null) {
            nameEdit.setText(item.getName());
            holdEdit.setText(String.valueOf(item.getHold()));

            channelEdit.setText(String.valueOf(item.getChannel()));
            noteEdit.setText(String.valueOf(item.getNote()));
            velocityEdit.setText(String.valueOf(item.getVelocity()));
            eventSwitch.setChecked(item.getEventOn());
        }
    }

    private void setControlsEnabled(boolean enabled) {
        if (!enabled) {
            if (nameEdit.hasFocus()) {
                nameEdit.clearFocus();
            } else if (holdEdit.hasFocus()) {
                holdEdit.clearFocus();
            }
        }

        nameEdit.setFocusable(enabled);
        nameEdit.setFocusableInTouchMode(enabled);
        holdEdit.setFocusable(enabled);
        holdEdit.setFocusableInTouchMode(enabled);
        channelEdit.setFocusable(enabled);
        channelEdit.setFocusableInTouchMode(enabled);
        noteEdit.setFocusable(enabled);
        noteEdit.setFocusableInTouchMode(enabled);
        velocityEdit.setFocusable(enabled);
        velocityEdit.setFocusableInTouchMode(enabled);
        eventSwitch.setEnabled(enabled);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        showHideKeyboard(view, hasFocus);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        previewButton = (Button)view.findViewById(R.id.midi_scene_detail_preview_button);
        previewButton.setOnClickListener(this);
        nameEdit = (EditText)view.findViewById(R.id.midi_scene_detail_name_edit);
        nameEdit.setOnFocusChangeListener(this);
        holdEdit = (EditText)view.findViewById(R.id.midi_scene_detail_hold_edit);
        holdEdit.setOnFocusChangeListener(this);
        channelEdit = (EditText)view.findViewById(R.id.midi_scene_detail_channel_edit);
        channelEdit.setOnFocusChangeListener(this);
        noteEdit = (EditText)view.findViewById(R.id.midi_scene_detail_note_edit);
        noteEdit.setOnFocusChangeListener(this);
        velocityEdit = (EditText)view.findViewById(R.id.midi_scene_detail_velocity_edit);
        velocityEdit.setOnFocusChangeListener(this);

        eventSwitch = (Switch)view.findViewById(R.id.midi_scene_detail_event_switch);

        setControlsEnabled(false);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.fragment_action_cancel, menu);
        setControlsEnabled(true);
        return super.onCreateActionMode(actionMode, menu);
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_item_cancel:
                actionMode.setTag("cancelled");
                actionMode.finish();
            default:
                break;
        }
        return super.onActionItemClicked(actionMode, menuItem);
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        boolean cancelled = actionMode.getTag() != null && actionMode.equals("cancelled");
        if (!cancelled) {
            if (!nameEdit.getText().toString().isEmpty()) {
                item.setName(nameEdit.getText().toString());
            }
            if (!holdEdit.getText().toString().isEmpty()) {
                item.setHold(Float.valueOf(holdEdit.getText().toString()));
            }
            if (!channelEdit.getText().toString().isEmpty()) {
                item.setChannel(Integer.valueOf(channelEdit.getText().toString()));
            }
            if (!noteEdit.getText().toString().isEmpty()) {
                item.setNote(Integer.valueOf(noteEdit.getText().toString()));
            }
            if (!velocityEdit.getText().toString().isEmpty()) {
                item.setVelocity(Integer.valueOf(velocityEdit.getText().toString()));
            }
            item.setEventOn(eventSwitch.isChecked());
        }
        setControlsEnabled(false);
        super.onDestroyActionMode(actionMode);
    }

    @Override
    public void onClick(View view) {
        if (view == previewButton && manager != null) {
            manager.showScene(item);
        }
    }
}
