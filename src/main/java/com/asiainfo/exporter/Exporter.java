package com.asiainfo.exporter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;

public class Exporter {
	
	private Context context;
	
	public Exporter(Context context){
		this.context = context;
	}
	
	public String exportTables(){
		try {
			Class.forName(context.getDriverClass());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("加载JDBC驱动类失败",e);
		}
		
		Connection con = null;
		Statement st = null;
		StringBuffer sb = new StringBuffer();
		try {
			con = DriverManager.getConnection(context.getJdbcUrl(), context.getUsername(), context.getPassword());
			st = con.createStatement();
			for(String tablename:context.getTableList()){
				sb.append(exportTable(st, tablename));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			if(st!=null){
				try {
					st.close();
				} catch (SQLException e) {}
			}
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e) {}
			}
		}
		
		return sb.toString();
	}
	
	private String exportTable(Statement st,String tablename){
		
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		ResultSet rs = null;
		try {
			String sql = "select * from "+ tablename +" where sonbr = "+context.getSonbr();
			TokenParser parser = new TokenParser("${", "}", context);
			sql = parser.parse(sql);
			System.out.println("execute sql:"+sql);
			rs = st.executeQuery(sql);
			
			if(!rs.next()){
				System.out.println("no data return of table "+tablename+".");
				return null;
			}
			ResultSetMetaData rsmd = rs.getMetaData();
			sb.append("insert into "+parser.parse(tablename)+"(");
			int colcount = rsmd.getColumnCount();
			Column[] columns = new Column[colcount];
			for(int i=1;i<=colcount;i++){
				Column c = new Column();
				
				c.name = rsmd.getColumnName(i);
				c.type = rsmd.getColumnType(i);
				//c.value = rs.getObject(i);
				sb.append(c.name);
				if(i<colcount){
					sb.append(", ");
				}
				columns[i-1] =  c;
			}
			sb.append(") values(");
			
			for(int i=0;i<columns.length;i++){
				if(columns[i].name.equalsIgnoreCase("u_id")){
					sb.append("(select max(u_id)+1 from "+parser.parse(tablename)+")");
				}else{
					switch(columns[i].type){
					case Types.DATE:;
					case Types.TIME:;
					case Types.TIMESTAMP: 
						if(rs.getDate(i+1)!=null){
							sb.append("to_date('yyyy-MM-dd hh:mi:ss','"+format.format(rs.getDate(i+1))+"')");
						}
					break;
					case Types.VARCHAR:
						if(rs.getString(i+1)!=null){
							sb.append("'"+rs.getString(i+1)+"'");
						}
						break;
					default:
						if(rs.getObject(i+1)!=null){
							sb.append(rs.getObject(i+1));
						}
					}
				}
				if(i!=columns.length-1){
					sb.append(",");
				}
			}
			
			sb.append(");\r\n");
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return sb.toString();
	}
	
	public void exportToFile(){
		String r = exportTables();
		FileUtils.writeToFile(r, context.getPath());
	}
	
	class Column{
		String name;
		int type;
		Object value;
	}
}
