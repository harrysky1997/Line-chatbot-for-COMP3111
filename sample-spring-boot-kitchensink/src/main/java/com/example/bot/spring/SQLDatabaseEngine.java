package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		String result = null;
		try {
			Connection connection = this.getConnection();
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT keyword, response FROM lab3 where LOWER(keyword) like LOWER(concat('%', ?, '%'))");
			stmt.setString(1, text);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getString(2);
			}
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			System.out.println("Failed to get from database" + e);
		}
		if (result != null)
			return result;
		throw new Exception("NOT FOUND");
	}
	
	/*List<String> getTourList() throws Exception {
		//Write your code here
		List<String> tourList = new ArrayList<String>();
		try {
			Connection connection = this.getConnection();
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT place FROM selectmenu");
			ResultSet placeList = stmt.executeQuery();
			while (placeList.next()) {
				tourList.add(placeList.getString(1));
			}
			placeList.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {
			System.out.println("Failed to get from database" + e);
		}
		if (tourList != null)
			return tourList;
		throw new Exception("NOT FOUND");
	}*/

	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}