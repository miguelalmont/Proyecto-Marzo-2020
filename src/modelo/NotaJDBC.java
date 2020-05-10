/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import static conexion.Conexion.close;
import static conexion.Conexion.getConnection;
import controlador.LoginControlador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author migue
 */
public class NotaJDBC extends conexion.Conexion{
    private final String SQL_INSERT
            = "INSERT INTO notas(tema, contenido, user_nota, id_libro, id_artic) VALUES(?,?,?,?,?)";

    private final String SQL_UPDATE
            = "UPDATE notas SET tema = ?, contenido = ?, id_libro = ?, id_artic = ? WHERE id_nota = ?";

    private final String SQL_DELETE
            = "DELETE FROM notas WHERE id_nota = ? AND user_nota = ?";

    private final String SQL_SELECT
            = "SELECT id_nota, tema, contenido, id_libro, id_artic, user_nota FROM notas WHERE user_nota = ? ORDER BY id_nota";
    
    /*private final String SQL_getId
            = "SELECT id_nota FROM notas WHERE user_nota = ? AND  = ?";*/
    
    
    public int insert(Nota nota) {
        Connection conn = null;
        PreparedStatement stmt = null;		
        int rows = 0;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            int index = 1;
            stmt.setString(index++, nota.getTema());
            stmt.setString(index++, nota.getContenido());
            stmt.setInt(index++, LoginControlador.user.getId());
            if (nota.getIdLibro() < 1) {
                stmt.setNull(index++, java.sql.Types.INTEGER);
            }
            else {
                stmt.setInt(index++, nota.getIdLibro());
            }
            if (nota.getIdArticulo() < 1) {
                stmt.setNull(index, java.sql.Types.INTEGER);
            }
            else {
                stmt.setInt(index, nota.getIdArticulo());
            }
            System.out.println("Ejecutando query:" + SQL_INSERT);
            rows = stmt.executeUpdate();
            System.out.println("Registros afectados:" + rows);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }
        return rows;
    }
    
    /*
    public void insertNotaLibro(Nota nota) {
        Connection conn = null;
        PreparedStatement stmt = null;		
        String sql = "INSERT INTO notas(tema, contenido, user_nota, id_libro) VALUES(?,?,?,?)";
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            int index = 1;
            stmt.setString(index++, nota.getTema());
            stmt.setString(index++, nota.getContenido());
            stmt.setInt(index++, LoginControlador.user.getId());
            stmt.setInt(index, nota.getIdLibro());
            System.out.println("Ejecutando query:" + sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }
    }
    
    public void insertNotaArticulo(Nota nota) {
        Connection conn = null;
        PreparedStatement stmt = null;		
        String sql = "INSERT INTO notas(tema, contenido, user_nota, id_artic) VALUES(?,?,?,?)";
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            int index = 1;
            stmt.setString(index++, nota.getTema());
            stmt.setString(index++, nota.getContenido());
            stmt.setInt(index++, LoginControlador.user.getId());
            stmt.setInt(index, nota.getIdArticulo());
            System.out.println("Ejecutando query:" + sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }
    }
    */
    
    public int update(Nota nota) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = getConnection();
            System.out.println("Ejecutando query:" + SQL_UPDATE);
            stmt = conn.prepareStatement(SQL_UPDATE);
            int index = 1;
            stmt.setString(index++, nota.getTema());
            stmt.setString(index++, nota.getContenido());
            stmt.setInt(index++, nota.getIdLibro());
            stmt.setInt(index++, nota.getIdArticulo());
            stmt.setInt(index, nota.getId());           
            
            rows = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }
        return rows;
    }
    
    public void updateLibro(Nota nota) {
        Connection conn = null;
        PreparedStatement stmt = null;		
        String sql = "UPDATE notas SET tema = ?, contenido = ?, id_libro = ? WHERE id_nota = ?";
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            int index = 1;
            stmt.setString(index++, nota.getTema());
            stmt.setString(index++, nota.getContenido());
            stmt.setInt(index++, nota.getIdLibro());
            stmt.setInt(index, nota.getId());
            System.out.println("Ejecutando query:" + sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }
    }
    
    public void updateArticulo(Nota nota) {
        Connection conn = null;
        PreparedStatement stmt = null;		
        String sql = "UPDATE notas SET tema = ?, contenido = ?, id_articulo = ? WHERE id_nota = ?";
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            int index = 1;
            stmt.setString(index++, nota.getTema());
            stmt.setString(index++, nota.getContenido());
            stmt.setInt(index++, nota.getIdArticulo());
            stmt.setInt(index, nota.getId());
            System.out.println("Ejecutando query:" + sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }
    }
    
    public int delete(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = getConnection();
            System.out.println("Ejecutando query:" + SQL_DELETE);
            stmt = conn.prepareStatement(SQL_DELETE);
            int index = 1;
            stmt.setInt(index++, id);
            stmt.setInt(index, LoginControlador.user.getId());
            rows = stmt.executeUpdate();
            System.out.println("Registros eliminados:" + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }
        return rows;
    }
    
    public List<Nota> select() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Nota nota = null;
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

    public int coincidencias(String busqueda) {
        int i = 0;
        List<String> lista = new ArrayList<>();
        List<Nota> notas = select();
        
        notas.stream().map((nota) -> {
            lista.add(Integer.toString(nota.getId()));
            return nota;
        }).map((nota) -> {
            lista.add(nota.getTema());
            return nota;
        }).map((nota) -> {
            lista.add(Integer.toString(nota.getIdLibro()));
            return nota;
        }).map((nota) -> {
            lista.add(Integer.toString(nota.getIdArticulo()));
            return nota;
        }).forEachOrdered((nota) -> {
            lista.add(nota.getContenido());
        });
        
        i = lista.stream().filter((resultado) -> (resultado.contains(busqueda))).map((_item) -> 1).reduce(i, Integer::sum);
        
        return i;
    }

    public List<Nota> buscar(String busqueda) {
        
        List<Nota> notas = select();
        List<Nota> objetivos = new ArrayList<>();
        
        notas.forEach((nota) -> {
            if(Integer.toString(nota.getId()).contains(busqueda)) {
                objetivos.add(nota);
            }
            else if(nota.getTema().contains(busqueda)) {
                objetivos.add(nota);
            }
            else if(Integer.toString(nota.getIdLibro()).contains(busqueda)) {
                objetivos.add(nota);
            }
            else if(Integer.toString(nota.getIdArticulo()).contains(busqueda)) {
                objetivos.add(nota);
            }
            else if(nota.getContenido().contains(busqueda)) {
                objetivos.add(nota);
            }
        });
        
        return objetivos;
    }
    
}
