package client;

import server.ServerCommInterface;

import java.rmi.Remote;

public interface MasterCommInterface extends Remote {

    public void register(String teamName, SlaveCommInterface cc, String arg) throws Exception;

}
