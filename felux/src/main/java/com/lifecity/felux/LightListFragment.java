package com.lifecity.felux;

import android.view.*;
import com.lifecity.felux.lights.DmxColorLight;
import com.lifecity.felux.lights.DmxLight;
import com.lifecity.felux.lights.Light;


/**
 * A list fragment representing a list of Lights. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link LightDetailFragment}.
 * <p>
 * interface.
 */
public class LightListFragment extends ItemListFragment<Light> {
    public static String TAG = "light_list_tag";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LightListFragment() {
        items.add(new DmxColorLight(1, "Screen"));
        items.add(new DmxColorLight(5, "Side"));
        items.add(new DmxColorLight(9, "Ceiling"));
        items.add(new DmxLight(13, "Stage"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.add:
            addItem(new DmxLight(0, "Light " + Integer.toString(items.size() + 1)));
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
  }
}
