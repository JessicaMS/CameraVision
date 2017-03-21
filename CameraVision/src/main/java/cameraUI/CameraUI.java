package cameraUI;


import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTable;

import cameras.Camera;
import cameras.FPSObserver;
import cameras.Kinect;
import computervision.Classification;
import computervision.ClassificationsObserver;
import computervision.ImageProcessor;
import computervision.SceneClassifier;

import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.table.DefaultTableModel;


import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class CameraUI implements FPSObserver, ClassificationsObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTable table;
	private static DecimalFormat df2 = new DecimalFormat("###.##");

	private JLabel lblFps;
	private JCheckBox chckbxCapture;
	private JPanel cameraPanel;
	private ImageProcessor myClassifier;


	public JFrame getFrame() {
		return this.frame;
	}

	/**
	 * Create the application.
	 */
	public CameraUI(JPanel cameraPanel, ImageProcessor myClassifier) {
		this.cameraPanel = cameraPanel;
		this.myClassifier = myClassifier;
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

		table = new JTable();
		table.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
				},
				new String[] {
						"Label", "Prediction"
				}
				));
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.insets = new Insets(0, 0, 0, 5);
		gbc_table.fill = GridBagConstraints.HORIZONTAL;
		gbc_table.gridx = 0;
		gbc_table.gridy = 2;
		frame.getContentPane().add(table, gbc_table);

		chckbxCapture = new JCheckBox("Capture / Analyze");
		chckbxCapture.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(chckbxCapture.isSelected()) {
					myClassifier.setImageProcessing(true);
				} else {
					myClassifier.setImageProcessing(false);
				}
			}
		});
		GridBagConstraints gbc_chckbxCapture = new GridBagConstraints();
		gbc_chckbxCapture.gridx = 1;
		gbc_chckbxCapture.gridy = 2;
		frame.getContentPane().add(chckbxCapture, gbc_chckbxCapture);
	}

	public void updateFPS(double FPS) {
		lblFps.setText("FPS: " + df2.format(FPS));
	}


	public void updatePredictions(ArrayList<Classification> predictions) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		while(model.getRowCount()> 0) {
			model.removeRow(0);
		}

		System.out.println( "Predictions for batch :");
		for(Classification pred:  predictions) {
			model.addRow(new Object[]{pred.getLabel(), pred.getProbability()});
		}
		table.setModel(model);

	}



}
