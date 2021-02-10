package com.jefferson.application.br;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.jefferson.application.br.*;
import java.io.*;
import java.util.*;

import com.jefferson.application.br.R;

public class ContatosAdapter extends BaseAdapter
{  
    ArrayList<ContactsData> selecionados;
	ArrayList<ContactsData> data;
	ContactsData contactsData;
	LayoutInflater inflater;
    public ContatosAdapter(ArrayList<ContactsData> data, Activity mActivity)
	{
		this.data = data;
		selecionados = new ArrayList<ContactsData>();
		inflater = (LayoutInflater) mActivity
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}
	@Override
	public int getCount()
	{

		return data.size();
	}

	@Override
	public Object getItem(int position)
	{
		
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_contatos_itens, null);
		TextView nome = (TextView)vi.findViewById(R.id.contatos_nome);
		TextView numero = (TextView)vi.findViewById(R.id.contatos_numero);
		//CircleImageView photo = (CircleImageView)vi.findViewById(R.id.imagePhoto);
		CheckBox checkW = (CheckBox)vi.findViewById(R.id.check_contacts);

		contactsData = data.get(position);
		nome.setText(contactsData.nome);
		numero.setText(contactsData.numero_phone);


       /* if (contactsData.photo == null)
			photo.setImageResource(R.drawable.ic_photo_contact_null);
		else 
			photo.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(data.get(position).photo)));

		if (selecionados.contains(contactsData))
		{
			checkW.setChecked(true);
		}
		else
		{
			checkW.setChecked(false);
		}
      */
		return vi;
	}

}
