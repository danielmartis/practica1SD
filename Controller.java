package Sonda;

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

    public void escribeSocket(Socket p_sk, String datos){
        try{
            PrintWriter out = new PrintWriter(p_sk.getOutputStream());
            out.println(datos);
            out.flush();
            out.close();
        }catch (Exception ex){
            System.out.println("Error en escritura: " + ex);
        }
        return;
    }
    
    public String pedirOperacion(String ip, String puerto, String object){
        Interfaz objetoRemoto = null;
        String res = "", objeto="ObjetoRemoto",tipoOp="",valor="",aux="",element="";
        try {
            StringTokenizer s = new StringTokenizer(object, "?");
            element = s.nextToken();
            //System.out.println(element);
            aux = s.nextToken();
            if(!aux.contains("sonda")){
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
        String names[];
        try{
            System.setSecurityManager(new RMISecurityManager());
            if(!tipoOp.contains("get") && !tipoOp.contains("set")){
                tipoOp = "get";
            }
            names = Naming.list(servidor);
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
                    StringTokenizer s = new StringTokenizer(names[i], "/");
                    s.nextToken();
                    String ident = s.nextToken();
                    s = new StringTokenizer(ident, "ObjetoRemoto");
                    ident = s.nextToken();
                   // System.out.println(ident);
                    res = res.concat("<br><a href=\"/controladorSD/all?sonda=" + ident + "\" post >Estacion " + ident + "</a> \n");
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
            case "all":
                try{
                    res = "<!DOCTYPE html>\n<html> \n"
                        + "    <head>\n"
                        + "        <meta charset=\"utf-8\">\n"
                        + "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                        + "        <title>ESTACIÓN "+objetoRemoto.getId()+"</title>\n"
                        + "        <meta name=\"description\" content=\"Pagina del parking\">\n"
                        + "    </head>\n"
                        + "    <body>\n"
                        + "        <h1>ESTACIÓN     "+objetoRemoto.getId()+"</h1>\n"
                        + "        <a href=\"/controladorSD/index\">Sondas</a>\n"
                        + "        <br><a href=\"/controladorSD/temperatura?sonda="+objetoRemoto.getId()+"\">Volumen: </a>"+ objetoRemoto.getVolumen()
                        + "        <br><a href=\"/controladorSD/luminosidad?sonda="+objetoRemoto.getId()+"\">Fecha: </a>"+ objetoRemoto.getFecha()
                        + "        <br><a href=\"/controladorSD/humedad?sonda="+objetoRemoto.getId()+"\">Ultima fecha: </a>"+ objetoRemoto.getUltimaFecha()
                        + "        <br><a href=\"/controladorSD/pantalla?sonda="+objetoRemoto.getId()+"\">Luz led: </a>"+ String.valueOf(objetoRemoto.getLed())
                        + "</body> \n </html>\n";
                }catch(Exception ex){
                    res="error";
                    System.err.println("Ha habido un problema al acceder a los datos del objeto: "+ex);
                }
                break;
            case "volumen":
                try{
                    if(tipoOp.equals("get")){
                        res = "<!DOCTYPE html>\n<html> \n"
                        + "    <head>\n"
                        + "        <meta charset=\"utf-8\">\n"
                        + "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                        + "        <title>ESTACIÓN "+objetoRemoto.getId()+"</title>\n"
                        + "        <meta name=\"description\" content=\"Pagina para el parking\">\n"
                        + "    </head>\n"
                        + "    <body>\n"
                        + "        <h1>ESTACIÓN     "+objetoRemoto.getId()+"</h1>\n"
                        + "        <a href=\"/controladorSD/index\">Sondas</a>\n"
                        + "        <a href=\"/controladorSD/all?sonda="+objetoRemoto.getId()+"\">Atrás</a>"
                        + "        <br>Volumen: "+ objetoRemoto.getVolumen()
                        + "</body> \n </html>\n";
                    }
                    else{res="error";}
                }catch(Exception ex){
                    res="error";
                    System.err.println("Ha habido un problema al acceder a los datos del objeto: "+ex);
                }
                break;
            case "fecha":
                try{
                    if(tipoOp.equals("get")){
                    res = "<!DOCTYPE html>\n<html> \n"
                        + "    <head>\n"
                        + "        <meta charset=\"utf-8\">\n"
                        + "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                        + "        <title>ESTACIÓN "+objetoRemoto.getId()+"</title>\n"
                        + "        <meta name=\"description\" content=\"Pagina del parking\">\n"
                        + "    </head>\n"
                        + "    <body>\n"
                        + "        <h1>ESTACIÓN     "+objetoRemoto.getId()+"</h1>\n"
                        + "        <a href=\"/controladorSD/index\">Estaciones</a>\n"
                        + "        <a href=\"/controladorSD/all?sonda="+objetoRemoto.getId()+"\">Atrás</a>"
                        + "        <br>Fecha: "+ objetoRemoto.getFecha()
                        + "</body> \n </html>\n";
                    }
                    else{
                        res="error";
                    }
                }
                catch(Exception ex){
                    res="error";
                    System.err.println("Ha habido un problema al acceder a los datos del objeto: "+ex);
                }
                break;
            case "ultimafecha":
                try{
                    if(tipoOp.equals("get")){
                        res = "<!DOCTYPE html>\n<html> \n"
                        + "    <head>\n"
                        + "        <meta charset=\"utf-8\">\n"
                        + "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                        + "        <title>ESTACIÓN "+objetoRemoto.getId()+"</title>\n"
                        + "        <meta name=\"description\" content=\"Pagina del parking\">\n"
                        + "    </head>\n"
                        + "    <body>\n"
                        + "        <h1>ESTACIÓN     "+objetoRemoto.getId()+"</h1>\n"
                        + "        <a href=\"/controladorSD/index\">Estaciones</a>\n"
                        + "        <a href=\"/controladorSD/all?sonda="+objetoRemoto.getId()+"\">Ultima fecha</a>"
                        + "        <br>Fecha: "+ objetoRemoto.getUltimaFecha()
                        + "</body> \n </html>\n";
                    }
                    else{
                        res="error";
                    }
                }
                catch(Exception ex){
                    res="error";
                    System.err.println("Ha habido un problema al acceder a los datos del objeto: "+ex);
                }
                break;
            case "luz":
                try{
                    if(tipoOp.equals("get")){
                    res = "<!DOCTYPE html>\n<html> \n"
                        + "    <head>\n"
                        + "        <meta charset=\"utf-8\">\n"
                        + "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                        + "        <title>ESTACIÓN "+objetoRemoto.getId()+"</title>\n"
                        + "        <meta name=\"description\" content=\"Un centro para el control de estaciones meteorológicas\">\n"
                        + "    </head>\n"
                        + "    <body>\n"
                        + "        <h1>ESTACIÓN     "+objetoRemoto.getId()+"</h1>\n"
                        + "        <a href=\"/controladorSD/index\">Estaciones</a>\n"
                        + "        <a href=\"/controladorSD/all?sonda="+objetoRemoto.getId()+"\">Atrás</a>"
                        + "        <br>Luz led: "+String.valueOf(objetoRemoto.getLed())
                            //Aqui es donde se introduce el form
                        + "        <FORM method=get action=\"pantalla\">"
                        + "        Introduce el nuevo valor de la pantalla:"
                        + "        <INPUT type=\"hidden\" name=\"sonda\" value=\""+objetoRemoto.getId()+"\">"
                        + "        <br><INPUT type=text name=\"set\">"
                        + "        <br><INPUT type=\"submit\" value=\"Enviar\"> "
                        + "        </FORM>"
                        + "    </body> \n </html>\n";
                    }
                    else if(tipoOp.equals("set")){
                        if(valor.isEmpty()){
                            res="errorComando";
                            break;
                        }
                        valor= valor.replace('+', ' ');
                        objetoRemoto.setLed(Integer.parseInt(valor));
                        
                        res = "<!DOCTYPE html>\n<html> \n"
                        + "    <head>\n"
                        + "        <meta charset=\"utf-8\">\n"
                        + "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                        + "        <title>ESTACIÓN "+objetoRemoto.getId()+"</title>\n"
                        + "        <meta name=\"description\" content=\"Un centro para el control de estaciones meteorológicas\">\n"
                        + "    </head>\n"
                        + "    <body>\n"
                        + "        <h1>ESTACIÓN     "+objetoRemoto.getId()+"</h1>\n"
                        + "        <a href=\"/controladorSD/index\">Estaciones</a>\n"
                        + "        <a href=\"/controladorSD/all?sonda="+objetoRemoto.getId()+"\">Atrás</a>"
                        + "        <br>Se ha cambiado correctamente el valor de la pantalla."
                        + "    </body> \n </html>\n";
                    }
                    else{
                        res="error";
                    }
                }
                catch(Exception ex){
                    res="error";
                    System.err.println("Ha habido un problema al acceder a los datos del objeto: "+ex);
                }
                break;
            default:
                res = "errorVariable";
                break;
        }
        return res; 
    }
    
    public static void main(String[] args){
        String ipRMI,puertoRMI,puertoHTTP;
        Controller cr = new Controller();
        int i = 0;
        if(args.length < 3){
            System.out.println("Los argumentos deben ser: ipRMI, puertoRMI y puerto HTTP");
        }
        ipRMI = args[0];
        puertoRMI = args[1];
        puertoHTTP = args[2];
        String op = "";
        String object = "";
        while (i == 0){
            try{
                ServerSocket skServidor = new ServerSocket(Integer.parseInt(puertoHTTP));
                System.out.println("Escuchando al servidor");
                for(;;){
                    Socket skCliente = skServidor.accept();
                    op = cr.leeSocket(skCliente,op);
                    System.out.println(op);
                    op = cr.pedirOperacion(ipRMI,puertoRMI,op);
                    cr.escribeSocket(skCliente,op);
                    skCliente.close();
                }
            } catch(Exception e){
                System.out.println("Error:" + e.toString());
            }
        }
    }
}
