package Station;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Controller{
    
    public String leeSocket(Socket p_sk, String p_datos){
        try {
            InputStream aux = p_sk.getInputStream();
            DataInputStream flujo = new DataInputStream(aux);
            p_datos = new String();
            p_datos = flujo.readUTF();
        }catch (Exception ex){
            System.out.println("Error en lectura: "+ ex);
        }
        return p_datos;
    }
    d
    public void escribeSocket(Socket p_sk, String datos){
        try{
            PrintWriter out = new PrintWriter(p_sk.getOutputStream());
            out.println(p_datos);
            out.flush();
            out.close();
        }catch (Exception ex){
            System.out.println("Error en escritura: " + ex);
        }
        return;
    }
    
    public string pedirOperacion(String ip, String puerto, String object){
        Interfaz objeto = null;
        String res = "", consultado="", objeto="ObjetoRemoto",tipoOp="",valor="",aux="";
        try {
            StringTokenizer st = new StringTokenizer(object, ?);
            element = s.nextToken();
            //System.out.println(element);
            aux = s.nextToken();
            if(!aux.contains("sonda"){
                return "error";
            }
            s = new StringTokenizer(aux, "=");
            tipoOp = s.nextToken();
            //System.out.println(tipoOp);
            objeto = objeto.concat(s.nextToken());
            valor = s.nextToken();
            s = new StringTokenizer(objeto,"&");
            objeto = s.nextToken();
            valor = s.nextToken();
            System.out.println(tipoOp+"="+valor);
        }catch(Exception ex){
            System.err.println("Excepción producida: " + ex);
        }
        
        System.out.println(element + " " + objeto + " " + tipoOp);
        String servidor = "rmi://" + ip + ":" + puerto;
        String servidorConcreto = servidor.concat("/" + objeto);
        System.out.println("Objeto:" + servidorConcreto);
        String nombres[]
        try{
            System.setSecurityManager(new RMISecurityManager);
            if(!tipoOp.contains("get") && !tipoOp.contains("set")){
                tipoOp = "get";
            }
            nombres = Naming.list(servidor);
            if (element.equals("index")) {
                //Crear una página HTML Con los nombres de los servidores
                res = "<!DOCTYPE html>\n<html> \n"
                        + "    <head>\n"
                        + "        <meta charset=\"utf-8\">\n"
                        + "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                        + "        <title>Página del parking</title>\n"
                        + "        <meta name=\"description\" content=\"Parking\">\n"
                        + "    </head>\n"
                        + "    <body>\n"
                        + "        <h1>Bienvenido a la página de gestión del parking</h1>\n"
                        + "        <a href=\"/index.html\">Inicio</a>\n";

                for (int i = 0; i < names.length; i++) {
                    StringTokenizer s = new StringTokenizer(nombres[i], "/");
                    s.nextToken();
                    String ident = s.nextToken();
                    s = new StringTokenizer(ident, "ObjetoRemoto");
                    ident = s.nextToken();
                   // System.out.println(ident);
                    res = res.concat("<br><a href=\"/controladorSD/all?station=" + ident + "\" post >Estacion " + ident + "</a> \n");
                }

                res = res.concat("</body> \n </html>\n");
                return res;
            }
            else {
                objetoRemoto = (Interfaz) Naming.lookup(servidorConcreto);
            }
        }catch(Exception ex){
            System.out.println("Error iniciando el objeto remoto: " + ex);
            res = "error";
        }
        switch(element){
        
        
        
        
        
        
        
        
        
        

        }
        return res    
    }     
}