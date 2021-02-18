package com.cssrc.ibms.core.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.security.Key;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.encrypt.Base64;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.string.UUIDGenerator;

public class FileOperator {
	public FileOperator() {
	}

	public static String keyName;

	public static boolean reNameFile(String oldName, String newName) {
		boolean jdg = true;
		try {
			File oldFile = new File(oldName);
			File newFile = new File(newName);
			oldFile.renameTo(newFile);
		} catch (Exception e) {
			e.printStackTrace();
			jdg = false;
		}
		return jdg;
	}

	/**
	 * 将原文件拷贝到目标文件
	 * 
	 * @param sourceFile
	 * @param targetFile
	 * @return boolean
	 */
	public static boolean copyFile(String sourceFile, String targetFile) {
		String path = sourceFile;
		String target = targetFile;
		boolean flag = true;
		try {
			File file = new File(path);
			if(!file.exists()) {
				return false;
			}
			FileInputStream stream = new FileInputStream(file);// 把文件读入
			OutputStream bos = new FileOutputStream(target);// 建立一个上传文件的输出流
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
				bos.write(buffer, 0, bytesRead);// 将文件写入服务器
			}
			bos.close();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	/**
	 * 文件夹下所有文件夹和文件的拷贝
	 * 
	 * @param source
	 * @param target
	 */
	public static void copyDirectiory(String source, String target) {
		String file1 = target;
		String file2 = source;
		try {
			File sourceFile=new File(file2);
			if(!sourceFile.exists()){
				return;
			}
			(new File(file1)).mkdirs();
			File[] file = (new File(file2)).listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					FileInputStream input = new FileInputStream(file[i]);
					FileOutputStream output = new FileOutputStream(file1 + "/"
							+ file[i].getName());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (file[i].isDirectory()) {
					copyDirectiory(file2 + "/" + file[i].getName(), file1 + "/"
							+ file[i].getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List fileWithPath = new ArrayList();

	private void getFileWithPath(String target) {
		String file2 = target;
		try {
			File[] file = (new File(file2)).listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					fileWithPath
							.add(file2 + File.separator + file[i].getName());
				}
				if (file[i].isDirectory()) {
					this.getAllFileWithPath(file2 + File.separator
							+ file[i].getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据文件路径取得该文件路径下的所有文件
	 * 
	 * @param target
	 *            文件路径
	 * @return List 返回的文件路径list
	 */
	public List getAllFileWithPath(String target) {
		this.getFileWithPath(target);
		return this.fileWithPath;
	}

	/**
	 * 删除文件夹，如果该文件夹下有子文件或者文件夹，则全部删除
	 * 
	 * @param path
	 *            要删除的文件夹
	 * @return boolean
	 */
	public static boolean delFoldsWithChilds(String path) {
		int files = 0;
		File file = new File(path);
		File[] tmp = file.listFiles();
		if (tmp == null) {
			files = 0;
		} else {
			files = tmp.length;
		}
		for (int i = 0; i < files; i++) {
			FileOperator.delFoldsWithChilds(tmp[i].getAbsolutePath());
		}
		boolean ret = FileOperator.deleteFolder(path);
		return ret;
	}

	/**
	 * 判断文件或者文件夹是否存在
	 * 
	 * @param folderName
	 *            文件或者文件夹的绝对路径
	 * @return boolean
	 */
	public static boolean isFileExist(String folderName) {
		File file = new File(folderName);
		boolean returnBoolean = file.exists();
		return returnBoolean;
	}

	/**
	 * 这里的路径格式必须是：c:\tmp\tmp\ 或者是c:\tmp\tmp
	 * 
	 * @param path
	 * @return boolean
	 */
	public static boolean createFolders(String path) {
		return (new File(path)).mkdirs();
	}

	/**
	 * 取得文件名称（带后缀）
	 * 
	 * @param filePath
	 *            文件路径（包括文件名称）
	 * @return String 文件名称
	 */
	public static String getFileName(String filePath) {
		String fileName = "";
		String tmpFilePath = filePath;
		int winIndex = tmpFilePath.lastIndexOf("\\");
		int linuxIndex = tmpFilePath.lastIndexOf("/");
		if (winIndex != -1)
			fileName = tmpFilePath
					.substring(winIndex + 1, tmpFilePath.length()).trim();
		if (linuxIndex != -1)
			fileName = tmpFilePath.substring(linuxIndex + 1,
					tmpFilePath.length()).trim();
		return fileName;
	}

	/**
	 * 取得文件路径(无文件名)
	 * 
	 * @param fileFullPath
	 *            文件路径（包括文件名称）
	 * @return String 文件名称
	 */
	public static String getFilePath(String fileFullPath) {
		String filePath = "";
		String tmpFilePath = fileFullPath;
		int winIndex = tmpFilePath.indexOf("\\");
		int linuxIndex = tmpFilePath.indexOf("/");
		if (winIndex != -1)
			filePath = tmpFilePath.substring(0, winIndex + 1).trim();
		if (linuxIndex != -1)
			filePath = tmpFilePath.substring(0, linuxIndex + 1).trim();
		return filePath;
	}

	/**
	 * 得到文件的后缀
	 * 
	 * @param fileName
	 *            文件名称
	 * @return String 后缀名称
	 */
	public static String getSuffix(String fileName) {
		String returnSuffix = "";
		String tmp = "";
		try {
			int index = fileName.lastIndexOf(".");
			if (index == -1) {
				tmp = "";
			} else
				tmp = fileName.substring(index + 1, fileName.length());
		} catch (Exception e) {
			tmp = "";
		}
		returnSuffix = tmp;
		return returnSuffix;
	}

	/**
	 * 递归创建文件
	 * 
	 * @param path
	 * @return boolean
	 */
	private static boolean createFolds(String path) {
		boolean ret = false;
		String child = path;
		if (!FileOperator.isFileExist(path)) {
			int i = path.lastIndexOf(File.separator);
			String pathTmp = path.substring(0, i);
			child = pathTmp;
			FileOperator.createFolds(pathTmp);
			ret = FileOperator.createFolder(child);
		} else {
			ret = false;
		}
		return ret;
	}

	/**
	 * 将文件的路径格式转换为标准的文件路径格式
	 * 
	 * @param inputPath
	 *            原文件路径
	 * @return String 转换后的文件路径
	 */
	public static String toStanderds(String inputPath) {
		String rtp = "";
		/**
		 * 这是使用正则表达式进行替换 先把所有的路径格式替换为linux下的，会出现多个连接的情况
		 */
		String pathChar = "/";
		String pathCharLin = "/";
		String pathCharWin = "\\";
		// char[] mychar = path.toCharArray();
		if (pathCharLin.equalsIgnoreCase(File.separator)) {
			pathChar = "/";
		}
		if (pathCharWin.equalsIgnoreCase(File.separator)) {
			pathChar = "\\\\";
		}
		rtp = FileOperator.replaceString("\\\\+|/+", inputPath, "/");
		rtp = FileOperator.replaceString("/+", rtp, pathChar);
		/**
		 * 这是使用正常的循环进行替换
		 */
		/***********************************************************************
		 * / String path = inputPath; char pathChar = '/'; char pathCharLin =
		 * '/'; char pathCharWin = '\\'; char[] mychar = path.toCharArray();
		 * if(String.valueOf((pathCharWin)).equalsIgnoreCase(File.separator)) {
		 * pathChar = pathCharWin; }
		 * if(String.valueOf((pathCharLin)).equalsIgnoreCase(File.separator)) {
		 * pathChar = pathCharLin; } for(int i = 0;i <mychar.length;i++) {
		 * if(mychar[i] == pathCharWin || mychar[i] == pathCharLin) { mychar[i]
		 * = pathChar; } if(mychar[i] != pathCharLin && mychar[i] !=
		 * pathCharWin) rtp += String.valueOf(mychar[i]); if(i <mychar.length-1)
		 * { if(mychar[i] == pathChar && mychar[i+1] != pathChar && mychar[i+1]
		 * != pathCharWin && mychar[i+1] != pathCharLin) { rtp +=
		 * String.valueOf(mychar[i]); } } } /
		 **********************************************************************/
		return rtp;
	}

	/**
	 * 将路径转换为linux路径－也可使用为将http的相对路径进行转换
	 * 
	 * @param inputPath
	 * @return String
	 */
	public static String toLinuxStanderds(String inputPath) {
		String rtp = "";
		/**
		 * 这是使用正则表达式进行替换
		 */
		rtp = FileOperator.replaceString("\\\\+|/+", inputPath, "/");
		rtp = FileOperator.replaceString("/+", rtp, "/");
		/**
		 * 这是使用正常的循环进行替换
		 */
		/***********************************************************************
		 * / String path = inputPath; char pathChar = '/'; char pathCharLin =
		 * '/'; char pathCharWin = '\\'; char[] mychar = path.toCharArray();
		 * if(String.valueOf((pathCharWin)).equalsIgnoreCase(File.separator)) {
		 * pathChar = pathCharWin; }
		 * if(String.valueOf((pathCharLin)).equalsIgnoreCase(File.separator)) {
		 * pathChar = pathCharLin; } pathChar = '/'; for(int i = 0;i
		 * <mychar.length;i++) { if(mychar[i] == pathCharWin || mychar[i] ==
		 * pathCharLin) { mychar[i] = pathChar; } if(mychar[i] != pathCharLin &&
		 * mychar[i] != pathCharWin) rtp += String.valueOf(mychar[i]); if(i
		 * <mychar.length-1) { if(mychar[i] == pathChar && mychar[i+1] !=
		 * pathChar && mychar[i+1] != pathCharWin && mychar[i+1] != pathCharLin)
		 * { rtp += String.valueOf(mychar[i]); } } } /
		 **********************************************************************/
		return rtp;
	}

	/**
	 * 在已经存在的路径下创建文件夹
	 * 
	 * @param path
	 * @return boolean
	 */
	public static boolean createFolder(String path/* ,String folderName */) {
		// String fPath = path + File.separator + folderName;
		File file = new File(path);
		boolean returnBoolean = file.mkdirs();
		return returnBoolean;
	}

	/**
	 * 删除文件夹，当该文件夹下有文件或者文件夹的时候不能删除该文件夹
	 * 
	 * @param path
	 * @return boolean
	 */
	public static boolean deleteFolder(String path/* ,String folderName */) {
		File file = new File(path);
		boolean returnBoolean = file.delete();
		return returnBoolean;
	}

	/**
	 * 删除目录
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path) {
		File file = new File(path);
		return file.delete();
	}

	/**
	 * 创建文件或者文件夹
	 * 
	 * @param path
	 * @param fileName
	 * @param inputStream
	 * @return boolean
	 */
	public static boolean createFile(String path, String fileName) {
		String fPath = path + File.separator + fileName;
		File file = new File(fPath);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean createFile(String fileName) {
		String fPath = fileName;
		File file = new File(fPath);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 替换函数
	 * 
	 * @param pattern
	 *            正则表达式
	 * @param inputStr
	 *            要替换的字符串
	 * @param replaceStr
	 *            要被替换的字符串
	 * @return String 替换之后的结果
	 */
	public static String replaceString(String pattern, String inputStr,
			String replaceStr) {
		java.util.regex.Pattern p = null; // 正则表达式
		java.util.regex.Matcher m = null; // 操作的字符串
		String value = "";
		try {// ['%\"|\\\\]校验非法字符.'"|\正则表达式
				// ^[0-9]*[1-9][0-9]*$
				// "['%\"|\n\t\\\\]"
				// 校验是否全部是空格：[^ ]
			p = java.util.regex.Pattern.compile(pattern);
			m = p.matcher(inputStr);
			value = m.replaceAll(replaceStr);
			m = p.matcher(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 获取classes路径。
	 * 
	 * @return 返回如下的路径E:\work\ibms\webapp\WEB-INF\classes\。
	 */
	public static String getClassesPath() {
		String path = StringUtil.trimSufffix(AppUtil.getRealPath("/"),
				File.separator)
				+ "\\WEB-INF\\classes\\".replace("\\", File.separator);

		return path;
	}

	/**
	 * 根据键在属性文件中获取数据。
	 * 
	 * @param fileName
	 *            属性文件名称。
	 * @param key
	 *            属性的键值。
	 * @return
	 */
	public static String readFromProperties(String fileName, String key) {
		String value = "";
		InputStream stream = null;
		try {
			stream = new BufferedInputStream(new FileInputStream(fileName));
			Properties prop = new Properties();
			prop.load(stream);
			value = prop.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}
	
	public static String readFromPropertiesAsStream(String fileName, String key) {
		String value = "";
		InputStream stream = null;
		try {
			stream =FileOperator.class.getClassLoader().getResourceAsStream(fileName);
			Properties prop = new Properties();
			prop.load(stream);
			value = prop.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}
	public static String generateFilename(String originalFilename) {
		SimpleDateFormat dirSdf = new SimpleDateFormat("yyyyMM");
		String filePre = dirSdf.format(new Date());
		String fileExt = "";
		int lastIndex = originalFilename.lastIndexOf('.');
		if (lastIndex != -1) {
			fileExt = originalFilename.substring(lastIndex);
		}
		String filename = filePre + File.pathSeparator
				+ UUIDGenerator.getUUID() + fileExt;
		return filename;
	}

	// 生成没有分号的文件名称
	public static String generateFilenameNoSemicolon(String originalFilename) {
		SimpleDateFormat dirSdf = new SimpleDateFormat("yyyyMM");
		String filePre = dirSdf.format(new Date());
		String fileExt = "";
		int lastIndex = originalFilename.lastIndexOf('.');
		if (lastIndex != -1) {
			fileExt = originalFilename.substring(lastIndex);
		}
		String filename = filePre + UUIDGenerator.getUUID() + fileExt;
		return filename;
	}

	/**
	 * 写字节数组到文件
	 * 
	 * @param fileName
	 * @param b
	 * @return
	 */
	public static boolean writeByte(String fileName, byte[] b) {
		try {
			BufferedOutputStream fos = new BufferedOutputStream(
					new FileOutputStream(fileName));
			fos.write(b);
			fos.flush();
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据字节大小获取带单位的大小。
	 * 
	 * @param size
	 * @return
	 */
	public static String getSize(double size) {
		DecimalFormat df = new DecimalFormat("0.00");
		if (size > 1024 * 1024) {
			double ss = size / (1024 * 1024);
			return df.format(ss) + " M";
		} else if (size > 1024) {
			double ss = size / 1024;
			return df.format(ss) + " KB";
		} else {
			return size + " bytes";
		}
	}

	/**
	 * 下载文件。
	 * 
	 * @param response
	 * @param fullPath
	 *            文件的全路径
	 * @param fileName
	 *            文件名称。
	 * @throws IOException
	 */
	public static void downLoadFile(HttpServletRequest request,
			HttpServletResponse response, String fullPath, String fileName)
			throws IOException {
		OutputStream outp = response.getOutputStream();
		File file = new File(fullPath);
		if (file.exists()) {
			response.setContentType("APPLICATION/OCTET-STREAM");
			String filedisplay = fileName;
			String agent = (String) request.getHeader("USER-AGENT");
			// firefox
			if (agent != null && agent.indexOf("MSIE") == -1) {
				String enableFileName = "=?UTF-8?B?"
						+ (new String(Base64.getBase64(filedisplay))) + "?=";
				response.setHeader("Content-Disposition",
						"attachment; filename=" + enableFileName);
			} else {
				filedisplay = URLEncoder.encode(filedisplay, "utf-8");
				response.addHeader("Content-Disposition",
						"attachment;filename=" + filedisplay);
			}
			FileInputStream in = null;
			try {
				outp = response.getOutputStream();
				in = new FileInputStream(fullPath);
				byte[] b = new byte[1024];
				int i = 0;
				while ((i = in.read(b)) > 0) {
					outp.write(b, 0, i);
				}
				outp.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					in.close();
					in = null;
				}
				if (outp != null) {
					outp.close();
					outp = null;
					response.flushBuffer();
				}
			}
		} else {
			outp.write("File does not exist!".getBytes("utf-8"));
		}
	}

	
	/**
	 * 下载文件。
	 * 
	 * @param response
	 * @param fullPath
	 *            文件的全路径
	 * @param fileName
	 *            文件名称。
	 * @throws IOException
	 */
	public static void downLoadFile(HttpServletRequest request,
			HttpServletResponse response, byte[] fbyte, String fileName)
			throws IOException {
		OutputStream outp = response.getOutputStream();
		response.setContentType("APPLICATION/OCTET-STREAM");
		String filedisplay = fileName;
		String agent = (String) request.getHeader("USER-AGENT");
		// firefox
		if (agent != null && agent.indexOf("MSIE") == -1) {
			String enableFileName = "=?UTF-8?B?"
					+ (new String(Base64.getBase64(filedisplay))) + "?=";
			response.setHeader("Content-Disposition",
					"attachment; filename=" + enableFileName);
		} else {
			filedisplay = URLEncoder.encode(filedisplay, "utf-8");
			response.addHeader("Content-Disposition",
					"attachment;filename=" + filedisplay);
		}
		FileInputStream in = null;
		try {
			outp.write(fbyte);
			outp.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
				in = null;
			}
			if (outp != null) {
				outp.close();
				outp = null;
				response.flushBuffer();
			}
		}
	
	}
	
	/**
	 * 克隆对象。
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Object cloneObject(Object obj) throws Exception {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(obj);

		ByteArrayInputStream byteIn = new ByteArrayInputStream(
				byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);

		return in.readObject();
	}

	/**
	 * 获取文件的字符集
	 * 
	 * @param file
	 * @return
	 */
	public static String getCharset(File file) {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1)
				return charset;
			if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE
					&& first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF
					&& first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();

			if (!checked) {
				int loc = 0;
				while ((read = bis.read()) != -1) {
					loc++;
					if (read >= 0xF0)
						break;
					// 单独出现BF以下的，也算是GBK
					if (0x80 <= read && read <= 0xBF)
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF)// 双字节 (0xC0 - 0xDF)
							// (0x80 -
							// 0xBF),也可能在GB编码内
							continue;
						else
							break;
						// 也有可能出错，但是几率较小
					} else if (0xE0 <= read && read <= 0xEF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}

			}
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charset;
	}

	/**
	 * 读取文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @return
	 */
	public static String readFile(String fileName) {
		try {
			File file = new File(fileName);
			String charset = getCharset(file);
			StringBuffer sb = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName), charset));
			String str;
			while ((str = in.readLine()) != null) {
				sb.append(str + "\r\n");
			}
			in.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 写入文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @param content
	 *            文件内容
	 */
	public static void writeFile(String fileName, String content) {
		writeFile(fileName, content, "utf-8");
	}

	/**
	 * 指定字符集，写入文件。
	 * 
	 * @param fileName
	 * @param content
	 * @param charset
	 */
	public static void writeFile(String fileName, String content, String charset) {
		Writer out;
		try {
			createFolder(fileName, true);
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), charset));
			out.write(content);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写入文件
	 * 
	 * @param fileName
	 * @param is
	 * @throws IOException
	 */
	public static void writeFile(String fileName, InputStream is)
			throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		byte[] bs = new byte[512];
		int n = 0;
		while ((n = is.read(bs)) != -1) {
			fos.write(bs, 0, n);
		}
		is.close();
		fos.close();
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 * @param isFile
	 */
	public static void createFolder(String path, boolean isFile) {
		if (isFile) {
			path = path.substring(0, path.lastIndexOf(File.separator));
		}
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
	}

	public static void createFolderFile(String path) {
		createFolder(path, true);
	}

	/**
	 * 创建文件目录
	 * 
	 * @param dirstr
	 *            根目录
	 * @param name
	 *            子目录名称
	 * @return
	 */
	public static void createFolder(String dirstr, String name) {
		dirstr = StringUtil.trimSufffix(dirstr, File.separator)
				+ File.separator + name;
		File dir = new File(dirstr);
		dir.mkdir();
	}

	/**
	 * 将stream按照utf-8编码转换为字符串。
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream input)
			throws IOException {
		return inputStream2String(input, "utf-8");

	}

	public static String inputStream2String(InputStream input, String charset)
			throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(input,
				charset));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line + "\n");
		}
		return buffer.toString();
	}

	/**
	 * 获取应用程序根路径。
	 * 
	 * @return 返回如下路径 E:\work\bpm\src\main\webapp\
	 */
	public static String getRootPath() {
		String rootPath = StringUtil.trimSufffix(AppUtil.getRealPath("/"),
				File.separator) + File.separator;
		return rootPath;
	}

	/**
	 * 根据参数生成KEY
	 * 
	 * @param strKey
	 * @return
	 */
	public static Key getKey(String strKey) {

		Key key = null;
		try {
			KeyGenerator _generator = KeyGenerator.getInstance("DES");
			//_generator.init(new SecureRandom(strKey.getBytes()));
			//防止linux下随机生成key
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(strKey.getBytes());
			_generator.init(56, secureRandom); 
			
			key = _generator.generateKey();
			_generator = null;
		} catch (Exception e) {
			throw new RuntimeException(
					"Error initializing SqlMap class. Cause: " + e);
		}
		return key;
	}

	/**
	 * 文件file进行加密并保存目标文件destFile中
	 *
	 * @param file
	 *            要加密的文件 如c:/test/srcFile.txt
	 * @param destFile
	 *            加密后存放的文件名 如c:/加密后文件.txt
	 */
	public static void encrypt(String file, String destFile) throws Exception {

		Cipher cipher = Cipher.getInstance("DES");
		Key key = getKey(keyName);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		InputStream is = new FileInputStream(file);
		OutputStream out = new FileOutputStream(destFile);
		CipherInputStream cis = new CipherInputStream(is, cipher);
		byte[] buffer = new byte[2048];
		int r;
		while ((r = cis.read(buffer)) > 0) {
			out.write(buffer, 0, r);
		}
		cis.close();
		is.close();
		out.close();
	}

	/**
	 * 文件采用DES算法解密文件
	 *
	 * @param file
	 *            已加密的文件 如c:/加密后文件.txt * @param destFile 解密后存放的文件名 如c:/
	 *            test/解密后文件.txt
	 */
	public static void decrypt(String file, String dest) throws Exception {

		Cipher cipher = Cipher.getInstance("DES");
		Key key = getKey(keyName);
		cipher.init(Cipher.DECRYPT_MODE, key);
		InputStream is = new FileInputStream(file);
		OutputStream out = new FileOutputStream(dest);
		CipherOutputStream cos = new CipherOutputStream(out, cipher);
		byte[] buffer = new byte[2048];
		int r;
		while ((r = is.read(buffer)) >= 0) {
			cos.write(buffer, 0, r);
		}
		cos.close();
		out.close();
		is.close();
	}
	
	/**
	 * 文件采用DES算法解密文件
	 *
	 * @param content 获取的文件字节流
	 * @param destFile 解密后存放的文件名 如c:/test/解密后文件.txt
	 */
	public static void decrypt(byte[] content, String dest) throws Exception {

		Cipher cipher = Cipher.getInstance("DES");
		Key key = getKey(keyName);
		cipher.init(Cipher.DECRYPT_MODE, key);
		InputStream is = new ByteArrayInputStream(content);
		OutputStream out = new FileOutputStream(dest);
		CipherOutputStream cos = new CipherOutputStream(out, cipher);
		byte[] buffer = new byte[2048];
		int r;
		while ((r = is.read(buffer)) >= 0) {
			cos.write(buffer, 0, r);
		}
		cos.close();
		out.close();
		is.close();
	}

	/**
	 * 根据文件全路径获取文件名称（带后缀）
	 * 
	 * @param filePath
	 *            文件路径（全路径：D:\ibms\attachFile\201606
	 *            b606eb83e8f84ef8828515a70f9444fa.docx）
	 * @return String 文件名称:201606b606eb83e8f84ef8828515a70f9444fa.docx
	 */
	public static String getFileNameByPath(String filePath) {

		String fileName = "";
		String tmpFilePath = filePath;
		int winIndex = tmpFilePath.lastIndexOf("\\");
		int linuxIndex = tmpFilePath.lastIndexOf("/");
		if (winIndex != -1)
			fileName = tmpFilePath
					.substring(winIndex + 1, tmpFilePath.length()).trim();
		if (linuxIndex != -1)
			fileName = tmpFilePath.substring(linuxIndex + 1,
					tmpFilePath.length()).trim();
		return fileName;
	}

}