package controlador;

import vista.InicioVista;

/**
 * GestionRefBiblio.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class GestionRefBiblio {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new InicioControlador(new InicioVista()).iniciar();
    }
}
