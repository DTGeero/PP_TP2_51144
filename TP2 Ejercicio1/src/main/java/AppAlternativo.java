import excepciones.CupoExcedidoException;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Sala;

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

        }
}
