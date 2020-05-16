package controlador;

import static controlador.InicioControlador.icon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import modelo.Hash;
import modelo.Usuario;
import modelo.UsuarioConexion;
import vista.LoginVista;
import vista.RegistroVista;

/**
 * RegistroControlador.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class RegistroControlador implements ActionListener {

    //Declaración de objetos necesarios  
    RegistroVista vista;
    Usuario newUser = new Usuario();

    public enum AccionMVC {
        __CREAR_USUARIO,
        __VOLVER
    }

    /**
     * Constructor de la clase RegistroControlador
     *
     * @param vista Instancia de clase interfaz
     *
     */
    public RegistroControlador(RegistroVista vista) {
        vista.setVisible(true);
        this.vista = vista;
    }

    /**
     * Inicia el skin y las diferentes variables que se utilizan
     */
    public void iniciar() {

        // Skin tipo WINDOWS
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this.vista);
            this.vista.setIconImage(icon.getImage());

            this.vista.setVisible(true);

            //Deshabilita el panel de inicio
            InicioControlador.vista.setEnabled(false);
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
        }

        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__CREAR_USUARIO.setActionCommand("__CREAR_USUARIO");
        this.vista.__CREAR_USUARIO.addActionListener(this);

        this.vista.__VOLVER.setActionCommand("__VOLVER");
        this.vista.__VOLVER.addActionListener(this);
    }

    //Control de eventos de los controles que tienen definido un "ActionCommand"
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (AccionMVC.valueOf(e.getActionCommand())) {
            case __CREAR_USUARIO:

                UsuarioConexion userCon = new UsuarioConexion();
                Usuario nuevoUsuario = new Usuario();

                //Coloca en Strings los passwords introducidos
                String pass = new String(this.vista.txtPassword.getPassword());
                String passConfirm = new String(this.vista.txtConfirmPass.getPassword());

                //Si algun campo esta vacio muestra error
                if (this.vista.txtUser.getText().isEmpty() || this.vista.txtPassword.getText().isEmpty() || this.vista.txtConfirmPass.getText().isEmpty() || this.vista.txtName.getText().isEmpty() || this.vista.txtMail.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Los campos no pueden estar vacios.");

                } else {

                    //Si los passwords no son iguales muestra error
                    if (pass.equals(passConfirm)) {

                        //Si ya existe el nombre de usuario muestra error
                        if (userCon.existeUsuario(this.vista.txtUser.getText()) == 0) {

                            //Si el email no cumple el patron muestra error
                            if (userCon.validarEmail(this.vista.txtMail.getText())) {

                                //Encripta el password
                                String encrypPass = Hash.sha1(pass);

                                //Asigna los atributos al objeto nuevoUsuario
                                nuevoUsuario.setUsuario(this.vista.txtUser.getText());
                                nuevoUsuario.setPassword(encrypPass);
                                nuevoUsuario.setNombre(this.vista.txtName.getText());
                                nuevoUsuario.setMail(this.vista.txtMail.getText());

                                //Si el metodo insert retorna true, muestra mensaje de exito, si no muestra error
                                if (userCon.insert(nuevoUsuario)) {
                                    JOptionPane.showMessageDialog(null, "Usuario registrado con exito.");

                                    //Cierra la ventana registro e inicia la de login
                                    this.vista.dispose();
                                    new LoginControlador(new LoginVista()).iniciar();
                                    clean();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Ha habido un error.");
                                    cleanPassword();
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "El correo electronico no es valido.");
                                cleanPassword();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "El usuario ya existe.");
                            cleanPassword();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.");
                        cleanPassword();
                    }
                }
                break;

            case __VOLVER:

                //Cierra la ventana registro y habilita la de inicio
                this.vista.dispose();
                InicioControlador.vista.setEnabled(true);
                InicioControlador.vista.toFront();
        }
    }

    /**
     * Limpia todos los campos
     */
    private void clean() {
        this.vista.txtUser.setText("");
        this.vista.txtPassword.setText("");
        this.vista.txtConfirmPass.setText("");
        this.vista.txtName.setText("");
        this.vista.txtMail.setText("");

    }

    /**
     * Limpia los campos password
     */
    private void cleanPassword() {
        this.vista.txtPassword.setText("");
        this.vista.txtConfirmPass.setText("");
    }
}
