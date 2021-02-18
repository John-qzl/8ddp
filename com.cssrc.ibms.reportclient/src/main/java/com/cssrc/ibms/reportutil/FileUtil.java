package com.cssrc.ibms.reportutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
	// 生成临时文件
	public static File generateTempFile(String path,String fileName) {
		try {
			File tempF=File.createTempFile(fileName, ".temp",new File(path));
			return tempF;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 根据文件路径获取文件名
	public static String getFileName(String filePath) {
		String[] reportlets = filePath.split("/");
		String reportletName = reportlets[reportlets.length - 1];
		return reportletName;
	}
	/**
	 * @param 读取文件流
	 * @return
	 */
	public static byte[] readByte(String filePath) {
		try {
			FileInputStream fis = new FileInputStream(filePath);
			byte[] r = new byte[fis.available()];
			fis.read(r);
			fis.close();
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param 读取文件流
	 * @return
	 */
	public static byte[] readByte(File f) {
		try {
			FileInputStream fis = new FileInputStream(f);
			byte[] r = new byte[fis.available()];
			fis.read(r);
			fis.close();
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    public static byte[] readByte(InputStream inputStream)
    {
        try {
            byte[] r = new byte[inputStream.available()];
            inputStream.read(r);
            inputStream.close();
            return r;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}