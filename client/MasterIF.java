package client;

import java.rmi.Remote;

public interface MasterIF extends Remote {

    void resultFound(int password) throws Exception;

}
