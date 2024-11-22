package com.java.payxpert.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionHelper {
	private static final ResourceBundle rb = ResourceBundle.getBundle("db");

	public static String getDriver() {
		return rb.getString("driver");
	}

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		String user = rb.getString("user");
		String pwd = rb.getString("password");
		String url = rb.getString("url");

		Class.forName(getDriver());
		return DriverManager.getConnection(url, user, pwd);
	}
}
