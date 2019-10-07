package com.jefferson.superbox.br.util;


import android.util.*;
import java.io.*;

public class MediaFilter {

	final static String[] imageExt = new String[]{".jpg", ".png", ".gif", ".jpeg"};
	final static String[] videoExt = new String[]{".mp4", ".3gp", ".flv"};

	public static boolean isVideo(File file) {

        for (String extension : videoExt) {
			Log.i("MediaFilter", "Extension: " +extension);
			
            if (file.getName().toLowerCase().endsWith(extension)) {
				Log.i("MediaFilter", "Returning TRUE for VIDEO in: " + file);
                return true;
            }
        }
		Log.i("MediaFilter", "Returning FALSE for VIDEO in: " + file);
		
        return false;
    }

    public static boolean isImage(File file) {

        for (String extension : imageExt) {
			Log.i("MediaFilter", "Extension: " +extension);
			
            if (file.getName().toLowerCase().endsWith(extension)) {
				Log.i("MediaFilter", "Returning TRUE for IMAGE in: " + file);
				
                return true;
            }
        }
		Log.i("MediaFilter", "Returning FALSE for IMAGE: " + file);
		
        return false;
    }
}
