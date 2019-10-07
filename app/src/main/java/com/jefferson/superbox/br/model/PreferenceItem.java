package com.jefferson.superbox.br.model;

public class PreferenceItem
{
	public String item_name;
	public int type;
	public String description;
    public int icon_id;
	public static final int ITEM_SWITCH_TYPE = 3;
	public static final int ITEM_TYPE = 2;
	public static final int SECTION_TYPE = 1;
	public boolean is_checked;
	
	public PreferenceItem()
	{
		this.item_name = null;
		type = 0;
		description = null;
	}
}
