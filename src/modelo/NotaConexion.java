/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controlador.LoginControlador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author migue
 */
public class NotaConexion extends conexion.Conexion{
    private final String SQL_INSERT
            = "INSERT INTO notas(tema, contenido, user_nota, id_libro, id_artic) VALUES(?,?,?,?,?)";

    private final String SQL_UPDATE
            = "UPDATE notas SET tema = ?, contenido = ?, id_libro = ?, id_artic = ? WHERE id_nota = ?";

    private final String SQL_DELETE
            = "DELETE FROM notas WHERE id_nota = ? AND user_nota = ?";

    private final String SQL_SELECT
            = "SELECT id_nota, tema, contenido, id_libro, id_artic, user_nota FROM notas WHERE user_nota = ? ORDER BY tema";
    
    /*private final String SQL_getId
            = "SELECT id_nota FROM notas WHERE user_nota = ? AND  = ?";*/
    
    public boolean insert(Nota nota) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            int index = 1;
            stmt.setString(index++, nota.getTema());
            if(nota.getContenido() == null)
                stmt.setNull(index++, java.sql.Types.VARCHAR);
            else
                stmt.setString(index++, nota.getContenido());
            stmt.setInt(index++, LoginControlador.user.getId());
            if (nota.getIdLibro() < 1)
                stmt.setNull(index++, java.sql.Types.INTEGER);
            else
                stmt.setInt(index++, nota.getIdLibro());
            if (nota.getIdArticulo() < 1)
                stmt.setNull(index, java.sql.Types.INTEGER);
            else
                stmt.setInt(index, nota.getIdArticulo());

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
    
    public boolean update(Nota nota) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            
            stmt = conn.prepareStatement(SQL_UPDATE);
            int index = 1;
            stmt.setString(index++, nota.getTema());
            if(nota.getContenido() == null)
                stmt.setNull(index++, java.sql.Types.VARCHAR);
            else
                stmt.setString(index++, nota.getContenido());
            if (nota.getIdLibro() < 1)
                stmt.setNull(index++, java.sql.Types.INTEGER);
            else
                stmt.setInt(index++, nota.getIdLibro());
            if (nota.getIdArticulo() < 1)
                stmt.setNull(index++, java.sql.Types.INTEGER);
            else
                stmt.setInt(index++, nota.getIdArticulo());
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
            
            if(rs.next()) {
                return rs.getInt(1);
            }
            else {
                return -1;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return -1;
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
    }
    
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
        }
        catch (SQLException e){
            e.printStackTrace();
            return 0;
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
    }

    public List<Nota> buscar(String busqueda) {
        
        List<Nota> notas = select();
        List<Nota> objetivos = new ArrayList<>();
        ArticuloConexion articuloConn = new ArticuloConexion();
        LibroConexion libroConn = new LibroConexion();
        
        
        for (Nota nota : notas) {
            if(nota.getTema().contains(busqueda))
                objetivos.add(nota);
            if(nota.getIdLibro() > 0)
                if(libroConn.getISBN(nota.getIdLibro()).contains(busqueda))
                    objetivos.add(nota);
            if(nota.getIdArticulo() > 0)
                if(articuloConn.getISSN(nota.getIdArticulo()).contains(busqueda))
                    objetivos.add(nota);
            if(nota.getContenido() != null)
                if(nota.getContenido().contains(busqueda))
                    objetivos.add(nota);
        }
        
        Set<Nota> hashSet = new HashSet<>(objetivos);
        objetivos.clear();
        objetivos.addAll(hashSet);
        
        return objetivos;
    }
    
}
