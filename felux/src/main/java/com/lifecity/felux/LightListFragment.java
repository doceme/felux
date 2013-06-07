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
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LightListFragment() {
    }

    public void setFeluxManager(FeluxManager manager) {
        this.items = manager.getLights();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.item_add:
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
