package com.pengpeng.elifelistenapp.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;


import android.os.Environment;
import android.util.Log;

/**
 * 日志工具类,提供打印行号开关，打印到文件函数f()
 * 
 * @author zhouyuanbin
 * @version 1.0
 * @date 2013-8-28
 */
public class LogUtil {
	private final static String DEFAULT_TAG = "BaiduNavi";

	/**
	 * 此常量用于控制是否打日志到Logcat中 release版本中本变量应置为false
	 */
	public static boolean LOGGABLE = true;
	/**
	 * 打印error级别的log
	 * 
	 * @param moduleName moduleType 模块名称
	 * @param str 内容
	 */
	public static void e(String moduleName, String str) {
		if (LOGGABLE) {
			StackTraceElement ste = new Throwable().getStackTrace()[1];
			Log.e(DEFAULT_TAG, makeLogDetailInfoString(moduleName, str, ste));
		}
	}

	/**
	 * 将日志打印到文件中
	 * 
	 * @param moduleName moduleType 模块名称
	 * @param str
	 */
	public static void f(String moduleName, String str) {
		String sLogFilePath = Environment.getExternalStorageDirectory().toString() + "/BaiduNaviLog.txt";
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",Locale.getDefault());
		String date = sDateFormat.format(new java.util.Date());
		StackTraceElement ste = new Throwable().getStackTrace()[1];
		String strLog = date + " " + makeLogDetailInfoString(moduleName, str, ste) + "\r\n";
		FileWriter writer;
		try {
			writer = new FileWriter(sLogFilePath, true);
			writer.write(strLog);
			writer.flush();
			writer.close();
		} catch (IOException e) {
		    LogUtil.e("", e.toString());
		}
	}
	
//	/**
//	 * 将日志打印到文件中
//	 *
//	 * @param moduleType moduleType 模块名称
//	 * @param str
//	 */
//	public static void saveFellowLogToFile(String moduleName, String str) {
//		if (LOGGABLE) {
//			String sLogFilePath = SysOSAPI.getInstance().GetSDCardPath() + "/fellow/FellowPlayLog.txt";
//			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",Locale.getDefault());
//			String date = sDateFormat.format(new java.util.Date());
//			StackTraceElement ste = new Throwable().getStackTrace()[1];
//			String strLog = date + " " + makeLogDetailInfoString(moduleName, str, ste) + "\r\n";
//			FileWriter writer;
//			try {
//				writer = new FileWriter(sLogFilePath, true);
//				writer.write(strLog);
//				writer.flush();
//				writer.close();
//			} catch (IOException e) {
//				LogUtil.e("", e.toString());
//			}
//		}
//	}

	/**
	 * 制作打log位置的文件名与文件行号详细信息
	 * 
	 * @param moduleName 模块类型
	 * @param str
	 * @param ste
	 * @return
	 */
	private static String makeLogDetailInfoString(String moduleName, String str, StackTraceElement ste) {

		String strLog = "[" + moduleName + "]-" + ste.getFileName() + "("+ ste.getLineNumber() + "): ";
		strLog += str;
		return strLog;
	}

	/**
	 * 打印调用堆栈
	 */
	public static void printCallStatck() {
		Throwable ex = new Throwable();
		StackTraceElement[] stackElements = ex.getStackTrace();
		if (stackElements != null) {
			LogUtil.e("printCallStatck", "----start----");
			for (int i = 0; i < stackElements.length; i++) {
				LogUtil.e("printCallStatck", "at " + stackElements[i].getClassName()
						+ "." + stackElements[i].getMethodName()
						+ "(" + stackElements[i].getFileName() + ":" 
						+ stackElements[i].getLineNumber() + ")\n");
			}
			LogUtil.e("printCallStatck", "----end----");
		}
	}
	
	public static String getCallStack() {
		StringBuffer sb = new StringBuffer();
		Throwable ex = new Throwable();
		StackTraceElement[] stackElements = ex.getStackTrace();
		if (stackElements != null) {
			for (int i = 0; i < stackElements.length; i++) {
				sb.append("at " + stackElements[i].getClassName()
						+ "." + stackElements[i].getMethodName()
						+ "(" + stackElements[i].getFileName() + ":" 
						+ stackElements[i].getLineNumber() + ")\n");
			}
		}
		return sb.toString();
	}
	
	public static String getCallStack(String filter) {
	    if ( filter == null ) {
	        return null;
	    }
        StringBuffer sb = new StringBuffer();
        Throwable ex = new Throwable();
        StackTraceElement[] stackElements = ex.getStackTrace();
        if (stackElements != null) {
            for (int i = 0; i < stackElements.length; i++) {
                if ( stackElements[i].getClassName() != null && stackElements[i].getClassName().contains(filter)) {
                    sb.append("at " + stackElements[i].getClassName()
                            + "." + stackElements[i].getMethodName()
                            + "(" + stackElements[i].getFileName() + ":" 
                            + stackElements[i].getLineNumber() + ")\n");
                }
            }
        }
        return sb.toString();
    }
}
