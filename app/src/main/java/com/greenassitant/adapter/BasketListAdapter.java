package com.greenassitant.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.greenassitant.R;
import com.greenassitant.metrics.CoMetric;
import com.greenassitant.model.BasketItem;

import java.util.Comparator;
import java.util.List;

public class BasketListAdapter extends ArrayAdapter<BasketItem> {

    private List<BasketItem> items;
    private Context context;
    private CoMetric coMetric;

    public BasketListAdapter(Context context, int textViewResourceId, List<BasketItem> items) {
        super(context, textViewResourceId, items);
        this.items = items;
        this.context = context;
        this.coMetric = new CoMetric();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.basket_item, parent, false);

        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/font.otf");
        TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);
        TextView itemScore = (TextView) rowView.findViewById(R.id.item_score);

        firstLine.setTypeface(custom_font);
        secondLine.setTypeface(custom_font);
        itemScore.setTypeface(custom_font);

        BasketItem basketItem = items.get(position);
        firstLine.setText(basketItem.getName());
        secondLine.setText(basketItem.getQuantity());
        itemScore.setText(Long.toString(coMetric.calculate(basketItem)));

        if (basketItem.isInBasket()) {
            firstLine.setPaintFlags(firstLine.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            rowView.setBackgroundColor(coMetric.colorForItem(basketItem));
        } else {
            firstLine.setPaintFlags(firstLine.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            rowView.setBackgroundColor(coMetric.colorForItem(basketItem));
        }
        return rowView;
    }
}
