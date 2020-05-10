/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import modelo.IOdatos;
import modelo.Articulo;
import modelo.ArticuloJDBC;
import vista.ArticuloVista;
import vista.NuevaNotaVista;
/**
 *
 * @author migue
 */
public class ArticuloControlador implements ActionListener, MouseListener {

    /**
     * instancia a nuestra interfaz de usuario
     */
    public ArticuloVista vista;
    public static NuevaNotaControlador nuevaNota;
    /**
     * instancia a nuestro modelo
     */
    ArticuloJDBC articuloConn = new ArticuloJDBC();
    IOdatos io = new IOdatos();
    public static FileChooserControlador fcc = null;

    /**
     * Se declaran en un ENUM las acciones que se realizan desde la interfaz de
     * usuario VISTA y posterior ejecución desde el controlador
     */
    public enum AccionMVC {
        __NUEVO_ARTICULO,
        __MODIFICAR_ARTICULO,
        __ELIMINAR_ARTICULO,
        __ANIADIR_NOTA,
        __GUARDAR_TABLA,
        __CARGAR_TABLA,
        __VOLVER,
        __BUSCAR
    }

    /**
     * Constrcutor de clase
     *
     * @param vista Instancia de clase interfaz
     */
    public ArticuloControlador(ArticuloVista vista) {
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
        this.vista.__NUEVO_ARTICULO.setActionCommand("__NUEVO_ARTICULO");
        this.vista.__NUEVO_ARTICULO.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__MODIFICAR_ARTICULO.setActionCommand("__MODIFICAR_ARTICULO");
        this.vista.__MODIFICAR_ARTICULO.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__ELIMINAR_ARTICULO.setActionCommand("__ELIMINAR_ARTICULO");
        this.vista.__ELIMINAR_ARTICULO.addActionListener(this);

        this.vista.__ANIADIR_NOTA.setActionCommand("__ANIADIR_NOTA");
        this.vista.__ANIADIR_NOTA.addActionListener(this);

        this.vista.__GUARDAR_TABLA.setActionCommand("__GUARDAR_TABLA");
        this.vista.__GUARDAR_TABLA.addActionListener(this);

        this.vista.__CARGAR_TABLA.setActionCommand("__CARGAR_TABLA");
        this.vista.__CARGAR_TABLA.addActionListener(this);

        this.vista.__BUSCAR.setActionCommand("__BUSCAR");
        this.vista.__BUSCAR.addActionListener(this);

        this.vista.__VOLVER.setActionCommand("__VOLVER");
        this.vista.__VOLVER.addActionListener(this);

        //añade e inicia el jtable
        ArticuloVista.__tabla_articulos.addMouseListener(this);
        ArticuloVista.__tabla_articulos.setModel(crearTabla(articuloConn.select()));

        ArticuloVista.issnCheckBox.addActionListener((ActionEvent event) -> {
            JCheckBox cb = (JCheckBox) event.getSource();
            if (cb.isSelected()) {
                ArticuloVista.issnBox.setEnabled(true);
            } else {
                ArticuloVista.issnBox.setEnabled(false);
            }
        });
    }

    //Eventos que suceden por el mouse
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1)//boton izquierdo
        {
            int fila = ArticuloVista.__tabla_articulos.rowAtPoint(e.getPoint());
            if (fila > -1) {
                ArticuloVista.issnBox.setText(String.valueOf(ArticuloVista.__tabla_articulos.getValueAt(fila, 0)));
                this.vista.tituloBox.setText(String.valueOf(ArticuloVista.__tabla_articulos.getValueAt(fila, 1)));
                this.vista.autorBox.setText(String.valueOf(ArticuloVista.__tabla_articulos.getValueAt(fila, 2)));
                this.vista.revistaBox.setText(String.valueOf(ArticuloVista.__tabla_articulos.getValueAt(fila, 3)));
                this.vista.anioFormatedBox.setText(String.valueOf(ArticuloVista.__tabla_articulos.getValueAt(fila, 4)));
                this.vista.mesFormatedBox.setText(String.valueOf(ArticuloVista.__tabla_articulos.getValueAt(fila, 5)));
                this.vista.pagIniFormatedBox.setText(String.valueOf(ArticuloVista.__tabla_articulos.getValueAt(fila, 5)));
                this.vista.pagFinFormatedBox.setText(String.valueOf(ArticuloVista.__tabla_articulos.getValueAt(fila, 5)));
                if (ArticuloVista.issnCheckBox.isSelected()) {
                    ArticuloVista.issnBox.setEnabled(false);
                    ArticuloVista.issnCheckBox.setSelected(false);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    //Control de eventos de los controles que tienen definido un "ActionCommand"
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (AccionMVC.valueOf(e.getActionCommand())) {
            case __NUEVO_ARTICULO:

                if (ArticuloVista.issnBox.getText().length() == 0 || this.vista.tituloBox.getText().length() == 0 || this.vista.autorBox.getText().length() == 0 || this.vista.revistaBox.getText().length() == 0 || this.vista.anioFormatedBox.getText().length() == 0 || this.vista.mesFormatedBox.getText().length() == 0 || this.vista.pagIniFormatedBox.getText().length() == 0 || this.vista.pagFinFormatedBox.getText().length() == 0) {

                    JOptionPane.showMessageDialog(null, "Los campos no pueden estar vacios.");
                } else {

                    if (articuloConn.existeISSN(ArticuloVista.issnBox.getText()) == 0) {

                        Articulo articulo = new Articulo();

                        articulo.setISSN(ArticuloVista.issnBox.getText());
                        articulo.setTitulo(this.vista.tituloBox.getText());
                        articulo.setAutor(this.vista.autorBox.getText());
                        articulo.setRevista(this.vista.revistaBox.getText());
                        articulo.setAnio(Integer.parseInt(this.vista.anioFormatedBox.getText()));
                        articulo.setMes(Integer.parseInt(this.vista.mesFormatedBox.getText()));
                        articulo.setPagInicio(Integer.parseInt(this.vista.pagIniFormatedBox.getText()));
                        articulo.setPagFin(Integer.parseInt(this.vista.pagFinFormatedBox.getText()));

                        articuloConn.insert(articulo);
                        ArticuloVista.__tabla_articulos.setModel(crearTabla(articuloConn.select()));
                        clean();
                    } else {
                        JOptionPane.showMessageDialog(null, "El ISSN ya existe.");
                    }
                }
                break;

            case __MODIFICAR_ARTICULO:

                if (articuloConn.existeISSN(ArticuloVista.issnBox.getText()) > 0) {

                    Articulo articulo = new Articulo();

                    articulo.setISSN(ArticuloVista.issnBox.getText());
                    articulo.setTitulo(this.vista.tituloBox.getText());
                    articulo.setAutor(this.vista.autorBox.getText());
                    articulo.setRevista(this.vista.revistaBox.getText());

                    int anio = Integer.parseInt(this.vista.anioFormatedBox.getText());
                    articulo.setAnio(anio);

                    int mes = Integer.parseInt(this.vista.mesFormatedBox.getText());
                    articulo.setMes(mes);
                    
                    int pagIni = Integer.parseInt(this.vista.pagIniFormatedBox.getText());
                    articulo.setPagInicio(pagIni);
                    
                    int pagFin = Integer.parseInt(this.vista.pagFinFormatedBox.getText());
                    articulo.setPagFin(pagFin);

                    articuloConn.update(articulo, ArticuloVista.issnBox.getText());

                    clean();
                    ArticuloVista.__tabla_articulos.setModel(crearTabla(articuloConn.select()));
                } else {
                    JOptionPane.showMessageDialog(null, "El ISSN introducido no esta registrado.");
                }
                break;

            case __ELIMINAR_ARTICULO:

                if (articuloConn.existeISSN(ArticuloVista.issnBox.getText()) > 0) {
                    articuloConn.delete(ArticuloVista.issnBox.getText());

                    ArticuloVista.__tabla_articulos.setModel(crearTabla(articuloConn.select()));
                } else {
                    JOptionPane.showMessageDialog(null, "El ISSN introducido no esta registrado.");
                }
                break;

            case __ANIADIR_NOTA:

                if (articuloConn.existeISSN(ArticuloVista.issnBox.getText()) > 0) {

                    nuevaNota = new NuevaNotaControlador(new NuevaNotaVista());
                    nuevaNota.fromArticulo = true;
                    nuevaNota.iniciar();

                } else {

                    JOptionPane.showMessageDialog(null, "Debes seleccionar un ISSN valido.");

                }

                break;

            case __BUSCAR:

                if (articuloConn.coincidencias(ArticuloVista.busquedaBox.getText()) > 0) {

                    ArticuloVista.__tabla_articulos.setModel(crearTabla(articuloConn.buscar(ArticuloVista.busquedaBox.getText())));

                } else {

                    JOptionPane.showMessageDialog(null, "La busqueda no ha sido satisfactoria.");
                    ArticuloVista.__tabla_articulos.setModel(crearTabla(articuloConn.select()));
                }

                break;
            case __GUARDAR_TABLA:

                JFileChooser fileChooser = new JFileChooser();
                int seleccion = fileChooser.showSaveDialog(this.vista);
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    try {
                        File fichero = fileChooser.getSelectedFile();
                        io.escrituraArticulo( getContenidoTabla(), fichero.getAbsolutePath());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Ha habido un error.");
                    }
                }

                break;
            case __CARGAR_TABLA:

                fileChooser = new JFileChooser();
                seleccion = fileChooser.showOpenDialog(this.vista);
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    try {
                        File fichero = fileChooser.getSelectedFile();
                        ArticuloVista.__tabla_articulos.setModel(crearTabla(io.lecturaArticulo(fichero.getAbsolutePath())));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Debes seleccionar un archivo valido.");
                    }

                }

                break;
            case __VOLVER:

                this.vista.dispose();
                HomeControlador.mArt = null;
                break;
        }

    }

    public void clean() {
        ArticuloVista.issnBox.setText("");
        this.vista.tituloBox.setText("");
        this.vista.autorBox.setText("");
        this.vista.revistaBox.setText("");
        this.vista.anioFormatedBox.setText("");
        this.vista.mesFormatedBox.setText("");
        this.vista.pagIniFormatedBox.setText("");
        this.vista.pagFinFormatedBox.setText("");
    }

    public DefaultTableModel crearTabla(List<Articulo> lista) {

        ArticuloVista.__tabla_articulos.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ISSN", "Titulo", "Autor", "Revista", "Año", "Mes", "Pag Inicio", "Pag Fin"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
            
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false
            };
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        DefaultTableModel modelo = (DefaultTableModel) ArticuloVista.__tabla_articulos.getModel();

        String[] columNames = {"ISSN", "Titulo", "Autor", "Revista", "Año", "Mes", "Pag Inicio", "Pag Fin"};

        if (articuloConn.cuentaArticulos() < 1) {
            //JOptionPane.showMessageDialog(null, "La lista de libros esta vacia.");  
        } else {

            Object[][] fila = new Object[lista.size()][8];
            int i = 0;

            for (Articulo articulo : lista) {
                fila[i][0] = articulo.getISSN();
                fila[i][1] = articulo.getTitulo();
                fila[i][2] = articulo.getAutor();
                fila[i][3] = articulo.getRevista();
                fila[i][4] = articulo.getAnio();
                fila[i][5] = articulo.getMes();
                fila[i][6] = articulo.getPagInicio();
                fila[i][7] = articulo.getPagFin();
                i++;

            }

            modelo.setDataVector(fila, columNames);
        }

        return modelo;
    }

    public List<Articulo> getContenidoTabla() {

        List<Articulo> lista = new ArrayList<>();

        for (int fila = 0; fila < ArticuloVista.__tabla_articulos.getRowCount(); fila++) {
            Articulo articulo = new Articulo();

            articulo.setISSN(String.valueOf(ArticuloVista.__tabla_articulos.getValueAt(fila, 0)));
            articulo.setTitulo(String.valueOf(ArticuloVista.__tabla_articulos.getValueAt(fila, 1)));
            articulo.setAutor(String.valueOf(ArticuloVista.__tabla_articulos.getValueAt(fila, 2)));
            articulo.setRevista(String.valueOf(ArticuloVista.__tabla_articulos.getValueAt(fila, 3)));
            articulo.setAnio((int) ArticuloVista.__tabla_articulos.getValueAt(fila, 4));
            articulo.setMes((int) ArticuloVista.__tabla_articulos.getValueAt(fila, 5));
            articulo.setPagInicio((int) ArticuloVista.__tabla_articulos.getValueAt(fila, 6));
            articulo.setPagFin((int) ArticuloVista.__tabla_articulos.getValueAt(fila, 7));

            lista.add(articulo);
        }

        return lista;
    }

}