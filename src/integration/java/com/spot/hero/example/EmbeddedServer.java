package com.spot.hero.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class EmbeddedServer
{
    private static Server server;
 
    public static final String TEST_CONTEXT = "/";
    public static final int TEST_PORT = 8080;
 
    public static void startIfRequired() throws Exception
    {
        if (server == null)
        {
            SLF4JBridgeHandler.removeHandlersForRootLogger();
            SLF4JBridgeHandler.install();

            System.setProperty("java.naming.factory.url.pkgs", "org.eclipse.jetty.jndi");
            System.setProperty("java.naming.factory.initial", "org.eclipse.jetty.jndi.InitialContextFactory");

            server = new Server(TEST_PORT);

            WebAppContext context = new WebAppContext();
            context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
            context.setResourceBase("src/main/webapp");
            context.setContextPath("/");
            context.setParentLoaderPriority(true);

            server.setHandler(context);

            server.start();
        }
    }
     
    public static void stop() throws Exception
    {
        if (server != null)
        {
            server.stop();
            server.join();
            server.destroy();
            server = null;
        }
    }
 
    public static void main(String[] args)
    {
        try
        {
            startIfRequired();
            server.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}