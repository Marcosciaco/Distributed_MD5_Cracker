package client;

import server.ServerCommInterface;

import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;

public class SlaveCommHandler extends UnicastRemoteObject implements SlaveCommInterface {

    private final HashMap<String, Integer> rainbowTable = new HashMap<>();
    private final String teamName;
    private boolean isBusy;

    protected SlaveCommHandler(String teamName) throws Exception {
        super();
        this.teamName = teamName;
    }

    @Override
    public void publishProblem(ServerCommInterface sci, ClientCommHandler cch, int start, int end) throws Exception {
        isBusy = true;
        MessageDigest md = MessageDigest.getInstance("MD5");
        if (rainbowTable.get(Arrays.toString(cch.currProblem)) != null && cch.currProblem != null) {
            int i = rainbowTable.get(Arrays.toString(cch.currProblem));
            System.out.println("Found password in rainbow-table: " + i);
            sci.submitSolution(teamName, String.valueOf(i));
            cch.currProblem = null;
            isBusy = false;
            return;
        }
        for (int i = start; i < end && isBusy; i++) {
            byte[] hash = md.digest(String.valueOf(i).getBytes());
            rainbowTable.put(Arrays.toString(hash), i);
            if (Arrays.equals(cch.currProblem, hash)) {
                System.out.println("Found password with algo: " + i);
                sci.submitSolution(teamName, String.valueOf(i));
                cch.currProblem = null;
                isBusy = false;
                return;
            }
        }
    }

    @Override
    public void interrupt() throws Exception {
        System.out.println("Slave received interrupt");
        isBusy = false;
    }

    @Override
    public boolean isBusy() throws Exception {
        return isBusy;
    }

}
