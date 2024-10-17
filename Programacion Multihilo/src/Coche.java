import java.util.Random;

public class Coche implements Runnable {
    private String nombre;
    private int distanciaRecorrida;
    private static final int DISTANCIA_META = 100;
    private static final Random random = new Random();

    public Coche(String nombre) {
        this.nombre = nombre;
        this.distanciaRecorrida = 0;
    }

    @Override
    public void run() {
        while (distanciaRecorrida < DISTANCIA_META) {
            int incremento = (int) (Math.random() * 10) + 1;
            if (chocaConObstaculo()) {
                System.out.println(nombre + " ha chocado contra un obstáculo y se ha ralentizado.");
                incremento /= 2; // Se reduce la velocidad a la mitad
            }
            distanciaRecorrida += incremento;
            imprimirProgreso();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(nombre + " ha terminado la carrera!");
    }

    private boolean chocaConObstaculo() {
        // Probabilidad de 20% de chocar con un obstáculo
        return random.nextInt(100) < 20;
    }

    private void imprimirProgreso() {
        StringBuilder progreso = new StringBuilder("|");
        for (int i = 0; i < distanciaRecorrida; i += 2) {
            progreso.append("-");
        }
        progreso.append(">");
        for (int i = distanciaRecorrida; i < DISTANCIA_META; i += 2) {
            progreso.append(" ");
        }
        progreso.append("|");
        System.out.println(nombre + " " + progreso.toString());
    }

    public String getNombre() {
        return nombre;
    }

    public int getDistanciaRecorrida() {
        return distanciaRecorrida;
    }
}