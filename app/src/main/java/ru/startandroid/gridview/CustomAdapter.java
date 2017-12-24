package ru.startandroid.gridview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Admin on 24.11.2017.
 */

public class CustomAdapter extends BaseAdapter {

    Context context;
    View.OnClickListener myClickListener;
    ArrayList<MainActivity.ColorButtonModel> colorsArray = null;


    CustomAdapter(Context context, ArrayList<MainActivity.ColorButtonModel> colorsArray,
                  View.OnClickListener myClickListener) {
        this.context = context;
        this.colorsArray = colorsArray;
        this.myClickListener = myClickListener;

    }

    @Override
    public int getCount() {
        if (colorsArray == null) {
            return 0;
        }
            return colorsArray.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button btn = new Button(context);
        btn.setOnClickListener(myClickListener);
        btn.setTag(position);
        MainActivity.ColorButtonModel model = colorsArray.get(position);
        int colorResId = model.getColorResId();
        btn.setBackgroundColor(context.getResources().getColor(colorResId));
        return btn;
    }

    public void setColorsArray(ArrayList<MainActivity.ColorButtonModel> arrayList){
        colorsArray = arrayList;
        notifyDataSetChanged();
    }







}