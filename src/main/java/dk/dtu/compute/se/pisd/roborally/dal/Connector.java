/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.dal;

import com.mysql.cj.util.StringUtils;
import dk.dtu.compute.se.pisd.roborally.fileaccess.IOUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * the connector to the database
 * @author Ekkart Kindler, ekki@dtu.dk
 */
class Connector {

	private static final String HOST = "localhost";
	private static final int PORT = 3306;
	private static final String DATABASE = "pisu";
	private static final String USERNAME = Passwords.DB_USER;
	private static final String PASSWORD = Passwords.DB_PWD;

	private static final String DELIMITER = ";;";

	private Connection connection;

	/**
	 * connects to the database with, the host, the port and the database. then it log ins with username and password.
	 */
	Connector() {
		try {
			String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
			//String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?serverTimezone=UTC";
			connection = DriverManager.getConnection(url, USERNAME, PASSWORD);

			createDatabaseSchema();
		} catch (SQLException e) {
			// TODO we should try to diagnose and fix some problems here and
			//      exit in a more graceful way
			e.printStackTrace();
			// Platform.exit();
		}
	}

	/**
	 * creates the databaseschema in mysql.
	 * @author s224549
	 * @author s224567
	 */
	private void createDatabaseSchema() {

		String createTablesStatement;
		try {
			ClassLoader classLoader = Connector.class.getClassLoader();
			URI uri = classLoader.getResource("schemas/createschema.sql").toURI();
			byte[] bytes = Files.readAllBytes(Paths.get(uri));
			createTablesStatement = new String(bytes);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			for (String sql : createTablesStatement.split(DELIMITER)) {
				if (!StringUtils.isEmptyOrWhitespaceOnly(sql)) {
					statement.executeUpdate(sql);
				}
			}

			statement.close();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO error handling
			try {
				connection.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * a method that you can call to get the connection.
	 * @return the connection
	 */
	Connection getConnection() {
		return connection;
	}

}
