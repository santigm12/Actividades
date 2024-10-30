import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class Curso {

    @XmlElement
    private String nombreCurso;
    @XmlElement
    private List<Estudiante> estudiantes;

    public Curso() {}

    public Curso(String nombreCurso, List<Estudiante> estudiantes) {
        this.nombreCurso = nombreCurso;
        this.estudiantes = estudiantes;
    }

    // Getters y Setters
    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    @Override
    public String toString() {
        return "Curso{nombreCurso='" + nombreCurso + "', estudiantes=" + estudiantes + "}";
    }
}

