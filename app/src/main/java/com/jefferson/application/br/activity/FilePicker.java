package com.jefferson.application.br.activity;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.jefferson.application.br.*;
import com.jefferson.application.br.adapter.*;
import com.jefferson.application.br.database.*;
import com.jefferson.application.br.fragment.*;
import com.jefferson.application.br.model.*;
import com.jefferson.application.br.util.*;
import java.io.*;
import java.util.*;

import android.support.v7.widget.Toolbar;

public class FilePicker extends MyCompatActivity implements OnItemClickListener {

    private String currentPath;
    private FilePickerAdapter mAdapter;
    private ListView mListView;
    private TextView mText;
    private Toolbar mToolbar;
    private List<String> paths;
    private int position;

    class MoveFiles extends AsyncTask {

        ArrayList<String> mMovedArray;
        String folder;
        ProgressDialog progress;

        public MoveFiles(FilePicker filePicker, String str) {

            this.mMovedArray = new ArrayList<>();
            this.folder = str;
        }

        @Override
        protected void onPreExecute() {

            this.progress = new ProgressDialog(FilePicker.this);
			this.progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progress.setMax(paths.size());
            this.progress.setTitle("Movendo...");
			this.progress.setProgress(0);
            this.progress.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Object[] objArr) {

            this.progress.setProgress(((Integer) objArr[0]).intValue());
            super.onProgressUpdate(objArr);
        }

        @Override
        protected void onPostExecute(Object obj) {

            this.progress.dismiss();
			Intent intent = new Intent();
			intent.putExtra("moved_files", mMovedArray);
            setResult(RESULT_OK, intent);
            finish();
            super.onPostExecute(obj);
        }

        @Override
        protected Object doInBackground(Object[] objArr) {

            for (String str : paths) {

                File file = new File(str);
                File newFile = new File(folder, file.getName());

                if (file.renameTo(newFile)) {
					mMovedArray.add(str);
                }
				publishProgress(progress.getProgress() + 1);
				
            }
            return "Ok";
        }
    }

    @Override
    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.list_view_layout);

        this.mListView = (ListView) findViewById(R.id.androidList);
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.mText = (TextView) findViewById(R.id.mTextView);

        this.position = getIntent().getIntExtra("position", -1);
        this.paths = getIntent().getStringArrayListExtra("selection");
        this.currentPath = getIntent().getStringExtra("current_path");

        this.mAdapter = new FilePickerAdapter(getModels(this.position), this);
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setOnItemClickListener(this);

        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Move to");
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {

        int selectedItem = this.mAdapter.getSelectedItem();
        invalidateOptionsMenu();
        if (selectedItem == -1) {
            invalidateOptionsMenu();
        }
        mAdapter.setSelectedItem(i);
    }

    public void update() {
        this.mAdapter.update(getModels(this.position));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        int itemId = menuItem.getItemId();
        if (itemId == R.id.confirm) {
            new MoveFiles(this, (mAdapter.models.get(mAdapter.getSelectedItem())).getPath()).execute();
        } else if (itemId == 2131231044) {
			MainFragment.createFolder(position, this, this);
        } else {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public List<PickerModel> getModels(int i) {

        ArrayList<PickerModel> arrayList = new ArrayList<>();
        File storageAndFolder = Storage.getFolder(i == 0 ? Storage.IMAGE : Storage.VIDEO);
        PathsData.Folder instance = PathsData.Folder.getInstance(this);
        File[] listFiles = storageAndFolder.listFiles();

        for (File file : listFiles) {

            if (file.isDirectory() && !file.getAbsolutePath().equals(currentPath)) {

                PickerModel pickerModel = new PickerModel();
                File[] listFiles2 = file.listFiles();
                String folderName = instance.getFolderName(file.getName(), position == 0 ? FileModel.IMAGE_TYPE : FileModel.VIDEO_TYPE);
                if (folderName == null) {
                    folderName = file.getName();
                }
                int length = listFiles2.length;
                if (length > 0) {
                    pickerModel.setTumbPath(listFiles2[0].getAbsolutePath());
                }
                pickerModel.setPath(file.getAbsolutePath());
                pickerModel.setName(folderName);
                pickerModel.setSize(length);
                arrayList.add(pickerModel);
            }
        }
        if (arrayList.isEmpty()) {
            mText.setText("Nenhuma pasta");
        } else {
            mText.setVisibility(View.GONE);
        }
        return arrayList;
    }
}
