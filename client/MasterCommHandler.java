package client;

import server.ServerCommInterface;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class MasterCommHandler extends UnicastRemoteObject implements MasterCommInterface {

    private final ArrayList<SlaveCommInterface> slaves = new ArrayList<SlaveCommInterface>();

    public MasterCommHandler() throws Exception {
        super();
    }

    public void solutionFound(String name, String sol) throws Exception {
        System.out.println("Solution found by " + name + ": " + sol);
    }

    public void register(String teamName, SlaveCommInterface sc, String arg) throws Exception {
        System.out.println("Registered " + teamName);
        slaves.add(sc);
    }

    public void sendProblem(byte[] problem, int problemSize, ServerCommInterface sci, ClientCommHandler cch) throws Exception {
        for (int i = 0; i < slaves.size(); i++) {
            SlaveCommInterface sc = slaves.get(i);
            int start = i * problemSize / slaves.size();
            int end = (i + 1) * problemSize / slaves.size();
            sc.publishProblem(sci, cch, start, end);
        }
    }

    public void interrupt() throws Exception {
        for (SlaveCommInterface sc : slaves) {
            sc.interrupt();
        }
    }

    public boolean isBusy() throws Exception {
        for (SlaveCommInterface sc : slaves) {
            if (sc.isBusy()) return true;
        }
        return false;
    }

}
