package com.example.catalogmovieapi;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class MovieWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(RemoteViewsService.class.getSimpleName(), "onGetViewFactory: service called");
        return new StackRemoteViewsFactory(this, intent);
    }
}
