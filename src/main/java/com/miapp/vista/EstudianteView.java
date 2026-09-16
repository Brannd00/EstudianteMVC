package com.miapp.vista;

import com.miapp.controlador.EstudianteController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class EstudianteView extends JFrame {

    private JTextField             txtNombre;
    private JButton                btnBuscar;
    private JTable                 tblResultados;
    private DefaultTableModel      modeloTabla;
    private JLabel                 lblEstado;

    private JTextField             txtNuevoNombre;
    private JTextField             txtNuevaCarrera;
    private JTextField             txtNuevoPromedio;
    private JButton                btnAgregar;

    private EstudianteController controlador;


    public EstudianteView() {
        initComponentes();
        initEventos();
    }


    private void initComponentes() {
        setTitle("Búsqueda de Estudiantes — MVC NetBeans");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Buscar estudiante"));

        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField(25);
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(59, 139, 212));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);

        panelBusqueda.add(lblNombre);
        panelBusqueda.add(txtNombre);
        panelBusqueda.add(btnBuscar);

        JPanel panelAgregar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelAgregar.setBorder(BorderFactory.createTitledBorder("Agregar estudiante"));

        JLabel lblNuevoNombre = new JLabel("Nombre:");
        txtNuevoNombre = new JTextField(15);
        JLabel lblNuevaCarrera = new JLabel("Carrera:");
        txtNuevaCarrera = new JTextField(15);
        JLabel lblNuevoPromedio = new JLabel("Promedio:");
        txtNuevoPromedio = new JTextField(5);
        btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(60, 160, 90));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);

        panelAgregar.add(lblNuevoNombre);
        panelAgregar.add(txtNuevoNombre);
        panelAgregar.add(lblNuevaCarrera);
        panelAgregar.add(txtNuevaCarrera);
        panelAgregar.add(lblNuevoPromedio);
        panelAgregar.add(txtNuevoPromedio);
        panelAgregar.add(btnAgregar);

        panelSuperior.add(panelBusqueda);
        panelSuperior.add(panelAgregar);

        String[] columnas = {"ID", "Nombre", "Carrera", "Promedio"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblResultados = new JTable(modeloTabla);
        tblResultados.setRowHeight(24);
        tblResultados.getTableHeader().setReorderingAllowed(false);
        tblResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tblResultados);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados"));

        lblEstado = new JLabel("Ingrese un nombre y presione Buscar.");
        lblEstado.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        lblEstado.setForeground(Color.GRAY);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll,        BorderLayout.CENTER);
        add(lblEstado,     BorderLayout.SOUTH);
    }


    private void initEventos() {
        btnBuscar.addActionListener((ActionEvent e) -> {
            if (controlador != null) {
                controlador.buscarEstudiante(txtNombre.getText().trim());
            }
        });

        txtNombre.addActionListener((ActionEvent e) -> btnBuscar.doClick());

        btnAgregar.addActionListener((ActionEvent e) -> onAgregar());

        txtNuevoPromedio.addActionListener((ActionEvent e) -> btnAgregar.doClick());
    }

    
    private void onAgregar() {
        if (controlador == null) {
            return;
        }

        String nombre  = txtNuevoNombre.getText().trim();
        String carrera = txtNuevaCarrera.getText().trim();
        String textoPromedio = txtNuevoPromedio.getText().trim();

        double promedio;
        try {
            promedio = Double.parseDouble(textoPromedio.replace(",", "."));
        } catch (NumberFormatException ex) {
            mostrarError("El promedio debe ser un número válido (ej: 4.5).");
            return;
        }

        controlador.agregarEstudiante(nombre, carrera, promedio);
    }

    
    public void mostrarEstudiante(Object[] fila) {
        limpiarTabla();
        agregarFila(fila);
        setEstado("Se encontró 1 estudiante.");
    }

    
    public void mostrarEstudiantes(List<Object[]> filas) {
        limpiarTabla();
        if (filas == null || filas.isEmpty()) {
            setEstado("No se encontraron estudiantes con ese criterio.");
            return;
        }
        for (Object[] fila : filas) {
            agregarFila(fila);
        }
        setEstado("Se encontraron " + filas.size() + " estudiante(s).");
    }

    
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        setEstado("Error: " + mensaje);
    }

    public void mostrarConfirmacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        setEstado(mensaje);
        limpiarFormularioAgregar();
    }

    
    public String getNombreBuscado() {
        return txtNombre.getText().trim();
    }


    public void setControlador(EstudianteController controlador) {
        this.controlador = controlador;
    }


    private void agregarFila(Object[] fila) {
        modeloTabla.addRow(fila);
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void setEstado(String texto) {
        lblEstado.setText(texto);
    }

    private void limpiarFormularioAgregar() {
        txtNuevoNombre.setText("");
        txtNuevaCarrera.setText("");
        txtNuevoPromedio.setText("");
        txtNuevoNombre.requestFocus();
    }
}