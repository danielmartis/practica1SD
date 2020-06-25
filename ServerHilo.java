package miniHttpServer;

import java.net.*;
import java.io.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alejandro
 */
public class ServerHilo extends Thread {

    private Socket cliente;
    private String ipController;
    private int puertoHilo;
    private Socket controlador;

    public ServerHilo(Socket sk, String ipController, int puertoHilo) {
        cliente = sk;
        this.ipController = ipController;
        this.puertoHilo = puertoHilo;
        /////
        //HAY QUE CREAR EL CLIENTE DEL CONTROLLER CON EL PUERTOHILO
        /////
    }

    /**
     * Lee desde el socket los datos que le pide el cliente y los devuelve
     *
     * @param sk
     * @param datos
     * @return
     */
    public String readSocket(Socket sk, String datos) {
        try {
            InputStream aux = sk.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(aux));;
            datos = new String();
            while (in.ready()) {
                datos = datos.concat(in.readLine() + "\n");
                //  System.out.println(datos); //Para comprobar que es lo que lee
            }
        } catch (Exception ex) {
            // System.err.println("No se ha podido leer el socket: " + ex);
        }

        return datos;
    }

    /**
     * Escribe en el socket que conecta al navegador los datos pasados por
     * string
     *
     * @param sk
     * @param datos
     */
    public void writeSocket(Socket sk, String datos) {
        try {
            PrintWriter out = new PrintWriter(sk.getOutputStream());
            BufferedReader br1;
            String data = "";
            String abrir = "./";
            if (datos.equals("/") || datos.equals("index.html")) {
                abrir = abrir.concat("index.html");
            }

            if (datos.contains("error")) {
                abrir = "./error.html";
            }
            if (abrir.equals("./index.html")) {
                //System.out.println(abrir);//Muestra el archivo que se abre en el fichero
                br1 = new BufferedReader(new FileReader(abrir));
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html; charset=utf-8");
                out.println("Server: MyHTTPServer");
                // este linea en blanco marca el final de los headers de la response
                out.println("");
                data = br1.readLine();
                while (data != null) {
                    out.println(data);
                    data = br1.readLine();
                    //System.out.println(data);
                    //Enviar a cliente
                }
            } else {
                out.println("HTTP/1.1 404 Not Found");
                out.println("Content-Type: text/html; charset=utf-8");
                out.println("Server: MyHTTPServer");
                out.println("");
                br1 = new BufferedReader(new FileReader("404.html"));
                data = br1.readLine();
                while (data != null){
                    out.println(data);
                    data = br1.readLine();
                }
            }
                

            out.flush();
            out.close();//debe ponerse

        } catch (Exception ex) {
            System.err.println("No se ha podido escribir en el socket: " + ex);
        }

    }

    /**
     * Escribe en el socket que conecta con el controlador los datos pasados por
     * string
     *
     * @param sk
     * @param datos
     */
    public void escribeSocket(Socket sk, String datos) {
        try {
            OutputStream aux = sk.getOutputStream();
            DataOutputStream flujo = new DataOutputStream(aux);
            flujo.writeUTF(datos);
        } catch (Exception e) {
            try{
                BufferedReader br1 = new BufferedReader(new FileReader("409.html"));
                PrintWriter out = new PrintWriter(cliente.getOutputStream());
                String data = "";
                System.out.println("No se ha podido conectar con el controlador: " + e.toString());
                out.println("HTTP/1.1 404 Not Found");
                out.println("Content-Type: text/html; charset=utf-8");
                out.println("Server: MyHTTPServer");
                out.println("");
                br1 = new BufferedReader(new FileReader("409.html"));
                data = br1.readLine();
                while (data != null){
                    out.println(data);
                    data = br1.readLine();
                }
                out.flush();
                out.close();
            }catch (Exception ex){
                System.err.println("Archivo no encontrado : " + e);
            }
        }
    }
        
        

    @Override
    public void run() {
        String cadena;
        String respuesta = "";
        try {
            do {
                cadena = "";
                cadena = readSocket(cliente, cadena);
                // Se escribe en pantalla la informacion recibida del cliente
                if (!cadena.isEmpty()) {
                    StringTokenizer s = new StringTokenizer(cadena);
                    cadena = s.nextToken();
                    if (cadena.equals("GET")) {
                        cadena = s.nextToken();
                        String[] aux = cadena.split("/");
                        if (aux.length > 0) {
                            cadena = aux[1];
                        }
                        System.out.println(aux.length);
                        if (cadena.equals("controladorSD") && aux.length > 2) {
                            //Creamos el socket para conectarnos al controlador
                            try {
                                controlador = new Socket(ipController, puertoHilo);
                            } catch (Exception ex) {
                                System.err.println("No se ha podido conectar con el controlador: " + ex);
                            }
                            //peticion RMI a Controller
                            respuesta = aux[2];
                            //En el caso de devolver error, algún parámetro no se habrá introducido bien
                            System.out.println(respuesta);
                            escribeSocket(controlador, respuesta);
                            respuesta = "";
                            while (respuesta.isEmpty()) {
                                respuesta = readSocket(controlador, respuesta);
                            }
                            //Para asegurarse de que respuesta recibe algo que se pueda copiar
                            cadena = respuesta;

                            //                         respuesta = "error";
                        }
                        else if (cadena.equals("controladorSD")){
                            try {
                                controlador = new Socket(ipController, puertoHilo);
                            }catch (Exception ex){
                                System.err.println("No se ha podido conectar con el controlador: " + ex);
                            }
                            respuesta = "";
                            escribeSocket(controlador, respuesta);
                            while(respuesta.isEmpty()){
                                respuesta = readSocket(controlador,respuesta);
                            }
                            cadena = respuesta;
                        }
                        //System.out.println(cadena); //Muestra el nombre del fichero solicitado por el explorador
                        else if (cadena.equals("favicon.ico")) {
                            break;
                        }

                        writeSocket(cliente, cadena);
                    }
                    else {
                        try{
                            BufferedReader br1 = new BufferedReader(new FileReader("405.html"));
                            PrintWriter out = new PrintWriter(cliente.getOutputStream());
                            String data = "";
                            System.err.println("Método no permitido");
                            out.println("HTTP/1.1 405 Método no permitido");
                            out.println("Content-Type: text/html; charset=utf-8");
                            out.println("Server: MyHTTPServer");
                            out.println("");
                            br1 = new BufferedReader(new FileReader("405.html"));
                            data = br1.readLine();
                            while (data != null){
                                out.println(data);
                                data = br1.readLine();
                            }
                            out.flush();
                            out.close();
                        }catch (Exception ex){
                            //System.err.println("Archivo no encontrado: " + ex);
                        }
                    }
                }
                /*else {
                        try{
                            BufferedReader br1 = new BufferedReader(new FileReader("405.html"));
                            PrintWriter out = new PrintWriter(cliente.getOutputStream());
                            String data = "";
                            System.err.println("Método no permitido");
                            out.println("HTTP/1.1 405 Método no permitido");
                            out.println("Content-Type: text/html; charset=utf-8");
                            out.println("Server: MyHTTPServer");
                            out.println("");
                            br1 = new BufferedReader(new FileReader("405.html"));
                            data = br1.readLine();
                            while (data != null){
                                out.println(data);
                                data = br1.readLine();
                            }
                            out.flush();
                            out.close();
                        }catch (Exception ex){
                            //System.err.println("Archivo no encontrado: " + ex);
                        }
                    }*/
            } while (true);

            //cliente.close();
        } catch (Exception ex) {

            System.err.println("Se ha producido un error y no se ha podido abrir el servidor: " + ex);
        }
        //Siempre cierra el socket
        try {
            cliente.close();
        } catch (IOException ex1) {
            Logger.getLogger(ServerHilo.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }
}
