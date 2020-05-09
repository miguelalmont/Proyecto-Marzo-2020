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
import modelo.Hash;
import modelo.Usuario;
import modelo.UsuariosJDBC;
import vista.RegistroVista;

/**
 *
 * @author migue
 */
public class RegistroControlador implements ActionListener{
    
    /** instancia a nuestra interfaz de usuario*/
    RegistroVista vista ;
    /** instancia a nuestro modelo */
    Usuario newUser = new Usuario();


    /** Se declaran en un ENUM las acciones que se realizan desde la
     * interfaz de usuario VISTA y posterior ejecución desde el controlador
     */
    public enum AccionMVC
    {
        __CREAR_USUARIO,
        __VOLVER
    }

    /** Constrcutor de clase
     * @param vista Instancia de clase interfaz
     */
    public RegistroControlador( RegistroVista vista )
    {
        vista.setVisible(true);
        this.vista = vista;
    }

    /** Inicia el skin y las diferentes variables que se utilizan */
    public void iniciar()
    {
        // Skin tipo WINDOWS
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(vista);
            vista.setVisible(true);
        } catch (UnsupportedLookAndFeelException ex) {}
          catch (ClassNotFoundException ex) {}
          catch (InstantiationException ex) {}
          catch (IllegalAccessException ex) {}

        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__CREAR_USUARIO.setActionCommand( "__CREAR_USUARIO" );
        this.vista.__CREAR_USUARIO.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__VOLVER.setActionCommand( "__VOLVER" );
        this.vista.__VOLVER.addActionListener(this);
    }
    
    //Control de eventos de los controles que tienen definido un "ActionCommand"
    @Override
    public void actionPerformed(ActionEvent e) {

    switch ( AccionMVC.valueOf( e.getActionCommand() ) )
        {
            case __CREAR_USUARIO:
                
        UsuariosJDBC userCon = new UsuariosJDBC();
        Usuario newUser = new Usuario();

        String pass = new String(this.vista.txtPassword.getPassword());
        String passConfirm = new String(this.vista.txtConfirmPass.getPassword());

        if (this.vista.txtUser.getText().length() == 0 || this.vista.txtPassword.getText().length() == 0 || this.vista.txtConfirmPass.getText().length() == 0 || this.vista.txtName.getText().length() == 0 || this.vista.txtMail.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "Los campos no pueden estar vacios.");

        } else {
            if (pass.equals(passConfirm)) {

                if (userCon.existeUsuario(this.vista.txtUser.getText()) == 0) {
                    
                    if (userCon.validarEmail(this.vista.txtMail.getText())) {
                        String encrypPass = Hash.sha1(pass);

                        newUser.setUsuario(this.vista.txtUser.getText());
                        newUser.setPassword(encrypPass);
                        newUser.setNombre(this.vista.txtName.getText());
                        newUser.setMail(this.vista.txtMail.getText());
                        

                        if (userCon.insert(newUser)) {
                            JOptionPane.showMessageDialog(null, "Usuario registrado con exito.");
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
                this.vista.dispose();
                InicioControlador.reg = null;   
        }
    }
        private void clean() {

        this.vista.txtUser.setText("");
        this.vista.txtPassword.setText("");
        this.vista.txtConfirmPass.setText("");
        this.vista.txtName.setText("");
        this.vista.txtMail.setText("");

    }
    
    private void cleanPassword() {
        this.vista.txtPassword.setText("");
        this.vista.txtConfirmPass.setText("");
    }
}
