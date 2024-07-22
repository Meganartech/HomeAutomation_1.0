package com.meganar.smart;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smart.R;

import java.util.ArrayList;
import java.util.HashSet;

public class ListViewAdapter extends ArrayAdapter<String> {


    ArrayList<String> list;


    Context context;

    // The ListViewAdapter Constructor
    // @param context: the Context from the MainActivity
    // @param items: The list of items in our Grocery List

    public ListViewAdapter(Context context, ArrayList<String> items) {
        super(context, R.layout.list_row, items);
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(items);
        items.clear();
        items.addAll(hashSet);
        items.equals(hashSet);
        items.clear();
        items.isEmpty();
        items.clear();
        this.context = context;
        list = items;
    }
    // The method we override to provide our own layout for each View (row) in the ListView
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_row, null);
            TextView name = convertView.findViewById(R.id.name);
            ImageView remove = convertView.findViewById(R.id.remove);
            name.setText(list.get(position));
            // Listeners for duplicating and removing an item.
            // They use the static removeItem and addItem methods created in MainActivity.
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MessageActivity.removeItem(position);
                }
            });
        }
        return convertView;
    }
}
