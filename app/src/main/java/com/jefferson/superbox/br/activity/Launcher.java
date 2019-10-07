package com.jefferson.superbox.br.activity;

import android.app.*;
import android.content.*;
import android.os.*;
import com.jefferson.superbox.br.*;

public class Launcher extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new Thread() {
			
			@Override
			public void run() {
				Intent i = new Intent(Launcher.this, VerifyActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
			}
		}.start();
	}
}
