package modelo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UsuarioConexion.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class UsuarioConexion extends conexion.Conexion {

    private final String SQL_INSERT
            = "CALL insert_usuario(?,?,?,?)";

    private final String SQL_UPDATE
            = "UPDATE usuarios SET usuario = ?, password = ?, nombre = ?, correo = ? WHERE id = ?";

    private final String SQL_DELETE
            = "DELETE FROM libros WHERE ISBN = ?";

    private final String SQL_SELECT
            = "SELECT id, usuario, nombre, correo, last_session FROM usuarios ORDER BY id";

    /**
     * Inserta un objeto en la base de datos
     *
     * @param user
     * @return
     */
    public boolean insert(Usuario user) {
        Connection conn = null;
        CallableStatement calst = null;
        try {
            conn = getConnection();
            calst = conn.prepareCall(SQL_INSERT);
            int index = 1;//contador de columnas
            calst.setString(index++, user.getUsuario());
            calst.setString(index++, user.getPassword());
            calst.setString(index++, user.getNombre());
            calst.setString(index, user.getMail());
            calst.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(calst);
            close(conn);
        }
    }

    /**
     * Modifica un objeto de la base de datos
     *
     * @param user
     * @param userUpdate
     * @return
     */
    public boolean update(Usuario user, Usuario userUpdate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            int index = 1;
            stmt.setString(index++, userUpdate.getUsuario());
            stmt.setString(index++, userUpdate.getPassword());
            stmt.setString(index++, userUpdate.getNombre());
            stmt.setString(index++, userUpdate.getMail());
            stmt.setInt(index, user.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(stmt);
            close(conn);
        }
    }

    /**
     * Elimina un objeto de la base de datos
     *
     * @param iSBN
     * @return
     */
    public boolean delete(String iSBN) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setString(1, iSBN);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(stmt);
            close(conn);
        }
    }

    /**
     * Crea una coleccion de todos los registros de la base de datos
     *
     * @return
     */
    public List<Usuario> select() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario user = null;
        List<Usuario> users = new ArrayList<Usuario>();
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String usuario = rs.getString(2);
                String nombre = rs.getString(3);
                String mail = rs.getString(4);
                String last_log = rs.getString(5);
                /*System.out.print(" " + id_persona);
                 System.out.print(" " + nombre);
                 System.out.print(" " + apellido);
                 System.out.println();
                 */
                user = new Usuario();
                user.setId(id);
                user.setUsuario(usuario);
                user.setNombre(nombre);
                user.setMail(mail);
                user.setLastSession(last_log);
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
        return users;
    }

    /**
     * Comprueba si una id esta en la base de datos
     *
     * @param usuario
     * @return
     */
    public int existeUsuario(String usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "SELECT count(id) FROM usuarios WHERE usuario = ?";

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }

    }

    /**
     * Devuelve true si el correo cumple el patron
     *
     * @param correo
     * @return
     */
    public boolean validarEmail(String correo) {

        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(correo);

        return mather.find();
    }

    /**
     * Devuelve true si el usuario introducido cumple las condiciones y
     * actualiza el campo last_session
     *
     * @param user
     * @return
     */
    public boolean login(Usuario user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sqlLogin = "SELECT id, usuario, password, nombre FROM usuarios WHERE usuario = ?";

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sqlLogin);
            stmt.setString(1, user.getUsuario());
            rs = stmt.executeQuery();

            if (rs.next()) {

                if (user.getPassword().equals(rs.getString(3))) {

                    String sqlUpdateSession = "UPDATE usuarios SET last_session = ? WHERE id = ?";
                    stmt = conn.prepareStatement(sqlUpdateSession);
                    stmt.setString(1, user.getLastSession());
                    stmt.setInt(2, rs.getInt(1));
                    stmt.execute();

                    user.setId(rs.getInt(1));
                    user.setNombre(rs.getString(4));
                    return true;
                } else {
                    return false;
                }

            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
    }
}
