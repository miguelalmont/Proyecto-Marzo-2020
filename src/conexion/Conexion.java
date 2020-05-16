package conexion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Conexion.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class Conexion {

    //Valores de conexion a MySql
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private static final String HOST = "malcantara.salesianas.es:3306";
    private static final String BD = "malcantara_proyecto";
    private static final String JDBC_URL = "jdbc:mysql://" + HOST + "/" + BD + "?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String JDBC_USER = "malcantara";
    private static final String JDBC_PASS = "Nervion123!!";
    private static Driver driver = null;

    /**
     * Carga el driver de la conexion y la inicia.
     *
     * @return Retorna la conexion con la base de datos.
     */
    public static synchronized Connection getConnection() throws SQLException {

        if (driver == null) {
            try {

                //Se registra el driver
                Class jdbcDriverClass = Class.forName(JDBC_DRIVER);
                driver = (Driver) jdbcDriverClass.newInstance();
                DriverManager.registerDriver(driver);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
                System.out.println("Fallo en cargar el driver JDBC");
            }

        }
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
    }

    /**
     * Cierre del resultSet
     *
     * @param rs Entra por parametro un objeto de la clase ResultSet.
     *
     */
    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException sqle) {
            System.out.println("Fallo en cerrar el driver JDBC");
        }
    }

    /**
     * Cierre del PrepareStatement
     *
     * @param stmt Entra por parametro un objeto de la clase PreparedStatement.
     *
     */
    public static void close(PreparedStatement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException sqle) {
            System.out.println("Fallo en cerrar el driver JDBC");
        }
    }

    /**
     * Cierre del CallableStatement
     *
     * @param stmt Entra por parametro un objeto de la clase CallableStatement.
     *
     */
    public static void close(CallableStatement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException sqle) {
            System.out.println("Fallo en cerrar el driver JDBC");
        }
    }

    /**
     * Cierre de la conexion
     *
     * @param conn Entra por parametro un objeto de la clase Connection.
     *
     */
    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException sqle) {
            System.out.println("Fallo en cerrar el driver JDBC");
        }
    }

}
