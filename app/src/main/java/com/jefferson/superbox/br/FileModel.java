package com.jefferson.superbox.br;

public class FileModel {
	
    public static final String IMAGE_TYPE = "imagem";
    public static final String VIDEO_TYPE = "video";
    private String dest_path;
    private String source_path;
    private String type;

    public String getDestination() {
        return this.dest_path;
    }

    public String getResource() {
        return this.source_path;
    }

    public void setDestination(String str) {
        this.dest_path = str;
    }

    public void setResource(String str) {
        this.source_path = str;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getType() {
        return this.type;
    }
}
