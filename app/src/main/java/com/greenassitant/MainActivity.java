package com.greenassitant;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.widget.Toast;

import com.greenassitant.adapter.BasketItemComparator;
import com.greenassitant.adapter.BasketListAdapter;
import com.greenassitant.dialogs.QuantityPicker;
import com.greenassitant.dialogs.TreeLimitDialog;
import com.greenassitant.listeners.SwipeDismissListViewTouchListener;
import com.greenassitant.metrics.CoMetric;
import com.greenassitant.metrics.TreeMetric;
import com.greenassitant.model.BasketItem;
import com.greenassitant.model.DummyData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.greenassitant.model.ProductData;


public class MainActivity extends Activity {

    public static final String CLICK_TO_ADD_ITEM = "Click to add item";

    private AutoCompleteTextView textViewAutoComplete;
    private ArrayAdapter<String> autoCompleteAdapter;

    private ListView listView;
    private BasketListAdapter basketListAdapter;
    private List<BasketItem> items = new ArrayList<BasketItem>();

    private DecimalFormat decimalFormat = new DecimalFormat("##.##");
    private CoMetric coMetric = new CoMetric();
    private TreeLimitDialog treeDialog = new TreeLimitDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] productNames_list = ProductData.getNamesByRegex("", getApplicationContext());
        autoCompleteAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,productNames_list);
        textViewAutoComplete = (AutoCompleteTextView) findViewById(R.id.autoComplete);

        // set basketListAdapter for the auto complete fields
        textViewAutoComplete.setAdapter(autoCompleteAdapter);

        // specify the minimum type of characters before drop-down list is shown
        textViewAutoComplete.setThreshold(1);

        textViewAutoComplete.setThreshold(1);

        TextView.OnEditorActionListener exampleListener = new TextView.OnEditorActionListener(){
            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                        basketListAdapter.add(ProductData.getByName(String.valueOf(exampleView.getText()), getApplicationContext()));
                        textViewAutoComplete.setText("",false);
                        calculateScore();
                }
                return true;
            }
        };

        textViewAutoComplete.setOnEditorActionListener(exampleListener);

        textViewAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View arg1, int itemIndex,
                                    long arg3) {
            basketListAdapter.add(ProductData.getByName(String.valueOf(adapterView.getItemAtPosition(itemIndex)), getApplicationContext()));
            textViewAutoComplete.setText("",false);
            calculateScore();
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        basketListAdapter = new BasketListAdapter(getApplicationContext(), R.layout.basket_item, items);

        listView.setAdapter(basketListAdapter);
        setListListeners();
        calculateScore();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            basketListAdapter.add(DummyData.getForName(query));
            calculateScore();
        }
    }

    private void setListListeners() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {

                View rowView = listView.getChildAt(position);
                BasketItem item = basketListAdapter.getItem(position);
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

                // resort elements
                basketListAdapter.sort(new BasketItemComparator());
                basketListAdapter.notifyDataSetChanged();
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
                            basketListAdapter.remove(basketListAdapter.getItem(position));
                            calculateScore();
                        }
                        basketListAdapter.notifyDataSetChanged();
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
                BasketItem item = basketListAdapter.getItem(position);
                item.setCount(value);
                basketListAdapter.notifyDataSetChanged();
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
        treeDialog.show(this, treeUsage);
    }
}