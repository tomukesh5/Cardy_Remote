package com.cardyapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.cardyapp.R;

public class DialogUtils {
    private static final String POSITIVE_TEXT = "Ok";
    private static final String NEGATIVE_TEXT = "Cancel";
    private static final boolean IS_DISMISSABLE = true;
    private static final boolean IS_CANCELED_ON_TOUCH_OUTSIDE = true;
    private static Context context;

    public static interface ActionListner {
        void onPositiveAction();
        void onNegativeAction();
    }

    /**
     * @param context
     * @param iconId
     * @param message
     * @param title
     * @param positive
     * @param negative
     * @param isdismiss
     * @param layoutId
     * @param actionListner
     */
    public static void show(Context context, Integer iconId, String message, String title, String positive, String negative, Boolean isdismiss, Boolean isCanceledOnTouchOutside, Integer layoutId, final ActionListner actionListner) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CardyCustomDialogTheme);
        builder.setTitle(title);
        builder.setMessage(message);

        if (iconId != null) {
            builder.setIcon(iconId);
        }

        if (positive != null) {
            builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    actionListner.onPositiveAction();
                }
            });
        }

        if (negative != null) {
            builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    actionListner.onNegativeAction();
                }
            });
        }

        AlertDialog dialog = builder.create();

        if (isdismiss != null) {
            dialog.setCancelable(isdismiss);
        } else{
            dialog.setCancelable(IS_DISMISSABLE);
        }

        if (isCanceledOnTouchOutside != null) {
            dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        } else {
            dialog.setCanceledOnTouchOutside(IS_CANCELED_ON_TOUCH_OUTSIDE);
        }

        if(!((Activity) context).isFinishing())
        {
            dialog.show();
            //show dialog
        }

    }

    public static void show(Context context, String message, String title) {
        show(context, null, message, title, null, null, null, null, null, null);
    }

    public static void show(Context context, String message, String title, ActionListner actionListner) {
        show(context, null, message, title, null, null, null, null, null, actionListner);
    }

    public static void show(Context context, String message, String title, String positive, String negative, ActionListner actionListner) {
        show(context, null, message, title, positive, negative, null, null, null, actionListner);
    }

    public static void show(Context context, String message, String title, String positive, ActionListner actionListner){
        show(context, null, message, title, positive, null, null, null, null, actionListner);
    }

    public static void show(Context context, String message, String title, String positive, String negative, boolean isDismiss, boolean isCanceledOnTouchOutside, ActionListner actionListner) {
        DialogUtils.context = context;
        show(context, null, message, title, positive, negative, isDismiss, isCanceledOnTouchOutside, null, actionListner);
    }

    public static void show(Context context, String message, String title, String positive, boolean isDismiss, boolean isCanceledOnTouchOutside, ActionListner actionListner) {
        DialogUtils.context = context;
        show(context, null, message, title, positive, null, isDismiss, isCanceledOnTouchOutside, null, actionListner);
    }
}
