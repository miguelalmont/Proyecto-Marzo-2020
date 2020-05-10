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
public class ArticuloJDBC extends conexion.Conexion{
        
    private final String SQL_INSERT
            = "INSERT INTO articulos(ISSN, autor, titulo, revista, anio, mes, pag_ini, pag_fin, user_artic) VALUES(?,?,?,?,?,?,?,?,?)";

    private final String SQL_UPDATE
            = "UPDATE articulos SET ISSN = ?, autor = ?, titulo = ?, revista = ?, anio = ?, mes = ?, pag_ini = ?, pag_fin = ? WHERE id_artic = ?";

    private final String SQL_DELETE
            = "DELETE FROM articulos WHERE ISSN = ? AND user_artic = ?";

    private final String SQL_SELECT
            = "SELECT ISSN, autor, titulo, revista, anio, mes, pag_ini, pag_fin FROM articulos WHERE user_artic = ? ORDER BY ISSN";
    
    private final String SQL_SELECT_ID
            = "SELECT id_artic FROM articulos WHERE user_artic = ? AND ISSN = ?";
    
    private final String SQL_SELECT_ISSN
            = "SELECT ISSN FROM articulos WHERE user_artic = ? AND id_artic = ?";
    
    
    public int insert(Articulo articulo) {
        Connection conn = null;
        PreparedStatement stmt = null;		
        int rows = 0;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            int index = 1;
            stmt.setString(index++, articulo.getISSN());
            stmt.setString(index++, articulo.getAutor());
            stmt.setString(index++, articulo.getTitulo());
            stmt.setString(index++, articulo.getRevista());
            stmt.setInt(index++, articulo.getAnio());
            stmt.setInt(index++, articulo.getMes());
            stmt.setInt(index++, articulo.getPagInicio());
            stmt.setInt(index++, articulo.getPagFin());
            stmt.setInt(index, LoginControlador.user.getId());
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
    
    public int update(Articulo articulo, String iSSNold) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = getConnection();
            System.out.println("Ejecutando query:" + SQL_UPDATE);
            stmt = conn.prepareStatement(SQL_UPDATE);
            int index = 1;
            stmt.setString(index++, articulo.getISSN());
            stmt.setString(index++, articulo.getAutor());
            stmt.setString(index++, articulo.getTitulo());
            stmt.setString(index++, articulo.getRevista());
            stmt.setInt(index++, articulo.getAnio());
            stmt.setInt(index++, articulo.getMes());
            stmt.setInt(index++, articulo.getPagInicio());
            stmt.setInt(index++, articulo.getPagFin());
            
            stmt.setInt(index, getId(iSSNold));           
            
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
    
    public int delete(String iSSN) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = getConnection();
            System.out.println("Ejecutando query:" + SQL_DELETE);
            stmt = conn.prepareStatement(SQL_DELETE);
            int index = 1;
            stmt.setString(index++, iSSN);
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
    
    public List<Articulo> select() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Articulo articulo = null;
        List<Articulo> articulos = new ArrayList<>();
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            stmt.setInt(1, LoginControlador.user.getId());
            rs = stmt.executeQuery();
            while (rs.next()) {
                String iSSN = rs.getString(1);
                String autor = rs.getString(2);
                String titulo = rs.getString(3);
                String revista = rs.getString(4);
                int anio = rs.getInt(5);
                int mes = rs.getInt(6);
                int pagIni = rs.getInt(7);
                int pagFin = rs.getInt(8);
                
                articulo = new Articulo();
                articulo.setISSN(iSSN);
                articulo.setAutor(autor);
                articulo.setTitulo(titulo);
                articulo.setRevista(revista);
                articulo.setAnio(anio);
                articulo.setMes(mes);
                articulo.setPagInicio(pagIni);
                articulo.setPagFin(pagFin);
                
                articulos.add(articulo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
        return articulos;
    }
    
    public int existeISSN(String iSSN) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT count(ISSN) FROM articulos WHERE ISSN = ? AND user_artic = ?";
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            int index = 1;
            stmt.setString(index++, iSSN);
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
    
    public int cuentaArticulos() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int registros;
        
        String sql = "SELECT count(*) AS total FROM articulos WHERE user_artic = ?";
        
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
    
    public int getId(String iSSN){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = SQL_SELECT_ID;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            int index = 1;
            stmt.setInt(index++, LoginControlador.user.getId());
            stmt.setString(index, iSSN);
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
    
    public String getISSN(int user, int id_artic){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = SQL_SELECT_ISSN;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            int index = 1;
            stmt.setInt(index++, user);
            stmt.setInt(index, id_artic);
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
    
    public int coincidencias(String busqueda) {
        
        int i = 0;
        List<String> lista = new ArrayList<>();
        List<Articulo> articulos = select();
        
        articulos.stream().map((articulo) -> {
            lista.add(articulo.getISSN());
            return articulo;
        }).map((articulo) -> {
            lista.add(articulo.getTitulo());
            return articulo;
        }).map((articulo) -> {
            lista.add(articulo.getAutor());
            return articulo;
        }).map((articulo) -> {
            lista.add(articulo.getRevista());
            return articulo;
        }).map((articulo) -> {
            lista.add(Integer.toString(articulo.getAnio()));
            return articulo;
        }).map((articulo) -> {
            lista.add(Integer.toString(articulo.getMes()));
            return articulo;
        }).map((articulo) -> {
            lista.add(Integer.toString(articulo.getPagInicio()));
            return articulo;
        }).forEachOrdered((articulo) -> {
            lista.add(Integer.toString(articulo.getPagFin()));
        });
        
        i = lista.stream().filter((resultado) -> (resultado.contains(busqueda))).map((_item) -> 1).reduce(i, Integer::sum);
        
        return i;
    }
    
    public List<Articulo> buscar(String busqueda) {
        
        List<Articulo> articulos = select();
        List<Articulo> objetivos = new ArrayList<>();
        
        articulos.forEach((articulo) -> {
            if(articulo.getISSN().contains(busqueda)) {
                objetivos.add(articulo);
            }
            else if(articulo.getTitulo().contains(busqueda)) {
                objetivos.add(articulo);
            }
            else if(articulo.getAutor().contains(busqueda)) {
                objetivos.add(articulo);
            }
            else if(articulo.getRevista().contains(busqueda)) {
                objetivos.add(articulo);
            }
            else if(Integer.toString(articulo.getAnio()).contains(busqueda)) {
                objetivos.add(articulo);
            }
            else if(Integer.toString(articulo.getMes()).contains(busqueda)) {
                objetivos.add(articulo);
            }
            else if(Integer.toString(articulo.getPagInicio()).contains(busqueda)) {
                objetivos.add(articulo);
            }
            else if(Integer.toString(articulo.getPagFin()).contains(busqueda)) {
                objetivos.add(articulo);
            }
        });
        
        return objetivos;
    }
}
