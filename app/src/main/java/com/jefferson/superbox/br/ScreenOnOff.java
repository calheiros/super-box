package com.jefferson.superbox.br;

import android.content.*;
import android.util.*;
import com.jefferson.superbox.br.database.*;

public class ScreenOnOff extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			new AppsDatabase(context).clearUnlockedApps();
			AppLockService.pActivity = null;
		}
    }
}
