/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import vista.FileChooserVista;

/**
 *
 * @author migue
 */
public class FileChooserControlador {
    
    public FileChooserVista vista;
    
    public FileChooserControlador( FileChooserVista vista )
    {
        this.vista = vista;
    }
    
    public void iniciar()
    {
        // Skins tipo WINDOWS
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(vista);
            HomeControlador.vista.setEnabled(false);
            vista.setVisible(true);
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {}
    }
    
    public String getRuta() {
        return this.vista.FileChooser.getSelectedFile().getAbsolutePath();
    }
}
