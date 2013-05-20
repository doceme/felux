package com.lifecity.felux;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lifecity.felux.light.Lights;

/**
 * A fragment representing a single Light detail screen.
 * on handsets.
 */
public class LightDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_DMX_ADDR = "dmx_address";
    public static final String TAG = "light_detail_tag";

    /**
     * The dummy content this fragment is presenting.
     */
    private Lights.Light mLight;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LightDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ARG_DMX_ADDR)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mLight = Lights.LIGHT_MAP.get(arguments.getInt(ARG_DMX_ADDR));
        }
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_light_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateLightView();
    }

    public Lights.Light getLight() {
        return mLight;
    }

    public void setLight(Lights.Light light) {
        mLight = light;
        updateLightView();
    }

    public void updateLightView() {
        View rootView = getView();
        if (rootView != null) {
            TextView textView = (TextView)rootView.findViewById(R.id.light_detail);
            if (textView != null) {
                if (mLight != null) {
                    textView.setText(mLight.name);
                } else {
                    textView.setText("");
                }
            }
        }
    }
}
