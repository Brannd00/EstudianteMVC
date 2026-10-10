package com.miapp.vista;

import com.miapp.controlador.EstudianteController;
import com.miapp.modelo.Estudiante;
import com.miapp.utilidades.EstadoMatricula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class EstudianteView extends JFrame {

    // ── Constantes finales para dimensiones ────────────────────────────────────
    private static final int ANCHO_VENTANA = 1000;
    private static final int ALTO_VENTANA = 750;
    private static final int ANCHO_CAMPO_BUSQUEDA = 18;
    private static final int ANCHO_CAMPO_AGREGAR = 12;
    private static final int ALTO_FILA_TABLA = 24;

    // ── Constantes finales para textos ─────────────────────────────────────────
    private static final String TITULO_VENTANA = "Gestión de Estudiantes — MVC (Búsqueda + Agregar + Cursos + Profesores)";
    private static final String TITULO_PANEL_BUSQUEDA = "Buscar estudiante por nombre";
    private static final String TITULO_PANEL_CARRERA = "Buscar por carrera";
    private static final String TITULO_PANEL_AGREGAR = "Agregar nuevo estudiante";
    private static final String TITULO_PANEL_CURSOS = "Cursos: inscripción y consulta";
    private static final String TITULO_PANEL_PROFESORES = "Profesores: agregar y asignar a curso";
    private static final String TITULO_PANEL_ESTADO = "Estado de matrícula: buscar y cambiar";
    private static final String TITULO_PANEL_RESULTADOS = "Resultados";
    private static final String LABEL_NOMBRE = "Nombre:";
    private static final String LABEL_APELLIDO = "Apellido:";
    private static final String LABEL_CARRERA = "Carrera:";
    private static final String LABEL_PROMEDIO = "Promedio:";
    private static final String LABEL_CURSO = "Curso:";
    private static final String LABEL_SALARIO = "Salario base:";
    private static final String LABEL_PROFESOR = "Profesor:";
    private static final String LABEL_CURSO_ASIGNAR = "Curso a asignar:";
    private static final String LABEL_NUEVO_ESTADO = "Nuevo estado:";
    private static final String BOTON_BUSCAR = "Buscar";
    private static final String BOTON_BUSCAR_CARRERA = "Buscar por Carrera";
    private static final String BOTON_LIMPIAR = "Limpiar";
    private static final String BOTON_AGREGAR = "Agregar Estudiante";
    private static final String BOTON_VER_CURSO = "Ver estudiantes del curso";
    private static final String BOTON_INSCRIBIR = "Inscribir en curso";
    private static final String BOTON_AGREGAR_PROFESOR = "Agregar Profesor";
    private static final String BOTON_VER_CURSOS_PROFESOR = "Ver cursos del profesor";
    private static final String BOTON_ASIGNAR_CURSO = "Asignar a curso";
    private static final String BOTON_BUSCAR_ESTADO = "Buscar por estado";
    private static final String BOTON_CAMBIAR_ESTADO = "Cambiar estado";
    private static final String OPCION_SELECCIONAR = "Seleccionar...";
    private static final String MENSAJE_INICIAL = "Ingrese un nombre o seleccione una carrera y presione Buscar.";
    private static final String MENSAJE_ENCONTRADO_UNO = "Se encontró 1 estudiante.";
    private static final String MENSAJE_ENCONTRADOS_VARIOS = "Se encontraron %d estudiante(s).";
    private static final String MENSAJE_SIN_RESULTADOS = "No se encontraron estudiantes con ese criterio.";
    private static final String MENSAJE_SELECCIONE_ESTUDIANTE = "Primero busque y seleccione un estudiante en la tabla.";
    private static final String PROFESOR_ASIGNADO_PREFIJO = "Profesor asignado: ";

    // ── Constantes finales para colores ────────────────────────────────────────
    private static final Color COLOR_BOTON_FONDO = new Color(59, 139, 212);
    private static final Color COLOR_BOTON_CARRERA = new Color(76, 175, 80);
    private static final Color COLOR_BOTON_LIMPIAR = new Color(244, 67, 54);
    private static final Color COLOR_BOTON_AGREGAR = new Color(103, 58, 183);
    private static final Color COLOR_BOTON_CURSO = new Color(0, 121, 107);
    private static final Color COLOR_BOTON_INSCRIBIR = new Color(255, 152, 0);
    private static final Color COLOR_BOTON_PROFESOR = new Color(63, 81, 181);
    private static final Color COLOR_BOTON_TEXTO = Color.WHITE;
    private static final Color COLOR_ESTADO_TEXTO = Color.GRAY;
    private static final Color COLOR_PROFESOR_ASIGNADO = new Color(30, 60, 200);

    // ── Columnas de la tabla (constante final) ─────────────────────────────────
    private static final String[] COLUMNAS_TABLA = {"ID", "Nombre", "Apellido", "Carrera", "Promedio", "Estado"};

    // ── Componentes UI - Búsqueda por nombre ────────────────────────────────────
    private JTextField             txtNombre;
    private JButton                btnBuscar;

    // ── Componentes UI - Búsqueda por carrera ──────────────────────────────────
    private JComboBox<String>      cmbCarrera;
    private JButton                btnBuscarCarrera;
    private JButton                btnLimpiar;

    // ── Componentes UI - Agregar estudiante ────────────────────────────────────
    private JTextField             txtAgregarNombre;
    private JTextField             txtAgregarApellido;
    private JComboBox<String>      cmbAgregarCarrera;
    private JSpinner               spinPromedio;
    private JButton                btnAgregar;

    // ── Componentes UI - Cursos ─────────────────────────────────────────────────
    private JComboBox<String>      cmbCurso;
    private JButton                btnVerCurso;
    private JButton                btnInscribir;
    private JLabel                 lblProfesorAsignado;

    // ── Componentes UI - Profesores ─────────────────────────────────────────────
    private JTextField             txtNombreProfesor;
    private JSpinner               spinSalario;
    private JButton                btnAgregarProfesor;
    private JComboBox<String>      cmbProfesor;
    private JButton                btnVerCursosProfesor;
    private JComboBox<String>      cmbCursoAsignar;
    private JButton                btnAsignarCurso;

    // ── Componentes UI - Estado de matrícula ────────────────────────────────────
    private JComboBox<EstadoMatricula> cmbEstado;
    private JButton                btnBuscarEstado;
    private JButton                btnCambiarEstado;

    // ── Componentes UI - Resultados y Estado ────────────────────────────────────
    private JTable                 tblResultados;
    private DefaultTableModel      modeloTabla;
    private JLabel                 lblEstado;
    private JLabel                 lblTotalEstudiantes;

    // ── Selección actual ─────────────────────────────────────────────────────
    private Integer                idEstudianteSeleccionado;

    // ── Controlador ───────────────────────────────────────────────────────────
    private EstudianteController controlador;

    // ── Constructor ───────────────────────────────────────────────────────────

    public EstudianteView() {
        initComponentes();
        initEventos();
    }

    // ── Inicialización de componentes ─────────────────────────────────────────

    private void initComponentes() {
        setTitle(TITULO_VENTANA);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(ANCHO_VENTANA, ALTO_VENTANA);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(new GridLayout(6, 1, 5, 5));
        panelSuperior.add(crearPanelBusquedaNombre());
        panelSuperior.add(crearPanelBusquedaCarrera());
        panelSuperior.add(crearPanelAgregarEstudiante());
        panelSuperior.add(crearPanelCursos());
        panelSuperior.add(crearPanelProfesores());
        panelSuperior.add(crearPanelEstadoMatricula());

        // ── Tabla de resultados ────────────────────────────────────────────────
        modeloTabla = new DefaultTableModel(COLUMNAS_TABLA, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblResultados = new JTable(modeloTabla);
        tblResultados.setRowHeight(ALTO_FILA_TABLA);
        tblResultados.getTableHeader().setReorderingAllowed(false);
        tblResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblResultados.getSelectionModel().addListSelectionListener(evt -> {
            if (!evt.getValueIsAdjusting()) {
                actualizarSeleccion();
            }
        });

        JScrollPane scroll = new JScrollPane(tblResultados);
        scroll.setBorder(BorderFactory.createTitledBorder(TITULO_PANEL_RESULTADOS));

        // ── Panel inferior: estado y total ────────────────────────────────────
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));

        lblEstado = new JLabel(MENSAJE_INICIAL);
        lblEstado.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        lblEstado.setForeground(COLOR_ESTADO_TEXTO);

        lblTotalEstudiantes = new JLabel();
        lblTotalEstudiantes.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        lblTotalEstudiantes.setForeground(Color.BLUE);
        actualizarTotalEstudiantes();

        panelInferior.add(lblEstado, BorderLayout.WEST);
        panelInferior.add(lblTotalEstudiantes, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll,        BorderLayout.CENTER);
        add(panelInferior,  BorderLayout.SOUTH);
    }

    private JPanel crearPanelBusquedaNombre() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder(TITULO_PANEL_BUSQUEDA));

        txtNombre = new JTextField(ANCHO_CAMPO_BUSQUEDA);
        btnBuscar = new JButton(BOTON_BUSCAR);
        estilizarBoton(btnBuscar, COLOR_BOTON_FONDO);

        panel.add(new JLabel(LABEL_NOMBRE));
        panel.add(txtNombre);
        panel.add(btnBuscar);
        return panel;
    }

    private JPanel crearPanelBusquedaCarrera() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder(TITULO_PANEL_CARRERA));

        cmbCarrera = new JComboBox<>();
        cmbCarrera.addItem(OPCION_SELECCIONAR);

        btnBuscarCarrera = new JButton(BOTON_BUSCAR_CARRERA);
        estilizarBoton(btnBuscarCarrera, COLOR_BOTON_CARRERA);

        btnLimpiar = new JButton(BOTON_LIMPIAR);
        estilizarBoton(btnLimpiar, COLOR_BOTON_LIMPIAR);

        panel.add(new JLabel(LABEL_CARRERA));
        panel.add(cmbCarrera);
        panel.add(btnBuscarCarrera);
        panel.add(btnLimpiar);
        return panel;
    }

    private JPanel crearPanelAgregarEstudiante() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder(TITULO_PANEL_AGREGAR));

        txtAgregarNombre = new JTextField(ANCHO_CAMPO_AGREGAR);
        txtAgregarApellido = new JTextField(ANCHO_CAMPO_AGREGAR);

        cmbAgregarCarrera = new JComboBox<>();
        cmbAgregarCarrera.addItem(OPCION_SELECCIONAR);

        spinPromedio = new JSpinner(new SpinnerNumberModel(3.0, 0.0, 5.0, 0.1));
        spinPromedio.setPreferredSize(new Dimension(60, 25));

        btnAgregar = new JButton(BOTON_AGREGAR);
        estilizarBoton(btnAgregar, COLOR_BOTON_AGREGAR);

        panel.add(new JLabel(LABEL_NOMBRE));
        panel.add(txtAgregarNombre);
        panel.add(new JLabel(LABEL_APELLIDO));
        panel.add(txtAgregarApellido);
        panel.add(new JLabel(LABEL_CARRERA));
        panel.add(cmbAgregarCarrera);
        panel.add(new JLabel(LABEL_PROMEDIO));
        panel.add(spinPromedio);
        panel.add(btnAgregar);
        return panel;
    }

    private JPanel crearPanelCursos() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder(TITULO_PANEL_CURSOS));

        cmbCurso = new JComboBox<>();

        btnVerCurso = new JButton(BOTON_VER_CURSO);
        estilizarBoton(btnVerCurso, COLOR_BOTON_CURSO);

        btnInscribir = new JButton(BOTON_INSCRIBIR);
        estilizarBoton(btnInscribir, COLOR_BOTON_INSCRIBIR);

        lblProfesorAsignado = new JLabel(PROFESOR_ASIGNADO_PREFIJO + "(ninguno)");
        lblProfesorAsignado.setForeground(COLOR_PROFESOR_ASIGNADO);

        panel.add(new JLabel(LABEL_CURSO));
        panel.add(cmbCurso);
        panel.add(btnVerCurso);
        panel.add(btnInscribir);
        panel.add(new JLabel("(primero busque y seleccione un estudiante en la tabla)"));
        panel.add(lblProfesorAsignado);
        return panel;
    }

    private JPanel crearPanelProfesores() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder(TITULO_PANEL_PROFESORES));

        txtNombreProfesor = new JTextField(ANCHO_CAMPO_AGREGAR);

        spinSalario = new JSpinner(new SpinnerNumberModel(3000000.0, 0.0, 50000000.0, 100000.0));
        spinSalario.setPreferredSize(new Dimension(100, 25));

        btnAgregarProfesor = new JButton(BOTON_AGREGAR_PROFESOR);
        estilizarBoton(btnAgregarProfesor, COLOR_BOTON_PROFESOR);

        cmbProfesor = new JComboBox<>();
        btnVerCursosProfesor = new JButton(BOTON_VER_CURSOS_PROFESOR);
        estilizarBoton(btnVerCursosProfesor, COLOR_BOTON_CURSO);

        cmbCursoAsignar = new JComboBox<>();
        btnAsignarCurso = new JButton(BOTON_ASIGNAR_CURSO);
        estilizarBoton(btnAsignarCurso, COLOR_BOTON_PROFESOR);

        panel.add(new JLabel(LABEL_NOMBRE));
        panel.add(txtNombreProfesor);
        panel.add(new JLabel(LABEL_SALARIO));
        panel.add(spinSalario);
        panel.add(btnAgregarProfesor);
        panel.add(new JLabel(LABEL_PROFESOR));
        panel.add(cmbProfesor);
        panel.add(btnVerCursosProfesor);
        panel.add(new JLabel(LABEL_CURSO_ASIGNAR));
        panel.add(cmbCursoAsignar);
        panel.add(btnAsignarCurso);
        return panel;
    }

    private JPanel crearPanelEstadoMatricula() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder(TITULO_PANEL_ESTADO));

        cmbEstado = new JComboBox<>(EstadoMatricula.values());

        btnBuscarEstado = new JButton(BOTON_BUSCAR_ESTADO);
        estilizarBoton(btnBuscarEstado, COLOR_BOTON_CURSO);

        btnCambiarEstado = new JButton(BOTON_CAMBIAR_ESTADO);
        estilizarBoton(btnCambiarEstado, COLOR_BOTON_CURSO);

        panel.add(new JLabel(LABEL_NUEVO_ESTADO));
        panel.add(cmbEstado);
        panel.add(btnBuscarEstado);
        panel.add(btnCambiarEstado);
        panel.add(new JLabel("(\"Cambiar estado\" requiere seleccionar un estudiante en la tabla)"));
        return panel;
    }

    private void estilizarBoton(JButton boton, Color fondo) {
        boton.setBackground(fondo);
        boton.setForeground(COLOR_BOTON_TEXTO);
        boton.setFocusPainted(false);
    }

    // ── Métodos de carga de combos ─────────────────────────────────────────────

    private void cargarCarreras() {
        if (controlador == null) return;
        String[] carreras = controlador.obtenerCarrerasUnicas();
        for (String carrera : carreras) {
            cmbCarrera.addItem(carrera);
            cmbAgregarCarrera.addItem(carrera);
        }
    }

    private void cargarCursos() {
        if (controlador == null) return;
        String[] codigos = controlador.obtenerCodigosCursos();
        for (String codigo : codigos) {
            cmbCurso.addItem(codigo);
            cmbCursoAsignar.addItem(codigo);
        }
    }

    public void actualizarListaProfesores(String[] nombresProfesores) {
        cmbProfesor.removeAllItems();
        for (String nombre : nombresProfesores) {
            cmbProfesor.addItem(nombre);
        }
    }

    public void actualizarProfesorAsignado(String nombreProfesor) {
        lblProfesorAsignado.setText(PROFESOR_ASIGNADO_PREFIJO + nombreProfesor);
    }

    // ── Eventos ───────────────────────────────────────────────────────────────

    private void initEventos() {
        btnBuscar.addActionListener((ActionEvent e) -> {
            if (controlador != null) controlador.buscarEstudiante(txtNombre.getText().trim());
        });
        txtNombre.addActionListener((ActionEvent e) -> btnBuscar.doClick());

        btnBuscarCarrera.addActionListener((ActionEvent e) -> {
            if (controlador == null) return;
            String seleccion = (String) cmbCarrera.getSelectedItem();
            if (seleccion != null && !seleccion.equals(OPCION_SELECCIONAR)) {
                controlador.buscarEstudiantePorCarrera(seleccion);
            } else {
                mostrarError("Seleccione una carrera válida.");
            }
        });

        btnLimpiar.addActionListener((ActionEvent e) -> limpiarBusqueda());

        btnAgregar.addActionListener((ActionEvent e) -> {
            if (controlador == null) return;
            String nombre = txtAgregarNombre.getText().trim();
            String apellido = txtAgregarApellido.getText().trim();
            String carrera = (String) cmbAgregarCarrera.getSelectedItem();
            double promedio = (double) spinPromedio.getValue();

            if (controlador.agregarEstudiante(nombre, apellido, carrera, promedio)) {
                txtAgregarNombre.setText("");
                txtAgregarApellido.setText("");
                cmbAgregarCarrera.setSelectedIndex(0);
                spinPromedio.setValue(3.0);
                actualizarTotalEstudiantes();
            }
        });

        btnVerCurso.addActionListener((ActionEvent e) -> {
            if (controlador == null) return;
            String codigo = (String) cmbCurso.getSelectedItem();
            if (codigo != null) controlador.verEstudiantesDelCurso(codigo);
        });

        btnInscribir.addActionListener((ActionEvent e) -> {
            if (controlador == null) return;
            String codigo = (String) cmbCurso.getSelectedItem();
            Estudiante seleccionado = obtenerEstudianteSeleccionado();
            if (seleccionado == null) {
                mostrarError(MENSAJE_SELECCIONE_ESTUDIANTE);
                return;
            }
            controlador.inscribirEnCurso(seleccionado, codigo);
        });

        btnAgregarProfesor.addActionListener((ActionEvent e) -> {
            if (controlador == null) return;
            String nombre = txtNombreProfesor.getText().trim();
            double salario = (double) spinSalario.getValue();
            if (controlador.agregarProfesor(nombre, salario)) {
                txtNombreProfesor.setText("");
                spinSalario.setValue(3000000.0);
            }
        });

        btnVerCursosProfesor.addActionListener((ActionEvent e) -> {
            if (controlador == null) return;
            String profesor = (String) cmbProfesor.getSelectedItem();
            if (profesor != null) controlador.verCursosDelProfesor(profesor);
        });

        btnAsignarCurso.addActionListener((ActionEvent e) -> {
            if (controlador == null) return;
            String profesor = (String) cmbProfesor.getSelectedItem();
            String curso = (String) cmbCursoAsignar.getSelectedItem();
            if (profesor != null && curso != null) {
                controlador.asignarProfesorACurso(profesor, curso);
            }
        });

        btnBuscarEstado.addActionListener((ActionEvent e) -> {
            if (controlador == null) return;
            EstadoMatricula estado = (EstadoMatricula) cmbEstado.getSelectedItem();
            controlador.buscarPorEstado(estado);
        });

        btnCambiarEstado.addActionListener((ActionEvent e) -> {
            if (controlador == null) return;
            Estudiante seleccionado = obtenerEstudianteSeleccionado();
            EstadoMatricula nuevoEstado = (EstadoMatricula) cmbEstado.getSelectedItem();
            if (seleccionado == null) {
                mostrarError(MENSAJE_SELECCIONE_ESTUDIANTE);
                return;
            }
            controlador.cambiarEstado(seleccionado, nuevoEstado);
        });
    }

    private void actualizarSeleccion() {
        int fila = tblResultados.getSelectedRow();
        if (fila >= 0) {
            idEstudianteSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);
        } else {
            idEstudianteSeleccionado = null;
        }
    }

    private Estudiante obtenerEstudianteSeleccionado() {
        if (idEstudianteSeleccionado == null || controlador == null) return null;
        return controlador.obtenerEstudiantePorId(idEstudianteSeleccionado);
    }

    // ── Métodos públicos usados por el controlador ──────────────────────────────

    public void mostrarEstudiante(Object[] fila) {
        limpiarTabla();
        modeloTabla.addRow(fila);
        setEstado(MENSAJE_ENCONTRADO_UNO);
    }

    public void mostrarEstudiantes(List<Object[]> filas) {
        limpiarTabla();
        if (filas == null || filas.isEmpty()) {
            setEstado(MENSAJE_SIN_RESULTADOS);
            return;
        }
        for (Object[] fila : filas) {
            modeloTabla.addRow(fila);
        }
        setEstado(String.format(MENSAJE_ENCONTRADOS_VARIOS, filas.size()));
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        setEstado("Error: " + mensaje);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
        setEstado(mensaje);
    }

    public String getNombreBuscado() {
        return txtNombre.getText().trim();
    }

    public void setControlador(EstudianteController controlador) {
        this.controlador = controlador;
        cargarCarreras();
        cargarCursos();
        actualizarTotalEstudiantes();
    }

    private void actualizarTotalEstudiantes() {
        int total = (controlador != null) ? controlador.obtenerTotalEstudiantes() : 0;
        lblTotalEstudiantes.setText("Total de estudiantes: " + total);
    }

    private void limpiarBusqueda() {
        txtNombre.setText("");
        cmbCarrera.setSelectedIndex(0);
        limpiarTabla();
        setEstado(MENSAJE_INICIAL);
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
        idEstudianteSeleccionado = null;
    }

    private void setEstado(String texto) {
        lblEstado.setText(texto);
    }
}