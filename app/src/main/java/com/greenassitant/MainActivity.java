package com.greenassitant;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.greenassitant.adapter.BasketListAdapter;
import com.greenassitant.dialogs.QuantityPicker;
import com.greenassitant.listeners.SwipeDismissListViewTouchListener;
import com.greenassitant.metrics.CoMetric;
import com.greenassitant.metrics.TreeMetric;
import com.greenassitant.model.BasketItem;
import com.greenassitant.model.DummyData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    public static final String CLICK_TO_ADD_ITEM = "Click to add item";

    private SearchView searchView;
    private ListView listView;
    private BasketListAdapter adapter;

    private List<BasketItem> items = new ArrayList<BasketItem>();
    private DecimalFormat decimalFormat = new DecimalFormat("##.##");
    private CoMetric coMetric = new CoMetric();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new BasketListAdapter(getApplicationContext(), R.layout.basket_item, items);

        listView.setAdapter(adapter);
        setListListeners();
        calculateScore();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            adapter.add(DummyData.getForName(query));
            searchView.setQuery("", false);
            calculateScore();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setQueryHint(CLICK_TO_ADD_ITEM);
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(true);
        return true;
    }

    private void setListListeners() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {

                View rowView = listView.getChildAt(position);
                BasketItem item = adapter.getItem(position);
                TextView textView = (TextView) rowView.findViewById(R.id.firstLine);

                if (item.isInBasket()) {
                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    item.setInBasket(false);
                    view.setBackgroundColor(coMetric.colorForItem(item));
                } else {
                    textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    item.setInBasket(true);
                    view.setBackgroundColor(coMetric.colorForItem(item));
                }
            }
        });

        listView.setOnTouchListener(new SwipeDismissListViewTouchListener(listView,
                new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            adapter.remove(adapter.getItem(position));
                            calculateScore();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }));

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pickQuantity(position);
                return false;
            }
        });
    }

    private void pickQuantity(final int position) {
        new QuantityPicker(this, new QuantityPicker.QuantityListener() {
            @Override
            public void selected(double value) {
                BasketItem item = adapter.getItem(position);
                item.setCount(value);
                adapter.notifyDataSetChanged();
                calculateScore();
            }
        }).show();
    }

    private void calculateScore() {
        long coUsageG = coMetric.calculate(items);
        double coUsageKG = ((double) coUsageG) / 1000;
        double treeUsage = new TreeMetric().calculate(coUsageG);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/font.otf");
        TextView coScore = (TextView) findViewById(R.id.co_score);
        TextView treeScore = (TextView) findViewById(R.id.tree_score);

        coScore.setTypeface(custom_font);
        treeScore.setTypeface(custom_font);
        coScore.setText(decimalFormat.format(coUsageKG));
        treeScore.setText(decimalFormat.format(treeUsage));
    }
}