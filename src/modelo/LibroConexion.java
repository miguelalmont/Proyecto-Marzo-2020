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
 * LibroConexion.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class LibroConexion extends conexion.Conexion {

    private final String SQL_INSERT
            = "CALL insert_libro(?,?,?,?,?,?,?)";

    private final String SQL_UPDATE
            = "UPDATE libros SET ISBN = ?, autor = ?, titulo = ?, editorial = ?, anio = ?, n_paginas = ? WHERE id_libro = ?";

    private final String SQL_DELETE
            = "DELETE FROM libros WHERE id_libro = ?";

    private final String SQL_SELECT
            = "SELECT ISBN, autor, titulo, editorial, anio, n_paginas, user_libro FROM libros WHERE user_libro = ? ORDER BY titulo";

    private final String SQL_SELECT_ID
            = "SELECT id_libro FROM libros WHERE user_libro = ? AND ISBN = ?";

    private final String SQL_SELECT_ISBN
            = "SELECT ISBN FROM libros WHERE id_libro = ?";

    /**
     * Inserta un objeto en la base de datos
     *
     * @param libro
     * @return
     */
    public boolean insert(Libro libro) {
        Connection conn = null;
        CallableStatement calst = null;
        try {
            conn = getConnection();
            calst = conn.prepareCall(SQL_INSERT);
            int index = 1;
            calst.setLong(index++, libro.getISBN());
            calst.setString(index++, libro.getAutor());
            calst.setString(index++, libro.getTitulo());
            if (libro.getEditorial().isEmpty() || libro.getEditorial() == null) {
                calst.setNull(index++, java.sql.Types.VARCHAR);
            } else {
                calst.setString(index++, libro.getEditorial());
            }
            if (libro.getAnio() == 0) {
                calst.setNull(index++, java.sql.Types.INTEGER);
            } else {
                calst.setInt(index++, libro.getAnio());
            }
            if (libro.getnPaginas() == 0) {
                calst.setNull(index++, java.sql.Types.INTEGER);
            } else {
                calst.setInt(index++, libro.getnPaginas());
            }
            calst.setInt(index, LoginControlador.user.getId());

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
     * @param libro
     * @param iSBNold
     * @return
     */
    public boolean update(Libro libro, long iSBNold) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            int index = 1;
            stmt.setLong(index++, libro.getISBN());
            stmt.setString(index++, libro.getAutor());
            stmt.setString(index++, libro.getTitulo());
            if (libro.getEditorial().isEmpty() || libro.getEditorial() == null) {
                stmt.setNull(index++, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(index++, libro.getEditorial());
            }
            if (libro.getAnio() == 0) {
                stmt.setNull(index++, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(index++, libro.getAnio());
            }
            if (libro.getnPaginas() == 0) {
                stmt.setNull(index++, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(index++, libro.getnPaginas());
            }

            stmt.setInt(index, getId(iSBNold));

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
    public boolean delete(long iSBN) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);
            int index = 1;
            stmt.setInt(index, getId(iSBN));

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
    public List<Libro> select() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Libro libro = null;
        List<Libro> libros = new ArrayList<>();
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            stmt.setInt(1, LoginControlador.user.getId());
            rs = stmt.executeQuery();
            while (rs.next()) {
                long iSBN = rs.getLong(1);
                String autor = rs.getString(2);
                String titulo = rs.getString(3);
                String editorial = rs.getString(4);
                int anio = rs.getInt(5);
                int nPaginas = rs.getInt(6);
                int idUser = rs.getInt(7);

                libro = new Libro();
                libro.setISBN(iSBN);
                libro.setAutor(autor);
                libro.setTitulo(titulo);
                libro.setEditorial(editorial);
                libro.setAnio(anio);
                libro.setnPaginas(nPaginas);
                libro.setIdUser(idUser);

                libros.add(libro);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
        return libros;
    }

    /**
     * Comprueba si una ISBN esta en la base de datos
     *
     * @param iSBN
     * @return
     */
    public int existeISBN(long iSBN) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "SELECT count(ISBN) FROM libros WHERE ISBN = ? AND user_libro = ?";

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, iSBN);
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
    public int cuentaLibros() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int registros;

        String sql = "SELECT count(*) AS total FROM libros WHERE user_libro = ?";

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
     * Devuelve la id de un registro
     *
     * @param iSBN
     * @return
     */
    public int getId(long iSBN) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = SQL_SELECT_ID;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            int index = 1;
            stmt.setInt(index++, LoginControlador.user.getId());
            stmt.setLong(index, iSBN);
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
     * Devuelve el ISBN de un registro
     *
     * @param id_libro
     * @return
     */
    public long getISBN(int id_libro) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = SQL_SELECT_ISBN;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_libro);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getLong(1);
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
     * Busca una cadena dentro de todos los campos de cada registro y devuelve
     * una lista de las coincidendias
     *
     * @param busqueda
     * @return
     */
    public List<Libro> buscar(String busqueda) {

        List<Libro> libros = select();
        List<Libro> objetivos = new ArrayList<>();

        for (Libro libro : libros) {
            if (Long.toString(libro.getISBN()).contains(busqueda)) {
                objetivos.add(libro);
            }
            if (libro.getTitulo().contains(busqueda)) {
                objetivos.add(libro);
            }
            if (libro.getAutor().contains(busqueda)) {
                objetivos.add(libro);
            }
            if (libro.getEditorial() != null) {
                if (libro.getEditorial().contains(busqueda)) {
                    objetivos.add(libro);
                }
            }
            if (libro.getAnio() > 0) {
                if (Integer.toString(libro.getAnio()).contains(busqueda)) {
                    objetivos.add(libro);
                }
            }
            if (libro.getnPaginas() > 0) {
                if (Integer.toString(libro.getnPaginas()).contains(busqueda)) {
                    objetivos.add(libro);
                }
            }
        }

        Set<Libro> hashSet = new HashSet<>(objetivos);
        objetivos.clear();
        objetivos.addAll(hashSet);

        return objetivos;
    }

}
