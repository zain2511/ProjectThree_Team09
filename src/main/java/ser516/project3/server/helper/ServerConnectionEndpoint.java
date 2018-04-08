package ser516.project3.server.helper;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;

import ser516.project3.server.controller.ServerController;
import ser516.project3.utilities.MessageEncoder;
import ser516.project3.utilities.ServerCommonData;

/**
 * The Web server socket endpoint class for the server application
 *
 * @author User
 */
@ServerEndpoint(value = "/server", encoders = {MessageEncoder.class})
public class ServerConnectionEndpoint {

    final static Logger logger = Logger.getLogger(ServerConnectionEndpoint.class);

    @OnOpen
    public void onOpen(final Session session) throws IOException {
        try {
            //  Here the logic is to start sending the message json based on the value
            // of auto send flag
            //  If the flag is false, just send the json once, else keep sending based
            // on the interval
            logger.info("New Client connected :::: " + session.getBasicRemote());
            ServerController.getInstance().getConsoleController().getConsoleModel().logMessage("Client Connected");
            ServerCommonData serverCommonDataObject = ServerCommonData.getInstance();
            while (true) {
                boolean isShouldSend = ServerController.getInstance().getTopController().getTopModel().isShouldSendData();
                boolean isAutoRepeat = ServerController.getInstance().getTopController().getTopModel().isAutoRepeatCheckBoxChecked();
                if (isShouldSend) {
                    if (isAutoRepeat) {
                        //send the message object
                        session.getBasicRemote().sendObject(serverCommonDataObject.getMessage());

                        //increment the time elapsed
                        double timeElapsed = ServerCommonData.getInstance().getMessage().getTimeStamp();
                        double dataInterval = ServerCommonData.getInstance().getMessage().getInterval();
                        ServerCommonData.getInstance().getMessage().setTimeStamp(timeElapsed + dataInterval);
                        ServerController.getInstance().getTimerController().updateTimeStamp(timeElapsed);
                    } else {
                        session.getBasicRemote().sendObject(serverCommonDataObject.getMessage());
                        long timeElapsed = (long) ServerCommonData.getInstance().getMessage().getTimeStamp();
                        double dataInterval = ServerCommonData.getInstance().getMessage().getInterval();
                        ServerCommonData.getInstance().getMessage().setTimeStamp(timeElapsed + dataInterval);
                        ServerController.getInstance().getTopController().getTopModel().setShouldSendData(false);
                    }
                }
                Thread.sleep((long) (serverCommonDataObject.getMessage().getInterval() * 1000));
            }

        } catch (IOException | EncodeException | InterruptedException e) {
            // TODO Auto-generated catch block
            logger.error("Error occurred in onOpen method :::: " + e.getStackTrace());
            ServerController.getInstance().getConsoleController().getConsoleModel().logMessage("Error occurred while connecting to client");
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // TODO: We have to write logic of what to do when we receive message from the
        // client. Or not!
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info("onClose: " + closeReason);
        try {
            session.getBasicRemote().sendText("Connection closed");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("Error occurred while sending text::::" + e.getStackTrace());
        }
    }

    @OnError
    public void onError(Session session, Throwable t) {
        logger.error("Error occurred in Server Endpoint");
    }
}