/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

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
import modelo.UsuariosJDBC;
import vista.HomeVista;
import vista.LoginVista;

/**
 *
 * @author migue
 */
public class LoginControlador implements ActionListener {

    LoginVista vista;
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
        
        this.vista = vista;
    }

    /**
     * Inicia el skin y las diferentes variables que se utilizan
     */
    public void iniciar() {
        // Skin tipo WINDOWS
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(vista);
            vista.setVisible(true);
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
        }

        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__INICIAR_SESION.setActionCommand("__INICIAR_SESION");
        this.vista.__INICIAR_SESION.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__VOLVER.setActionCommand("__VOLVER");
        this.vista.__VOLVER.addActionListener(this);
    }

    //Control de eventos de los controles que tienen definido un "ActionCommand"
    public void actionPerformed(ActionEvent e) {

        switch (AccionMVC.valueOf(e.getActionCommand())) {
            case __INICIAR_SESION:
                UsuariosJDBC logUser = new UsuariosJDBC();
                user = new Usuario();

                LocalDateTime sessionDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
                String formatDateTime = sessionDateTime.format(formatter);

                String pass = new String(this.vista.txtPassword.getPassword());

                if (this.vista.txtUser.getText().length() == 0 || this.vista.txtPassword.getText().length() == 0) {
                    JOptionPane.showMessageDialog(null, "Los campos no pueden estar vacios.");

                } else {
                    String encryptedPass = Hash.sha1(pass);

                    user.setUsuario(this.vista.txtUser.getText());
                    user.setPassword(encryptedPass);
                    user.setLastSession(formatDateTime);

                    if (logUser.login(user)) {

                        InicioControlador.log.vista.dispose();

                        if (InicioControlador.reg != null) {
                            InicioControlador.reg.vista.dispose();
                            InicioControlador.reg = null;
                        }

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
                InicioControlador.log = null;
                ;
                break;
        }
    }

    private void cleanPassword() {
        this.vista.txtPassword.setText("");
    }
}
