package miniHttpServer;

import java.io.*;
import java.net.*;

public class HttpServer{
    public static void main(String[] args){
        HttpServer myHTTPServer = new HttpServer();
        int puertoServer, puertoController, conexiones;
        String ipController;
        try{
            if (args.length < 4){
                System.out.println("Introduzca los argumentos en el siguiente orden: puerto servidor, ip controlador, puerto controlador y número máximo de conexiones");
            }
            else {
                puertoServer = args[0];
                ipController = args[1];
                puertoController = args[2];
                conexiones = args[3];
            }
            catch (IOException | NumberFormatException ex) {
                System.err.println("Error en los argumentos, el orden es: puerto servidor, ip controlador, puerto controlado y número máximo de conexiones " + ex);
            }
        }
        
        HttpServer.crearHilos(puertoServer,ipController,puertoController,conexiones);
    }
    
    public void crearHilos(int pServer, String ipControl, int pControl, int conex){
        int conexionesActuales = 0;
        try {
            ServerSocket sockServidor = new ServerSocket(pServer);
            System.out.println("Escuchando por el puerto: " +pServer);
            for(;;){
                conexionesActuales++;
                Socket cliente = sockServidor.accept();
                Thread t = new HiloServidor(cliente,ipControl,pControl);
                t.start();
                conexionesActuales--;
            }
        }
        catch (Exception ex) {
            System.err.println("Error con la conexión: " + ex);
        }
    }
}
        