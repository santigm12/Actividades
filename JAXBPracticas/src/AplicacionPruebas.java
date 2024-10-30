import java.io.File;
import java.util.Arrays;

public class AplicacionPruebas {

    public static void main(String[] args) {
        try {
            // Crear un curso con estudiantes
            Estudiante estudiante1 = new Estudiante("Juan", 20);
            Estudiante estudiante2 = new Estudiante("María", 22);
            Curso curso = new Curso("Programación Avanzada", Arrays.asList(estudiante1, estudiante2));

            // Archivo XML donde se guardará el curso
            File file = new File("curso.xml");

            // Serializar el curso a XML
            JAXBUtil.marshal(curso, file);
            System.out.println("Curso serializado a XML en: " + file.getAbsolutePath());

            // Deserializar el archivo XML a un objeto Curso
            Curso cursoRecuperado = JAXBUtil.unmarshal(file);
            System.out.println("Curso deserializado desde XML: " + cursoRecuperado);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
