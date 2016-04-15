package server;

import javax.websocket.server.ServerContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;


public class JettyServer
{
	public static void main(String[] args) throws Exception
	{
		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(9595);
		server.addConnector(connector);

		// Setup the basic application "context" for this application at "/"
		// This is also known as the handler tree (in jetty speak)
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		// Initialize javax.websocket layer
		ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
		// Add WebSocket endpoint to javax.websocket layer
		wscontainer.addEndpoint(EventSocketServer.class);

		server.start();
		server.dump(System.err);
		server.join();
	}
}