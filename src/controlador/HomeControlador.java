package controlador;

import static controlador.InicioControlador.icon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import vista.HomeVista;

/**
 * HomeControlador.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class HomeControlador {

    //Declaración de objetos necesarios
    public static HomeVista vista;
    public LibroControlador libroControl;
    public ArticuloControlador articuloControl;
    public NotaControlador notaControl;

    /**
     * Constructor de la clase HomeControlador
     *
     * @param vista Instancia de clase interfaz
     *
     */
    public HomeControlador(HomeVista vista) {
        HomeControlador.vista = vista;

    }

    /**
     * Inicia el skin y las diferentes variables que se utilizan
     */
    public void iniciar() {
        // Skin tipo WINDOWS
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(HomeControlador.vista);

            HomeControlador.vista.setIconImage(icon.getImage());
            HomeControlador.vista.setVisible(true);

            //Inicia los controladores de cada panel
            new LibroControlador(HomeControlador.vista.libroPanel).iniciar();
            new ArticuloControlador(HomeControlador.vista.articuloPanel).iniciar();
            new NotaControlador(HomeControlador.vista.notaPanel).iniciar();

        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
        }
    }
}
