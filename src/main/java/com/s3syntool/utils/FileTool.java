package com.s3syntool.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class FileTool {

	static ObjectOutputStream oos = null;
	static ObjectInputStream ois=null;
	
	public static void writeObjectToFile(Object o,File file) {
		try {
        	oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(o);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static Object readObjectFromFile(File file) {
		
		Object obj = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(file));
			obj = ois.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return obj;
	}	
	
}
