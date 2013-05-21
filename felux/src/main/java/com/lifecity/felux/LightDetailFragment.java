package com.lifecity.felux;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single Light detail screen.
 * on handsets.
 */
public class LightDetailFragment extends ItemDetailFragment<Light> {
    public static final String TAG = "light_detail_tag";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LightDetailFragment() {
        super(R.layout.fragment_light_detail);
    }

    public void updateItemView() {
        View rootView = getView();
        if (rootView != null) {
            TextView textView = (TextView)rootView.findViewById(R.id.light_detail);
            if (textView != null) {
                if (item != null) {
                    textView.setText(item.name);
                } else {
                    textView.setText("");
                }
            }
        }
    }
}
