package client;

import server.ServerCommInterface;

import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Random;

public class MasterCommHandler extends UnicastRemoteObject implements MasterCommInterface {

    private final HashMap<String, SlaveCommInterface> slaves = new HashMap<>();

    public MasterCommHandler() throws Exception {
        super();
    }

    public void register(String teamName, SlaveCommInterface sc, String arg) throws Exception {
        Random r = new Random();
        slaves.put(String.valueOf(r.nextInt()), sc);
        System.out.println("Registered slave " + teamName + " at " + arg);
    }

    public void sendProblem(ServerCommInterface sci, ClientCommHandler cch) throws Exception {
        for (int i = 0; i < slaves.size(); i++) {
            SlaveCommInterface sc = slaves.values().toArray(new SlaveCommInterface[0])[i];
            int start = i * cch.currProblemSize / slaves.size();
            int end = (i + 1) * cch.currProblemSize / slaves.size();
            sc.publishProblem(sci, cch.currProblem, start, end);
        }
    }

    public void interrupt() throws Exception {
        for (SlaveCommInterface sc : slaves.values()) {
            sc.interrupt();
        }
    }

    public boolean bothSlavesBusy() throws Exception {
        for (SlaveCommInterface sc : slaves.values()) {
            if (!sc.isBusy()) {
                return false;
            }
        }
        return true;
    }

}
