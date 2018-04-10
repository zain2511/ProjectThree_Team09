package ser516.project3.client.view;

import ser516.project3.constants.ClientConstants;
import ser516.project3.interfaces.ViewInterface;
import ser516.project3.model.ExpressionsModel;

import javax.swing.*;
import java.awt.*;

/**
 * The ExpressionsView class creates the expressions panel on the client UI.
 * 
 * @author vsriva12
 *
 */
@SuppressWarnings("serial")
public class ExpressionsView extends JPanel implements ViewInterface {
	private ExpressionsModel expressionsModel;

	public ExpressionsView(ExpressionsModel expressionsModel) {
		this.expressionsModel = expressionsModel;
	}

	@Override
	public void initializeView(ViewInterface[] subViews) {
		GraphView graphView = (GraphView) subViews[0];

		setLayout(new GridLayout(1, 2, 8, 8));
		setBackground(Color.decode(ClientConstants.PANEL_COLOR_HEX));

		add(FaceView.getInstance(), BorderLayout.LINE_START); // Need to replace with a panel for displaying the face.
		add(graphView, BorderLayout.LINE_END);
		setVisible(true);
	}
}
