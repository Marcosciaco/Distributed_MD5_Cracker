package client;

import java.rmi.Naming;
import java.rmi.registry.Registry;

public class Slave {

    public static void main(String[] args) throws Exception {
        String teamName = args[2];
        System.setProperty("java.rmi.master.hostname", args[1]);
        MasterCommInterface mci = (MasterCommInterface) Naming.lookup("rmi://" + args[0] + ":5000/master");

        SlaveCommHandler sch = new SlaveCommHandler(teamName);
        mci.register(teamName, sch, args[1]);
    }

}
