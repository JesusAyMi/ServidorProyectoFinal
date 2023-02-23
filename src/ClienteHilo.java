import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteHilo extends Thread {

    private DataInputStream in;
    private DataOutputStream out;

    public ClienteHilo(DataInputStream in, DataOutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {

        Scanner entrada = new Scanner(System.in);

        String mensaje;
        int opcion = 0;
        boolean salir = false;

        while (!salir) {

            try {
                System.out.println("1. Nuevo Producto");
                System.out.println("2. Numero de Productos en Venta");
                System.out.println("3. Lista de Productos");
                System.out.println("4. Salir");
                System.out.println("Escoge una opcion: ");

                opcion = entrada.nextInt();
                out.writeInt(opcion);

                switch (opcion) {
                    case 1:
                        Scanner scProducto = new Scanner(System.in);
                        // CREO UN PRODUCTO
                        System.out.println("Nombre del producto: ");
                        String nombreProducto = scProducto.nextLine();
                        // ENVÍO AL SERVIDOR EL PRODUCTO
                        out.writeUTF(nombreProducto);
                        // RECIBO EL MENSAJE DEL SERVIDOR
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        break;
                    case 2:
                        int numLineas = in.readInt();
                        System.out.println("Hay " + numLineas + " productos");
                        break;
                    case 3:
                        // RECIBO DEL SERVIDOR EL NÚMERO DE PRODUCTOS
                        int limite = in.readInt();
                        // MUESTRO TODOS LOS PRODUCTOS
                        for (int i = 0; i < limite; i++) {
                            System.out.println(in.readUTF());
                        }
                        break;
                    case 4:
                        salir = true;
                        break;
                    default:
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(ClienteHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}