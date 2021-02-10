package com.jefferson.application.br;

import android.content.*;
import android.util.*;
import com.jefferson.application.br.database.*;

public class BootCompletedReceiver extends BroadcastReceiver
 {
	@Override
	public void onReceive(Context context, Intent arg1) {
	
		Log.w("boot_broadcast_poc", "starting service...");
		new AppsDatabase(context).clearUnlockedApps();
		context.startService(new Intent(context, AppLockService.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	    
	}
}

