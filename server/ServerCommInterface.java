package server;

import java.rmi.Remote;
import client.ClientCommInterface;

public interface ServerCommInterface extends Remote {

	public void register(String teamName, ClientCommInterface cc) throws Exception;

	public void submitSolution(String name, String sol) throws Exception;

}
