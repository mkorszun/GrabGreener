package com.greenassitant;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.greenassitant.adapter.BasketListAdapter;
import com.greenassitant.listeners.SwipeDismissListViewTouchListener;
import com.greenassitant.metrics.CoMetric;
import com.greenassitant.metrics.TreeMetric;
import com.greenassitant.model.BasketItem;
import com.greenassitant.model.DummyData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements SearchView.OnQueryTextListener {

    private View main;
    private SearchView searchView;
    private ListView listView;
    private BasketListAdapter adapter;
    private List<BasketItem> items = new ArrayList<BasketItem>();

    private String nums[] = {"0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0", "1.1", "1.2", "1.3", "1.4", "1.5", "1.6", "1.7", "1.8", "1.9", "2.0"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new BasketListAdapter(getApplicationContext(), R.layout.basket_item, items);

        listView.setAdapter(adapter);
        setListListeners();

//        searchView = (SearchView) findViewById(R.id.searchView);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.summary, null, false);
        //listView.addFooterView(footerView);
//        searchView.setIconifiedByDefault(false);
//        searchView.setIconified(true);

        calculateScore();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
            adapter.add(DummyData.getForName(query));
            calculateScore();
            searchView.setQuery("", false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setQueryHint("Click to add item");
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void calculateScore() {
        CoMetric metric = new CoMetric();
        long calculate1 = metric.calculate(items);
        double s = ((double) calculate1) / 1000;
        TextView score = (TextView) findViewById(R.id.co_score);
        TextView treeScore = (TextView) findViewById(R.id.tree_score);
        score.setText(new DecimalFormat("##.##").format(s));
        double calculate = new TreeMetric().calculate(calculate1);
        treeScore.setText(new DecimalFormat("##.##").format(calculate));
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
                } else {
                    textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    item.setInBasket(true);
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
                show(position);
                calculateScore();
                return false;
            }
        });
    }

    public void show(final int position) {
        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("Quantity picker");
        d.setContentView(R.layout.countity_picker);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(nums.length - 1);
        np.setMinValue(0);
        np.setDisplayedValues(nums);
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasketItem item = adapter.getItem(position);
                double count = Double.valueOf(nums[np.getValue()]);
                item.setCount(count);
                adapter.notifyDataSetChanged();
                calculateScore();
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }
}