package com.greenassitant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.greenassitant.R;
import com.greenassitant.metrics.CoMetric;
import com.greenassitant.model.BasketItem;

import java.util.List;

public class BasketListAdapter extends ArrayAdapter<BasketItem> {

    private List<BasketItem> items;
    private Context context;

    public BasketListAdapter(Context context, int textViewResourceId, List<BasketItem> items) {
        super(context, textViewResourceId, items);
        this.items = items;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.basket_item, parent, false);
        TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);
        TextView itemScore = (TextView) rowView.findViewById(R.id.item_score);
        BasketItem basketItem = items.get(position);
        firstLine.setText(basketItem.getName());
        secondLine.setText(basketItem.getCount() + " " + basketItem.getUnit());
        CoMetric coMetric = new CoMetric();
        long score = coMetric.calculate(basketItem);
        itemScore.setText(score + "");
        rowView.setBackgroundColor(coMetric.colorForLevel((long)basketItem.getScore()));
        return rowView;
    }
}
