package com.jefferson.superbox.br.util;
import java.util.*;

public class RandomString
{
	public static String Dictionary = "abcdefghijklmnopqrstuvwxyz01234567890123456789";
	
	public static String  getRandomString (int length)
	{
		String generate = new String();
		Random random = new Random();
		
		for(int i = 0; i < length; i++)
		{
			int pos = random.nextInt(Dictionary.length());
			generate += Dictionary.charAt(pos);
		}
		return generate;
	}
}
