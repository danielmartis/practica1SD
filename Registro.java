package Sonda;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.io.*;

/**
 *
 * @author alejandro
 */


public class Registro {

    @SuppressWarnings("deprecation")
    public static void main(String args[]) {
        String URLRegistro;
        try {
            System.setSecurityManager(new RMISecurityManager());
	    //String puerto = args[0];
            int tam=Naming.list("rmi://127.0.0.1:1099").length;
            for(int i=0;i<tam;i++){//Para limpiar el registro de objetos sin reiniciar
                URLRegistro = "/ObjetoRemoto"+(tam-i);
                Naming.unbind(URLRegistro);
            }
            for (int i = 1; i <= 3; i++) {
                Sonda objetoRemoto = new Sonda(i);
                URLRegistro = "ObjetoRemoto"+i;
		System.out.println(URLRegistro);
                Naming.rebind(URLRegistro, objetoRemoto);
                System.out.println("Servidor de objeto preparado.");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
