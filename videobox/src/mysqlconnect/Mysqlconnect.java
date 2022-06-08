package mysqlconnect;

import java.sql.*;


public class Mysqlconnect {
	public static void main(String args[]){
		//加载
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("成功加载驱动！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("加载驱动失败！");
			e.printStackTrace();
		}
		Connection con;
		String url = "jdbc:sqlserver://localhost:1433;integratedSecurity=true; DatabaseName=videouser";
		
		try {
			con = DriverManager.getConnection(url);
			if(!con.isClosed())
				System.out.println("成功连接数据库");
			Statement statement = con.createStatement();
			String sql = "select * from user";
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				System.out.print(rs.getString("姓名"));
				System.out.print(" ");
				System.out.print(rs.getString("年龄"));
				System.out.print(" ");
				System.out.print(rs.getString("性别"));
				System.out.print(" ");
			}
		}catch(Exception e) {
			System.out.println("获取信息失败");
			e.printStackTrace();
		}
	}
}
