package com.lifecity.felux;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.lifecity.felux.cues.Cue;
import com.lifecity.felux.items.Item;
import com.lifecity.felux.scenes.LightScene;
import com.lifecity.felux.scenes.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * A fragment representing a single Scene detail screen.
 * on handsets.
 */
public class CueDetailFragment extends ItemDetailFragment<Cue> implements AdapterView.OnItemClickListener, AddCueSceneDialogFragment.AddCueSceneDialogListener, CompoundButton.OnCheckedChangeListener, View.OnFocusChangeListener, CueSceneDialogFragment.CueSceneDialogListener, ItemChangedListener, View.OnClickListener {
    private CueSceneListAdapter adapter;
    private Scene currentScene;
    private Button previewButton;
    private CheckBox selectAll;
    private MenuItem removeScene;
    private MenuItem moveUpScene;
    private MenuItem moveDownScene;
    private ListView sceneListView;
    private EditText nameEdit;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CueDetailFragment() {
        super(R.layout.fragment_cue_detail);
    }

    private void updateScenes() {
        adapter.clear();
        for (Scene scene: item.getScenes()) {
            adapter.add(scene);
        }
    }

    @Override
    public void updateItemView() {
        if (nameEdit != null && adapter != null && item != null) {
            nameEdit.setText(item.getName());
            updateScenes();
            adapter.notifyDataSetChanged();
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
        sceneListView.setEnabled(enabled);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        showHideKeyboard(view, hasFocus);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        previewButton = (Button)view.findViewById(R.id.cue_detail_preview_button);
        previewButton.setOnClickListener(this);
        nameEdit = (EditText)view.findViewById(R.id.cue_detail_name_edit);
        nameEdit.setOnFocusChangeListener(this);
        List<Scene> adapterScenes = new ArrayList<Scene>(item.getScenes().size());
        for (Scene scene: item.getScenes()) {
            adapterScenes.add(scene);
        }

        adapter = new CueSceneListAdapter(
                getActivity(),
                R.layout.cue_scene_list_row,
                adapterScenes
        );

        adapter.setItemChangedListener(this);

        sceneListView = (ListView)view.findViewById(R.id.cue_detail_scene_list);
        selectAll =(CheckBox)view.findViewById(R.id.cue_detail_scenes_select_all);
        selectAll.setOnCheckedChangeListener(this);
        selectAll.setVisibility(view.GONE);
        nameEdit.setText(item.getName());
        sceneListView.setAdapter(adapter);
        sceneListView.setItemsCanFocus(false);
        sceneListView.setOnItemClickListener(this);

        setControlsEnabled(false);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        currentScene = (Scene)adapterView.getItemAtPosition(i);
        boolean isLightScene = currentScene instanceof LightScene;
        CueSceneDialogFragment dialog = new CueSceneDialogFragment(this, isLightScene);
        dialog.setHold(currentScene.getHold());
        if (isLightScene) {
            LightScene lightScene = (LightScene) currentScene;
            dialog.setFade(lightScene.getFade());
        }
        dialog.show(getActivity().getSupportFragmentManager(), "cue_light_scene_dialog_tag");
    }

    @Override
    public void onCueChanged(float hold, float fade, boolean isLightScene) {
        if (currentScene != null) {
            currentScene.setHold(hold);
            if (isLightScene) {
                ((LightScene) currentScene).setFade(fade);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onScenesSelected(List<Scene> scenes) {
        for (Scene scene: scenes) {
            item.addScene(scene);
            adapter.add(scene);
        }
        selectAll.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCanceled() {

    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.fragment_add_remove_move_menu, menu);
        menu.findItem(R.id.action_item_add).setTitle(R.string.action_item_add_scene_label);
        removeScene = menu.findItem(R.id.action_item_remove);
        moveUpScene = menu.findItem(R.id.action_item_moveup);
        moveDownScene = menu.findItem(R.id.action_item_movedown);
        removeScene.setVisible(adapter.areAnyItemsChecked());
        adapter.startEditMode();
        if (adapter.getCount() > 0) {
            selectAll.setVisibility(View.VISIBLE);
        }
        updateMoveIcons();
        setControlsEnabled(true);
        return super.onCreateActionMode(actionMode, menu);
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        int position = adapter.getSingleCheckedItemPosition();
        switch (menuItem.getItemId()) {
            case R.id.action_item_add:
                List<Scene> newScenes = new ArrayList<Scene>(manager.getScenes().size());
                for (Scene scene: manager.getScenes()) {
                    newScenes.add((Scene)scene.copy());
                }
                if (newScenes.size() > 0) {
                    AddCueSceneDialogFragment dialog = new AddCueSceneDialogFragment(this, newScenes);
                    dialog.show(getActivity().getSupportFragmentManager(), "add_cue_scene_dialog_tag");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.cue_detail_no_scenes)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, null)
                    .show();
                }
                break;
            case R.id.action_item_remove:
                adapter.removeChecked();
                ListIterator<Scene> iterator = item.getScenes().listIterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getChecked()) {
                        iterator.remove();
                    }
                }
                if (adapter.getCount() == 0) {
                    selectAll.setVisibility(View.GONE);
                }
                removeScene.setVisible(adapter.areAnyItemsChecked());
                return true;
            case R.id.action_item_moveup:
                moveScene(this.item.getScenes().get(position), false);
                return true;
            case R.id.action_item_movedown:
                moveScene(this.item.getScenes().get(position), true);
                return true;
            default:
                break;
        }
        return super.onActionItemClicked(actionMode, menuItem);
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        Object tag = actionMode.getTag();
        boolean cancelled = tag != null && tag.equals(R.string.cancelled);
        if (!cancelled) {
            if (!nameEdit.getText().toString().isEmpty()) {
                item.setName(nameEdit.getText().toString());
            }
        }
        adapter.stopEditMode();
        selectAll.setVisibility(View.GONE);
        setControlsEnabled(false);
        super.onDestroyActionMode(actionMode);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        adapter.selectAll(checked);
        removeScene.setVisible(checked);
    }

    public void moveScene(Scene scene, boolean down) {
        int oldPosition = adapter.getSingleCheckedItemPosition();
        int newPosition;

        if (oldPosition >= 0) {
            if (down) {
                if (oldPosition < (sceneListView.getCount() - 1)) {
                    newPosition = oldPosition + 1;
                }  else {
                    return;
                }
            } else {
                if (oldPosition > 0) {
                    newPosition = oldPosition - 1;
                } else {
                    return;
                }
            }

            List<Scene> scenes = item.getScenes();
            Scene temp = scenes.get(newPosition);
            scenes.set(newPosition, scene);
            scenes.set(oldPosition, temp);
            updateScenes();
            updateMoveIcons();
            adapter.notifyDataSetChanged();
        }
    }

    private void updateMoveIcons() {
        int position = adapter.getSingleCheckedItemPosition();
        if (position >= 0) {
            int size = sceneListView.getCount();

            if (position >= 0 && size > 0) {
                moveUpScene.setVisible(position > 0);
                moveDownScene.setVisible(position < (size - 1));
            } else {
                moveUpScene.setVisible(false);
                moveDownScene.setVisible(false);
            }
        } else {
            moveUpScene.setVisible(false);
            moveDownScene.setVisible(false);
        }
    }

    @Override
    public void onItemCheckedChanged(Item item) {
        updateMoveIcons();
        removeScene.setVisible(adapter.areAnyItemsChecked());
    }

    @Override
    public void onClick(View view) {
        if (view == previewButton && manager != null) {
            manager.showCue(item);
        }
    }
}
