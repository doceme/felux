package com.lifecity.felux;

import android.view.MenuItem;
import com.lifecity.felux.cues.Cue;
import com.lifecity.felux.scenes.Scene;

/**
 *
 */
public class CueListFragment extends ItemListFragment<Cue> {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CueListFragment() {
    }

    public void setFeluxManager(FeluxManager manager) {
        super.setFeluxManager(manager);
        this.items = manager.getCues();
    }

    @Override
    public void onItemsLoaded(FeluxManager manager) {
        this.items = manager.getCues();
        super.onItemsLoaded(manager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.item_add:
            addItem(new Cue("Cue " + Integer.toString(items.size() + 1)));
            return super.onOptionsItemSelected(item);
        default:
            return super.onOptionsItemSelected(item);
    }
  }
}
