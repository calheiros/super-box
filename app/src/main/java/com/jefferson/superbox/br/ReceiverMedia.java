package com.jefferson.superbox.br;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.support.v7.app.*;
import android.widget.*;
import com.jefferson.superbox.br.model.*;
import com.jefferson.superbox.br.task.*;
import com.jefferson.superbox.br.util.*;
import java.io.*;
import java.util.*;
import com.jefferson.superbox.br.task.ImportTask;
import android.support.v7.app.AlertDialog;

public class ReceiverMedia extends Activity {

	private ArrayList<Uri> mediaUris;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String action = intent.getAction();
		try {
			onReceive(action, intent);
		} catch (Exception e) {
			Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
			finish();
		}
	}
	private void onReceive(String action, Intent intent) throws Exception {

		if (action.equals(Intent.ACTION_SEND)) {

			Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
			File mFile = new File(CodeManager.getPathFromMediaUri(uri, this));

			ArrayList<FileModel> models = new ArrayList<>();
			FileModel model = getModel(mFile.getPath());
			if (model != null) {
				models.add(model);
				new ImportTask(models, this, ImportTask.SESSION_OUTSIDE_APP).execute();
			} else {
				finish();
			}
		} else if (action.equals(intent.ACTION_SEND_MULTIPLE)) {

			mediaUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
			ArrayList<FileModel> models = new ArrayList<>();

			for (Uri uri : mediaUris) {
				String path = CodeManager.getPathFromMediaUri(uri, this);
				FileModel model = getModel(path);
				if (model != null)
					models.add(model);
			}
			if (models.size() > 0) {
				new ImportTask(models, this, ImportTask.SESSION_OUTSIDE_APP).execute();
			} else {
				Toast.makeText(this, "Arquivo(s) não suportado(s)", Toast.LENGTH_LONG).show();
				finish();
			}
		}}

	private FileModel getModel(String res) {
		
		FileModel model = new FileModel();
		model.setResource(res);

		if (MediaFilter.isImage(new File(res))) {

			model.setDestination(Storage.getFolder(Storage.IMAGE).getAbsolutePath());
			model.setType(FileModel.IMAGE_TYPE);
		} else if (MediaFilter.isVideo(new File(res))) {

			model.setDestination(Storage.getFolder(Storage.VIDEO).getAbsolutePath());
			model.setType(FileModel.VIDEO_TYPE);
		} else return null;


		return model;
	}
}
	

	
