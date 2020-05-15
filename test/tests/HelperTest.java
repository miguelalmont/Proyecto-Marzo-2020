/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import controlador.LoginControlador;
import static junit.framework.Assert.assertEquals;
import modelo.ArticuloConexion;
import modelo.LibroConexion;
import modelo.NotaConexion;
import modelo.Usuario;
import org.junit.Test;

/**
 *
 * @author migue
 */
public class HelperTest {
    
    @Test
    public void testCuentaLibros(){
        Usuario usuario = new Usuario();
        usuario.setId(1);
        LoginControlador.user = usuario;
        LibroConexion libroConn = new LibroConexion();
        assertEquals(libroConn.select().size(), libroConn.cuentaLibros());
    }
    
    @Test
    public void testCuentaArticulos() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        LoginControlador.user = usuario;
        ArticuloConexion articuloConn = new ArticuloConexion();
        assertEquals( articuloConn.select().size(), articuloConn.cuentaArticulos());
    }
    
    @Test
    public void testCuentaNotas() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        LoginControlador.user = usuario;
        NotaConexion notaConn = new NotaConexion();
        assertEquals( notaConn.select().size(), notaConn.cuentaNotas());
    }
}
