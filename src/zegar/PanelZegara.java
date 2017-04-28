package zegar;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

public class PanelZegara extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage bi;
	private Graphics2D g2bi;
	private Calendar time = Calendar.getInstance();
	private volatile boolean start = false;

	public PanelZegara(Dimension size) {
		this.setSize(size);
		this.setPreferredSize(size);
		this.setOpaque(false);
		initialize();
	}

	private void initialize() {
		if (bi == null || bi.getWidth() != this.getWidth() || bi.getHeight() != this.getHeight()) {
			this.bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
			this.g2bi = bi.createGraphics();
		}
		paintClock(g2bi, time);
	}

	private void paintClock(Graphics2D g2, Calendar time) {
		java.awt.geom.Point2D.Double punktSrotkowy = new Point2D.Double(bi.getWidth() / 2.0, bi.getHeight() / 2.0);
		int second = time.get(Calendar.SECOND);
		int minuty = time.get(Calendar.MINUTE);
		int godziny = time.get(Calendar.HOUR_OF_DAY);
		g2.setColor(new Color(230, 100, 100, 200));
		// czyszczenie
		Composite defaultComposite = g2.getComposite();
		g2.setComposite(AlphaComposite.Clear);
		g2.fillRect(0, 0, bi.getWidth(), bi.getHeight());
		g2.setComposite(defaultComposite);

		// wygladzenie krawedzi
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// ustawienie linni
		float strokeWidth = 10.5f;
		float strokeWidth2 = 4f;
		Stroke stroke2 = new BasicStroke(strokeWidth2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(stroke2);
		
		
		g2.drawOval(0, 0, bi.getWidth(), bi.getHeight());
		Line2D.Double line3 = new Line2D.Double(175,1,175,17);
		g2.draw(line3);
		Line2D.Double line4 = new Line2D.Double(175,350,175,333);
		g2.draw(line4);
		Line2D.Double line5 = new Line2D.Double(0,175,17,175);
		g2.draw(line5);
		Line2D.Double line6 = new Line2D.Double(350,175,333,175);
		g2.draw(line6);
		
		Stroke stroke = new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(stroke);

		// ustawienie czcionki
		Font font = new Font("Tahoma", Font.BOLD + Font.ITALIC, bi.getHeight() / 12);
		g2.setFont(font);

		// rozmiar tekstu
		// String str = String.valueOf("Time: " + second);
		// Rectangle2D stringBounds = font.getStringBounds(str,
		// g2.getFontRenderContext());
		// g2.drawString(String.valueOf(stringBounds.getWidth()), 100, 100);

		String str = String.valueOf(godziny + ":" + minuty + ":" + second);
		g2.drawString(String.valueOf(str), 190, 180);

		
		double katSekundy = (2 * Math.PI / 60.0) * second - Math.PI / 2.0;
		double promien = Math.min(bi.getWidth() / 2.2, bi.getHeight() / 2.2);
		Point2D punktNaTarczy = getPunktNaTarczy(katSekundy, punktSrotkowy, promien);
		Line2D.Double line = new Line2D.Double(punktSrotkowy, punktNaTarczy);
		g2.draw(line);

		double katminnuty = (2 * Math.PI / 60.0) * minuty - Math.PI / 2.0;
		double promien2 = Math.min(bi.getWidth() / 2.7, bi.getHeight() / 2.7);
		Point2D punktNaTarczy2 = getPunktNaTarczy(katminnuty, punktSrotkowy, promien2);

		double katgodziny = (2 * Math.PI / 12.0) * godziny - Math.PI / 2.0;
		double promien3 = Math.min(bi.getWidth() / 4.1, bi.getHeight() / 4.1);
		Point2D punktNaTarczy3 = getPunktNaTarczy(katgodziny, punktSrotkowy, promien3);

		// Point2D punkt1 = new Point2D.Double(100.5, 100.5);
		Line2D.Double line1 = new Line2D.Double(punktSrotkowy, punktNaTarczy2);
		g2.draw(line1);
		Line2D.Double line2 = new Line2D.Double(punktSrotkowy, punktNaTarczy3);
		g2.draw(line2);
		// Ellipse2D.Double ellipse = new Ellipse2D.Double(50.0, 50.0, 85, 35);
		// g2.fill(ellipse);

	}

	private Point2D getPunktNaTarczy(double kat, Point2D srodek, double promien) {
		double x = srodek.getX() + promien * Math.cos(kat);
		double y = srodek.getY() + promien * Math.sin(kat);
		return new Point2D.Double(x, y);
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// wygladzenie krawedzi
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(new Color(150, 100, 70, 135));
		// g2.fillOval(50, 50, 100, 150);
		g2.drawImage(bi, 0, 0, this);
	}

	public void start() {
		this.start = true;
		Thread thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		this.start = false;
	}

	@Override
	public void run() {
		int prevSecond = -1;
		while (start) {
			time.setTimeInMillis(System.currentTimeMillis());
			int second = time.get(Calendar.SECOND);
			if (second != prevSecond) {
				prevSecond = second;
				paintClock(g2bi, time);
				repaint();
			}

			try {
				TimeUnit.MILLISECONDS.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
