package com.jefferson.superbox.br;

import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;

import android.support.v7.widget.Toolbar;

public class Intruder_fragment extends AppCompatActivity
{
	public Intruder_fragment()
	{

	}
	IntruderAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intruder_main);

		initToolbar();

		String path = "/.application/data/Intruder";
		File dirs = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), path);
		dirs.mkdirs();

		ArrayList<String>lista = new ArrayList<String>();
		File[] listDirs = dirs.listFiles();

		for (int i = 0; i < listDirs.length; i++)
		{
			lista.add(listDirs[i].getAbsolutePath());
		}
	    TextView empity = (TextView)findViewById(R.id.ic_empty);
		
		if (lista.size() == 0)
		{
			empity.setVisibility(View.VISIBLE);
		}
		RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.mrecycler_view);
		mRecyclerView.setNestedScrollingEnabled(false);
		GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

		adapter = new IntruderAdapter(lista, this);
		mRecyclerView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		
		return super.onCreateOptionsMenu(menu);
	}
    
	private void initToolbar()
	{
		Toolbar tool =(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(tool);
	}

}
	
