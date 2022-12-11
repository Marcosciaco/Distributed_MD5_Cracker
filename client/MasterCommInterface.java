package client;

import server.ServerCommInterface;

import java.rmi.Remote;

public interface MasterCommInterface extends Remote {

    public void solutionFound(String name, String sol) throws Exception;

    public void register(String teamName, SlaveCommInterface cc, String arg) throws Exception;

}
