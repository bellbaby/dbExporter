package com.asiainfo.exporter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class Context {
	
	private static String _configFilePath = "export.conf";
	private static String _timeformat = "yyyy-mm-dd hh24:mm:ss";
	private String configFilePath = _configFilePath;
	private String timeFormat = _timeformat;
	private String jdbcUrl;
	private String driverClass;
	private String username;
	private String password;
	private String YYYYMM;
	private String sonbr;
	private String path;
	private String homePath;
	
	private List<String> tableList = new ArrayList<String>();
	private Map<String,String> properties;
	

	
	public Context(String sonbr,String path,Map<String,String> properties){
		this.path = path;
		this.sonbr = sonbr;
		this.properties = new HashMap<String,String>();
		if(properties!=null){
			this.properties .putAll(properties);
		}
		this.homePath = System.getenv("EXPORTER_HOME");
	}
	
	public void init(){
		Properties ps=new Properties();
		InputStream is=null;
		try {
			 ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		        if (is == null) {
		            try {
		                is = classLoader.getResourceAsStream(configFilePath);
		            } catch (Exception e) {
		            }
		        }

		        if (is == null) {
		            try {
		                is = classLoader.getResourceAsStream("/"+configFilePath);
		            } catch (Exception e) {
		            }
		        }

		        if (is == null) {
		            try {
		                is = classLoader.getResourceAsStream("META-INF/"+configFilePath);
		            } catch (Exception e) {
		            }
		        }
			if(is==null){
				throw new RuntimeException("can not find config file："+Context.class.getResource(configFilePath));
			}
			ps.load(is);
			
			this.driverClass = ps.getProperty("jdbc.driver");
			this.jdbcUrl = ps.getProperty("jdbc.url");
			this.username = ps.getProperty("jdbc.user");
			this.password = ps.getProperty("jdbc.password");
			
			if(this.path==null){
				this.path = ps.getProperty("export.file");
			}
			
			String tables = ps.getProperty("table.namelist");
			String[] tableList = tables.split(",");
			for(String table:tableList){
				this.tableList.add(table);
			}
			
			for(Entry<Object, Object> e:ps.entrySet()){
				this.properties.put(e.getKey().toString(),e.getValue().toString());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally{
			if(is!=null)try{is.close();}catch(IOException e){}
		}
	}
	public String getConfigFilePath() {
		return configFilePath;
	}
	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getTableList() {
		return tableList;
	}
	public void setTableList(List<String> tableList) {
		this.tableList = tableList;
	}
	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	public String getYYYYMM() {
		return YYYYMM;
	}
	public void setYYYYMM(String yYYYMM) {
		YYYYMM = yYYYMM;
	}
	public String getSonbr() {
		return sonbr;
	}
	public void setSonbr(String sonbr) {
		this.sonbr = sonbr;
	}
	public String getTimeFormat() {
		return timeFormat;
	}
	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
}
