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
import modelo.Nota;
import modelo.NotaConexion;
import vista.HomeVista;
import vista.NuevaNotaVista;

/**
 * ArticuloControlador.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class ArticuloControlador implements ActionListener, MouseListener {

    //Declaración de objetos y variables necesarios
    public JPanel panel;
    public HomeVista vista;
    public static NuevaNotaControlador nuevaNota;
    public static int issn = 0;

    ArticuloConexion articuloConn = new ArticuloConexion();
    NotaConexion notaConn = new NotaConexion();
    IOdatos io = new IOdatos();

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
        __ACTUALIZAR_TABLA_ARTICULOS,
        __EXPORTAR_ARTICULO

    }

    /**
     * Constructor de la clase ArticuloControlador
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

        this.vista.__MODIFICAR_ARTICULO.setActionCommand("__MODIFICAR_ARTICULO");
        this.vista.__MODIFICAR_ARTICULO.addActionListener(this);

        this.vista.__ELIMINAR_ARTICULO.setActionCommand("__ELIMINAR_ARTICULO");
        this.vista.__ELIMINAR_ARTICULO.addActionListener(this);

        this.vista.__ANIADIR_NOTA_ARTICULO.setActionCommand("__ANIADIR_NOTA_ARTICULO");
        this.vista.__ANIADIR_NOTA_ARTICULO.addActionListener(this);

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

        this.vista.__EXPORTAR_ARTICULO.setActionCommand("__EXPORTAR_ARTICULO");
        this.vista.__EXPORTAR_ARTICULO.addActionListener(this);

        this.vista.__ACTUALIZAR_TABLA_ARTICULOS.setActionCommand("__ACTUALIZAR_TABLA_ARTICULOS");
        this.vista.__ACTUALIZAR_TABLA_ARTICULOS.addActionListener(this);

        //Añade, define e inicia el jtable
        this.vista.__tabla_articulos.addMouseListener(this);
        this.definirTabla();
        this.vista.__tabla_articulos.setModel(setTabla(articuloConn.select()));

        //Define el comportacmiento del checkBox
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
                this.vista.pagIniArticuloFormatedBox.setText(String.valueOf(this.vista.__tabla_articulos.getValueAt(fila, 6)));
                this.vista.pagFinArticuloFormatedBox.setText(String.valueOf(this.vista.__tabla_articulos.getValueAt(fila, 7)));
                if (this.vista.issnCheckBox.isSelected()) {
                    HomeVista.issn1ArticuloBox.setEnabled(false);
                    HomeVista.issn2ArticuloBox.setEnabled(false);
                    this.vista.issnCheckBox.setSelected(false);
                }
            }

            //Añade a la variable issn el contenido de las cajas unidas
            String cadena = HomeVista.issn1ArticuloBox.getText() + HomeVista.issn2ArticuloBox.getText();
            issn = Integer.parseInt(cadena);
        }
    }

    //No se usa
    @Override
    public void mousePressed(MouseEvent e) {
    }

    //No se usa
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    //No se usa
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    //No se usa
    @Override
    public void mouseExited(MouseEvent e) {
    }

    //Control de eventos de los controles que tienen definido un "ActionCommand"
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (AccionMVC.valueOf(e.getActionCommand())) {
            case __NUEVO_ARTICULO:

                //Si algun campo obligatorio esta vacio salta un mensaje
                if (HomeVista.issn1ArticuloBox.getText().isEmpty()
                        || HomeVista.issn2ArticuloBox.getText().isEmpty()
                        || this.vista.tituloArticuloBox.getText().isEmpty()
                        || this.vista.autorArticuloBox.getText().isEmpty()
                        || this.vista.revistaArticuloBox.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Los campos ISSN, Articulo, Autor y Revista no pueden estar vacios.");
                } else {

                    //Añade a la variable issn el contenido de las cajas unidas
                    String cadena = HomeVista.issn1ArticuloBox.getText() + HomeVista.issn2ArticuloBox.getText();
                    issn = Integer.parseInt(cadena);

                    //Si el issn no existe crea un objeto articulo con los parametros de las cajas
                    if (articuloConn.existeISSN(ArticuloControlador.issn) == 0) {
                        Articulo articulo = new Articulo();

                        articulo.setISSN(ArticuloControlador.issn);
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

                        //Si el metodo insert retorna true muestra un mensaje de exito
                        if (articuloConn.insert(articulo)) {
                            JOptionPane.showMessageDialog(null, "Articulo introducido con exito.");
                            this.vista.__tabla_articulos.setModel(setTabla(articuloConn.select()));
                            this.clean();
                        } else {
                            JOptionPane.showMessageDialog(null, "Ha habido un error.");
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
                    if (articuloConn.existeISSN(ArticuloControlador.issn) > 0) {

                        Articulo articulo = new Articulo();

                        articulo.setISSN(ArticuloControlador.issn);
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

                        if (articuloConn.update(articulo, ArticuloControlador.issn)) {
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

                //Si el issn existe borra el registro, en otro caso muestra error
                if (articuloConn.existeISSN(ArticuloControlador.issn) > 0) {
                    if (articuloConn.delete(ArticuloControlador.issn)) {
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

                //Si el issn existe llama a la ventana Nueva Nota
                if (articuloConn.existeISSN(ArticuloControlador.issn) > 0) {
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

                //Si la caja de buscar registro esta vacia no hace nada
                if (this.vista.busquedaArticuloBox.getText().isEmpty()) {
                } else {

                    //Si la busqueda retorna contenido, refresca la tabla con ese contenido
                    if (!articuloConn.buscar(this.vista.busquedaArticuloBox.getText()).isEmpty()) {
                        this.vista.__tabla_articulos.setModel(setTabla(articuloConn.buscar(this.vista.busquedaArticuloBox.getText())));
                    } else {
                        JOptionPane.showMessageDialog(null, "Busquedas sin resultados.");
                    }
                }
                break;
            case __GUARDAR_TABLA_ARTICULO:

                //Intancia un objeto JFileChooser
                JFileChooser fileChooser = new JFileChooser();
                int seleccion = fileChooser.showSaveDialog(this.vista);

                //Si selecciona un fichero y la ruta es valida, guarda el contenido de la tabla en ese fichero
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
                //Si selecciona un fichero y la ruta es valida, carga el contenido del fichero en la tabla
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

                //Refresca el contenido de la tabla con todos los registros en la base de datos
                this.vista.__tabla_articulos.setModel(setTabla(articuloConn.select()));

                break;
            case __EXPORTAR_ARTICULO:

                fileChooser = new JFileChooser();
                seleccion = fileChooser.showSaveDialog(this.vista);

                //Si selecciona un fichero y la ruta es valida, guarda el contenido de la tabla en un txt
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    try {
                        File fichero = fileChooser.getSelectedFile();
                        io.exportarTxt(generarTxt(getContenidoTabla()), fichero.getAbsolutePath());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Debes seleccionar un archivo valido.");
                    }

                }

                break;
            case __VOLVER_ARTICULO:

                //Muestra mensaje de confirmación, si es positivo, cierra la sesion y vuelve a la pantalla de login
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

    /**
     * Pone en blanco todas las cajas de texto
     */
    public void clean() {
        HomeVista.issn1ArticuloBox.setText("");
        HomeVista.issn2ArticuloBox.setText("");

        //Si el chechBox no esta marcado, lo marca y habilita las cajas de issn
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

    /**
     * Define la tabla de articulos
     */
    public void definirTabla() {
        this.vista.__tabla_articulos.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ISSN", "Titulo", "Autor", "Revista", "Año", "Mes", "Pag Inicio", "Pag Fin", "Usuario"
                }
        ) {
            //Define la clase de cada columna
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            //Niega la edicion de las columnas
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
    }

    /**
     * Introduce los atributos de una coleccion de articulos en la tabla
     *
     * @param lista Entra por parametro un List de objetos Articulo
     * @return Retorna el modelo de la tabla con contenido
     */
    public DefaultTableModel setTabla(List<Articulo> lista) {

        //Ordena la lista por nombre
        Collections.sort(lista, Articulo.tituloComparator);
        //Instancia un objeto para modificar columnas
        TableColumnModel tcm = this.vista.__tabla_articulos.getColumnModel();

        //Instancia el modelo
        DefaultTableModel modelo = (DefaultTableModel) this.vista.__tabla_articulos.getModel();

        String[] columNames = {"ISSN", "Titulo", "Autor", "Revista", "Año", "Mes", "Pag Inicio", "Pag Fin", "Usuario"};

        //Instancia una matriz (numero de objetos)*(numero de columnas)
        Object[][] fila = new Object[lista.size()][9];
        int i = 0;

        //Recorre la lista e introduce el contenido en la matriz
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

        //Inserta el contenido de la matriz en el modelo
        modelo.setDataVector(fila, columNames);

        //Oculta una columna
        tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex("Usuario")));

        return modelo;
    }

    /**
     * Devuelve el contenido de la tabla en forma de coleccion
     *
     * @return Retorna un ArrayList de Articulo
     */
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

    /**
     * Genera un String de datos con formato a partir de una coleccion
     *
     * @param lista La coleccion de Articulo que se va a introducir
     * @return El String con los datos de la coleccion
     */
    public String generarTxt(List<Articulo> lista) {

        //Inicializa el String vacio;
        String contenido = "";

        //Inicializa el String que servira para el salto de linea
        String saltolinea = System.getProperty("line.separator");

        //Por cada articulo en la lista añade los datos requeridos al String
        for (Articulo articulo : lista) {

            List<Nota> notas = notaConn.selectPorArticulo(articuloConn.getId(articulo.getISSN()));

            contenido += articulo.getTitulo();
            contenido += ", ";
            contenido += articulo.getAutor();
            contenido += ", ";
            contenido += articulo.getRevista();

            if (articulo.getAnio() > 0) {
                contenido += ", ";
                contenido += articulo.getAnio();
            }
            if (articulo.getMes() > 0) {
                contenido += ", ";
                contenido += articulo.getMes();
            }
            if (articulo.getPagInicio() > 0) {
                contenido += ", ";
                contenido += articulo.getPagInicio();
            }

            if (articulo.getPagFin() > 0) {
                contenido += ", ";
                contenido += articulo.getPagFin();
            }

            //Si la lista de notas no esta vacía la recorre
            if (!notas.isEmpty()) {
                contenido += saltolinea;
                //Por cada nota en la lista añade los datos requeridos al String
                for (Nota nota : notas) {

                    contenido += "     " + nota.getTema();
                    if (!nota.getContenido().isEmpty()) {
                        contenido += ": ";
                        contenido += nota.getContenido();
                    }

                    contenido += saltolinea;
                }
            }
            contenido += saltolinea;
        }
        return contenido;
    }
}
