/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import static controlador.InicioControlador.icon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import modelo.Hash;
import modelo.Usuario;
import modelo.UsuariosConexion;
import vista.LoginVista;
import vista.HomeVista;

/**
 *
 * @author migue
 */
public class LoginControlador implements ActionListener {

    public static LoginVista vista;
    public static Usuario user;
    private final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    /**
     * Se declaran en un ENUM las acciones que se realizan desde la interfaz de
     * usuario VISTA y posterior ejecución desde el controlador
     */
    public enum AccionMVC {
        __INICIAR_SESION,
        __VOLVER
    }

    /**
     * Constrcutor de clase
     *
     * @param vista Instancia de clase interfaz
     */
    public LoginControlador(LoginVista vista) {
        
        LoginControlador.vista = vista;
    }

    /**
     * Inicia el skin y las diferentes variables que se utilizan
     */
    public void iniciar() {
        // Skin tipo WINDOWS
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(LoginControlador.vista);
            LoginControlador.vista.setIconImage(icon.getImage());
            LoginControlador.vista.setVisible(true);
            InicioControlador.vista.setEnabled(false);
            
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
        }

        //declara una acción y añade un escucha al evento producido por el componente
        LoginControlador.vista.__INICIAR_SESION.setActionCommand("__INICIAR_SESION");
        LoginControlador.vista.__INICIAR_SESION.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        LoginControlador.vista.__VOLVER.setActionCommand("__VOLVER");
        LoginControlador.vista.__VOLVER.addActionListener(this);
    }

    //Control de eventos de los controles que tienen definido un "ActionCommand"
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (AccionMVC.valueOf(e.getActionCommand())) {
            case __INICIAR_SESION:
                UsuariosConexion logUser = new UsuariosConexion();
                user = new Usuario();

                LocalDateTime sessionDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
                String formatDateTime = sessionDateTime.format(formatter);

                String pass = new String(LoginControlador.vista.txtPassword.getPassword());

                if (LoginControlador.vista.txtUser.getText().isEmpty() || LoginControlador.vista.txtPassword.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Los campos no pueden estar vacios.");

                } else {
                    String encryptedPass = Hash.sha1(pass);

                    user.setUsuario(LoginControlador.vista.txtUser.getText());
                    user.setPassword(encryptedPass);
                    user.setLastSession(formatDateTime);

                    if (logUser.login(user)) {

                        LoginControlador.vista.setVisible(false);
                        InicioControlador.vista.setVisible(false);

                        new HomeControlador( new HomeVista() ).iniciar();
                        
                    } else {
                        JOptionPane.showMessageDialog(null, "Datos incorrectos.");
                        cleanPassword();
                    }
                }
                break;
            case __VOLVER:
                user = null;
                vista.dispose();
                InicioControlador.vista.setEnabled(true);
                InicioControlador.vista.toFront();
                
                break;
        }
    }

    private void cleanPassword() {
        LoginControlador.vista.txtPassword.setText("");
    }
}
