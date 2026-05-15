package top.manpok.blog.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import top.manpok.blog.R
import top.manpok.blog.activity.CounterActivity
import top.manpok.blog.ds.DataStoreManager

class CounterWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_INCREMENT -> {
                val count = DataStoreManager.instance.getCounterCountSync(context) + 1
                DataStoreManager.instance.setCounterCountSync(context, count)
                refreshAllWidgets(context)
            }
            ACTION_RESET -> {
                DataStoreManager.instance.setCounterCountSync(context, 0)
                refreshAllWidgets(context)
            }
            else -> super.onReceive(context, intent)
        }
    }

    private fun refreshAllWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            android.content.ComponentName(context, CounterWidgetProvider::class.java)
        )
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val title = DataStoreManager.instance.getCounterTitleSync(context)
        val count = DataStoreManager.instance.getCounterCountSync(context)

        val views = RemoteViews(context.packageName, R.layout.widget_counter)
        views.setTextViewText(R.id.widget_counter_title, title)
        views.setTextViewText(R.id.widget_counter_count, count.toString())

        val openAppIntent = Intent(context, CounterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_counter_title, openAppPendingIntent)

        val incrementIntent = Intent(context, CounterWidgetProvider::class.java).apply {
            action = ACTION_INCREMENT
        }
        val incrementPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            incrementIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_counter_count, incrementPendingIntent)

        val resetIntent = Intent(context, CounterWidgetProvider::class.java).apply {
            action = ACTION_RESET
        }
        val resetPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            resetIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_counter_reset, resetPendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    companion object {
        const val ACTION_INCREMENT = "top.manpok.blog.action.WIDGET_INCREMENT"
        const val ACTION_RESET = "top.manpok.blog.action.WIDGET_RESET"

        fun refreshWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                android.content.ComponentName(context, CounterWidgetProvider::class.java)
            )
            if (appWidgetIds.isNotEmpty()) {
                val title = DataStoreManager.instance.getCounterTitleSync(context)
                val count = DataStoreManager.instance.getCounterCountSync(context)
                val views = RemoteViews(context.packageName, R.layout.widget_counter)
                views.setTextViewText(R.id.widget_counter_title, title)
                views.setTextViewText(R.id.widget_counter_count, count.toString())

                val openAppIntent = Intent(context, CounterActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val openAppPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    openAppIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_counter_title, openAppPendingIntent)

                val incrementIntent = Intent(context, CounterWidgetProvider::class.java).apply {
                    action = ACTION_INCREMENT
                }
                val incrementPendingIntent = PendingIntent.getBroadcast(
                    context,
                    1,
                    incrementIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_counter_count, incrementPendingIntent)

                val resetIntent = Intent(context, CounterWidgetProvider::class.java).apply {
                    action = ACTION_RESET
                }
                val resetPendingIntent = PendingIntent.getBroadcast(
                    context,
                    2,
                    resetIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_counter_reset, resetPendingIntent)

                appWidgetManager.updateAppWidget(appWidgetIds, views)
            }
        }
    }
}
