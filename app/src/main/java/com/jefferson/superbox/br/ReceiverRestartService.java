package com.jefferson.superbox.br;
import android.content.*;
import android.util.*;
import java.util.*;

public class ReceiverRestartService extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		context.startService(new Intent(context,AppLockService.class).addFlags(intent.FLAG_ACTIVITY_NEW_TASK));
	}

}

	


