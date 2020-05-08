package controlador;

import vista.Inicio;

/**
 *
 * @author migue
 */
public class GestionRefBiblio {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new InicioControlador( new Inicio() ).iniciar();
    }
}