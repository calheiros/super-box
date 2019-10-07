package com.jefferson.superbox.br;

import android.os.*;
import java.io.*;
import java.util.*;
import android.content.*;
import android.widget.*;
import android.util.*;

public class PasswordManager
{
	public File file;
	public Context context;
	public PasswordManager(Context context)
	{
		this.context = context;
		this.file = context.getFilesDir();
		Log.i("passwordManager", file.getPath());
	}
	public void setPassword(String password)
	{
		try
		{
			file.getParentFile().mkdirs();
			file.createNewFile();

			OutputStream out = new FileOutputStream(file);

			out.write(password.getBytes());
			out.flush();
			out.close();
		}
		catch (IOException e)
		{}

	}
	public String getPassword()
	{
	    String pass = new String();
		try
		{
			Scanner scan = new Scanner(file);
			pass = scan.nextLine();
		}
		catch (FileNotFoundException e)
		{

		}
		if (pass.isEmpty())
		{
			String sharedPass = getSharedPassword(context);
			if (!sharedPass.isEmpty())
			{
				setPassword(sharedPass);
				return sharedPass;
			}
		}
		return pass;
	}
	public static String getSharedPassword(Context context)
	{
		SharedPreferences sharedPrefers = context.getSharedPreferences("config", context.MODE_PRIVATE);

		return sharedPrefers.getString("pattern", "");
	}
}
