package com.jefferson.superbox.br.adapter;

import android.app.*;
import android.view.*;
import android.widget.*;
import com.jefferson.superbox.br.*;
import com.jefferson.superbox.br.model.*;
import java.util.*;
import com.jefferson.superbox.br.fragment.*;
import android.content.pm.*;

public class SettingAdapter extends BaseAdapter {
	
    public LayoutInflater inflater;
    public ArrayList<PreferenceItem> items;
	public SettingFragment mSettingFragmemt;
	
    public SettingAdapter(ArrayList<PreferenceItem> arrayList, SettingFragment fragment) {
        
		this.mSettingFragmemt = fragment;
        this.items = arrayList;
        this.inflater = (LayoutInflater) fragment.getActivity().getSystemService("layout_inflater");
    }

    @Override
    public int getCount() {
		
        return this.items.size();
    }

    @Override
    public PreferenceItem getItem(int id) {
		
        return this.items.get(id);
    }

    @Override
    public long getItemId(int id) {
  
        return (long) id;
    }
   
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
		
        PreferenceItem preferenceItem = this.items.get(i);
        TextView mTextView = null, mTextView2 = null;
		ImageView mImageView = null;
		
        switch (preferenceItem.type) {
            case 3:
                view = inflater.inflate(R.layout.preference_switch_item, (ViewGroup) null);
                mTextView = (TextView) view.findViewById(R.id.title_view);
				
                ((ImageView) view.findViewById(R.id.ic_view)).setImageResource(preferenceItem.icon_id);
                ((Switch) view.findViewById(R.id.my_switch)).setChecked(mSettingFragmemt.getComponentEnabledSetting() == PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
                mTextView2 = (TextView) view.findViewById(R.id.description_text_view);
                if (preferenceItem.description == null)
					mTextView2.setVisibility(View.GONE);
                else {
					mTextView2.setText(preferenceItem.description);
				}
                mTextView.setText(preferenceItem.item_name);
                return view;
            case 1:
                view = inflater.inflate(R.layout.preference_section_item, (ViewGroup) null);
                ((TextView) view.findViewById(R.id.title_view)).setText(preferenceItem.item_name);
                return view;
            case 2:
                view = inflater.inflate(R.layout.preference_common_item, (ViewGroup) null);
                mTextView = (TextView) view.findViewById(R.id.description_text_view);
                mImageView = (ImageView) view.findViewById(R.id.ic_view);
                ((TextView) view.findViewById(R.id.item_title)).setText(preferenceItem.item_name);
                mImageView.setImageResource(preferenceItem.icon_id);
                if (preferenceItem.description != null) {
                    mTextView.setVisibility(View.VISIBLE);
                    mTextView.setText(preferenceItem.description);
                }
                return view;
            default:
                return (View) null;
        }
    }
}
