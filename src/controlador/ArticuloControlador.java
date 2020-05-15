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
import java.util.Collections;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import modelo.IOdatos;
import modelo.Articulo;
import modelo.ArticuloConexion;
import vista.HomeVista;
import vista.NuevaNotaVista;
import org.apache.commons.validator.routines.ISSNValidator;

/**
 *
 * @author migue
 */
public class ArticuloControlador implements ActionListener, MouseListener {

    /**
     * instancia a nuestra interfaz de usuario
     */
    public JPanel panel;
    public HomeVista vista;
    public static NuevaNotaControlador nuevaNota;
    public static int issn = 0;
    /**
     * instancia a nuestro modelo
     */
    ArticuloConexion articuloConn = new ArticuloConexion();
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
        __ANIADIR_NOTA_ARTICULO,
        __LIMPIAR_ARTICULO,
        __GUARDAR_TABLA_ARTICULO,
        __CARGAR_TABLA_ARTICULO,
        __BUSCAR_ARTICULO,
        __VOLVER_ARTICULO,
        __ACTUALIZAR_TABLA_ARTICULOS

    }

    /**
     * Constrcutor de clase
     *
     * @param panel Instancia de clase interfaz
     *
     */
    public ArticuloControlador(JPanel panel) {
        this.vista = HomeControlador.vista;
        this.panel = panel;
    }

    /**
     * Inicia el skin y las diferentes variables que se utilizan
     */
    public void iniciar() {
        // Skin tipo WINDOWS
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(vista);

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
        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__ANIADIR_NOTA_ARTICULO.setActionCommand("__ANIADIR_NOTA_ARTICULO");
        this.vista.__ANIADIR_NOTA_ARTICULO.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__LIMPIAR_ARTICULO.setActionCommand("__LIMPIAR_ARTICULO");
        this.vista.__LIMPIAR_ARTICULO.addActionListener(this);

        this.vista.__GUARDAR_TABLA_ARTICULO.setActionCommand("__GUARDAR_TABLA_ARTICULO");
        this.vista.__GUARDAR_TABLA_ARTICULO.addActionListener(this);

        this.vista.__CARGAR_TABLA_ARTICULO.setActionCommand("__CARGAR_TABLA_ARTICULO");
        this.vista.__CARGAR_TABLA_ARTICULO.addActionListener(this);

        this.vista.__BUSCAR_ARTICULO.setActionCommand("__BUSCAR_ARTICULO");
        this.vista.__BUSCAR_ARTICULO.addActionListener(this);

        this.vista.__VOLVER_ARTICULO.setActionCommand("__VOLVER_ARTICULO");
        this.vista.__VOLVER_ARTICULO.addActionListener(this);

        this.vista.__ACTUALIZAR_TABLA_ARTICULOS.setActionCommand("__ACTUALIZAR_TABLA_ARTICULOS");
        this.vista.__ACTUALIZAR_TABLA_ARTICULOS.addActionListener(this);

        //añade e inicia el jtable
        this.vista.__tabla_articulos.addMouseListener(this);
        this.definirTabla();
        this.vista.__tabla_articulos.setModel(setTabla(articuloConn.select()));

        this.vista.issnCheckBox.addActionListener((ActionEvent event) -> {
            JCheckBox cb = (JCheckBox) event.getSource();
            if (cb.isSelected()) {
                HomeVista.issn1ArticuloBox.setEnabled(true);
                HomeVista.issn2ArticuloBox.setEnabled(true);
            } else {
                HomeVista.issn1ArticuloBox.setEnabled(false);
                HomeVista.issn2ArticuloBox.setEnabled(false);
            }
        });

        this.vista.issnCheckBox.setSelected(true);
        
    }

    //Eventos que suceden por el mouse
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1)//boton izquierdo
        {
            int fila = this.vista.__tabla_articulos.rowAtPoint(e.getPoint());
            if (fila > -1) {
                
                
                HomeVista.issn1ArticuloBox.setText(String.valueOf(this.vista.__tabla_articulos.getValueAt(fila, 0).toString().substring(0, 4)));
                HomeVista.issn2ArticuloBox.setText(String.valueOf(this.vista.__tabla_articulos.getValueAt(fila, 0).toString().substring(4)));
                this.vista.tituloArticuloBox.setText(String.valueOf(this.vista.__tabla_articulos.getValueAt(fila, 1)));
                this.vista.autorArticuloBox.setText(String.valueOf(this.vista.__tabla_articulos.getValueAt(fila, 2)));
                this.vista.revistaArticuloBox.setText(String.valueOf(this.vista.__tabla_articulos.getValueAt(fila, 3)));
                this.vista.anioArticuloFormatedBox.setText(String.valueOf(this.vista.__tabla_articulos.getValueAt(fila, 4)));
                this.vista.mesArticuloFormatedBox.setText(String.valueOf(this.vista.__tabla_articulos.getValueAt(fila, 5)));
                this.vista.pagIniArticuloFormatedBox.setText(String.valueOf(this.vista.__tabla_articulos.getValueAt(fila, 5)));
                this.vista.pagFinArticuloFormatedBox.setText(String.valueOf(this.vista.__tabla_articulos.getValueAt(fila, 5)));
                if (this.vista.issnCheckBox.isSelected()) {
                    HomeVista.issn1ArticuloBox.setEnabled(false);
                    HomeVista.issn2ArticuloBox.setEnabled(false);
                    this.vista.issnCheckBox.setSelected(false);
                }
            }
//ERROR AL PARSEAR
            String cadena = HomeVista.issn1ArticuloBox.getText() + HomeVista.issn2ArticuloBox.getText();
            issn = Integer.parseInt(cadena);
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

                if (HomeVista.issn1ArticuloBox.getText().isEmpty()
                        || HomeVista.issn2ArticuloBox.getText().isEmpty()
                        || this.vista.tituloArticuloBox.getText().isEmpty()
                        || this.vista.autorArticuloBox.getText().isEmpty()
                        || this.vista.revistaArticuloBox.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Los campos ISSN, Articulo, Autor y Revista no pueden estar vacios.");
                } else {
                    String cadena = HomeVista.issn1ArticuloBox.getText() + HomeVista.issn2ArticuloBox.getText();
                    issn = Integer.parseInt(cadena);
                    if (articuloConn.existeISSN(this.issn) == 0) {
                        ISSNValidator issnValidator = new ISSNValidator();
                        if(issnValidator.isValid(HomeVista.issn1ArticuloBox.getText())) {
                            Articulo articulo = new Articulo();

                            articulo.setISSN(this.issn);
                            articulo.setTitulo(this.vista.tituloArticuloBox.getText());
                            articulo.setAutor(this.vista.autorArticuloBox.getText());
                            articulo.setRevista(this.vista.revistaArticuloBox.getText());
                            if (this.vista.anioArticuloFormatedBox.getText().isEmpty()) {
                                articulo.setAnio(0);
                            } else {
                                articulo.setAnio(Integer.parseInt(this.vista.anioArticuloFormatedBox.getText()));
                            }
                            if (this.vista.mesArticuloFormatedBox.getText().isEmpty()) {
                                articulo.setMes(0);
                            } else {
                                articulo.setMes(Integer.parseInt(this.vista.mesArticuloFormatedBox.getText()));
                            }
                            if (this.vista.pagIniArticuloFormatedBox.getText().isEmpty()) {
                                articulo.setPagInicio(0);
                            } else {
                                articulo.setPagInicio(Integer.parseInt(this.vista.pagIniArticuloFormatedBox.getText()));
                            }
                            if (this.vista.pagFinArticuloFormatedBox.getText().isEmpty()) {
                                articulo.setPagFin(0);
                            } else {
                                articulo.setPagFin(Integer.parseInt(this.vista.pagFinArticuloFormatedBox.getText()));
                            }

                            if (articuloConn.insert(articulo)) {
                                JOptionPane.showMessageDialog(null, "Articulo introducido con exito.");
                                this.vista.__tabla_articulos.setModel(setTabla(articuloConn.select()));
                                this.clean();
                            } else {
                                JOptionPane.showMessageDialog(null, "Ha habido un error.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Introduce un ISSN valido.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "El ISSN ya existe.");
                    }
                }
                break;
            case __MODIFICAR_ARTICULO:
                if (HomeVista.issn1ArticuloBox.getText().isEmpty()
                        || this.vista.tituloArticuloBox.getText().isEmpty()
                        || this.vista.autorArticuloBox.getText().isEmpty()
                        || this.vista.revistaArticuloBox.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Los campos ISSN, Articulo, Autor y Revista no pueden estar vacios.");
                } else {
                    if (articuloConn.existeISSN(this.issn) > 0) {

                        Articulo articulo = new Articulo();

                        articulo.setISSN(this.issn);
                        articulo.setTitulo(this.vista.tituloArticuloBox.getText());
                        articulo.setAutor(this.vista.autorArticuloBox.getText());
                        articulo.setRevista(this.vista.revistaArticuloBox.getText());

                        if (this.vista.anioArticuloFormatedBox.getText().isEmpty()) {
                            articulo.setAnio(0);
                        } else {
                            int anio = Integer.parseInt(this.vista.anioArticuloFormatedBox.getText());
                            articulo.setAnio(anio);
                        }

                        if (this.vista.mesArticuloFormatedBox.getText().isEmpty()) {
                            articulo.setMes(0);
                        } else {
                            int mes = Integer.parseInt(this.vista.mesArticuloFormatedBox.getText());
                            articulo.setMes(mes);
                        }

                        if (this.vista.pagIniArticuloFormatedBox.getText().isEmpty()) {
                            articulo.setPagInicio(0);
                        } else {
                            int pagIni = Integer.parseInt(this.vista.pagIniArticuloFormatedBox.getText());
                            articulo.setPagInicio(pagIni);
                        }

                        if (this.vista.pagFinArticuloFormatedBox.getText().isEmpty()) {
                            articulo.setPagFin(0);
                        } else {
                            int pagFin = Integer.parseInt(this.vista.pagFinArticuloFormatedBox.getText());
                            articulo.setPagFin(pagFin);
                        }

                        if (articuloConn.update(articulo, this.issn)) {
                            JOptionPane.showMessageDialog(null, "Articulo modificado con exito.");
                            this.vista.__tabla_articulos.setModel(setTabla(articuloConn.select()));
                        } else {
                            JOptionPane.showMessageDialog(null, "Ha habido un error.");
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "El ISSN introducido no esta registrado.");
                    }
                }
                break;
            case __ELIMINAR_ARTICULO:

                if (articuloConn.existeISSN(this.issn) > 0) {
                    if (articuloConn.delete(this.issn)) {
                        JOptionPane.showMessageDialog(null, "Articulo eliminado con exito.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Ha habido un error.");
                    }
                    this.vista.__tabla_articulos.setModel(setTabla(articuloConn.select()));
                } else {
                    JOptionPane.showMessageDialog(null, "El ISSN introducido no esta registrado.");
                }
                break;
            case __ANIADIR_NOTA_ARTICULO:
                if (articuloConn.existeISSN(this.issn) > 0) {
                    nuevaNota = new NuevaNotaControlador(new NuevaNotaVista());
                    nuevaNota.fromArticulo = true;
                    nuevaNota.iniciar();
                } else {
                    JOptionPane.showMessageDialog(null, "Debes seleccionar un ISSN valido.");
                }

                break;
            case __LIMPIAR_ARTICULO:
                this.clean();
                break;
            case __BUSCAR_ARTICULO:
                if (this.vista.busquedaArticuloBox.getText().isEmpty()) {
                } else {
                    if (!articuloConn.buscar(this.vista.busquedaArticuloBox.getText()).isEmpty()) {
                        this.vista.__tabla_articulos.setModel(setTabla(articuloConn.buscar(this.vista.busquedaArticuloBox.getText())));
                    } else {
                        JOptionPane.showMessageDialog(null, "Busquedas sin resultados.");
                    }
                }
                break;
            case __GUARDAR_TABLA_ARTICULO:

                JFileChooser fileChooser = new JFileChooser();
                int seleccion = fileChooser.showSaveDialog(this.vista);
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    try {
                        File fichero = fileChooser.getSelectedFile();
                        io.escrituraArticulo(getContenidoTabla(), fichero.getAbsolutePath());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Ha habido un error.");
                    }
                }

                break;
            case __CARGAR_TABLA_ARTICULO:

                fileChooser = new JFileChooser();
                seleccion = fileChooser.showOpenDialog(this.vista);
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    try {
                        File fichero = fileChooser.getSelectedFile();
                        this.vista.__tabla_articulos.setModel(setTabla(io.lecturaArticulo(fichero.getAbsolutePath())));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Debes seleccionar un archivo valido.");
                    }

                }

                break;
            case __ACTUALIZAR_TABLA_ARTICULOS:

                this.vista.__tabla_articulos.setModel(setTabla(articuloConn.select()));

                break;
            case __VOLVER_ARTICULO:

                int option = JOptionPane.showConfirmDialog(null,
                        "¿Estás seguro de que quieres cerrar la sesion?",
                        "Cierre de sesion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    this.vista.dispose();
                    LoginControlador.vista.dispose();
                    InicioControlador.vista.setEnabled(true);
                    InicioControlador.vista.setVisible(true);
                }

                break;
        }

    }

    public void clean() {
        HomeVista.issn1ArticuloBox.setText("");
        if (!this.vista.issnCheckBox.isSelected()) {
            HomeVista.issn1ArticuloBox.setEnabled(true);
            HomeVista.issn2ArticuloBox.setEnabled(true);
            this.vista.issnCheckBox.setSelected(true);
        }
        this.vista.tituloArticuloBox.setText("");
        this.vista.autorArticuloBox.setText("");
        this.vista.revistaArticuloBox.setText("");
        this.vista.anioArticuloFormatedBox.setText("");
        this.vista.mesArticuloFormatedBox.setText("");
        this.vista.pagIniArticuloFormatedBox.setText("");
        this.vista.pagFinArticuloFormatedBox.setText("");
    }
    
    public void definirTabla() {
        this.vista.__tabla_articulos.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ISSN", "Titulo", "Autor", "Revista", "Año", "Mes", "Pag Inicio", "Pag Fin", "Usuario"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });  
    }
    
    public DefaultTableModel setTabla(List<Articulo> lista) {

        Collections.sort(lista, Articulo.tituloComparator);

        TableColumnModel tcm = this.vista.__tabla_articulos.getColumnModel();

        DefaultTableModel modelo = (DefaultTableModel) this.vista.__tabla_articulos.getModel();

        String[] columNames = {"ISSN", "Titulo", "Autor", "Revista", "Año", "Mes", "Pag Inicio", "Pag Fin", "Usuario"};

        Object[][] fila = new Object[lista.size()][9];
        int i = 0;

        for (Articulo articulo : lista) {
            fila[i][0] = articulo.getISSN();
            fila[i][1] = articulo.getTitulo();
            fila[i][2] = articulo.getAutor();
            fila[i][3] = articulo.getRevista();
            if (articulo.getAnio() == 0) {
                fila[i][4] = "";
            } else {
                fila[i][4] = articulo.getAnio();
            }
            if (articulo.getMes() == 0) {
                fila[i][5] = "";
            } else {
                fila[i][5] = articulo.getMes();
            }
            if (articulo.getPagInicio() == 0) {
                fila[i][6] = "";
            } else {
                fila[i][6] = articulo.getPagInicio();
            }
            if (articulo.getPagFin() == 0) {
                fila[i][7] = "";
            } else {
                fila[i][7] = articulo.getPagFin();
            }

            fila[i][8] = articulo.getIdUser();
            i++;

        }

        modelo.setDataVector(fila, columNames);

        tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex("Usuario")));

        return modelo;
    }

    public List<Articulo> getContenidoTabla() {

        List<Articulo> lista = new ArrayList<>();

        for (int fila = 0; fila < this.vista.__tabla_articulos.getRowCount(); fila++) {
            Articulo articulo = new Articulo();

            articulo.setISSN((int) this.vista.__tabla_articulos.getValueAt(fila, 0));
            articulo.setTitulo(String.valueOf(this.vista.__tabla_articulos.getValueAt(fila, 1)));
            articulo.setAutor(String.valueOf(this.vista.__tabla_articulos.getValueAt(fila, 2)));
            articulo.setRevista(String.valueOf(this.vista.__tabla_articulos.getValueAt(fila, 3)));
            articulo.setAnio((int) this.vista.__tabla_articulos.getValueAt(fila, 4));
            articulo.setMes((int) this.vista.__tabla_articulos.getValueAt(fila, 5));
            articulo.setPagInicio((int) this.vista.__tabla_articulos.getValueAt(fila, 6));
            articulo.setPagFin((int) this.vista.__tabla_articulos.getValueAt(fila, 7));
            articulo.setIdUser((int) this.vista.__tabla_articulos.getModel().getValueAt(fila, 8));

            lista.add(articulo);
        }

        return lista;
    }

}
