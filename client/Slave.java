package client;

import server.ServerCommInterface;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Slave {

    private final String teamName;
    private final String slaveIp;

    private HashMap<String, Integer> rainbowTable = new HashMap<>();
    private boolean isSolving;

    public Slave(String teamName, String slaveIp) {
        this.teamName = teamName;
        this.slaveIp = slaveIp;
    }

    public void solveProblem(int start, int end, ServerCommInterface sci, ClientCommHandler cch) throws Exception {
        isSolving = true;
        MessageDigest md = MessageDigest.getInstance("MD5");
        if (rainbowTable.get(Arrays.toString(cch.currProblem)) != null && cch.currProblem != null) {
            int i = rainbowTable.get(Arrays.toString(cch.currProblem));
            System.out.println("Found password in rainbow-table: " + i);
            sci.submitSolution(teamName, String.valueOf(i));
            cch.currProblem = null;
            return;
        }
        for (int i = start; i < end; i++) {
            byte[] hash = md.digest(String.valueOf(i).getBytes());
            rainbowTable.put(Arrays.toString(hash), i);
            if (Arrays.equals(cch.currProblem, hash)) {
                System.out.println("Found password with algo: " + i);
                sci.submitSolution(teamName, String.valueOf(i));
                cch.currProblem = null;
                return;
            }
        }
    }

    public boolean isSolving() {
        return isSolving;
    }

    public boolean setSolving(boolean solving) {
        return isSolving = solving;
    }
}
