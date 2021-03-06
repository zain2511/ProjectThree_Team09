package ser516.project3.server.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.glassfish.tyrus.server.Server;

import ser516.project3.utilities.ServerCommonData;

public class ServerContainerThread implements Runnable {
	final static Logger logger = Logger.getLogger(ServerContainerThread.class);
	private static final int PORT = 1516;
	private Server server;

	@Override
	public void run() {
		server = new Server("localhost", PORT, "", null, ServerConnectionEndpoint.class);
		try {
			server.start();
			ServerCommonData.getInstance().setServerStarted(true);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			reader.readLine();
		} catch (Exception e) {
			logger.error("Error occurred while trying to start the server websocket::::" + e.getStackTrace());
		} finally {
			server.stop();
			ServerCommonData.getInstance().setServerStarted(false);
		}
	}

	/**
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(Server server) {
		this.server = server;
	}
	

}
