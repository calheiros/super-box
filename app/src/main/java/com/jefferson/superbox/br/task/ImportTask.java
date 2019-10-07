package com.jefferson.superbox.br.task;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import com.google.android.gms.ads.*;
import com.jefferson.superbox.br.*;
import com.jefferson.superbox.br.activity.*;
import com.jefferson.superbox.br.app.*;
import com.jefferson.superbox.br.database.*;
import com.jefferson.superbox.br.fragment.*;
import com.jefferson.superbox.br.util.*;
import java.io.*;
import java.util.*;

import com.jefferson.superbox.br.R;

public class ImportTask extends AsyncTask {

    public static final int SESSION_OUTSIDE_APP = 1;
	public static final int SESSION_INSIDE_APP = 2;

    private ArrayList<String> importedFilesPath = new ArrayList<>();
	private int maxProgress;
	private PathsData db;
    private ArrayList<FileModel> models;
	private SimpleDialog mDialog;
	private Activity activity;
	private int mode;
	private PathsData.Folder folderDB;
	private StringBuilder err_message = new StringBuilder();
	private int err_count = 0;
	private FileTransfer mTransfer;
	private ProgressThreadUpdate mUpdate;
	private boolean waiting = false;
	private String WARNING_ALERT = "warning_alert";
	private String no_left_space_error_message = "\nNão há espaço suficiente no dispositivo\n";


	public ImportTask(ArrayList<FileModel> models, Activity activity, int mode) {

		this.activity = activity;
		this.maxProgress = models.size();
		this.mode = mode;
		this.models = models;
		this.folderDB = PathsData.Folder.getInstance(activity);
        this.db = PathsData.getInstance(activity, Storage.getDefaultStorage());
		this.mTransfer = new FileTransfer();

	}

	@Override
	protected void onPreExecute() {

	    if(SESSION_INSIDE_APP == mode) {
			((MainActivity)activity).prepareAd();
		}
		mDialog = new SimpleDialog(activity, SimpleDialog.PROGRESS_STYLE);
		mDialog.setCancelable(false);
		mDialog.setContentTitle(activity.getString(R.string.movendo))
			.setNegativeButton(activity.getString(R.string.cancelar), new SimpleDialog.OnDialogClickListener(){

				@Override
				public boolean onClick(SimpleDialog dialog) {
					mTransfer.cancel();
					cancel(true);
					return true;
				}
			})
			.show();

		mUpdate = new ProgressThreadUpdate(mTransfer, mDialog);
	}
	@Override
	protected void onPostExecute(Object result) {

		synchronize();
		String message = err_count > 0 ? "Transferencia completada com " + err_count + " " + (err_count > 1 ? "erros": "erro") + ":\n"  + err_message.toString() : "Transferência concluída com sucesso.";
		mDialog.setStyle(SimpleDialog.ALERT_STYLE);
		mDialog.setContentTitle("Resultado");
		mDialog.setContentText(message);
		mDialog.setPositiveButton("Ok", new SimpleDialog.OnDialogClickListener(){

				@Override
				public boolean onClick(SimpleDialog progress) {
					if (mode == SESSION_OUTSIDE_APP)
						activity.finish();
					return true;
				}
			}).show();
		mDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){

				@Override
				public void onDismiss(DialogInterface dialog) {
					if (mode == SESSION_INSIDE_APP) {
						((MainActivity)activity).showAd();
					}
				}
			});
		folderDB.close();
		db.close();
	}

	private void synchronize() {

		mUpdate.die();
		mDialog.dismiss();
		Storage.scanMediaFiles(importedFilesPath.toArray(new String[importedFilesPath.size()]));
		if (mode == SESSION_INSIDE_APP) {
		    ((MainActivity)activity)
				.update(MainFragment.ID.BOTH);
		} 
	}
	@Override
	protected void onCancelled(Object result) { 

		synchronize();
		if (mode == SESSION_OUTSIDE_APP) {
			activity.finish();
		}
		Toast.makeText(activity, "Cancelado!", 1).show();
	}

	@Override
	protected void onProgressUpdate(Object[] values) { 
		if (WARNING_ALERT.equals(values[0])) {
			warningAlert((String)values[1]);
		} else {
			String name = (String)values[1];
			mDialog.setContentText(name);
		}
	}
	@Override
	protected Boolean doInBackground(Object[] v) {

		long max = 0;
        for (FileModel resource : models) {
            File file = new File(resource.getResource());
            max += file.length();
        }
		File target = new File(Storage.getDefaultStorage());

		if ((target.getFreeSpace() < max)) {
			publishProgress(WARNING_ALERT, "Pode não haver espaço no dispositivo para completar a tranfêrencia, quer tentar continuar mesmo assim?");
			waitForResponse();
		}
		max /= 1024;
		mUpdate.setMax(max);
		mUpdate.start();

		try {
			for (FileModel model: models) {
				if (isCancelled()) return null;
                
				File file = new File(model.getResource());
				if (!file.exists()) {
					err_count++;
					err_message.append("\n" + activity.getString(R.string.erro) + " " + err_count + ": O arquivo \"" + file.getName() + "\" não existe!\n");
					continue;
				}
				publishProgress(null, file.getName());

				String folderName = file.getParentFile().getName();
				String randomString = RandomString.getRandomString(24);
				String randomString2 = RandomString.getRandomString(24);

				String folderId = folderDB.getFolderId(folderName, model.getType());
				String str = folderId;

				if (folderId == null) {
					folderDB.addName(folderName, randomString2, model.getType());
				} else {
					randomString2 = str;
				}
				File destFile = new File(model.getDestination() + File.separator + randomString2 + File.separator + randomString);
				destFile.getParentFile().mkdir();

				InputStream inputStream = new FileInputStream(file);
				OutputStream outputStream = new FileOutputStream(destFile);
				String response = mTransfer.transferStream(inputStream, outputStream);

				if (FileTransfer.OK.equals(response)) {
					if (Storage.deleteFile(file)) {
						db.insertData(randomString, model.getResource());
						importedFilesPath.add(file.getAbsolutePath());
					} else {
						destFile.delete();
					}
				} else {
					destFile.delete();
					err_count++;
					if (FileTransfer.Error.NO_LEFT_SPACE.equals(response)) {
						err_message.append(no_left_space_error_message);
						break;
					} else {
						err_message.append("\n" + activity.getString(R.string.erro) + err_count + ": " + response + " when moving: " + file.getName() + "\n");
					}
				}
			}
		} catch (Exception e) {
			err_message.append("Erro inesperado ocorrido!");
		}
		return true;
	}
	public void waitForResponse() {

		waiting = true;
		while (waiting) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {}
		}
	}
	private void warningAlert(String msg) {

		SimpleDialog mDialog = new SimpleDialog(activity, SimpleDialog.ALERT_STYLE);
		mDialog.setContentTitle("Aviso");
		mDialog.setContentText(msg);
		mDialog.setCancelable(false);
		mDialog.setPositiveButton("Continuar", new SimpleDialog.OnDialogClickListener() {

				@Override
				public boolean onClick(SimpleDialog dialog) {
					waiting = false;
					return true;
				}
			});

		mDialog.setNegativeButton(activity.getString(R.string.cancelar), new SimpleDialog.OnDialogClickListener(){

				@Override
				public boolean onClick(SimpleDialog dialog) {
					cancel(true);
					waiting = false;
					return true;
				}
			});
		mDialog.show();
	}
}
