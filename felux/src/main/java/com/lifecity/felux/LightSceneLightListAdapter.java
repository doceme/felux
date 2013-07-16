package com.lifecity.felux;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.lifecity.felux.lights.DmxColorLight;
import com.lifecity.felux.lights.DmxLight;
import com.lifecity.felux.lights.DmxSwitchLight;
import com.lifecity.felux.lights.Light;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by doceme on 6/6/13.
 */
public class LightSceneLightListAdapter extends ArrayAdapter<Light> implements View.OnClickListener {
    private Context context;
    private int layoutResourceId;
    private List<Light> lights;
    private boolean editMode;
    private ItemChangedListener itemChangedListener;
    private int minValueWidth;

    @Override
    public void onClick(View view) {
        CheckBox checkBox = (CheckBox)view;
        Light light = (Light)checkBox.getTag();
        light.setChecked(checkBox.isChecked());

        if (itemChangedListener != null) {
            itemChangedListener.onItemCheckedChanged(light);
        }
    }

    static class LightHolder {
        CheckBox enable;
        ImageView color;
        TextView value;
        LinearLayout valueLayout;
        TextView name;
    }

    public LightSceneLightListAdapter(Context context, int layoutResourceId, List<Light> lights) {
        super(context, layoutResourceId, lights);
        this.context = context;
        minValueWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, context.getResources().getDisplayMetrics());
        this.layoutResourceId = layoutResourceId;
        this.lights = lights;
        /*
        this.lights = new ArrayList<Light>(lights.size());
        for (Light light: lights) {
            light.setChecked(false);
            this.lights.add(light);
        }
        */
    }

    public void setItemChangedListener(ItemChangedListener itemChangedListener) {
        this.itemChangedListener = itemChangedListener;
    }

    public void startEditMode() {
        editMode = true;
        notifyDataSetChanged();
    }

    public void stopEditMode() {
        editMode = false;
        notifyDataSetChanged();
    }

    public void selectAll(boolean select) {
        for (Light light: lights) {
            light.setChecked(select);
        }
        notifyDataSetChanged();
    }

    public int getSingleCheckedItemPosition() {
        int result = -1;
        boolean checked = false;
        for (int i = 0; i < lights.size(); i++) {
            if (lights.get(i).getChecked()) {
                if (checked) {
                    return -1;
                } else {
                    checked = true;
                    result = i;
                }
            }
        }

        return result;
    }

    public boolean areAnyItemsChecked() {
        for (Light light: lights) {
            if (light.getChecked()) {
                return true;
            }
        }
        return false;
    }

    public void removeChecked() {
        ListIterator<Light> iterator = lights.listIterator();
        while (iterator.hasNext()) {
            Light light = iterator.next();
            if (light.getChecked()) {
                iterator.remove();
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LightHolder holder = null;
        Light light = getItem(position);

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new LightHolder();
            holder.enable = (CheckBox)row.findViewById(R.id.light_scene_light_list_row_checkbox);
            holder.color = (ImageView)row.findViewById(R.id.light_scene_light_list_row_color);
            holder.value = (TextView)row.findViewById(R.id.light_scene_light_list_row_value);
            holder.valueLayout = (LinearLayout)row.findViewById(R.id.light_scene_light_list_row_value_layout);
            holder.name = (TextView)row.findViewById(R.id.light_scene_light_list_row_name);
            holder.enable.setOnClickListener(this);

            row.setTag(holder);

        } else {
            holder = (LightHolder)row.getTag();
        }

        holder.enable.setTag(light);
        holder.enable.setVisibility(editMode ? View.VISIBLE : View.GONE);
        holder.enable.setChecked(light.getChecked());
        holder.name.setText(light.getName());

        if (light instanceof DmxColorLight) {
            DmxColorLight dmxColorLight = (DmxColorLight)light;
            GradientDrawable gd = new GradientDrawable();
            gd.setShape(GradientDrawable.RECTANGLE);
            gd.setColor(dmxColorLight.getColor());
            gd.setCornerRadius(5.0f);
            holder.valueLayout.setMinimumWidth(0);
            holder.value.setVisibility(View.GONE);
            holder.color.setVisibility(View.VISIBLE);
            holder.color.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable();
            gd.setShape(GradientDrawable.RECTANGLE);
            gd.setColor(Color.DKGRAY);
            gd.setCornerRadius(5.0f);
            holder.color.setVisibility(View.GONE);
            holder.valueLayout.setMinimumWidth(minValueWidth);
            holder.valueLayout.setBackground(gd);
            holder.value.setVisibility(View.VISIBLE);
            if (light instanceof DmxSwitchLight) {
                holder.value.setText(light.getValue() == DmxLight.MIN_VALUE ? "Off" : "On");
            } else {
                holder.value.setText(String.valueOf(light.getPercent()) + "%");
            }
        }

        return row;
    }
}
