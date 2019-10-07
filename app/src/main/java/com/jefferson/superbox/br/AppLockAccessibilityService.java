package com.jefferson.superbox.br;

import android.app.*;
import android.app.usage.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.accessibility.*;
import android.widget.*;
import com.jefferson.superbox.br.widget.*;
import java.util.*;

public class AppLockAccessibilityService extends android.accessibilityservice.AccessibilityService {

	private ArrayList<String> logPackages;
    private static String TAG = AppLockAccessibilityService.class.getName();
	private String activityOnTop;
	public String pActivity="";
	private Runnable Runnable;
	private View v;
	private WindowManager wm;
	private Handler Handler;
	private Boolean RemoveCallback;
	private MaterialLockView materialLockView;
	private String CorrectPattern;

	@Override
	public void onCreate() {
		super.onCreate();
	    logPackages = new ArrayList<String>();

		HardwareKeyWatcher mHardwareKeyWatcher = new HardwareKeyWatcher(this);
        mHardwareKeyWatcher.setOnHardwareKeysPressedListenerListener(new HardwareKeyWatcher.OnHardwareKeysPressedListener() {
				@Override
				public void onHomePressed() {
					Toast.makeText(getApplicationContext(), "home pressed", Toast.LENGTH_LONG).show();

					System.out.println("stop your service here");
				}

				@Override
				public void onRecentAppsPressed() {
					Toast.makeText(getApplicationContext(), "recents pressed", Toast.LENGTH_LONG).show();

					System.out.println("stop your service here");
				}
			});
		mHardwareKeyWatcher.startWatch();
	}
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
		String CorrentPack = event.getPackageName().toString();
        Toast.makeText(this, printForegroundTask(), Toast.LENGTH_LONG).show();
		ArrayList<String> listApps = new ArrayList<String>();
		listApps.add("com.whatsapp");
		activityOnTop = event.getPackageName().toString();

		if (!activityOnTop.equals(pActivity)) {
			if (!listApps.contains(activityOnTop)) {
				pActivity = activityOnTop;

			} else {
				final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
					WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.TYPE_PRIORITY_PHONE,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
					PixelFormat.RGBX_8888);


				wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
				v = LayoutInflater.from(this)
					.inflate(R.layout.pattern, null);

				materialLockView = (MaterialLockView) v.findViewById(R.id.pattern);
				ImageView image=(ImageView)v.findViewById(R.id.iconApp);
				image.setImageDrawable(icon(event.getPackageName().toString()));

				materialLockView.setOnPatternListener(new MaterialLockView.OnPatternListener() {
						public void onPatternStart() {
							if (Handler != null && Runnable != null) {
								Handler.removeCallbacks(Runnable);
							}
						}

						public void onPatternDetected(List<MaterialLockView.Cell>pattern, String SimplePattern) {
							SharedPreferences patternLock = getSharedPreferences("config", MODE_PRIVATE);
							CorrectPattern = patternLock.getString("pattern", "");

							if (!SimplePattern.equals(CorrectPattern)) {
								materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Wrong);

								Handler = new Handler();
								Runnable = new Runnable(){

									@Override
									public void run() {
										materialLockView.clearPattern();
										RemoveCallback = !RemoveCallback;
									}
								};
								Handler.postDelayed(Runnable, 2000);

							} else {
								wm.removeView(v);
							}
							super.onPatternDetected(pattern, SimplePattern);

						}});

				wm.addView(v, params);
				pActivity = activityOnTop;
			}
		}
	}
	private String printForegroundTask() {
		String currentApp = "NULL";
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			UsageStatsManager usm = (UsageStatsManager)this.getSystemService("usagestats");
			long time = System.currentTimeMillis();
			List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000 * 1000, time);
			if (appList != null && appList.size() > 0) {
				SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
				for (UsageStats usageStats : appList) {
					mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
				}
				if (mySortedMap != null && !mySortedMap.isEmpty()) {
					currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
				}
			}
		} else {
			ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
			currentApp = tasks.get(0).processName;
		}

		Log.e("adapter", "Current App in foreground is: " + currentApp);
		return currentApp;
	}

	private Drawable icon(String packageName) {

		Drawable icon = null;
		try {
			icon = getPackageManager().getApplicationIcon(packageName);
		} catch (PackageManager.NameNotFoundException e) {}
		return icon;
	}

    @Override
    public void onInterrupt() {
        Log.e(TAG , " interrupt");
	}

	private String defaultHome() {

		PackageManager localPackageManager = getPackageManager();
		Intent intent = new Intent("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		String str = localPackageManager.resolveActivity(intent,
														 PackageManager.MATCH_DEFAULT_ONLY).activityInfo.packageName;		
		startActivity(localPackageManager.getLaunchIntentForPackage(str));

		return str;
	}
	
	@Override
	protected boolean onKeyEvent(KeyEvent event) {

		Toast.makeText(this, "key", Toast.LENGTH_LONG).show();

		return super.onKeyEvent(event);

	}
}
