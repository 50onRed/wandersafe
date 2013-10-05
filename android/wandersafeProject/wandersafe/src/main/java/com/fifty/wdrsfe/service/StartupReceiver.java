package com.fifty.wdrsfe.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartupReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, WandersafeService.class);
        context.startService(service);
    }
}
