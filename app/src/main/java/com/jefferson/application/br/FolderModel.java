package com.jefferson.application.br;

import java.util.*;

public class FolderModel
{
	String name = "";
	String path = "";
	ArrayList<String> items = new ArrayList<>();

	public String getPath() {
		return path;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void addItem(String path) {
		items.add(path);
	}
	public String getName() {
		return name;
	}
	public void setPath(String path){
		this.path = path;
	}
	public ArrayList<String> getItems(){
	
		return items;
	}
}
