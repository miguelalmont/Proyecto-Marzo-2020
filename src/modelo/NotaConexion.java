package modelo;

import controlador.LoginControlador;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * NotaConexion.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class NotaConexion extends conexion.Conexion {

    private final String SQL_INSERT
            = "CALL insert_nota(?,?,?,?,?)";

    private final String SQL_UPDATE
            = "UPDATE notas SET tema = ?, contenido = ?, id_libro = ?, id_artic = ? WHERE id_nota = ?";

    private final String SQL_DELETE
            = "DELETE FROM notas WHERE id_nota = ? AND user_nota = ?";

    private final String SQL_SELECT
            = "SELECT id_nota, tema, contenido, id_libro, id_artic, user_nota FROM notas WHERE user_nota = ? ORDER BY tema";

    private final String SQL_SELECT_POR_LIBRO
            = "SELECT tema, contenido FROM notas WHERE user_nota = ? AND id_libro = ?";

    private final String SQL_SELECT_POR_ARTICULO
            = "SELECT tema, contenido FROM notas WHERE user_nota = ? AND id_artic = ?";

    /**
     * Inserta un objeto en la base de datos
     *
     * @param nota
     * @return
     */
    public boolean insert(Nota nota) {
        Connection conn = null;
        CallableStatement calst = null;

        try {
            conn = getConnection();
            calst = conn.prepareCall(SQL_INSERT);
            int index = 1;
            calst.setString(index++, nota.getTema());
            if (nota.getContenido() == null) {
                calst.setNull(index++, java.sql.Types.VARCHAR);
            } else {
                calst.setString(index++, nota.getContenido());
            }
            calst.setInt(index++, LoginControlador.user.getId());
            if (nota.getIdLibro() < 1) {
                calst.setNull(index++, java.sql.Types.INTEGER);
            } else {
                calst.setInt(index++, nota.getIdLibro());
            }
            if (nota.getIdArticulo() < 1) {
                calst.setNull(index, java.sql.Types.INTEGER);
            } else {
                calst.setInt(index, nota.getIdArticulo());
            }

            calst.executeUpdate();
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
     * @param nota
     * @return
     */
    public boolean update(Nota nota) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();

            stmt = conn.prepareStatement(SQL_UPDATE);
            int index = 1;
            stmt.setString(index++, nota.getTema());
            if (nota.getContenido() == null) {
                stmt.setNull(index++, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(index++, nota.getContenido());
            }
            if (nota.getIdLibro() < 1) {
                stmt.setNull(index++, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(index++, nota.getIdLibro());
            }
            if (nota.getIdArticulo() < 1) {
                stmt.setNull(index++, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(index++, nota.getIdArticulo());
            }
            stmt.setInt(index, nota.getId());

            stmt.setInt(index, nota.getId());

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
     * @param id
     * @return
     */
    public boolean delete(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();

            stmt = conn.prepareStatement(SQL_DELETE);
            int index = 1;
            stmt.setInt(index++, id);
            stmt.setInt(index, LoginControlador.user.getId());
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
    public List<Nota> select() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Nota nota;
        List<Nota> notas = new ArrayList<>();
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            stmt.setInt(1, LoginControlador.user.getId());
            rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String tema = rs.getString(2);
                String contenido = rs.getString(3);
                int idLibro = rs.getInt(4);
                int idArticulo = rs.getInt(5);
                int idUser = rs.getInt(6);

                nota = new Nota();
                nota.setId(id);
                nota.setTema(tema);
                nota.setContenido(contenido);
                nota.setIdLibro(idLibro);
                nota.setIdArticulo(idArticulo);
                nota.setIdUser(idUser);

                notas.add(nota);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
        return notas;
    }

    /**
     * Comprueba si una id esta en la base de datos
     *
     * @param id
     * @return
     */
    public int existeIdNota(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "SELECT count(id_nota) FROM notas WHERE id_nota = ? AND user_nota = ?";

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            int index = 1;
            stmt.setInt(index++, id);
            stmt.setInt(index, LoginControlador.user.getId());
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
     * Cuenta el numero de entradas de un usuario en la base de datos
     *
     * @return
     */
    public int cuentaNotas() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int registros;

        String sql = "SELECT count(*) AS total FROM notas WHERE user_nota = ?";

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, LoginControlador.user.getId());
            rs = stmt.executeQuery();
            rs.next();
            registros = rs.getInt("total");
            return registros;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
    }

    /**
     * Busca una cadena dentro de todos los campos de cada registro y devuelve
     * una lista de las coincidendias
     *
     * @param busqueda
     * @return
     */
    public List<Nota> buscar(String busqueda) {

        List<Nota> notas = select();
        List<Nota> objetivos = new ArrayList<>();
        ArticuloConexion articuloConn = new ArticuloConexion();
        LibroConexion libroConn = new LibroConexion();

        for (Nota nota : notas) {
            if (nota.getTema().contains(busqueda)) {
                objetivos.add(nota);
            }
            if (nota.getIdLibro() > 0) {
                if (Long.toString(libroConn.getISBN(nota.getIdLibro())).contains(busqueda)) {
                    objetivos.add(nota);
                }
            }
            if (nota.getIdArticulo() > 0) {
                if (Integer.toString(articuloConn.getISSN(nota.getIdArticulo())).contains(busqueda)) {
                    objetivos.add(nota);
                }
            }
            if (nota.getContenido() != null) {
                if (nota.getContenido().contains(busqueda)) {
                    objetivos.add(nota);
                }
            }
        }

        Set<Nota> hashSet = new HashSet<>(objetivos);
        objetivos.clear();
        objetivos.addAll(hashSet);

        return objetivos;
    }

    /**
     * Crea una coleccion de las notas asociadas a un libro
     *
     * @param idLibro
     * @return
     */
    public List<Nota> selectPorLibro(int idLibro) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Nota nota;
        List<Nota> notas = new ArrayList<>();
        try {
            conn = getConnection();

            stmt = conn.prepareStatement(SQL_SELECT_POR_LIBRO);
            int index = 1;
            stmt.setInt(index++, LoginControlador.user.getId());
            stmt.setInt(index, idLibro);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String tema = rs.getString(1);
                String contenido = rs.getString(2);

                nota = new Nota();
                nota.setTema(tema);
                nota.setContenido(contenido);

                notas.add(nota);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }
        return notas;
    }

    /**
     * Crea una coleccion de las notas asociadas a un articulo
     *
     * @param idArticulo
     * @return
     */
    public List<Nota> selectPorArticulo(int idArticulo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Nota nota;
        List<Nota> notas = new ArrayList<>();
        try {
            conn = getConnection();

            stmt = conn.prepareStatement(SQL_SELECT_POR_ARTICULO);
            int index = 1;
            stmt.setInt(index++, LoginControlador.user.getId());
            stmt.setInt(index, idArticulo);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String tema = rs.getString(1);
                String contenido = rs.getString(2);

                nota = new Nota();
                nota.setTema(tema);
                nota.setContenido(contenido);

                notas.add(nota);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }
        return notas;
    }
}
