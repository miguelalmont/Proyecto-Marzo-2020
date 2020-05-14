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
public class LibroConexion extends conexion.Conexion{
    
    private final String SQL_INSERT
            = "INSERT INTO libros(ISBN, autor, titulo, editorial, anio, n_paginas, user_libro) VALUES(?,?,?,?,?,?,?)";

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
    
    
    public boolean insert(Libro libro) {
        Connection conn = null;
        PreparedStatement stmt = null;	
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            int index = 1;
            stmt.setString(index++, libro.getISBN());
            stmt.setString(index++, libro.getAutor());
            stmt.setString(index++, libro.getTitulo());
            if(libro.getEditorial().isEmpty() || libro.getEditorial() == null)
                stmt.setNull(index++, java.sql.Types.VARCHAR);
            else
                stmt.setString(index++, libro.getEditorial());
            if(libro.getAnio() == 0)
                stmt.setNull(index++, java.sql.Types.INTEGER);
            else
                stmt.setInt(index++, libro.getAnio());
            if(libro.getnPaginas() == 0)
                stmt.setNull(index++, java.sql.Types.INTEGER);
            else
                stmt.setInt(index++, libro.getnPaginas());
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
    
    public boolean update(Libro libro, String iSBNold) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            int index = 1;
            stmt.setString(index++, libro.getISBN());
            stmt.setString(index++, libro.getAutor());
            stmt.setString(index++, libro.getTitulo());
            if(libro.getEditorial().isEmpty() || libro.getEditorial() == null)
                stmt.setNull(index++, java.sql.Types.VARCHAR);
            else
                stmt.setString(index++, libro.getEditorial());
            if(libro.getAnio() == 0)
                stmt.setNull(index++, java.sql.Types.INTEGER);
            else 
                stmt.setInt(index++, libro.getAnio());
            if(libro.getnPaginas() == 0)
                stmt.setNull(index++, java.sql.Types.INTEGER);
            else 
                stmt.setInt(index++, libro.getnPaginas());
            
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
    
    public boolean delete(String iSBN) {
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
                String iSBN = rs.getString(1);
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
    
    public int existeISBN(String iSBN) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT count(ISBN) FROM libros WHERE ISBN = ? AND user_libro = ?";
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            int index = 1;
            stmt.setString(index++, iSBN);
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
    
    public int getId(String iSBN){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = SQL_SELECT_ID;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            int index = 1;
            stmt.setInt(index++, LoginControlador.user.getId());
            stmt.setString(index, iSBN);
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
    
    public String getISBN(int id_libro){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = SQL_SELECT_ISBN;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_libro);
            rs = stmt.executeQuery();
            
            if(rs.next()) {
                return rs.getString(1);
            }
            else {
                return null;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
    }
    
    public List<Libro> buscar(String busqueda) {
        
        List<Libro> libros = select();
        List<Libro> objetivos = new ArrayList<>();
        
        for (Libro libro : libros) {
            if(libro.getISBN().contains(busqueda))
                objetivos.add(libro);
            if(libro.getTitulo().contains(busqueda))
                objetivos.add(libro);
            if(libro.getAutor().contains(busqueda))
                objetivos.add(libro);
            if(libro.getEditorial() != null) 
                if(libro.getEditorial().contains(busqueda))
                    objetivos.add(libro);
            if(libro.getAnio() > 0)
                if(Integer.toString(libro.getAnio()).contains(busqueda))
                objetivos.add(libro);
            if(libro.getnPaginas() > 0) 
                if(Integer.toString(libro.getnPaginas()).contains(busqueda))
                    objetivos.add(libro);
        }
        
        Set<Libro> hashSet = new HashSet<>(objetivos);
        objetivos.clear();
        objetivos.addAll(hashSet);
        
        return objetivos;
    }
    
}
