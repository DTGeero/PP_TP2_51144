# TP2 - Sistema de Eventos Universitarios

Trabajo práctico de Paradigmas de Programación (UTN FRM), continuación del sistema de eventos de talleres, charlas y cursos armado antes. Ahora se le suma manejo de excepciones propias, persistencia de objetos por serialización, emisión de certificados mediante interfaces, y filtrado/cálculo de costos con métodos genéricos y wildcards.

Se armó en 3 ejercicios, cada uno agregando algo nuevo sobre lo anterior: primero excepciones y persistencia, después certificados con interfaces, después filtrado y cálculo de costos con genéricos acotados y wildcards.

## Qué clases hay

- **EventoUniversitario**: el evento en sí. Contiene id, título, costo base, si es gratis o no, una sala asignada y las actividades creadas. También se encarga de persistir/recuperar eventos y de filtrar y calcular costos sobre sus actividades.
- **Sala**: lugar donde se realiza el evento. Existe aparte del evento (agregación).
- **Actividad**: abstracta, no se puede instanciar directo. Tiene lo común a toda actividad (id, título, cupo máximo, cupo mínimo, inscripciones).
- **Charla**: hereda de Actividad. No es certificable.
- **Taller**: hereda de Actividad e implementa Certificable.
- **Curso**: hereda de Actividad e implementa Certificable. Es el tipo de actividad nuevo que se agregó en este TP.
- **Estudiante**: legajo y nombre.
- **Inscripcion**: qué estudiante se anotó en qué actividad, fecha y estado.
- **CupoExcedidoException**: excepción propia (paquete `excepciones`), se lanza cuando una actividad ya no tiene cupo disponible.
- **Certificable** (interface, paquete `certificacion`): define la constante `ENTIDAD_EMISORA` y el método `generarCertificado(Estudiante)`. La implementan Taller y Curso.
- **App**: clase principal que arma todo, prueba los flujos con try-catch-finally y muestra los resultados por consola.

## Cómo se organiza en paquetes

- `modelo`: EventoUniversitario y Sala.
- `actividades`: Actividad y sus subtipos (Charla, Taller, Curso), Estudiante e Inscripcion.
- `excepciones`: CupoExcedidoException.
- `certificacion`: la interface Certificable.

## Cómo se relacionan

- El evento **agrega** una sala (la sala existe sola, independiente del evento).
- El evento **compone** sus actividades (si el evento no existe, tampoco tienen sentido sus actividades sueltas).
- Charla, Taller y Curso **heredan** de Actividad, y cada una implementa a su manera `calcularCostoMateriales()` y `getTipo()` — polimorfismo puro.
- Taller y Curso además **implementan** la interface Certificable; Charla no, porque las charlas no emiten certificado.
- Cada Actividad tiene una lista de Inscripcion, y cada Inscripcion sabe a qué estudiante corresponde.
- `inscribir(Estudiante)` puede lanzar `CupoExcedidoException` cuando ya no hay cupo; queda declarada con `throws` y se atrapa en App.

## Manejo de excepciones

- Al inscribir un estudiante en una actividad sin cupo, se lanza `CupoExcedidoException` (excepción chequeada, propia). App la atrapa y muestra un mensaje claro por consola.
- La persistencia de objetos (guardar/leer el evento) maneja sus excepciones de forma granular: cada tipo de error de E/S o de lectura del archivo se atrapa por separado, con su propio mensaje.
- En App se armó un flujo con `try-catch-finally` que intenta, en orden: inscribir un estudiante, persistir el evento y después leerlo de nuevo. El `finally` se usa para dejar constancia de que el flujo terminó, se haya logrado todo o no.
- Se probaron a propósito un caso exitoso (inscripción y persistencia OK) y un caso fallido controlado (por ejemplo, intentar inscribir sin cupo), para que se vea el manejo de excepciones funcionando en los dos sentidos.

## Persistencia de eventos

- `persistirEvento()` guarda el EventoUniversitario armado (con su sala y sus actividades) serializado en un archivo.
- `recuperarEvento(String id)` es estático y lee ese archivo para reconstruir el objeto.
- Todo el árbol de objetos (Sala, Actividades, Inscripciones, Estudiantes) implementa Serializable para que la serialización funcione completa, no solo el evento.

## Certificados

- La interface Certificable define qué significa que una actividad pueda emitir certificado: una entidad emisora fija y el método `generarCertificado(Estudiante)`.
- Solo Taller y Curso la implementan. Charla queda afuera a propósito, porque las charlas no son certificables.
- Los certificados se emiten únicamente para los estudiantes que efectivamente se inscribieron en talleres y/o cursos, y después se muestran todos los emitidos por consola.

## Filtrado y costos con genéricos

Se agregaron dos métodos a EventoUniversitario usando tipos parametrizados y wildcards, para no tener que hacer casteos ni `instanceof` a mano:

- `public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo)`: filtra las actividades del evento por un tipo concreto (Charla, Taller o Curso) y devuelve la lista ya tipada — por ejemplo `List<Taller>` en vez de `List<Actividad>`.
- `public double calcularCostoMateriales(List<? extends Actividad> actividades)`: recibe cualquier lista de actividades (sea `List<Actividad>` o de un subtipo puntual) y suma el costo de materiales de cada una, apoyándose en el polimorfismo de `calcularCostoMateriales()`.

Con esto se puede filtrar las actividades de un evento por tipo, contar cuántas hay de cada una, y calcular el costo de materiales de cada grupo por separado.

## Cómo se calcula el costo

- Si el evento es gratis, el costo total es $0.
- Si no, es `(costo_base + lo que cuesten las actividades) * 1.21` (el 21% es el IVA).
- Las charlas no tienen costo de materiales.
- Los talleres cuestan $5000 si hace falta notebook, o $2000 si no.
- Los cursos calculan su costo de materiales según su nivel.
- Si una actividad ya no tiene cupo, no te deja inscribir más gente: lanza `CupoExcedidoException` y te avisa por consola.
