package 용사키우기2;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.*;
import java.awt.image.*;

public class BossBattle extends JPanel implements Serializable {
	private Player player = new Player(new Point(350,650));
	private mainclass main;
	private PressedKey keys = new PressedKey();
	private Thread thread;
	private Thread gamethread;
	private ArrayList<Thread> threads = new ArrayList<>();
	private ArrayList<Bullet> bulletlist = new ArrayList<>();
	private static BossBattle instance;
	public BossBattle(mainclass a) {
		main = a;
		setLayout(null);
		setPreferredSize(new Dimension(700,700));
		//setBackground(Color.WHITE);
		add(player);
		instance = this;
	}
	
	public void start() {
		bulletpattern3();
		gamethread = new Thread(new Runnable() {
			@Override
			public void run() {
				long time = 0L;
				Random r = new Random();
				while(true) {
					int a = r.nextInt(100)+1;
					System.out.println(a);
					if(a >= 65) {
						bulletpattern3();
					}
					time++;
				}
			}
		});
		//gamethread.start();
	}
	
	public void bulletpattern3() {
		Point point = new Point(0,700);
		new Thread(new Runnable() {
			@Override
			public void run() {
				int[] angles = {297,72};
				int cnt = 0;
				while(true) {
					if(point.x > 720 || point.y > 720 || point.x < 0 || point.y < 0) cnt++;
					Bullet e = new Bullet(point);
					add(e);
					e.repaint();
					double newX = (double)(point.x + 30 * Math.cos(Math.toRadians(angles[cnt])));
			        double newY = (double)(point.y + 30 * Math.sin(Math.toRadians(angles[cnt])));
			        point.x = (int) newX;
			        point.y = (int) newY;
			        try {
			        	Thread.sleep(30);
			        }
			        catch(Exception ae) {
			        	ae.printStackTrace();
			        }
				}
			}
		}).start();
	}
	
	public void spawnbullet2(Point spawnpoint, double velocity, double angle) {
		Thread a = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					for(int i = 0,cnt1=0; i<=12; i++) {
						for(int j = 0,cnt=0; j<12; j++,cnt+=30) {
							double a = new Random().nextDouble() * Math.PI * 2;
							//double angle1 = Math.atan2(player.getPos().y - 350, player.getPos().x - 350);
							int amo = i*30;
							
					        int x = spawnpoint.x + (int) (50 * Math.cos(Math.toRadians(amo)));
					        int y = spawnpoint.y + (int) (50 * Math.sin(Math.toRadians(amo)));
					        
					        
							Point aa = new Point((int)(spawnpoint.x+(int)50*Math.cos(Math.toRadians(amo))),(int)(spawnpoint.y+(int)50*Math.sin(Math.toRadians(amo))));
							Bullet e = new Bullet(aa,10,cnt,new Random().nextInt(20));
							bulletlist.add(e);
							add(e);
							e.repaint();
						}
						try {
							Thread.sleep(100);
						}catch(Exception e111) {
							e111.printStackTrace();
						}
					}
					try {
						Thread.sleep(300);
					}catch(Exception e111) {
						e111.printStackTrace();
					}
				}
			}
		});
		a.start();
		threads.add(a);
	}
	
	public void spawnbullet(Point spawnpoint, double velocity, double angle) {
		Thread a = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					double dx = player.getPos().x - spawnpoint.x;
			        double dy = player.getPos().y - spawnpoint.y;
			        
			        double angle = Math.atan2(dy, dx);
			        
			        if (angle < 0) {
			            angle += 2 * Math.PI;
			        }
					for(int i = 0; i<10; i++) {
						
						Bullet bullet = new Bullet(spawnpoint,5,Math.toDegrees(angle),0);
						add(bullet);
						try {
							Thread.sleep(56);
						}
						catch(Exception e) {
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(500);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		a.start();
		threads.add(a);
	}
	
	
	
	public void setlistener() {
		start();
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					int speed = 10;
					if(keys.isShift()) {
						speed = 4;
					}
					if(keys.isUp()) {
						player.setPos(new Point(player.getPos().x,player.getPos().y-speed < 0 ? 0 : player.getPos().y-speed));
					}
					if(keys.isDown()) {
						player.setPos(new Point(player.getPos().x,player.getPos().y+speed > 680 ? 680 : player.getPos().y+speed));
					}
					if(keys.isLeft()) {
						player.setPos(new Point(player.getPos().x-speed < 0 ? 0 : player.getPos().x-speed,player.getPos().y));
					}
					if(keys.isRight()) {
						player.setPos(new Point(player.getPos().x+speed > 700 ? 700 : player.getPos().x+speed,player.getPos().y));
					}
					try {
						Thread.sleep(18);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
		main.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					keys.setLeft(true);
					break;
				case KeyEvent.VK_RIGHT:
					keys.setRight(true);
					break;
				case KeyEvent.VK_UP:
					keys.setUp(true);
					break;
				case KeyEvent.VK_DOWN:
					keys.setDown(true);
					break;
				case KeyEvent.VK_SHIFT:
					keys.setShift(true);
					break;
				case KeyEvent.VK_C:
					spawnbullet(new Point(350,150),3.0f,70.0f);
					spawnbullet(new Point(600,150),3.0f,70.0f);
					spawnbullet(new Point(100,150),3.0f,70.0f);
					spawnbullet2(new Point(350,200),5.0f,0);
					break;
				case KeyEvent.VK_X:
					for(Thread a : threads)
						try {
							a.join();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					break;
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					keys.setLeft(false);
					break;
				case KeyEvent.VK_RIGHT:
					keys.setRight(false);
					break;
				case KeyEvent.VK_UP:
					keys.setUp(false);
					break;
				case KeyEvent.VK_DOWN:
					keys.setDown(false);
					break;
				case KeyEvent.VK_SHIFT:
					keys.setShift(false);
					break;
				}
			}
		});
	}
	
	public void paintComponent(Graphics graphic) {
		super.paintComponent(graphic);
		System.out.println(getComponents().length);
	}
	
	public void removebullet(Object_BulletHell a) {
		if(a == null) return;
		SwingUtilities.invokeLater(() -> {
			remove(a);
			validate();
		});
		revalidate();
	}
	
	public Player getPlayer() { return player; }
	
	public static BossBattle getInstance() {
		return instance;
	}
}

class Object_BulletHell extends JPanel {
	private Point pos = new Point(0,0);
	private BufferedImage img;
	private Path2D.Double hitbox;
	public Object_BulletHell(Point p, Ellipse2D.Double boxhit) {
		pos = p;
		hitbox = new Path2D.Double() {{append(boxhit,false);}};
		setBounds(pos.x-getHitBox().getBounds().width,pos.y-getHitBox().getBounds().height,getHitBox().getBounds().width*2,getHitBox().getBounds().height*2);
		setOpaque(false);
	}
	@Override
	public void paintComponent(Graphics graphic) {
		super.paintComponent(graphic);
		//graphic.drawImage(img,0,0,hitbox.getBounds().width*2,hitbox.getBounds().height*2,this);
		if(getPos().x > 720 || getPos().y > 720 || getPos().x < -20 || getPos().y < -21) return;
		graphic.drawImage(getImg(),0,0,getHitBox().getBounds().width*2,getHitBox().getBounds().height*2,0,0,getImg().getWidth(),getImg().getHeight(),this);
	}
	public void setPos(Point p) {
		pos = p;
		setLocation(pos.x-hitbox.getBounds().width,pos.y-hitbox.getBounds().height);
		repaint();
	}
	public Point getPos() { return pos; }
	public void setImg(BufferedImage i) { img = i; }
	public BufferedImage getImg() { return img; }
	public Path2D.Double getHitBox() { return hitbox; }
}

class Player extends Object_BulletHell {
	public Player(Point p) {
		super(p,new Ellipse2D.Double(p.x,p.y,10.0f,10.0f));
		try {
			setImg(ImageIO.read(new File("ahphDMx.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repaint();
	}
}

class PressedKey {
	private boolean isUp=false,isDown=false,isRight=false,isLeft=false,isShift=false;

	public boolean isUp() {
		return isUp;
	}

	public void setUp(boolean isUp) {
		this.isUp = isUp;
	}

	public boolean isDown() {
		return isDown;
	}

	public void setDown(boolean isDown) {
		this.isDown = isDown;
	}

	public boolean isRight() {
		return isRight;
	}

	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}

	public boolean isLeft() {
		return isLeft;
	}

	public void setLeft(boolean isLeft) {
		this.isLeft = isLeft;
	}
	
	public boolean isShift() {
		return isShift;
	}

	public void setShift(boolean isShift) {
		this.isShift = isShift;
	}
	
}

class Bullet extends Object_BulletHell {
	private double velocity = 0.0f;
	private double angle = 0.0f;
	private Thread thread;
	public Bullet(Point p, double velocity, double angle, int offset) {
		super(p,new Ellipse2D.Double(p.x,p.y,15.0f,15.0f));
		this.velocity = velocity;
		this.angle = angle;
		try {
			//setImg(ImageIO.read(new File("OyfMt1v.png")));
			setImg(ImageIO.read(new File("bullet.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repaint();
		move(offset);
	}
	
	public Bullet(Point p) {
		super(p,new Ellipse2D.Double(p.x,p.y,15.0f,15.0f));
		
		try {
			//setImg(ImageIO.read(new File("OyfMt1v.png")));
			setImg(ImageIO.read(new File("bullet.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repaint();
	}
	
	public void start(double v, double a) {
		this.velocity = v;
		this.angle = a;
		move(0);
	}
	
	
	@Override
	public void setPos(Point p) {
		super.setPos(p);
	}
	
	public void removebullet() {
		BossBattle.getInstance().removebullet(this);
	}
	
	public void placeabluecirclewherethespawnpointis() {
		System.out.println("no u");
	}
	
	public boolean checkintersect(Point p) {
		placeabluecirclewherethespawnpointis();
		double dx = BossBattle.getInstance().getPlayer().getPos().x - p.x;
	    double dy = BossBattle.getInstance().getPlayer().getPos().y - p.y;

	    double d = Math.sqrt((dy * dy) + (dx * dx));

	    if (d > (getHitBox().getBounds().width + BossBattle.getInstance().getPlayer().getHitBox().getBounds().width)) {
	    	return false;
	    } else if (d < Math.abs(getHitBox().getBounds().width - BossBattle.getInstance().getPlayer().getHitBox().getBounds().width)) {
	    	return false;
	    } else {
	    	return true;
	    }
//		if(BossBattle.aaa.getPlayer().getHitBox().intersects(getHitBox().getBounds2D())) {
//			BossBattle.aaa.remove(BossBattle.aaa.getPlayer());
//		}
	}
	
	public void move(int a) {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						double newX = (double)(getPos().x + velocity * Math.cos(Math.toRadians(angle+a)));
				        double newY = (double)(getPos().y + velocity * Math.sin(Math.toRadians(angle+a)));
				        if(newX > 720 || newY > 720 || newX < -20 || newY < -21) {
				        	removebullet();
				        	thread.join();
				        	break;
				        }
				        else {
				        	setPos(new Point((int)newX,(int)newY));
				        	if(checkintersect(getPos())) {
				        		//BossBattle.getInstance().remove(BossBattle.getInstance().getPlayer());
				        	}
				        	Thread.sleep((long) (velocity*2));
				        }
						
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}
}

class Bullet2 extends Object_BulletHell {
	private double velocity = 0.0f;
	private double angle = 0.0f;
	private Thread thread;
	public Bullet2(Point p, double velocity, double angle, int offset) {
		super(p,new Ellipse2D.Double(p.x,p.y,15.0f,7.0f));
		this.velocity = velocity;
		this.angle = angle;
		try {
			//setImg(ImageIO.read(new File("OyfMt1v.png")));
			setImg(ImageIO.read(new File("bullet2.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repaint();
		move(offset);
	}
	
	@Override
	public void setPos(Point p) {
		super.setPos(p);
	}
	
	public void removebullet() {
		BossBattle.getInstance().removebullet(this);
	}
	
	public void placeabluecirclewherethespawnpointis() {
		System.out.println("no u");
	}
	
	public boolean checkintersect(Point p) {
		placeabluecirclewherethespawnpointis();
		double dx = BossBattle.getInstance().getPlayer().getPos().x - p.x;
	    double dy = BossBattle.getInstance().getPlayer().getPos().y - p.y;

	    double d = Math.sqrt((dy * dy) + (dx * dx));

	    if (d > (getHitBox().getBounds().width + BossBattle.getInstance().getPlayer().getHitBox().getBounds().width)) {
	    	return false;
	    } else if (d < Math.abs(getHitBox().getBounds().width - BossBattle.getInstance().getPlayer().getHitBox().getBounds().width)) {
	    	return false;
	    } else {
	    	return true;
	    }
//		if(BossBattle.aaa.getPlayer().getHitBox().intersects(getHitBox().getBounds2D())) {
//			BossBattle.aaa.remove(BossBattle.aaa.getPlayer());
//		}
	}
	
	
	public void move(int a) {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						double newX = (double)(getPos().x + velocity * Math.cos(Math.toRadians(angle+a)));
				        double newY = (double)(getPos().y + velocity * Math.sin(Math.toRadians(angle+a)));
				        if(newX > 720 || newY > 720 || newX < -20 || newY < -21) {
				        	removebullet();
				        	thread.join();
				        	break;
				        }
				        else {
				        	setPos(new Point((int)newX,(int)newY));
				        	if(checkintersect(getPos())) {
				        		//BossBattle.getInstance().remove(BossBattle.getInstance().getPlayer());
				        	}
				        	Thread.sleep((long) (velocity*2));
				        }
						
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}
}