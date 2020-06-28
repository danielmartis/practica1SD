package Sonda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("serial")
public class Sonda extends UnicastRemoteObject implements Serializable, Interfaz{
    private int volumen,led;
    private String ultimaFecha;
    private final int id;
    
    public Sonda(int id) throws RemoteException{
        super();
        this.id = id;
        try{
            getData("./Sonda"+id+".txt");
            if(led < 0 || led > 65535 || volumen < 0 || volumen > 100){
                throw new Exception();
            }
        } catch (Exception e){
            this.volumen = 0;
            this.led = 0;
            this.ultimaFecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
;
            setData("./Sonda" +id+".txt"); 
        }
    }
    
    @Override
    public int getId(){
        return id;
    }
    
    @Override
    public int getVolumen(){
        try{
            getData("./Sonda"+id+".txt");
        }catch(Exception e){
            System.err.println("Error con el archivo");
        }
        return volumen;
    }
    @Override
    public void setVolumen(int volumen){
        if(volumen > 0 && volumen < 100)
            this.volumen = volumen;
        ultimaFecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        setData("./Sonda"+getId()+".txt");
    }
    
    @Override
    public void setUltimaFecha(String fecha){
        this.ultimaFecha = fecha;
        setData("./Sonda"+getId()+".txt");
    }
    
    @Override
    public String getFecha(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
    @Override
    public String getUltimaFecha(){
        try{
            getData("./Sonda"+id+".txt");
        }catch(Exception e){
            System.err.println("Error con el archivo");
        }
        return ultimaFecha;
    }
    
    @Override
    public int getLed(){
        try{
            getData("./Sonda"+id+".txt");
        }catch(Exception e){
            System.err.println("Error con el archivo");
        }
        return led;
    }
    @Override
    public void setLed(int a){
        if(a > 0 && a < 65535)
            led = a;
        ultimaFecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        setData("./Sonda"+id+".txt");
        
    }
    
    @Override
    public void getData(String a){
        try {
            File archivo= new File(a);
            try (FileReader file = new FileReader(archivo)) {
                BufferedReader br1= new BufferedReader(file);
                String lect=br1.readLine();
                while(lect!=null){
                    String aux[]=lect.split("=");
                    if(!aux[1].isEmpty()){
                        if(lect.contains("Volumen=")){
                           setVolumen(Integer.parseInt(aux[1]));
                        }
                        else if(lect.contains("UltimaFecha=")){
                            setUltimaFecha(aux[1]);
                        }
                        else if(lect.contains("Led=")){
                            setLed(Integer.parseInt(aux[1]));
                        }
                    }
                    lect =br1.readLine();
                }
                br1.close();
                file.close();
            }
        } catch (Exception ex) {
            System.err.println("No se ha podido leer el fichero: "+ex);
        } 
    }
    @Override
    public void setData(String a){
        try{
            File file = new File(a);
            BufferedWriter bw;
            if(file.exists()){
                bw = new BufferedWriter(new FileWriter(a));
            }
            else{
                bw = new BufferedWriter(new FileWriter(a));
            }
            String text;
            text = "Volumen="+volumen+"\n"+"UltimaFecha="+ultimaFecha+"\n"+"Led"+led;
            bw.write(text);
            bw.close();
        }catch(Exception e){
            System.err.println("No se ha podido crear el archivo: "+e);
        }
    }

}
    
