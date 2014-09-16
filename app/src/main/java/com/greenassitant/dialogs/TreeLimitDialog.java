package com.greenassitant.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.greenassitant.R;

public class TreeLimitDialog {

    private boolean oneShown;
    private boolean fiveShown;

    public void show(Context context, double treeUsage) {
        if (treeUsage > 1 && treeUsage < 5 && !isOneShown()) {
            show(context, context.getResources().getString(R.string.one_tree_usage_msg),
                    context.getResources().getString(R.string.one_tree_dialog_confirmation));
            setOneShown(true);
        }

        if (treeUsage >= 5 && !isFiveShown()) {
            show(context, context.getResources().getString(R.string.five_tree_usage_msg),
                    context.getResources().getString(R.string.five_tree_dialog_confirmation));
            setFiveShown(true);
        }
    }

    public void show(Context context, String message, String confirmation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton(confirmation, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO
                    }
                });
        builder.create().show();
    }

    public boolean isOneShown() {
        return oneShown;
    }

    public void setOneShown(boolean oneShown) {
        this.oneShown = oneShown;
    }

    public boolean isFiveShown() {
        return fiveShown;
    }

    public void setFiveShown(boolean fiveShown) {
        this.fiveShown = fiveShown;
    }
}
