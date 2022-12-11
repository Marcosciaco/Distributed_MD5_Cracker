package client;

import server.ServerCommInterface;
import java.rmi.Remote;

public interface SlaveCommInterface extends Remote {

    public void publishProblem(ServerCommInterface sci, ClientCommHandler cch, int start, int end) throws Exception;

    void interrupt() throws Exception;

    boolean isBusy() throws Exception;
}
