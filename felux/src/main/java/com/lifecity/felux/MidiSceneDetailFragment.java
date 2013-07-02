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
public class MidiSceneDetailFragment extends ItemDetailFragment<MidiScene> implements View.OnFocusChangeListener {
    private EditText nameEdit;
    private TextView channelLabel;
    private EditText channelEdit;
    private TextView noteLabel;
    private EditText noteEdit;
    private TextView velocityLabel;
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
        if (nameEdit != null && item != null) {
            nameEdit.setText(item.getName());

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
            }
        }

        nameEdit.setFocusable(enabled);
        nameEdit.setFocusableInTouchMode(enabled);
        channelEdit.setFocusable(enabled);
        channelEdit.setFocusableInTouchMode(enabled);
        noteEdit.setFocusable(enabled);
        noteEdit.setFocusableInTouchMode(enabled);
        velocityEdit.setFocusable(enabled);
        velocityEdit.setFocusableInTouchMode(enabled);
        eventSwitch.setEnabled(enabled);
        eventSwitch.setFocusable(enabled);
        eventSwitch.setFocusableInTouchMode(enabled);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        showHideKeyboard(view, hasFocus);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        nameEdit = (EditText)view.findViewById(R.id.midi_scene_detail_name_edit);
        nameEdit.setOnFocusChangeListener(this);
        channelEdit = (EditText)view.findViewById(R.id.midi_scene_detail_channel_edit);
        channelEdit.setOnFocusChangeListener(this);
        noteEdit = (EditText)view.findViewById(R.id.midi_scene_detail_note_edit);
        noteEdit.setOnFocusChangeListener(this);
        velocityEdit = (EditText)view.findViewById(R.id.midi_scene_detail_velocity_edit);
        velocityEdit.setOnFocusChangeListener(this);

        channelLabel = (TextView)view.findViewById(R.id.midi_scene_detail_channel_label);
        noteLabel = (TextView)view.findViewById(R.id.midi_scene_detail_note_label);
        velocityLabel = (TextView)view.findViewById(R.id.midi_scene_detail_velocity_label);

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
        ListIterator<Light> iterator;
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
}
