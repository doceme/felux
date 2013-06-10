package com.lifecity.felux;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lifecity.felux.R;
import com.lifecity.felux.lights.DmxColorLight;
import com.lifecity.felux.lights.DmxGroupLight;
import com.lifecity.felux.lights.DmxLight;
import com.lifecity.felux.lights.Light;

import java.util.List;

/**
 * Created by doceme on 6/6/13.
 */
public class LightSceneLightListAdapter extends ArrayAdapter<Light> {
    private Context context;
    private int layoutResourceId;
    private List<Light> lights;
    //private boolean[] enabledLights;

    static class LightHolder {
        CheckBox enable;
        ImageView color;
        TextView name;
    }

    public LightSceneLightListAdapter(Context context, int layoutResourceId, List<Light> lights) {
        super(context, layoutResourceId, lights);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.lights = lights;
        //enabledLights = new boolean[lights.size()];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LightHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new LightHolder();
            holder.enable = (CheckBox)row.findViewById(R.id.light_scene_light_list_row_checkbox);
            holder.color = (ImageView)row.findViewById(R.id.light_scene_light_list_row_color);
            holder.name = (TextView)row.findViewById(R.id.light_scene_light_list_row_name);

            row.setTag(holder);

        } else {
            holder = (LightHolder)row.getTag();
        }

        Light light = lights.get(position);
        holder.name.setText(light.getName());

        if (light instanceof DmxColorLight) {
            DmxColorLight dmxColorLight = (DmxColorLight)light;
            GradientDrawable gd = new GradientDrawable();
            gd.setShape(GradientDrawable.RECTANGLE);
            gd.setColor(dmxColorLight.getColor());
            //gd.setStroke(3, Color.WHITE);
            gd.setCornerRadius(5.0f);
            holder.color.setVisibility(View.VISIBLE);
            holder.color.setBackground(gd);
        } else {
            holder.color.setVisibility(View.GONE);
        }

        return row;
    }


}
