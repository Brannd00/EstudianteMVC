package com.miapp.controlador;

import com.miapp.modelo.Curso;
import com.miapp.modelo.Estudiante;
import com.miapp.modelo.Profesor;
import com.miapp.servicios.IBuscador;
import com.miapp.utilidades.EstadoMatricula;
import com.miapp.vista.EstudianteView;
 
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
 
 
public class EstudianteController implements IBuscador {
 
    // ── Constantes finales ────────────────────────────────────────────────────
    private static final String MENSAJE_BUSQUEDA_VACIA = "Por favor ingrese un nombre para buscar.";
    private static final String MENSAJE_BUSQUEDA_CARRERA_VACIA = "Por favor seleccione una carrera para buscar.";
 
    // ── Vista ─────────────────────────────────────────────────────────────────
    private EstudianteView vista;
 
    // ── Fuentes de datos ─────────────────────────────────────────────────────
    private List<Estudiante> estudiantes;
    private List<Curso> cursos = new ArrayList<>();
    private List<Profesor> profesores = new ArrayList<>();
 
    // ── Estado de la última búsqueda (para ordenar/listar todo) ────────────────
    private List<Estudiante> ultimosResultados;
    private boolean ordenAscendente = true;
 
    // ── Constructor ───────────────────────────────────────────────────────────
 
    public EstudianteController(EstudianteView vista) {
        this.vista = vista;
        // IMPORTANTE: primero se cargan los datos y luego se enlaza la vista,
        // porque setControlador() llena los combos de carreras/cursos y necesita
        // que estudiantes/cursos ya existan.
        cargarDatos();
        this.vista.setControlador(this);
    }
 
    // ── Implementación de la interfaz IBuscador ───────────────────────────────
 
    @Override
    public void cargarDatos() {
        inicializarEstudiantes();
        inicializarCursos();
    }
 
    @Override
    public void buscarEstudiante(String criterio) {
        buscarPorCriterio(criterio);
    }
 
    @Override
    public void buscarEstudiantePorCarrera(String carrera) {
        buscarPorCarrera(carrera);
    }
 
    // ── Carga de datos iniciales ──────────────────────────────────────────────
 
    private void inicializarEstudiantes() {
        estudiantes = new ArrayList<>();
        Estudiante.reiniciarContador();
 
        estudiantes.add(new Estudiante(1,  "Ana",           "García",    "Ingeniería de Sistemas", 4.5));
        estudiantes.add(new Estudiante(2,  "Carlos",        "López",     "Ingeniería Civil",       3.8));
        estudiantes.add(new Estudiante(3,  "María",         "Rodríguez", "Medicina",               4.9));
        estudiantes.add(new Estudiante(4,  "José",          "Martínez",  "Derecho",                3.5));
        estudiantes.add(new Estudiante(5,  "Laura",         "Sánchez",   "Administración",         4.1));
        estudiantes.add(new Estudiante(6,  "Andrés",        "Torres",    "Ingeniería de Sistemas", 3.9));
        estudiantes.add(new Estudiante(7,  "Valentina",     "Gómez",     "Psicología",             4.3));
        estudiantes.add(new Estudiante(8,  "Luis",          "Herrera",   "Economía",               3.7));
        estudiantes.add(new Estudiante(9,  "Sofía",         "Díaz",      "Ingeniería Civil",       4.6));
        estudiantes.add(new Estudiante(10, "Juliana",       "Morales",   "Medicina",               4.8));
        estudiantes.add(new Estudiante(11, "Ana Milena",    "Ruiz",      "Derecho",                4.0));
        estudiantes.add(new Estudiante(12, "Carlos Andrés", "Paz",       "Administración",         3.6));
 
        System.out.println("Total de estudiantes cargados: " + estudiantes.size());
    }
 
    private void inicializarCursos() {
        cursos.add(new Curso("SIS101", "Programación I", 4));
        cursos.add(new Curso("BDA150", "Bases de Datos", 3));
        cursos.add(new Curso("CIV200", "Estructuras", 4));
        cursos.add(new Curso("MED300", "Anatomía", 5));
    }
 
    // ── Lógica de búsqueda por nombre/apellido ────────────────────────────────
 
    private void buscarPorCriterio(String criterio) {
        if (criterio == null || criterio.isEmpty()) {
            vista.mostrarError(MENSAJE_BUSQUEDA_VACIA);
            return;
        }
 
        List<Estudiante> resultados = new ArrayList<>();
        String criterioBajo = criterio.toLowerCase();
 
        for (Estudiante e : estudiantes) {
            if (e.getNombre().toLowerCase().contains(criterioBajo) ||
                e.getApellido().toLowerCase().contains(criterioBajo)) {
                resultados.add(e);
            }
        }
 
        ultimosResultados = resultados;
        mostrarResultados(resultados);
    }
 
    private void buscarPorCarrera(String carrera) {
        if (carrera == null || carrera.isEmpty() || carrera.equals("Seleccionar...")) {
            vista.mostrarError(MENSAJE_BUSQUEDA_CARRERA_VACIA);
            return;
        }
 
        List<Estudiante> resultados = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            if (e.getCarrera().equalsIgnoreCase(carrera)) {
                resultados.add(e);
            }
        }
 
        ultimosResultados = resultados;
        vista.mostrarEstudiantes(convertirAFilas(resultados));
    }
 
    private void mostrarResultados(List<Estudiante> resultados) {
        if (resultados.isEmpty()) {
            vista.mostrarEstudiantes(new ArrayList<>());
        } else if (resultados.size() == 1) {
            vista.mostrarEstudiante(convertirAFila(resultados.get(0)));
        } else {
            vista.mostrarEstudiantes(convertirAFilas(resultados));
        }
    }
 
    /**
     * Muestra todos los estudiantes registrados (sin filtro).
     */
    public void mostrarTodos() {
        ultimosResultados = new ArrayList<>(estudiantes);
        vista.mostrarEstudiantes(convertirAFilas(ultimosResultados));
    }
 
    /**
     * Ordena los resultados de la última búsqueda/listado por "Nombre" o "Promedio",
     * alternando entre ascendente y descendente en cada llamada.
     */
    public void ordenarPor(String criterio) {
        if (ultimosResultados == null || ultimosResultados.isEmpty()) {
            vista.mostrarError("No hay resultados para ordenar. Realice una búsqueda primero.");
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
 
    // ── Estado de matrícula ────────────────────────────────────────────────────
 
    public void buscarPorEstado(EstadoMatricula estado) {
        if (estado == null) {
            vista.mostrarError("Seleccione un estado válido.");
            return;
        }
        List<Estudiante> resultados = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            if (e.getEstado() == estado) {
                resultados.add(e);
            }
        }
        ultimosResultados = resultados;
        vista.mostrarEstudiantes(convertirAFilas(resultados));
    }
 
    public boolean cambiarEstado(Estudiante estudiante, EstadoMatricula nuevoEstado) {
        if (estudiante == null) {
            vista.mostrarError("Seleccione un estudiante en la tabla primero.");
            return false;
        }
        if (nuevoEstado == null) {
            vista.mostrarError("Seleccione un estado válido.");
            return false;
        }
        estudiante.setEstado(nuevoEstado);
        vista.mostrarMensaje("Estado actualizado a " + nuevoEstado + " para " + estudiante.getNombre());
        return true;
    }
 
    // ── Cursos: inscripción y consulta ─────────────────────────────────────────
 
    public String[] obtenerCodigosCursos() {
        List<String> codigos = new ArrayList<>();
        for (Curso c : cursos) {
            codigos.add(c.getCodigo());
        }
        return codigos.toArray(new String[0]);
    }
 
    public Curso obtenerCursoPorCodigo(String codigo) {
        for (Curso c : cursos) {
            if (c.getCodigo().equalsIgnoreCase(codigo)) {
                return c;
            }
        }
        return null;
    }
 
    public void verEstudiantesDelCurso(String codigoCurso) {
        Curso curso = obtenerCursoPorCodigo(codigoCurso);
        if (curso == null) {
            vista.mostrarError("Curso no encontrado.");
            return;
        }
        vista.mostrarEstudiantes(convertirAFilas(curso.getEstudiantes()));
 
        String nombreProfesor = (curso.getProfesor() != null)
                ? curso.getProfesor().getNombre() + " " + curso.getProfesor().getApellido()
                : "(ninguno)";
        vista.actualizarProfesorAsignado(nombreProfesor);
    }
 
    public boolean inscribirEnCurso(Estudiante estudiante, String codigoCurso) {
        if (estudiante == null) {
            vista.mostrarError("Seleccione un estudiante en la tabla primero.");
            return false;
        }
        Curso curso = obtenerCursoPorCodigo(codigoCurso);
        if (curso == null) {
            vista.mostrarError("Curso no encontrado.");
            return false;
        }
 
        boolean exito = estudiante.inscribir(curso);
        if (exito) {
            vista.mostrarMensaje(estudiante.getNombre() + " fue inscrito en " + curso.getCodigo());
        } else {
            vista.mostrarError("No se pudo inscribir (cupo máximo de materias alcanzado o ya estaba inscrito).");
        }
        return exito;
    }
 
    // ── Profesores: agregar y asignar a curso ───────────────────────────────────
 
    public boolean agregarProfesor(String nombreCompleto, double salarioBase) {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            vista.mostrarError("Ingrese el nombre del profesor.");
            return false;
        }
        String[] partes = nombreCompleto.trim().split(" ", 2);
        String nombre = partes[0];
        String apellido = partes.length > 1 ? partes[1] : "";
        int id = profesores.size() + 1;
 
        profesores.add(new Profesor(id, nombre, apellido, salarioBase));
        vista.mostrarMensaje("Profesor agregado correctamente.");
        vista.actualizarListaProfesores(obtenerNombresProfesores());
        return true;
    }
 
    public String[] obtenerNombresProfesores() {
        List<String> nombres = new ArrayList<>();
        for (Profesor p : profesores) {
            nombres.add(p.getNombre() + " " + p.getApellido());
        }
        return nombres.toArray(new String[0]);
    }
 
    public Profesor obtenerProfesorPorNombreCompleto(String nombreCompleto) {
        for (Profesor p : profesores) {
            if ((p.getNombre() + " " + p.getApellido()).equalsIgnoreCase(nombreCompleto)) {
                return p;
            }
        }
        return null;
    }
 
    public void verCursosDelProfesor(String nombreCompleto) {
        Profesor profesor = obtenerProfesorPorNombreCompleto(nombreCompleto);
        if (profesor == null) {
            vista.mostrarError("Profesor no encontrado.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Curso c : profesor.getCursos()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(c.getCodigo());
        }
        vista.mostrarMensaje("Cursos de " + nombreCompleto + ": " +
                (sb.length() == 0 ? "(ninguno)" : sb.toString()));
    }
 
    public boolean asignarProfesorACurso(String nombreCompleto, String codigoCurso) {
        Profesor profesor = obtenerProfesorPorNombreCompleto(nombreCompleto);
        Curso curso = obtenerCursoPorCodigo(codigoCurso);
        if (profesor == null || curso == null) {
            vista.mostrarError("Seleccione un profesor y un curso válidos.");
            return false;
        }
 
        boolean exito = profesor.asignarCurso(curso);
        if (exito) {
            vista.mostrarMensaje(profesor.getNombre() + " fue asignado a " + curso.getCodigo());
        } else {
            vista.mostrarError("El profesor ya estaba asignado a ese curso.");
        }
        return exito;
    }
 
    // ── Conversión a filas de tabla ──────────────────────────────────────────
 
    private Object[] convertirAFila(Estudiante e) {
        return new Object[]{
            e.getId(),
            e.getNombre(),
            e.getApellido(),
            e.getCarrera(),
            String.format("%.2f", e.getPromedio()),
            e.getEstado()
        };
    }
 
    private List<Object[]> convertirAFilas(List<Estudiante> lista) {
        List<Object[]> filas = new ArrayList<>();
        for (Estudiante e : lista) {
            filas.add(convertirAFila(e));
        }
        return filas;
    }
 
    // ── Utilitarios generales ─────────────────────────────────────────────────
 
    public Estudiante obtenerEstudiantePorId(int id) {
        for (Estudiante e : estudiantes) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }
 
    public String[] obtenerCarrerasUnicas() {
        List<String> carreras = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            String carrera = e.getCarrera();
            if (!carreras.contains(carrera)) {
                carreras.add(carrera);
            }
        }
        return carreras.toArray(new String[0]);
    }
 
    public final int obtenerTotalEstudiantes() {
        return estudiantes.size();
    }
 
    /**
     * Valida cada campo por separado (mensajes específicos) y agrega el estudiante.
     * Al finalizar, refresca la tabla mostrando el listado completo actualizado.
     */
    public boolean agregarEstudiante(String nombre, String apellido, String carrera, double promedio) {
        if (nombre == null || nombre.trim().isEmpty()) {
            vista.mostrarError("El nombre no puede estar vacío.");
            return false;
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            vista.mostrarError("El apellido no puede estar vacío.");
            return false;
        }
        if (carrera == null || carrera.trim().isEmpty() || carrera.equals("Seleccionar...")) {
            vista.mostrarError("La carrera no puede estar vacía.");
            return false;
        }
        if (promedio < Estudiante.PROMEDIO_MINIMO || promedio > Estudiante.PROMEDIO_MAXIMO) {
            vista.mostrarError("El promedio debe estar entre " + Estudiante.PROMEDIO_MINIMO +
                                " y " + Estudiante.PROMEDIO_MAXIMO + ".");
            return false;
        }
 
        int proximoId = Estudiante.getProximoId();
        Estudiante nuevo = new Estudiante(proximoId, nombre.trim(), apellido.trim(), carrera.trim(), promedio);
        estudiantes.add(nuevo);
 
        vista.mostrarMensaje("Estudiante \"" + nuevo.getNombre() + " " + nuevo.getApellido() +
                            "\" agregado correctamente.\nTotal de estudiantes: " + estudiantes.size());
 
        mostrarTodos();
        return true;
    }
}