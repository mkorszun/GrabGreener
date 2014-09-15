package com.greenassitant.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.greenassitant.R;

public class QuantityPicker {

    private static final String NUMS[] = {
            "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7",
            "0.8", "0.9", "1.0", "1.1", "1.2", "1.3", "1.4",
            "1.5", "1.6", "1.7", "1.8", "1.9", "2.0"};

    private Dialog dialog;
    private NumberPicker np;
    private QuantityListener listener;

    private Button b1;
    private Button b2;

    public QuantityPicker(Context context, QuantityListener listener) {
        this.listener = listener;
        this.dialog = new Dialog(context);

        dialog.setTitle("Quantity picker");
        dialog.setContentView(R.layout.countity_picker);

        b1 = (Button) dialog.findViewById(R.id.button1);
        b2 = (Button) dialog.findViewById(R.id.button2);

        np = (NumberPicker) dialog.findViewById(R.id.numberPicker1);
    }

    public void show() {

        np.setMinValue(0);
        np.setMaxValue(NUMS.length - 1);
        np.setDisplayedValues(NUMS);
        np.setWrapSelectorWheel(false);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.selected(Double.valueOf(NUMS[np.getValue()]));
                dialog.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public interface QuantityListener {
        public void selected(double value);
    }
}
