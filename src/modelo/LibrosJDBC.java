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
import java.util.List;

/**
 *
 * @author migue
 */
public class LibrosJDBC extends conexion.Conexion{
    
    private final String SQL_INSERT
            = "INSERT INTO libros(ISBN, autor, titulo, editorial, anio, n_paginas, user_libro) VALUES(?,?,?,?,?,?,?)";

    private final String SQL_UPDATE
            = "UPDATE libros SET ISBN = ?, autor = ?, titulo = ?, editorial = ?, anio = ?, n_paginas = ? WHERE id_libro = ?";

    private final String SQL_DELETE
            = "DELETE FROM libros WHERE ISBN = ? AND user_libro = ?";

    private final String SQL_SELECT
            = "SELECT ISBN, autor, titulo, editorial, anio, n_paginas FROM libros WHERE user_libro = ? ORDER BY ISBN";
    
    private final String SQL_GET_ID
            = "SELECT id_libro FROM libros WHERE user_libro = ? AND ISBN = ?";
    
    
    public int insert(Libro libro) {
        Connection conn = null;
        PreparedStatement stmt = null;		
        int rows = 0;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            int index = 1;//contador de columnas
            stmt.setString(index++, libro.getISBN());//param 1 => ?
            stmt.setString(index++, libro.getAutor());//param 2 => ?
            stmt.setString(index++, libro.getTitulo());
            stmt.setString(index++, libro.getEditorial());
            stmt.setInt(index++, libro.getAnio());
            stmt.setInt(index++, libro.getnPaginas());
            stmt.setInt(index, LoginControlador.user.getId());
            System.out.println("Ejecutando query:" + SQL_INSERT);
            rows = stmt.executeUpdate();//no. registros afectados
            System.out.println("Registros afectados:" + rows);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }
        return rows;
    }
    
    public int update(Libro libro, String iSBNold) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = getConnection();
            System.out.println("Ejecutando query:" + SQL_UPDATE);
            stmt = conn.prepareStatement(SQL_UPDATE);
            int index = 1;
            stmt.setString(index++, libro.getISBN());
            stmt.setString(index++, libro.getAutor());
            stmt.setString(index++, libro.getTitulo());
            stmt.setString(index++, libro.getEditorial());
            stmt.setInt(index++, libro.getAnio());
            stmt.setInt(index++, libro.getnPaginas());
            
            stmt.setInt(index, getId(iSBNold));           
            
            rows = stmt.executeUpdate();
            System.out.println("Registros actualizados:" + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }
        return rows;
    }
    
    public int delete(String iSBN) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = getConnection();
            System.out.println("Ejecutando query:" + SQL_DELETE);
            stmt = conn.prepareStatement(SQL_DELETE);
            int index = 1;
            stmt.setString(index++, iSBN);
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
                
                libro = new Libro();
                libro.setISBN(iSBN);
                libro.setAutor(autor);
                libro.setTitulo(titulo);
                libro.setEditorial(editorial);
                libro.setAnio(anio);
                libro.setnPaginas(nPaginas);
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
        
        String sql = SQL_GET_ID;
        
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
    
    public int coincidencias(String busqueda) {
        
        int i = 0;
        List<String> lista = new ArrayList<>();
        List<Libro> libros = select();
        
        libros.stream().map((libro) -> {
            lista.add(libro.getISBN());
            return libro;
        }).map((libro) -> {
            lista.add(libro.getTitulo());
            return libro;
        }).map((libro) -> {
            lista.add(libro.getAutor());
            return libro;
        }).map((libro) -> {
            lista.add(libro.getEditorial());
            return libro;
        }).map((libro) -> {
            lista.add(Integer.toString(libro.getAnio()));
            return libro;
        }).forEachOrdered((libro) -> {
            lista.add(Integer.toString(libro.getnPaginas()));
        });
        
        i = lista.stream().filter((resultado) -> (resultado.contains(busqueda))).map((_item) -> 1).reduce(i, Integer::sum);
        
        return i;
    }
    
    public List<Libro> buscar(String busqueda) {
        
        List<Libro> libros = select();
        List<Libro> objetivos = new ArrayList<>();
        
        libros.forEach((libro) -> {
            if(libro.getISBN().contains(busqueda)) {
                objetivos.add(libro);
            }
            else if(libro.getTitulo().contains(busqueda)) {
                objetivos.add(libro);
            }
            else if(libro.getAutor().contains(busqueda)) {
                objetivos.add(libro);
            }
            else if(libro.getEditorial().contains(busqueda)) {
                objetivos.add(libro);
            }
            else if(Integer.toString(libro.getAnio()).contains(busqueda)) {
                objetivos.add(libro);
            }
            else if(Integer.toString(libro.getnPaginas()).contains(busqueda)) {
                objetivos.add(libro);
            }
        });
        
        return objetivos;
    }
    
}
