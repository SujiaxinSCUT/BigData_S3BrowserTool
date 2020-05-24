package com.s3syntool.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.s3syntool.controller.SecondaryController;

public class Logger {

	private SecondaryController controller;
	
	private SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm" );
	
	public Logger(SecondaryController controller) {
		this.controller = controller;
	}
	
	public void info(String mes) {
		StringBuilder info = new StringBuilder("[Info]");
		Date date = new Date(System.currentTimeMillis());
		info.append(sdf.format(date)).append(":").append(mes);
		controller.logInfo(info.toString());
	}
}
