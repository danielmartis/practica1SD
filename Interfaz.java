package Sonda;

import java.rmi.Remote;

public interface Interfaz extends Remote{
    public void setLed(int a) throws java.rmi.RemoteException,Exception;
    public int getVolumen() throws java.rmi.RemoteException;
    public String getFecha() throws java.rmi.RemoteException;
    public String getUltimaFecha() throws java.rmi.RemoteException;
    public int getLed() throws java.rmi.RemoteException;
    public void setData(String a) throws java.rmi.RemoteException;
    public void getData(String a) throws java.rmi.RemoteException, Exception;
    public void setVolumen(int volumen) throws java.rmi.RemoteException;
    public void setUltimaFecha(String fecha) throws java.rmi.RemoteException;
    public int getId() throws java.rmi.RemoteException;
}
