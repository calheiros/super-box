package com.jefferson.superbox.br.receiver;

import android.content.*;
import com.jefferson.superbox.br.*;
import com.jefferson.superbox.br.util.*;

public class WifiReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(Utils.isConnected(context)) {
			//App.app.requestNewAd();
		}
	}
}
