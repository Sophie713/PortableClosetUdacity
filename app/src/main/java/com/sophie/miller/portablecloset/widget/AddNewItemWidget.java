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

/**
 * Implementation of App Widget functionality.
 */
public class AddNewItemWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            CharSequence widgetText = context.getString(R.string.bought_sth_new_widget);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.add_new_item_widget);
            views.setTextViewText(R.id.appwidget_normal_text, widgetText);
            views.setOnClickPendingIntent(R.id.appwidget_normal_text, mainActivityIntent(context));

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    /**
     * Main activity intent
     *
     * @param context
     * @return
     */
    private PendingIntent mainActivityIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(FragmentCodes.OPEN_FRAGMENT, FragmentCodes.DETAIL_EDIT_FRAGMENT);
        return PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

