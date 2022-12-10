package client;

import java.rmi.Remote;

public interface SlaveIF extends Remote {

    void interrupt() throws Exception;

}
