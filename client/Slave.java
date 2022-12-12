package client;

import java.rmi.Naming;
import java.util.HashMap;

public class Slave {

    public static void main(String[] args) throws Exception {
        String teamName = args[2];
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        System.setProperty("java.rmi.master.hostname", args[1]);
        MasterCommInterface mci = (MasterCommInterface) Naming.lookup("rmi://" + args[0] + ":5000/master");
        SlaveCommHandler sch = new SlaveCommHandler(teamName);
        mci.register(teamName, sch, args[1]);
    }
}
