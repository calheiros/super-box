package com.jefferson.superbox.br.util;

import com.jefferson.superbox.br.*;
import java.util.*;

public class Thumbnails 
{
	private static int[] thumbs = {R.drawable.thumb_1, R.drawable.thumb_2, R.drawable.thumb_3};
	
	public static int getThumbnail() {
		Random rand = new Random();
		return thumbs[rand.nextInt(thumbs.length)];
		
	}
}
