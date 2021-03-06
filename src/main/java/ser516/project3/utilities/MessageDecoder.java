package ser516.project3.utilities;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import ser516.project3.model.MessageModel;
import ser516.project3.model.MessageModel.AbstractExpression;
import ser516.project3.model.MessageModel.ConcreteExpression;
import ser516.project3.model.MessageModel.Emotion;



public class MessageDecoder implements Decoder.Text<MessageModel> {

    @Override
    public void init(EndpointConfig config) {
        // Intentionally empty.
    }

    @Override
    public void destroy() {
        // Intentionally empty.
    }

    @Override
    public MessageModel decode(String payload) throws DecodeException {

        MessageModel messageModel = new MessageModel();

        // Read the payload.
        JsonObject root = Json.createReader(new StringReader(payload)).readObject();
        JsonObject timeAttributes= root.getJsonObject("Time-Attributes");
        JsonObject expressionAttributes = root.getJsonObject("Expression");
        JsonObject emotionAttributes = root.getJsonObject("Emotion");
        
        //Unmarshal the time attributes
        messageModel.setInterval(timeAttributes.getJsonNumber("Interval").doubleValue());
        messageModel.setTimeStamp(timeAttributes.getJsonNumber("TimeStamp").doubleValue());

        // Unmarshal the expression attributes.
        for(AbstractExpression aex : AbstractExpression.values()) {
        	messageModel.setAbstractExpression(aex.name(), expressionAttributes.getJsonNumber(aex.name()).doubleValue());
        }
        for(ConcreteExpression cex : ConcreteExpression.values()) {
        	messageModel.setConcreteExpression(cex.name(), expressionAttributes.getBoolean(cex.name()));
        }
        // Unmarshal the emotion attributes.
        for(Emotion em : Emotion.values()) {
        	messageModel.setEmotion(em.name(), emotionAttributes.getJsonNumber(em.name()).doubleValue());
        }

        return messageModel;
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

}