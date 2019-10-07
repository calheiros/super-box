package com.jefferson.superbox.br.activity;

import android.content.*;
import android.os.*;
import android.support.v7.widget.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.jefferson.superbox.br.activity.MyCompatActivity;
import java.util.*;

import android.support.v7.widget.Toolbar;
import com.jefferson.superbox.br.*;

public class Gridview_selection extends MyCompatActivity implements MultiSelectRecyclerViewAdapter.ViewHolder.ClickListener 
{
	private String titulo;
	private Toolbar toolbar;
	private ImageView ic_select;
	private ArrayList<String> list;
    private RecyclerView mRecyclerView;
	private MultiSelectRecyclerViewAdapter  mAdapter;
	
	private App app;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_grid);
	    
		setupToolbar();
		mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
		LinearLayout mLayout=(LinearLayout)findViewById(R.id.lock_layout);
		
		titulo = getIntent().getStringExtra("name");
		list = getIntent().getStringArrayListExtra("data");
		
		getSupportActionBar().setTitle(titulo + "(0)");
		
		mRecyclerView.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);
	
        mAdapter = new MultiSelectRecyclerViewAdapter(Gridview_selection.this, list, this);
        mRecyclerView.setAdapter(mAdapter);

		View selecAll = findViewById(R.id.selectAll);
	    ic_select = (ImageView)findViewById(R.id.ic_selet);

		mLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					ArrayList<String> listPaths = selectedItens();
					if (listPaths.size() > 0)
					{
						Intent i = new Intent();
						i.putExtra("selection", listPaths);
						setResult(RESULT_OK, (i));
						finish();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Você deve selecionar pelo menos um.", Toast.LENGTH_LONG).show();
					}
				}
			});

		selecAll.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{
					if (mAdapter.getSelectedItemCount() == list.size())
					{
						mAdapter.clearSelection();
					}
					else 
					{
						for (int i= 0; i < list.size();i++)
						{
							if (!mAdapter.isSelected(i))
							{
								mAdapter.toggleSelection(i);
							}
						}
					}
					updateIcon();
					setCountTitle();
				}
			});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}
	
	@Override
	protected void onRestart()
	{
		app.stopCount();
		super.onRestart();
	}
	
	@Override
	public void onItemClicked(int position)
	{
		mAdapter.toggleSelection(position);
		updateIcon();
		setCountTitle();
	}
  
	@Override
	public boolean onItemLongClicked(int position)
	{
		mAdapter.toggleSelection(position);
		updateIcon();
		setCountTitle();
		return true;
	}

	void setupToolbar()
	{
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	private List<Integer> selectedItensPosition()
	{
		List<Integer> integer = mAdapter.getSelectedItems();
		return integer;
	}
	private ArrayList<String> selectedItens()
	{
		ArrayList<String> selectedItens = new ArrayList<String>();

		for (int i : selectedItensPosition())
		{
			selectedItens.add(list.get(i));
		}
		return selectedItens;
	}
	private void setCountTitle()
	{
		CodeManager.updateTitle(Integer.toString(mAdapter.getSelectedItemCount()), titulo, getSupportActionBar());
	}
	private void updateIcon()
    {
		if (mAdapter.getSelectedItemCount() == list.size())
		{
			ic_select.setImageResource(R.drawable.ic_unselect_all);
		}
		else
		{
			ic_select.setImageResource(R.drawable.ic_select_all);
		}
	}
}
