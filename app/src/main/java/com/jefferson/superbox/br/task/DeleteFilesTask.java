package com.jefferson.superbox.br.task;

import android.content.*;
import android.os.*;
import android.widget.*;
import com.jefferson.superbox.br.*;
import com.jefferson.superbox.br.app.*;
import com.jefferson.superbox.br.database.*;
import com.jefferson.superbox.br.util.*;
import java.io.*;
import java.util.*;

public class DeleteFilesTask extends AsyncTask {

	private int progress;
	private List<String> items;
	private SimpleDialog dialog;
	private Context context;
	private int position;
	private File rootFile;
	private PathsData mData;

	public DeleteFilesTask(Context context,ArrayList<String> items, int position, File rootFile) {

		this.items = items;
		this.position = position;
		this.rootFile = rootFile;
		this.context = context;
		File file = new File(Storage.getDefaultStorage());
		this.mData = PathsData.getInstance(context, file.getAbsolutePath());

	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		dialog = new SimpleDialog(context);
		dialog.showProgressBar(true)
			.setContentTitle("Excluindo")
			.setMax(items.size())
			.setProgress(0)
			.showPositiveButton(false)
			.setNegativeButton(context.getString(R.string.cancelar), new SimpleDialog.OnDialogClickListener(){
				@Override
				public boolean onClick(SimpleDialog dialog) {
					cancel(true);
					return true;
				}
			});
		dialog.show();
	}

	@Override
	protected void onCancelled(Object result) {
		Toast.makeText(context, "Cancelado!", 1).show();
		//synchronizeData();
	}

	@Override
	protected void onPostExecute(Object result) {

		dialog.dismiss();

		if (rootFile.list().length == 0) {
			deleteFolder(rootFile);
		}
		//synchronizeData();
	}

	@Override
	protected void onProgressUpdate(Object[] values) {

		super.onProgressUpdate(values);
		dialog.setProgress(progress);
		dialog.setContentText((String)values[1]);
		//mAdapter.removeItem(values[0]);
	}

	@Override
	protected Object doInBackground(Object[] p1) {

		try {
			for (String item : items) {
				if (isCancelled()) {
					break;
				}
				File file = new File(item);
				if (file.delete()) {
					progress++;
					String name = null;
					if ((name = mData.getPath(file.getName())) != null) {
						mData.deleteData(file.getName());
					}
					publishProgress(item, name);
				}
			}
		} catch (Exception e) {}
		return 0;
	}

	private void deleteFolder(File file) {
		PathsData.Folder folderDatabase = PathsData.Folder.getInstance(context);
		if (file.delete()) {
			folderDatabase.delete(file.getName(), position == 0 ? FileModel.IMAGE_TYPE: FileModel.VIDEO_TYPE);
		}
		folderDatabase.close();
	}
}
