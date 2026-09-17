package com.miapp.controlador;

import com.miapp.modelo.Estudiante;
import com.miapp.vista.EstudianteView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class EstudianteController {

    private EstudianteView vista;
    private List<Estudiante> estudiantes;     
    private int siguienteId;                   

    private List<Estudiante> ultimosResultados; 
    private boolean ordenAscendente = true;      

    public EstudianteController(EstudianteView vista) {
        this.vista = vista;
        this.vista.setControlador(this);
        cargarDatos();
    }


    private void cargarDatos() {
        estudiantes = new ArrayList<>();
        estudiantes.add(new Estudiante(1,  "Ana García",        "Ingeniería de Sistemas",  4.5));
        estudiantes.add(new Estudiante(2,  "Carlos López",      "Ingeniería Civil",        3.8));
        estudiantes.add(new Estudiante(3,  "María Rodríguez",   "Medicina",                4.9));
        estudiantes.add(new Estudiante(4,  "José Martínez",     "Derecho",                 3.5));
        estudiantes.add(new Estudiante(5,  "Laura Sánchez",     "Administración",          4.1));
        estudiantes.add(new Estudiante(6,  "Andrés Torres",     "Ingeniería de Sistemas",  3.9));
        estudiantes.add(new Estudiante(7,  "Valentina Gómez",   "Psicología",              4.3));
        estudiantes.add(new Estudiante(8,  "Luis Herrera",      "Economía",                3.7));
        estudiantes.add(new Estudiante(9,  "Sofía Díaz",        "Ingeniería Civil",        4.6));
        estudiantes.add(new Estudiante(10, "Juliana Morales",   "Medicina",                4.8));
        estudiantes.add(new Estudiante(11, "Ana Milena Ruiz",   "Derecho",                 4.0));
        estudiantes.add(new Estudiante(12, "Carlos Andrés Paz", "Administración",          3.6));

        siguienteId = estudiantes.size() + 1;
    }


    public void buscarEstudiante(String criterio) {

        if (criterio == null || criterio.isEmpty()) {
            vista.mostrarError("Por favor ingrese un nombre para buscar.");
            return;
        }

        List<Estudiante> resultados = new ArrayList<>();
        String criterioBajo = criterio.toLowerCase();

        for (Estudiante e : estudiantes) {
            if (e.getNombre().toLowerCase().contains(criterioBajo)) {
                resultados.add(e);
            }
        }

        ultimosResultados = resultados;

        if (resultados.isEmpty()) {
            vista.mostrarEstudiantes(new ArrayList<>()); 
        } else if (resultados.size() == 1) {
            vista.mostrarEstudiante(convertirAFila(resultados.get(0)));
        } else {
            vista.mostrarEstudiantes(convertirAFilas(resultados));
        }
    }

    
    public void mostrarTodos() {
        ultimosResultados = new ArrayList<>(estudiantes);
        vista.mostrarEstudiantes(convertirAFilas(ultimosResultados));
    }

    
    public void ordenarPor(String criterio) {

        if (ultimosResultados == null || ultimosResultados.isEmpty()) {
            vista.mostrarError("No hay resultados para ordenar. Realice una busqueda primero.");
            return;
        }

        Comparator<Estudiante> comparador;

        if ("Promedio".equalsIgnoreCase(criterio)) {
            comparador = Comparator.comparingDouble(Estudiante::getPromedio);
        } else {
            comparador = Comparator.comparing(Estudiante::getNombre, String.CASE_INSENSITIVE_ORDER);
        }

        if (!ordenAscendente) {
            comparador = comparador.reversed();
        }

        ultimosResultados.sort(comparador);

        ordenAscendente = !ordenAscendente;

        vista.mostrarEstudiantes(convertirAFilas(ultimosResultados));
    }

    
    public void agregarEstudiante(String nombre, String carrera, double promedio) {

        if (nombre == null || nombre.trim().isEmpty()) {
            vista.mostrarError("El nombre no puede estar vacio.");
            return;
        }

        if (carrera == null || carrera.trim().isEmpty()) {
            vista.mostrarError("La carrera no puede estar vacia.");
            return;
        }

        if (promedio < 0.0 || promedio > 5.0) {
            vista.mostrarError("El promedio debe estar entre 0.0 y 5.0.");
            return;
        }

        Estudiante nuevo = new Estudiante(siguienteId, nombre.trim(), carrera.trim(), promedio);
        estudiantes.add(nuevo);
        siguienteId++;

        vista.mostrarConfirmacion("Estudiante \"" + nuevo.getNombre() + "\" agregado correctamente.");

        mostrarTodos();
    }


    private Object[] convertirAFila(Estudiante e) {
        return new Object[]{
            e.getId(),
            e.getNombre(),
            e.getCarrera(),
            String.format("%.2f", e.getPromedio())
        };
    }


    private List<Object[]> convertirAFilas(List<Estudiante> lista) {
        List<Object[]> filas = new ArrayList<>();
        for (Estudiante e : lista) {
            filas.add(convertirAFila(e));
        }
        return filas;
    }

}