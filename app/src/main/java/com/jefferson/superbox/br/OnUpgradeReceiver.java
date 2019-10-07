package com.jefferson.superbox.br;
import android.content.*;
import android.widget.*;
import com.jefferson.superbox.br.database.*;
import com.jefferson.superbox.br.util.*;

public class OnUpgradeReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, Intent intent) {
		
		context.startService(new Intent(context, AppLockService.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}
}
