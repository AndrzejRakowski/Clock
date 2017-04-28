package zegar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OknoZegara {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OknoZegara window = new OknoZegara();
					window.frame.pack();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public OknoZegara() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();

		frame.setType(JFrame.Type.UTILITY);
		frame.setUndecorated(true);
		frame.setBackground(new Color(0, 0, 0, 0));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PanelZegara pz = new PanelZegara(new Dimension(350, 350));
		pz.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent me) {
				if (me.isPopupTrigger()) {
					System.exit(0);
				}
				
			}
		});
		frame.setContentPane(pz);
		pz.start();
	}

}
