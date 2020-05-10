/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import static controlador.InicioControlador.icon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import vista.HomeVista;

/**
 *
 * @author migue
 */
public class HomeControlador {
    /** instancia a nuestra interfaz de usuario*/
    public static HomeVista vista;
    public LibroControlador libroControl;
    public ArticuloControlador articuloControl;
    public NotaControlador notaControl;

    /** Constrcutor de clase
     * @param vista Instancia de clase interfaz
     */
    public HomeControlador( HomeVista vista )
    {
        HomeControlador.vista = vista;
        

    }

    /** Inicia el skin y las diferentes variables que se utilizan */
    public void iniciar()
    {
        // Skin tipo WINDOWS
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(HomeControlador.vista);
            HomeControlador.vista.setIconImage(icon.getImage());
            HomeControlador.vista.setVisible(true);
            new LibroControlador( HomeControlador.vista.libroPanel).iniciar();
            new ArticuloControlador( HomeControlador.vista.articuloPanel).iniciar();
            new NotaControlador( HomeControlador.vista.notaPanel).iniciar();
            
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {}
    }
}