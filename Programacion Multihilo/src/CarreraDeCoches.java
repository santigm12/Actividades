public class CarreraDeCoches {

    public static void main(String[] args) {
        Coche coche1 = new Coche("Coche Rojo");
        Coche coche2 = new Coche("Coche Azul");
        Coche coche3 = new Coche("Coche Verde");
        Coche coche4 = new Coche("Coche Amarillo");
        Coche coche5 = new Coche("Coche Negro");

        Thread hilo1 = new Thread(coche1);
        Thread hilo2 = new Thread(coche2);
        Thread hilo3 = new Thread(coche3);
        Thread hilo4 = new Thread(coche4);
        Thread hilo5 = new Thread(coche5);

        hilo1.start();
        hilo2.start();
        hilo3.start();
        hilo4.start();
        hilo5.start();

        try {
            hilo1.join();
            hilo2.join();
            hilo3.join();
            hilo4.join();
            hilo5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("La carrera ha terminado!");
    }
}