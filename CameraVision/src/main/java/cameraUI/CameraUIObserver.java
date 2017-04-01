package cameraUI;


import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import computervision.ImageLabel;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;

public class CameraUIObserver implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private static DecimalFormat df2 = new DecimalFormat("###.##");

	private JLabel lblFps;
	private JCheckBox chckbxCapture;
	public JCheckBox getChckbxCapture() {
		return chckbxCapture;
	}

	private JPanel cameraPanel;
	private JLabel lblImageLabel;


	public JFrame getFrame() {
		return this.frame;
	}

	/**
	 * Create the application.
	 */
	public CameraUIObserver(JPanel cameraPanel, Observable observable) {
		this.cameraPanel = cameraPanel;
		observable.addObserver(this);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		//		frame.addWindowListener(new WindowAdapter() {
		//			@Override
		//			public void windowClosing(WindowEvent arg0) {
		//				myClassifier.stopThread();
		//				myCamera.close();
		//			}
		//		});
		frame.setBounds(100, 100, 765, 522);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{612, 113, 0};
		gridBagLayout.rowHeights = new int[]{0, 36, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);

		//JPanel panel = new JPanel();
		//panel.setBackground(Color.BLUE);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		//frame.getContentPane().add(panel, gbc_panel);
		frame.getContentPane().add(cameraPanel, gbc_panel);

		lblFps = new JLabel("FPS");
		GridBagConstraints gbc_lblFps = new GridBagConstraints();
		gbc_lblFps.insets = new Insets(0, 0, 5, 5);
		gbc_lblFps.gridx = 0;
		gbc_lblFps.gridy = 1;
		frame.getContentPane().add(lblFps, gbc_lblFps);

		chckbxCapture = new JCheckBox("Capture / Analyze");

		
		lblImageLabel = new JLabel("Image Label");
		GridBagConstraints gbc_lblImageLabel = new GridBagConstraints();
		gbc_lblImageLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblImageLabel.gridx = 0;
		gbc_lblImageLabel.gridy = 2;
		frame.getContentPane().add(lblImageLabel, gbc_lblImageLabel);
		GridBagConstraints gbc_chckbxCapture = new GridBagConstraints();
		gbc_chckbxCapture.gridx = 1;
		gbc_chckbxCapture.gridy = 2;
		frame.getContentPane().add(chckbxCapture, gbc_chckbxCapture);
	}

	public void updateFPS(double FPS) {
		lblFps.setText("FPS: " + df2.format(FPS));
	}


	public void updatePredictions(ImageLabel scannedLabel) {
		

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof ImageLabel) {
			System.out.println(((ImageLabel)arg0).toString());
		}
		
	}



}
