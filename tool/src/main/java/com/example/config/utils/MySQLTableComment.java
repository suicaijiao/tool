package com.example.config.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MySQLTableComment {

	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://10.98.4.165:3306/edu?useUnicode=true&characterEncoding=utf8&useSSL=false";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "123456";

	private static final String SQL = "SELECT * FROM ";// 数据库操作

	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据库连接
	 *
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param conn
	 */
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取数据库下的所有表名
	 */
	public static List<String> getTableNames() {
		List<String> tableNames = new ArrayList<>();
		Connection conn = getConnection();
		ResultSet rs = null;
		try {
			// 获取数据库的元数据
			DatabaseMetaData db = conn.getMetaData();
			// 从元数据中获取到所有的表名
			rs = db.getTables(null, null, null, new String[] { "TABLE" });
			while (rs.next()) {
				tableNames.add(rs.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return tableNames;
	}

	/**
	 * 获取表中所有字段名称
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 */
	public static List<String> getColumnNames(String tableName) {
		List<String> columnNames = new ArrayList<>();
		// 与数据库的连接
		Connection conn = getConnection();
		PreparedStatement pStemt = null;
		String tableSql = SQL + tableName;
		try {
			pStemt = conn.prepareStatement(tableSql);
			// 结果集元数据
			ResultSetMetaData rsmd = pStemt.getMetaData();
			// 表列数
			int size = rsmd.getColumnCount();
			for (int i = 0; i < size; i++) {
				columnNames.add(rsmd.getColumnName(i + 1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pStemt != null) {
				try {
					pStemt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return columnNames;
	}

	/**
	 * 获取表中所有字段类型与名称
	 * 
	 * @param tableName
	 * @return
	 */
	public static Map<String, String> getColumnTypes(String tableName) {
		Map<String, String> columnTypes = new LinkedHashMap<>();
		// 与数据库的连接
		Connection conn = getConnection();
		PreparedStatement pStemt = null;
		String tableSql = SQL + tableName;
		try {
			pStemt = conn.prepareStatement(tableSql);
			// 结果集元数据
			ResultSetMetaData rsmd = pStemt.getMetaData();
			// 表列数
			int size = rsmd.getColumnCount();
			for (int i = 0; i < size; i++) {
				columnTypes.put(rsmd.getColumnName(i + 1), rsmd.getColumnTypeName(i + 1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pStemt != null) {
				try {
					pStemt.close();
					closeConnection(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return columnTypes;
	}

	/**
	 * 获取表中主键名称和主键类型
	 * 
	 * @param tableName
	 * @return
	 */
	public static String getPrimaryKeyName(String tableName) {
		String pkName = "";
		// 与数据库的连接
		Connection conn = getConnection();
		PreparedStatement pStemt = null;
		String tableSql = SQL + tableName;
		ResultSet rs = null;
		try {
			pStemt = conn.prepareStatement(tableSql);
			rs = pStemt.executeQuery("show full columns from " + tableName);
			while (rs.next()) {
				if ("PRI".equals(rs.getString("key"))) {
					pkName = rs.getString("Field");
					pkName +=","+rs.getString("Type");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
					closeConnection(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return pkName;
	}

	/**
	 * 获取表中字段的所有列名和注释
	 * 
	 * @param tableName
	 * @return
	 */
	public static Map<String, String> getColumnComments(String tableName) {
		Map<String, String> map = new HashMap<>();// 列名注释集合
		// 与数据库的连接
		Connection conn = getConnection();
		PreparedStatement pStemt = null;
		String tableSql = SQL + tableName;
		ResultSet rs = null;
		try {
			pStemt = conn.prepareStatement(tableSql);
			rs = pStemt.executeQuery("show full columns from " + tableName);
			while (rs.next()) {
				map.put(rs.getString("Field"), rs.getString("Comment"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
					closeConnection(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	/**
	 * 获取当前数据库下的所有表名称
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<String> getAllTableName() throws Exception {
		List<String> tables = new ArrayList<>();
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SHOW TABLES ");
		while (rs.next()) {
			String tableName = rs.getString(1);
			tables.add(tableName);
		}
		rs.close();
		stmt.close();
		closeConnection(conn);
		return tables;
	}

	/**
	 * 获得某表的建表语句
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getCommentByTableName(List<String> tableName) throws Exception {
		Map<String, String> map = new HashMap<>();
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		for (int i = 0; i < tableName.size(); i++) {
			String table = (String) tableName.get(i);
			ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + table);
			if (rs != null && rs.next()) {
				String createDDL = rs.getString(2);
				String comment = parse(createDDL);
				map.put(table, comment);
			}
			rs.close();
		}
		stmt.close();
		closeConnection(conn);
		return map;
	}

	/**
	 * 返回注释信息
	 * 
	 * @param all
	 * @return
	 */

	public static String parse(String all) {
		String comment = null;
		int index = all.indexOf("COMMENT='");
		if (index < 0) {
			return "";
		}
		comment = all.substring(index + 9);
		comment = comment.substring(0, comment.length() - 1);
		return comment;
	}

	/**
	 * 首字母大写
	 * 
	 * @param srcStr
	 * @return
	 */
	public static String firstCharacterToUpper(String srcStr) {
		return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1);
	}

	/**
	 * 首字母小写
	 * 
	 * @param srcStr
	 * @return
	 */
	public static String firstCharacterToLower(String srcStr) {
		return srcStr.substring(0, 1).toLowerCase() + srcStr.substring(1);
	}

	/**
	 * 改变下划线后面第一个字符为大写
	 * 
	 * @param srcStr
	 * @param org
	 * @return
	 */
	public static String replaceUnderlineAndfirstToUpper(String srcStr, String org) {
		String newString = "";
		int first = 0;
		while (srcStr.indexOf(org) != -1) {
			first = srcStr.indexOf(org);
			if (first != srcStr.length()) {
				newString = newString + srcStr.substring(0, first);
				srcStr = srcStr.substring(first + org.length(), srcStr.length());
				srcStr = firstCharacterToUpper(srcStr);
			}
		}
		newString = newString + srcStr;
		return newString;
	}

	/**
	 * 改变下划线后面第一个字符为小写
	 * 
	 * @param srcStr
	 * @param org
	 * @return
	 */
	public static String replaceUnderlineAndfirstTo(String srcStr, String org) {
		String newString = "";
		int first = 1;
		while (srcStr.indexOf(org) != -1) {
			first = srcStr.indexOf(org);
			if (first != srcStr.length()) {
				newString = newString + srcStr.substring(0, first);
				srcStr = srcStr.substring(first + org.length(), srcStr.length());
				srcStr = firstCharacterToUpper(srcStr);
			}
		}
		newString = newString + srcStr;
		return newString;
	}

	/**
	 * 功能：获得列的数据类型 Jhipster JAVA MySQL String String VARCHAR Integer Integer INT
	 * Long Long BIGINT Float Float FLOAT Double Double DOUBLE BigDecimal
	 * BigDecimal Decimal Instant Instant DATETIME Blob byte[] BLOB
	 * 
	 * @param sqlType
	 * @return
	 */
	public static String sqlType2JavaType(String sqlType) {

		if (sqlType.equalsIgnoreCase("bit")) {
			return "boolean";
		} else if (sqlType.equalsIgnoreCase("tinyint")) {
			return "byte";
		} else if (sqlType.equalsIgnoreCase("smallint")) {
			return "short";
		} else if (sqlType.equalsIgnoreCase("int")) {
			return "Integer";
		} else if (sqlType.equalsIgnoreCase("bigint")) {
			return "Long";
		} else if (sqlType.equalsIgnoreCase("float")) {
			return "float";
		} else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")
				|| sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")
				|| sqlType.equalsIgnoreCase("smallmoney")) {
			return "BigDecimal";
		} else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")
				|| sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
				|| sqlType.equalsIgnoreCase("text")) {
			return "String";
		} else if (sqlType.equalsIgnoreCase("datetime")) {
			return "Date";
		} else if (sqlType.equalsIgnoreCase("image")) {
			return "Blod";
		}

		return null;
	}

	public static void main(String[] args) throws Exception {
		// List<String> tablesInfo = getColumnNames("student_assess");
		// for(String str : tablesInfo){
		// System.out.println(str);
		// }
		StringBuilder builder = new StringBuilder();
		Map<String, String> tablesTypeInfo = getColumnTypes("bit_product");
		for (String key : tablesTypeInfo.keySet()) {
			String value = tablesTypeInfo.get(key);
//			key = replaceUnderlineAndfirstToUpper(key, "_");
//			value = sqlType2JavaType(value);
			System.out.println(key+"****"+value);
		}
		
//		String keyName = MySQLTableComment.getPrimaryKeyName("bit_product").split(",")[0];
//		String keyType = MySQLTableComment.getPrimaryKeyName("bit_product").split(",")[1];
//		System.out.println(keyName+"***"+keyType);
	}
}
