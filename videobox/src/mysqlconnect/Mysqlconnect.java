package mysqlconnect;

import java.sql.*;


public class Mysqlconnect {
	public static void main(String args[]){
		//����
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("�ɹ�����������");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("��������ʧ�ܣ�");
			e.printStackTrace();
		}
		Connection con;
		String url = "jdbc:sqlserver://localhost:1433;integratedSecurity=true; DatabaseName=videouser";
		
		try {
			con = DriverManager.getConnection(url);
			if(!con.isClosed())
				System.out.println("�ɹ��������ݿ�");
			Statement statement = con.createStatement();
			String sql = "select * from user";
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				System.out.print(rs.getString("����"));
				System.out.print(" ");
				System.out.print(rs.getString("����"));
				System.out.print(" ");
				System.out.print(rs.getString("�Ա�"));
				System.out.print(" ");
			}
		}catch(Exception e) {
			System.out.println("��ȡ��Ϣʧ��");
			e.printStackTrace();
		}
	}
}
