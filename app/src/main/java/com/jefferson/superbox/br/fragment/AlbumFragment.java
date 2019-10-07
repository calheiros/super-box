package com.jefferson.superbox.br.fragment;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.view.*;
import com.jefferson.superbox.br.*;
import com.jefferson.superbox.br.activity.*;
import com.jefferson.superbox.br.adapter.*;
import com.jefferson.superbox.br.database.*;
import com.jefferson.superbox.br.util.*;
import java.io.*;
import java.util.*;

import android.support.v4.app.Fragment;

public class AlbumFragment extends Fragment {
	
	private String root;
	private int position;
	private AlbumAdapter mAdapter;
	private View view;
	private SharedPreferences sharedPref;

	public AlbumFragment() {

	}

	public static Fragment newInstance(int position) {
		AlbumFragment frament = new AlbumFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("position", position);
		frament.setArguments(bundle);
	
		return frament;
	}

	public int getPagerPosition() {
		return position;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.main_gallery, container, false);
		
		sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		root = Environment.getExternalStorageDirectory().getAbsolutePath();
		position = getArguments() != null ? getArguments().getInt("position", 0): 0;

		RecyclerView mRecyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
	    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

		mRecyclerView.setLayoutManager(layoutManager);
		mAdapter = new AlbumAdapter(this, getLocalList());
        mRecyclerView.setAdapter(mAdapter);

		return view;
	}
	private ArrayList<FolderModel> getLocalList() {

		ArrayList<FolderModel> models = new ArrayList<FolderModel>();
        File root = Storage.getFolder(position == 0 ? Storage.IMAGE: Storage.VIDEO);
		root.mkdirs();
		PathsData.Folder db = PathsData.Folder.getInstance(getContext());
		if (root.exists()) {

			String Files[] = root.list();
			for (int i = 0;i < Files.length;i++) {
				File file = new File(root, Files[i]);

				if (file.isDirectory()) {
					File folder_list[] = file.listFiles();
					String folder_name = db.getFolderName(Files[i], position == 0 ? FileModel.IMAGE_TYPE : FileModel.VIDEO_TYPE);
					FolderModel model = new FolderModel();
					
					model.setName(folder_name == null ?  Files[i]: folder_name);
					model.setPath(file.getAbsolutePath());
					
					for (int j = 0; j < folder_list.length; j++) {
						model.addItem(folder_list[j].getAbsolutePath());
					}
					models.add(model);
					/*if (folder_list.length > 0)
						models.add(model);
					else file.delete();*/
				}
			}
		}
		View notice_view = view.findViewById(R.id.empty_linearLayout);
		notice_view.setVisibility(models.isEmpty() ? View.VISIBLE : View.GONE);
		db.close();
		return models;
	}
	final class DeleteAlbumTask extends AsyncTask {

		private FolderModel f_model;
	    private PathsData.Folder folderDatabase;
		private PathsData pathDatabase;
		
		public DeleteAlbumTask(FolderModel f_model) {
			this.f_model = f_model;
			this.folderDatabase = PathsData.Folder.getInstance(getContext());
			this.pathDatabase = PathsData.getInstance(getContext(), Storage.getDefaultStorage());
			
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Object result) {
			((MainActivity)getActivity()).update(getPagerPosition() == 0 ? MainFragment.ID.FIRST:MainFragment.ID.SECOND);
			super.onPostExecute(result);
		}
        
		@Override
		protected Object doInBackground(Object[] obj) {
			for(String path :f_model.getItems()) {
				File file = new File(path);
			    if(file.delete()) {
					pathDatabase.deleteData(file.getName());
				}
			}
			File folder_file = new File(f_model.getPath());
			if(folder_file.delete()) {
				folderDatabase.delete(folder_file.getName(), position == 0 ? FileModel.IMAGE_TYPE : FileModel.VIDEO_TYPE);
			}
			folderDatabase.close();
			return null;
		}
	}
    public void deleteAlbum(final FolderModel model) {
		
		String name = model.getName();
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle(getString(R.string.apagar));
		builder.setMessage("Tem certeza que deseja apagar a pasta \"" + name + "\" e todo o seu conteudo permanentimente?");
		builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface inter, int p2) {
					new DeleteAlbumTask(model).execute();
				}
		});
		builder.setNegativeButton("Não", null);
		builder.create().show();
	}
	public void Update() {
		if (mAdapter != null) {
			mAdapter.setUpdatedData(getLocalList());
		}
	}
}
