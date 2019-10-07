package com.jefferson.superbox.br.widget;

import android.content.*;
import android.util.*;
import android.widget.*;
import com.jefferson.superbox.br.*;

public class CheckWidget extends CheckBox {

    public CheckWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	public void setChecked(boolean checked) {
		
		if (checked) {
			this.setBackgroundResource(R.drawable.ic_lock);
		} else {
			this.setBackgroundResource(R.drawable.ic_lock_unlocked);
		}
		super.setChecked(checked);
	}
}
