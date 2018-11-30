package com.example.ybook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookListAdapter extends ArrayAdapter<BookListModel> {

    private final Context context;
    private final List<BookListModel> modelsArrayList;

    public BookListAdapter(Context context, List<BookListModel> modelsArrayList) {
        super(context, R.layout.list_item, modelsArrayList);

        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView;

        rowView = inflater.inflate(R.layout.list_item, parent, false);

        // 3. Get icon,title & counter views from the rowView
        ImageView imgView = rowView.findViewById(R.id.itemIcon);
        TextView titleView = rowView.findViewById(R.id.itemTitle);

        // 4. Set the text for textView
        imgView.setImageResource(modelsArrayList.get(position).getIcon());
        titleView.setText(modelsArrayList.get(position).getTitle());

        // 5. return rowView
        return rowView;
    }
}
