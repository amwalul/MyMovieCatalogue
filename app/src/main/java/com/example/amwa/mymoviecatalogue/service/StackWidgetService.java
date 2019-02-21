package com.example.amwa.mymoviecatalogue.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.amwa.mymoviecatalogue.adapter.StackRemoteViewsFactory;

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}
