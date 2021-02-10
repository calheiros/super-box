package com.jefferson.application.br.util;
import java.io.*;

public class MyLog {
	static FileWriter fw = null;
	public static void log(String name, Object log) {
		String fmt = name + " :           " + String.valueOf(log) + "\n";
		try {
			File file = new File(Storage.getInternalStorage(), "log.txt");
			if (fw == null) {
				fw = new FileWriter(file);
			}
			fw.append(fmt);
			fw.close();
		} catch (IOException e) {}
	}
}
