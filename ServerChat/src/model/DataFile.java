package model;

import java.io.Serializable;
import javax.swing.ImageIcon;

public class DataFile implements Serializable {
	
	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    private byte[] file;
    private String name;
}
