package com.hws.gateway;
import py4j.GatewayServer;
import com.hws.gateway.ServiceAplicationProxy;
import org.apache.log4j.Logger;


public class HWSGatewayHB {
    
    private static Logger logger = Logger.getLogger(HWSGatewayHB.class);
    
    public static void main(String[] args) {
        GatewayServer server = new GatewayServer(new ServiceAplicationProxy(), 25535);
        server.start();
        logger.debug("Server Starting");
        System.out.println("Server Starting");
    }
}
