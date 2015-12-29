package com.asiainfo.exporter;

import java.util.HashMap;
import java.util.Map;

public class Main {
	public static void main(String[] args) {
		if(args==null || args.length==0){
			System.out.println("sonbr is requred!");
			System.exit(1);
		}
		
		String sonbr = args[0];
//    	String sonbr= "1015109900384616";
		String yyyyMM = "20"+sonbr.substring(2,6);
		String path = null;
		if(args.length>=2){
			path = args[1];
		}
		Map<String,String> properties = new HashMap<String,String>();
		properties.put("YYYYMM", yyyyMM);
		Context context = new Context(sonbr,path,properties);
		context.init();
		Exporter e = new Exporter(context);
		
		e.exportToFile();
	}
}
