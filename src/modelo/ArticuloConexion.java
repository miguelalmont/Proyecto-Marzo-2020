/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author migue
 */
public class ArticuloConexion extends conexion.Conexion{
        
    private final String SQL_INSERT
            = "CALL insert_articulo(?,?,?,?,?,?,?,?,?)";

    private final String SQL_UPDATE
            = "UPDATE articulos SET ISSN = ?, autor = ?, titulo = ?, revista = ?, anio = ?, mes = ?, pag_ini = ?, pag_fin = ? WHERE id_artic = ?";

    private final String SQL_DELETE
            = "DELETE FROM articulos WHERE ISSN = ? AND user_artic = ?";

    private final String SQL_SELECT
            = "SELECT ISSN, autor, titulo, revista, anio, mes, pag_ini, pag_fin, user_artic FROM articulos WHERE user_artic = ? ORDER BY titulo";
    
    private final String SQL_SELECT_ID
            = "SELECT id_artic FROM articulos WHERE user_artic = ? AND ISSN = ?";
    
    private final String SQL_SELECT_ISSN
            = "SELECT ISSN FROM articulos WHERE id_artic = ?";
    
    
    public boolean insert(Articulo articulo) {
        Connection conn = null;
        CallableStatement calst = null;
        try {
            conn = getConnection();
            calst = conn.prepareCall(SQL_INSERT);
            int index = 1;
            calst.setString(index++, articulo.getISSN());
            calst.setString(index++, articulo.getAutor());
            calst.setString(index++, articulo.getTitulo());
            calst.setString(index++, articulo.getRevista());
            if(articulo.getAnio() == 0)
                calst.setNull(index++, java.sql.Types.INTEGER);
            else
                calst.setInt(index++, articulo.getAnio());
            if(articulo.getMes() == 0)
                calst.setNull(index++, java.sql.Types.INTEGER);
            else
                calst.setInt(index++, articulo.getMes());
            if(articulo.getPagInicio() == 0)
                calst.setNull(index++, java.sql.Types.INTEGER);
            else
                calst.setInt(index++, articulo.getPagInicio());
            if(articulo.getPagFin() == 0)
                calst.setNull(index++, java.sql.Types.INTEGER);
            else
                calst.setInt(index++, articulo.getPagFin());
            
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
    
    public boolean update(Articulo articulo, String iSSNold) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            int index = 1;
            stmt.setString(index++, articulo.getISSN());
            stmt.setString(index++, articulo.getAutor());
            stmt.setString(index++, articulo.getTitulo());
            stmt.setString(index++, articulo.getRevista());
            if(articulo.getAnio() == 0)
                stmt.setNull(index++, java.sql.Types.INTEGER);
            else
                stmt.setInt(index++, articulo.getAnio());
            if(articulo.getMes() == 0)
                stmt.setNull(index++, java.sql.Types.INTEGER);
            else
                stmt.setInt(index++, articulo.getMes());
            if(articulo.getPagInicio() == 0)
                stmt.setNull(index++, java.sql.Types.INTEGER);
            else
                stmt.setInt(index++, articulo.getPagInicio());
            if(articulo.getPagFin() == 0)
                stmt.setNull(index++, java.sql.Types.INTEGER);
            else
                stmt.setInt(index++, articulo.getPagFin());
            
            stmt.setInt(index, getId(iSSNold));           
            
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
    
    public boolean delete(String iSSN) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);
            int index = 1;
            stmt.setString(index++, iSSN);
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
                int idUser = rs.getInt(9);
                
                articulo = new Articulo();
                articulo.setISSN(iSSN);
                articulo.setAutor(autor);
                articulo.setTitulo(titulo);
                articulo.setRevista(revista);
                articulo.setAnio(anio);
                articulo.setMes(mes);
                articulo.setPagInicio(pagIni);
                articulo.setPagFin(pagFin);
                articulo.setIdUser(idUser);
                
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
    
    public String getISSN(int id_artic){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = SQL_SELECT_ISSN;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_artic);
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
    
    public List<Articulo> buscar(String busqueda) {
        
        List<Articulo> articulos = select();
        List<Articulo> objetivos = new ArrayList<>();
        
        for (Articulo articulo : articulos) {
            if(articulo.getISSN().contains(busqueda))
                objetivos.add(articulo);
            if(articulo.getTitulo().contains(busqueda))
                objetivos.add(articulo);
            if(articulo.getAutor().contains(busqueda))
                objetivos.add(articulo);
            if(articulo.getRevista().contains(busqueda))
                objetivos.add(articulo);
            if(articulo.getAnio() > 0) 
                if(Integer.toString(articulo.getAnio()).contains(busqueda))
                    objetivos.add(articulo);
            if(articulo.getMes() > 0)
                if(Integer.toString(articulo.getMes()).contains(busqueda)) 
                    objetivos.add(articulo);
            if(articulo.getPagInicio() > 0)
                if(Integer.toString(articulo.getPagInicio()).contains(busqueda))
                    objetivos.add(articulo);
            if(articulo.getPagFin() > 0)
                if(Integer.toString(articulo.getPagFin()).contains(busqueda))
                    objetivos.add(articulo);
        }
        
        Set<Articulo> hashSet = new HashSet<>(objetivos);
        objetivos.clear();
        objetivos.addAll(hashSet);
        
        return objetivos;
    }
}
