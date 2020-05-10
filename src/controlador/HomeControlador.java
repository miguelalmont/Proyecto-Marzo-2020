/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import vista.HomeVista;
import vista.LibroVista;
import vista.NotaVista;
import vista.ArticuloVista;

/**
 *
 * @author migue
 */
public class HomeControlador implements ActionListener{
    /** instancia a nuestra interfaz de usuario*/
    public static HomeVista vista;
    public static LibroControlador mLib = null;
    public static ArticuloControlador mArt = null;
    public static NotaControlador mNot = null;

    /** instancia a nuestro modelo */
    

    /** Se declaran en un ENUM las acciones que se realizan desde la
     * interfaz de usuario VISTA y posterior ejecución desde el controlador
     */
    public enum AccionMVC
    {
        __LIBRO,
        __ARTICULO,
        __NOTA,
        __CERRAR_SESION
    }

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
            HomeControlador.vista.setVisible(true);
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {}

        //declara una acción y añade un escucha al evento producido por el componente
        HomeControlador.vista.__LIBRO.setActionCommand( "__LIBRO" );
        HomeControlador.vista.__LIBRO.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        HomeControlador.vista.__ARTICULO.setActionCommand( "__ARTICULO" );
        HomeControlador.vista.__ARTICULO.addActionListener(this);
        
        HomeControlador.vista.__NOTA.setActionCommand( "__NOTA" );
        HomeControlador.vista.__NOTA.addActionListener(this);
        
        HomeControlador.vista.__CERRAR_SESION.setActionCommand( "__CERRAR_SESION" );
        HomeControlador.vista.__CERRAR_SESION.addActionListener(this);
    }
 
    //Control de eventos de los controles que tienen definido un "ActionCommand"
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (AccionMVC.valueOf(e.getActionCommand()))
            {
                case __LIBRO:
                    if (mLib == null) {
                        mLib = new LibroControlador(new LibroVista());
                        mLib.iniciar();
                        LibroVista.isbnCheckBox.setSelected(true);
                    }
                    break;
                case __ARTICULO:
                    
                    if(mArt == null) {
                        mArt = new ArticuloControlador(new ArticuloVista());
                        mArt.iniciar();
                    }
                    break;
                case __NOTA:
                    
                    if(mNot == null) {
                        mNot = new NotaControlador(new NotaVista());
                        mNot.iniciar();
                        
                    }
                    break;
                case __CERRAR_SESION:
                    int option = JOptionPane.showConfirmDialog(null,
                            "¿Estás seguro de que quieres abandonar la sesion?",
                            "Confirmacion de cierre",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (option == JOptionPane.YES_OPTION) {

                        if (mLib != null) {
                            mLib.vista.dispose();
                            mLib = null;
                        }
                        
                        if (mArt != null) {
                            mArt.vista.dispose();
                            mArt = null;
                        }
                        
                        if (mNot != null) {
                            mNot.vista.dispose();
                            mNot = null;
                        }

                        LoginControlador.user = null;
                        InicioControlador.log = null;
                        HomeControlador.vista.dispose();
                        InicioControlador.vista.setVisible(true);
                    }
                    break; 
            }
    }
}

