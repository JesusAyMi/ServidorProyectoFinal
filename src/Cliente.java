
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

    public static void main(String[] args) {

        try {
            Scanner entrada = new Scanner(System.in);
            entrada.useDelimiter("\n");

            Socket sc = new Socket("127.0.0.1", 5000);

            DataInputStream in = new DataInputStream(sc.getInputStream());
            DataOutputStream out = new DataOutputStream(sc.getOutputStream());

            // LEE EL MENSAJE DEL SERVIDOR
            String mensaje = in.readUTF();
            System.out.println(mensaje);

            // REGISTRA AL CLIENTE
            String nombre = entrada.next();
            out.writeUTF(nombre);

            // EJECUTA ClienteHilo
            ClienteHilo hilo = new ClienteHilo(in, out);
            hilo.start();
            hilo.join();

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}