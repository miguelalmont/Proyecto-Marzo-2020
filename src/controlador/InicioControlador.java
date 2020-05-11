/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import vista.InicioVista;
import vista.LoginVista;
import vista.RegistroVista;

/**
 *
 * @author migue
 */
public class InicioControlador implements ActionListener{
    
    /** instancia a nuestra interfaz de usuario*/
    public static InicioVista vista;
    public static LoginControlador log = null;

    /**
     *
     */
    public URL iconURL;
    public static ImageIcon icon;
    
    

    /** Se declaran en un ENUM las acciones que se realizan desde la
     * interfaz de usuario VISTA y posterior ejecución desde el controlador
     */
    public enum AccionMVC
    {
        __INICIAR_SESION,
        __NUEVO_USUARIO,
    }

    /** Constrcutor de clase
     * @param vista Instancia de clase interfaz
     */
    public InicioControlador( InicioVista vista )
    {
        InicioControlador.vista = vista;
    }

    /** Inicia el skin y las diferentes variables que se utilizan */
    public void iniciar()
    {
        // Skin tipo WINDOWS
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(vista);
            iconURL = getClass().getResource("/recursos/icon.png");
            icon = new ImageIcon(iconURL);
            vista.setIconImage(icon.getImage());
            vista.setVisible(true);
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {}

        //declara una acción y añade un escucha al evento producido por el componente
        InicioControlador.vista.__INICIAR_SESION.setActionCommand( "__INICIAR_SESION" );
        InicioControlador.vista.__INICIAR_SESION.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        InicioControlador.vista.__NUEVO_USUARIO.setActionCommand( "__NUEVO_USUARIO" );
        InicioControlador.vista.__NUEVO_USUARIO.addActionListener(this);
    }
 
    //Control de eventos de los controles que tienen definido un "ActionCommand"
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (AccionMVC.valueOf(e.getActionCommand()))
            {
                case __INICIAR_SESION:
                    
                    LoginControlador log = new LoginControlador(new LoginVista());
                    log.iniciar();
                    
                    break;
                case __NUEVO_USUARIO:
                    
                    RegistroControlador reg = new RegistroControlador(new RegistroVista());
                    reg.iniciar();
                    
                    break;     
            }
        
    }
}
