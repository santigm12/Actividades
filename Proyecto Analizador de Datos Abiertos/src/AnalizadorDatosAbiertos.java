import com.google.gson.JsonElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AnalizadorDatosAbiertos {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce la ruta del archivo a analizar:");
        String rutaArchivo = scanner.nextLine();
        if (rutaArchivo.endsWith(".csv")) {
            List<String[]> datosCSV = parsearCSV(rutaArchivo);
            mostrarResumenCSV(datosCSV);
        } else if (rutaArchivo.endsWith(".json")) {
            JsonObject datosJSON = parsearJSON(rutaArchivo);
            mostrarResumenJSON(datosJSON);
        } else if (rutaArchivo.endsWith(".xml")) {
            Document datosXML = parsearXML(rutaArchivo);
            mostrarResumenXML(datosXML);
        } else {
            System.out.println("Formato de archivo no soportado.");
        }
    }
    public static List<String[]> parsearCSV(String rutaArchivo) {
        List<String[]> registros = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new
                FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(",");
                registros.add(valores);
            }
        } catch (Exception e) {
            System.out.println("Error al leer el archivo CSV: " +
                    e.getMessage());
        }
        return registros;
    }
    public static JsonObject parsearJSON(String rutaArchivo) {
        JsonObject jsonObject = null;
        try (FileReader reader = new FileReader(rutaArchivo)) {
            jsonObject = new Gson().fromJson(reader, JsonObject.class);
        } catch (Exception e) {
            System.out.println("Error al leer el archivo JSON: " +
                    e.getMessage());
        }
        return jsonObject;
    }
    public static Document parsearXML(String rutaArchivo) {
        Document doc = null;
        try {
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(rutaArchivo);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            System.out.println("Error al leer el archivo XML: " +
                    e.getMessage());
        }
        return doc;
    }
    public static void mostrarResumenCSV(List<String[]> datos) {
        if (datos.isEmpty()) {
            System.out.println("No se encontraron datos.");
            return;
        }
        System.out.println("Resumen del archivo CSV:");
        System.out.println("Número total de filas: " + datos.size());
        System.out.println("Número de columnas: " + datos.get(0).length);
        System.out.println("\nPrimeros 5 registros:");
        for (int i = 0; i < Math.min(5, datos.size()); i++) {
            System.out.println(String.join(" | ", datos.get(i)));
        }
    }

    public static void mostrarResumenJSON(JsonObject datos) {
        if (datos == null || datos.entrySet().isEmpty()) {
            System.out.println("No se encontraron datos en el archivo JSON.");
            return;
        }

        System.out.println("Resumen del archivo JSON:");
        System.out.println("Número total de claves en el JSON: " + datos.size());

        System.out.println("\nPrimeras 5 claves y sus valores:");
        int contador = 0;
        for (Map.Entry<String, JsonElement> entrada : datos.entrySet()) {
            System.out.println("Clave: " + entrada.getKey() + ", Valor: " + entrada.getValue());
            contador++;
            if (contador == 5) break;
        }
    }

    public static void mostrarResumenXML(Document datos) {
        if (datos == null) {
            System.out.println("No se encontraron datos en el archivo XML.");
            return;
        }

        System.out.println("Resumen del archivo XML:");
        Element elementoRaiz = datos.getDocumentElement();
        System.out.println("Elemento raíz: " + elementoRaiz.getNodeName());

        NodeList nodos = elementoRaiz.getChildNodes();
        System.out.println("Número de nodos hijos: " + nodos.getLength());

        System.out.println("\nPrimeros 5 nodos hijos:");
        int contador = 0;
        for (int i = 0; i < nodos.getLength(); i++) {
            Node nodo = nodos.item(i);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                System.out.println("Nodo: " + nodo.getNodeName() + ", Valor: " + nodo.getTextContent());
                contador++;
                if (contador == 5) break;
            }
        }
    }
}