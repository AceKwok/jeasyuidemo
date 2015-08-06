package com.wenlong.jeasyuidemo.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wenlong.jeasyuidemo.pojo.User;
import com.wenlong.jeasyuidemo.util.JsonUtil;

public class UserServlet extends HttpServlet {
	public static Statement getStmt(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//step.2创建连接对象
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wenlong", "root", "123456");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//step.2创建Statement 对象
		Statement stmt = null;
		
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stmt;
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Statement stmt = null;
		
		stmt=getStmt();
		String method=req.getParameter("m");
		
		if("Servlet".equals(method)){
			//step.4创建Result对象，进行处理数据
			ResultSet rs = null;
			List<User> l = new ArrayList<User>();
			try {
				rs = stmt.executeQuery("select * from users");
				while(rs.next()){
					//数据的封装
					User user = new User();
					user.setId(rs.getInt("id"));
					user.setFirstname(rs.getString("firstname"));
					user.setLastname(rs.getString("lastname"));
					user.setPhone(rs.getString("phone"));
					user.setEmail(rs.getString("email"));
					l.add(user);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String jsonStr = JsonUtil.toJson(l);
			PrintWriter out = resp.getWriter();
			out.println(jsonStr);
		}else if("Update".equals(method)){
			//step.4创建Result对象，进行处理数据
			String id = req.getParameter("id");
			String firstname = req.getParameter("firstname");
			String lastname = req.getParameter("lastname");
			String phone = req.getParameter("phone");
			String email = req.getParameter("email");
			
			try {
				System.out.println("update users set firstname = '"+firstname+"',lastname ='"+lastname+"',phone ='"+phone+"', email = '"+email+"' where id=" +id);
				stmt.executeUpdate("update users set firstname = '"+firstname+"',lastname ='"+lastname+"',phone ='"+phone+"', email = '"+email+"' where id=" +id);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if("Insert".equals(method)){

			//step.4创建Result对象，进行处理数据
			String firstname = req.getParameter("firstname");
			String lastname = req.getParameter("lastname");
			String phone = req.getParameter("phone");
			String email = req.getParameter("email");
			//System.out.println(firstname+"|"+lastname+"|"+phone+"|"+email);
			try {
				System.out.println("insert into users(firstname,lastname,phone,email) values('"+firstname+"','"+lastname+"','"+phone+"','"+email+"')");
				stmt.executeUpdate("insert into users(firstname,lastname,phone,email) values('"+firstname+"','"+lastname+"','"+phone+"','"+email+"')");
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if("Delete".equals(method)){
			String id = req.getParameter("id");
			try {
				System.out.println("DELETE FROM users where id=" +id);
				stmt.executeUpdate("DELETE FROM users where id=" +id);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		doGet(req,resp);
	}

}

