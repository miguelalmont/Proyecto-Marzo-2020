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
import modelo.UsuarioConexion;
import vista.LoginVista;
import vista.HomeVista;

/**
 * LoginControlador.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class LoginControlador implements ActionListener {

    //Declaración de objetos necesarios    
    public static LoginVista vista;
    public static Usuario user;
    private final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    public enum AccionMVC {
        __INICIAR_SESION,
        __VOLVER
    }

    /**
     * Constructor de la clase LoginControlador
     *
     * @param vista Instancia de clase interfaz
     *
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

            //Deshabilita el panel de inicio
            InicioControlador.vista.setEnabled(false);

        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
        }

        //declara una acción y añade un escucha al evento producido por el componente
        LoginControlador.vista.__INICIAR_SESION.setActionCommand("__INICIAR_SESION");
        LoginControlador.vista.__INICIAR_SESION.addActionListener(this);

        LoginControlador.vista.__VOLVER.setActionCommand("__VOLVER");
        LoginControlador.vista.__VOLVER.addActionListener(this);
    }

    //Control de eventos de los controles que tienen definido un "ActionCommand"
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (AccionMVC.valueOf(e.getActionCommand())) {
            case __INICIAR_SESION:

                UsuarioConexion UserConn = new UsuarioConexion();

                //Instancia un objeto usuario
                user = new Usuario();

                //Coloca en un String la fecha y la hora actual con formato local
                LocalDateTime sessionDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
                String formatDateTime = sessionDateTime.format(formatter);

                //Coloca en un String el password introducido
                String pass = new String(LoginControlador.vista.txtPassword.getPassword());

                //Si algun campo esta vacio muestra error
                if (LoginControlador.vista.txtUser.getText().isEmpty() || LoginControlador.vista.txtPassword.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Los campos no pueden estar vacios.");

                } else {

                    //Encripta el password
                    String encryptedPass = Hash.sha1(pass);

                    //Asigna los atributos al objeto user
                    user.setUsuario(LoginControlador.vista.txtUser.getText());
                    user.setPassword(encryptedPass);
                    user.setLastSession(formatDateTime);

                    //Si los atributos son correctos, se inicia sesion y se muestra la pantalla Home, si no muestra error
                    if (UserConn.login(user)) {

                        LoginControlador.vista.setVisible(false);
                        InicioControlador.vista.setVisible(false);

                        new HomeControlador(new HomeVista()).iniciar();

                    } else {
                        JOptionPane.showMessageDialog(null, "Datos incorrectos.");
                        cleanPassword();
                    }
                }
                break;

            case __VOLVER:

                //Vuelve a poner el objeto user a null, cierra la ventana login y habilita la de inicio
                user = null;
                vista.dispose();
                InicioControlador.vista.setEnabled(true);
                InicioControlador.vista.toFront();

                break;
        }
    }

    /**
     * Limpia el campo password
     */
    private void cleanPassword() {
        LoginControlador.vista.txtPassword.setText("");
    }
}
