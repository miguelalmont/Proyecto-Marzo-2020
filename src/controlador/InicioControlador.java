/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import vista.Inicio;
import vista.Login;
import vista.Registro;

/**
 *
 * @author migue
 */
public class InicioControlador implements ActionListener{
    
    /** instancia a nuestra interfaz de usuario*/
    public static Inicio vista;
    public static LoginControlador log = null;
    public static RegistroControlador reg = null;

    /** instancia a nuestro modelo */
    

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
    public InicioControlador( Inicio vista )
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
                    
                    if(log == null) {
                        log = new LoginControlador(new Login());
                        log.iniciar();
                    }
                    break;
                case __NUEVO_USUARIO:
                    
                    if(reg == null) {
                        reg = new RegistroControlador(new Registro());
                        reg.iniciar();
                    }
                    break;     
            }
        
    }
}
