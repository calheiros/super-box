package com.jefferson.superbox.br.util;

import android.content.*;
import android.net.*;

public class IntentUtils {
	public static void shareApp(Context context) throws ActivityNotFoundException {

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "http://app-security.br.uptodown.com/android");
		context.startActivity(intent);
	}
	public static void reportBug(Context context)throws ActivityNotFoundException {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "jefferson.calheiros10@gmail.com"));
		intent.putExtra(Intent.EXTRA_SUBJECT, "Super Box - bug report");
		context.startActivity(Intent.createChooser(intent, "Relatar bug"));
	}
}
