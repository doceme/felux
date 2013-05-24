package com.lifecity.felux;

import android.view.MenuItem;
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
        items.add(new DmxColorLight("Screen", 1));
        items.add(new DmxColorLight("Side", 5));
        items.add(new DmxColorLight("Ceiling", 9));
        items.add(new DmxLight("Stage", 13));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.item_add:
            //addItem(new DmxLight("Light " + Integer.toString(items.size() + 1)));
            AddLightDialogFragment dialog = new AddLightDialogFragment(new AddLightDialogFragment.AddLightDialogListener() {
                @Override
                public void onTypeSelected(Light light) {
                    if (light != null) {
                        light.setName("Light " + Integer.toString(items.size() + 1));
                        addItem(light);
                    }
                }

                @Override
                public void onCanceled() {

                }
            });
            dialog.show(getActivity().getSupportFragmentManager(), "add_light_dialog_tag");
            return super.onOptionsItemSelected(item);
        default:
            return super.onOptionsItemSelected(item);
    }
  }
}
