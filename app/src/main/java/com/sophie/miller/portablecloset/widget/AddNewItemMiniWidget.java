package com.sophie.miller.portablecloset.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.sophie.miller.portablecloset.MainActivity;
import com.sophie.miller.portablecloset.R;
import com.sophie.miller.portablecloset.constants.FragmentCodes;

import static android.app.PendingIntent.getActivity;

/**
 * Implementation of App Widget functionality.
 */
public class AddNewItemMiniWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        //update all widgets
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.add_new_item_mini_widget);
            views.setOnClickPendingIntent(R.id.appwidget_mini_btn, mainActivityIntent(context));

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    /**
     * Main activity intent
     * @param context
     * @return
     */
    private PendingIntent mainActivityIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("OPEN_FRAGMENT", FragmentCodes.DETAIL_EDIT_FRAGMENT);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}

