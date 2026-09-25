package modelo;

import modelo.actividades.Actividad;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Clase asociativa entre modelo.actividades.Actividad y Estudiantes.
 * La inscripción existe porque un estudiante se inscribe a una actividad concreta.
 * Debe notarse que esta relación tiene atributos propios que no pertenecen ni a la actividad ni al estudiante,
 * por eso es necesario modelarla como una clase independiente.
 */
public class Inscripcion implements Serializable {
    private Actividad actividad;
    private Estudiante estudiante;
    private LocalDate fecha;
    private String estado;

    public Inscripcion(Actividad actividad, Estudiante estudiante, LocalDate fecha, String estado) {
        this.actividad = actividad;
        this.estudiante = estudiante;
        this.fecha = fecha;
        this.estado = estado;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void confirmar() {
        this.estado = "CONFIRMADA";
    }
}
