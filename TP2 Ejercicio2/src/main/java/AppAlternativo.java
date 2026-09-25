import excepciones.CupoExcedidoException;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.Sala;
import modelo.actividades.Actividad;
import modelo.certificacion.Certificable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class AppAlternativo {
        public static void main(String[] args) {

            List<Estudiante> estudiantes = new ArrayList<>();

            estudiantes.add(new Estudiante("51144", "Gerónimo Vera"));
            estudiantes.add(new Estudiante("80098", "Carlos Rojas"));
            estudiantes.add(new Estudiante("76661", "Roberto Ayala"));

            EventoUniversitario evento = new EventoUniversitario("001", "Encuentro de POO",2000,true);

            Sala sala = new Sala(1, "Salón Principal");

            evento.asignarSala(sala);

            evento.crearActividad(1, "modelo.actividades.Taller de POO",2,"taller" );
            evento.crearActividad(2, "modelo.actividades.Charla de PL",30,"charla" );

            try {
                evento.getActividades().get(0).inscribir(estudiantes.get(0));
                evento.getActividades().get(0).inscribir(estudiantes.get(1));
                evento.getActividades().get(0).inscribir(estudiantes.get(2));

                evento.getActividades().get(1).inscribir(estudiantes.get(1));
                evento.getActividades().get(1).inscribir(estudiantes.get(2));
            } catch (CupoExcedidoException e) {
                    System.out.println("Error al inscribir: " + e.getMessage());
            }

            evento.mostrarDatos();

            try { //bloque de intento para persistir el evento.
                System.out.println("*** Almacenando el evento Id° " + evento.getId()  + " ***");
                evento.persistirEvento();
            }
            catch (FileNotFoundException e)
            {
                System.out.println("Imposible guardar el evento id°  " +  evento.getId() +". Error al guardar el archivo: "+ e.getMessage());
            }
            catch (IOException e)
            {
                System.out.println("Imposible guardar el evento id°  " + evento.getId()  + ".");
                e.printStackTrace();
            }

            try {
                EventoUniversitario copiaDesdeArchivo = evento.recuperarEvento(evento.getId());
                System.out.println("*** Mostrando el evento Id° " + evento.getId()  + " almacenado previamente. ***");
                copiaDesdeArchivo.mostrarDatos();
            }
            catch (ClassNotFoundException e)
            {
                System.out.println("No fue posible reconstruir el objeto almacenado: " + e.getMessage());
            }
            catch (FileNotFoundException e)
            {
                System.out.println("Imposible recuperar el evento id°  " + evento.getId() +". Error al guardar el archivo: "+ e.getMessage());
            }
            catch (IOException e)
            {
                System.out.println("Imposible recuperar el evento id°  " + evento.getId() + ".");
                e.printStackTrace();
            }

            /* Se emiten los Certificados que correspondan para los estudiantes inscriptos en las actividades Certificables */
            for (Actividad actividad: evento.getActividades()){
                if (actividad instanceof Certificable certificable){
                    System.out.println("CERTIFICADOS EMITIDOS PARA LA ACTIVIDAD " + actividad.getTitulo());
                    for (Inscripcion inscripcion: actividad.getInscripciones()){
                        String certificado = certificable.generarCertificado(inscripcion.getEstudiante());
                        System.out.println(certificado);
                    }
                }
            }
        }


}
