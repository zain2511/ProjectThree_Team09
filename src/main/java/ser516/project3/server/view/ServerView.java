package ser516.project3.server.view;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

/**
 * The JFrame class of Server application
 * 
 * @author User
 *
 */
@SuppressWarnings("serial")
public class ServerView extends JDialog implements Runnable {
	final static Logger logger = Logger.getLogger(ServerView.class);

	ProcessBuilder pb = new ProcessBuilder(
			"java", "-cp", "build/classes", "ser516.project3.server.view.ServerView");
	private volatile Process proc;

	/**
	 * Constructor to initialize all the components of the server application
	 */
	public ServerView() {
		//setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Server");
		setMinimumSize(new Dimension(500, 800));
		setLayout(new BorderLayout());
		setVisible(true);
		add(ServerPanelGenerator.createTopPanels(), BorderLayout.PAGE_START);
		add(ServerPanelGenerator.createConfigurationPanels(), BorderLayout.CENTER);
		setVisible(true);

		ServerView.this.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				ServerPanelGenerator.getServerControllerImpl().stopServer();
				logger.info("server window closed");
			}
		});
	}

	@Override
	public void run() {
		try {
			proc = pb.start();
			proc.waitFor();
		} catch (IOException | InterruptedException ex) {
			ex.printStackTrace(System.err);
		}
		EventQueue.invokeLater(this::reset);
	}

	private void reset() {
		proc = null;

	}

}
