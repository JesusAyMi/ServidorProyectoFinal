import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorHilo extends Thread {

    private Socket sc;
    private DataInputStream in;
    private DataOutputStream out;
    private String nombreCliente;
    private ArrayList<String> listaProductos;

    public ServidorHilo(Socket sc, DataInputStream in, DataOutputStream out, String nombreCliente) {
        this.sc = sc;
        this.in = in;
        this.out = out;
        this.nombreCliente = nombreCliente;
        this.listaProductos = new ArrayList<String>();
    }

    @Override
    public void run() {

        int opcion;
        File f = new File("Productos.txt");
        boolean salir = false;

        while (!salir) {

            try {
                opcion = in.readInt();
                switch (opcion) {
                    case 1:
                        // RECIBO EL PRODUCTO
                        String nombreProducto = in.readUTF();
                        // ESCRIBO EL PRODUCTO
                        escribirProducto(f, nombreProducto);
                        System.out.println("El cliente " + nombreCliente + " ha creado un producto.");
                        out.writeUTF("Producto guardado correctamente");
                        break;
                    case 2:
                        // CUENTO EL NÚMERO DE LÍNEAS
                        int numLineas = contarNumLineas(f);
                        // ENVÍO EL RESULTADO AL CLIENTE
                        out.writeInt(numLineas);
                        break;
                    case 3:
                        // CUENTO EL NÚMERO DE PRODUCTOS
                        ArrayList<String> productos = listaNumeros(f);

                        // ENVÍO EL NÚMERO DE PRODUCTOS AL CLIENTE
                        out.writeInt(productos.size());

                        // ENVÍO LOS PRODUCTOS AL CLIENTE
                        for (String n:productos) {
                            out.writeUTF(n);
                        }
                        break;
                    case 4:
                        salir = true;
                        break;
                    default:
                        out.writeUTF("Solo numeros del 1 al 4");
                }
            } catch (IOException ex) {
                Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            sc.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("El cliente " + nombreCliente + " se ha desconectado");
    }

    public void escribirProducto(File f, String nombreProducto) throws IOException{

        FileWriter fw = new FileWriter(f, true);
        // Por ejemplo, fer:52
        fw.write(nombreCliente + " --> "+nombreProducto + "\r\n");
        fw.close();

    }

    public int contarNumLineas(File f) throws FileNotFoundException, IOException{

        int numLineas = 0;
        BufferedReader br = new BufferedReader(new FileReader(f));

        String linea = "";

        while( (linea = br.readLine()) != null){
            numLineas++;
        }

        br.close();
        return numLineas;
    }

    public ArrayList<String> listaNumeros(File f) throws FileNotFoundException, IOException {

        ArrayList<String> productos = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(f));
        String linea = "";

        // BUCLE QUE LEE EL ARCHIVO PRODUCTOS.TXT Y AÑADE SUS LÍNEAS COMO PRODUCTOS
        while( (linea = br.readLine()) != null){
            String producto = linea;
            // AÑADE EL PRODUCTO AL ARRAYLIST
            productos.add(producto);
        }
        br.close();
        return productos;
    }
}