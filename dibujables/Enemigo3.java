package dibujables;

import java.awt.geom.Point2D;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import entorno.Entorno;
import entorno.Herramientas;
import juego.DisenioDeNivel;
import juego.Juego;
import tools.ManipuladorDeImagenes;
import java.util.ArrayList;

public class Enemigo3 extends Dibujable implements ActionListener{
	public final Categoria [] categorias = {Categoria.ENEMIGO};
	protected double velocidad = 0.1;
	protected Point2D.Double direccion = new Point2D.Double(0, 0);
	private Random rand = new Random();
	private static Image parte1, parte2, parte3, parte4, parte5;
	int asd;
	double giro;
	double size;
	long startAt;
	int retraso = 4000;
	Timer timer;
	boolean ignicion;
	long pivot = 20000;
	boolean subiendo;
	boolean bossInicializado;
	double comba;
	String soundsPlayed = "";
	private Entorno entorno;
	private ArrayList<Dibujable> dibujablesAtraidos;
	private String notDoneYetVar;
	private DibujableBasico fuego1, fuego2;
	
	public Enemigo3(double x, double y) {
		super(x, y);
		this.zIndex = 0;
		this.entorno = Juego.entorno;
		this.setSprite(Herramientas.cargarImagen("recursos/nodriza/2.png"));
		this.setSprite(ManipuladorDeImagenes.resizeImage(getSprite(), 800, 300));
		this.ancho = 800;
		this.alto = 300;
		this.startAt = System.currentTimeMillis();
		this.bossInicializado = false;
		this.comba = 0;
		this.ignicion = false;
		this.notDoneYetVar = "";
		this.giro = 0;
		this.size = 1;
		this.subiendo = false;
		this.dibujablesAtraidos = new ArrayList<Dibujable>();
		parte1 = Herramientas.cargarImagen("recursos/nodriza/7.png");
		parte1 = ManipuladorDeImagenes.resizeImage(parte1, 400, 400);
		parte2 = Herramientas.cargarImagen("recursos/nodriza/6.png");
		parte3 = ManipuladorDeImagenes.espejar(getSprite());
		parte4 = Herramientas.cargarImagen("recursos/nodriza/4.png");
		parte4 = ManipuladorDeImagenes.resizeImage(parte4, 400, 200);
		parte5 = ManipuladorDeImagenes.espejar(parte4);
	}
	@Override
	public void dibujad() {
		dibujad(rotacion, escala);
	}
	@Override
	public void dibujad(double rotacion, double escala) {
		Entorno e = Juego.entorno;
		if(!bossInicializado) {
			return;
		}
		long tiempoTranscurrido = System.currentTimeMillis() - startAt - retraso;
		if(y > 260) {
			subiendo = true;
		}
		if(y < 180 ) {
			subiendo = false;
		}
		if(subiendo) {
			y -= 0.4;
		}else {
			y += 0.3;
		}
		if(tiempoTranscurrido > 18800) {
			double asd = 400;
			double dsa = 200;
			size += ignicion ? 0.0005 : -0.00092;
			giro = ignicion ? giro - 0.1 : giro - 0.005;
			e.dibujarImagen(parte1, x , y - 270 , 0, 1.2);
			e.dibujarImagen(parte1, x , y - 250 , 0, 1);
			e.dibujarImagen(parte4, x +270, y -dsa , 0);
			e.dibujarImagen(parte5, x - 270, y -dsa , 0);
			e.dibujarImagen(parte1, x , y - 100 , 0);
			e.dibujarImagen(parte2, x, y + 20, giro, size);
			e.dibujarImagen(getSprite(), x - asd, y - 50, 3/4);
			e.dibujarImagen(parte3, x + asd, y -50 , 1/4);
			
			if(rand.nextInt(100) < 10)
				e.dibujarCirculo(x - 28, y -238 , 4, new Color(180,180,80));
			if(rand.nextInt(100) < 10)
				e.dibujarCirculo(x - 40, y -270 , 3, new Color(180,180,80));
			if(rand.nextInt(100) < 10)
				e.dibujarCirculo(x - 45, y -268 , 3, new Color(180,180,80));
			if(rand.nextInt(100) < 20)
				e.dibujarCirculo(x + 100, y -265 , 3, new Color(180,180,80));
			if(rand.nextInt(100) < 25)
				e.dibujarTriangulo(x + 35, y -245 , 4, 4, 8, new Color(180,180,80));
			if(rand.nextInt(100) < 15)
				e.dibujarCirculo(x + 31, y -275 , 3, new Color(180,180,80));
			if(rand.nextInt(100) < 10)
				e.dibujarCirculo(x - 35, y -280 , 3, new Color(180,180,80));

			if(!soundsPlayed.contains("musicaBoss.wav")) {
				Herramientas.play("recursos/nodriza/sonido/musicaBoss.wav");
				soundsPlayed += ", musicaBoss.wav";
			}
			if(tiempoTranscurrido > 23000) {
				if(!soundsPlayed.contains("interferencia.wav")) {
					Herramientas.play("recursos/nodriza/sonido/interferencia.wav");
					soundsPlayed += ", interferencia.wav";
				}
				if(tiempoTranscurrido > 28500) {
					if(!soundsPlayed.contains("bossVoice.wav")) {
						Herramientas.play("recursos/nodriza/sonido/bossVoice.wav");
						soundsPlayed += ", bossVoice.wav";
						comba = getY();
					}
				}
			}
		}
		if(tiempoTranscurrido > 35500) {
			atraed(Juego.miNave);
			for(Dibujable a : dibujablesAtraidos) {
				atraed(a);
				if(Math.abs(a.y - y) < 40) {
					a.morid();
				}
			}
			dibujablesAtraidos.retainAll(Colisionable.instancias);
			if(!ignicion) {
				Herramientas.play("recursos/nodriza/sonido/explosion.wav");
				e.dibujarRectangulo(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2, Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2, Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight(), 0, new Color(80,80,80));
				parte2 = Herramientas.cargarImagen("recursos/nodriza/5.png").getScaledInstance(500, 500, 0);
				size = 0.05;
				ignicion = true;
			}
			if(System.currentTimeMillis() - startAt > pivot+150) {
				x += new Random().nextDouble(8) -4;
				y -= new Random().nextDouble(8) -4;
				int rand = new Random().nextInt(250);
				if(rand < 5) {
					if(rand < 2) {
						size += 0.0005;
					}
					Herramientas.play("recursos/nodriza/sonido/explosion.wav");
					e.dibujarRectangulo(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2, Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2, Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight(), 0, new Color(80,80,80));
				}
				if(size > 0.3) {
					size -= 0.05;
					Herramientas.play("recursos/nodriza/sonido/explosion.wav");
					e.dibujarRectangulo(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2, Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2, Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight(), 0, new Color(80,80,80));
				}
			}
		}
	}
	public void moved() {
		this.y -= 0.01;
	}

	@Override
	public void actualizad() {
		if(!bossInicializado) {
			mostrarIntro();
		}else {
			moved();
		}
	}
	
	private double getRotacionDeAtraido(Dibujable atr) {
		return Herramientas.radianes(180 + (atr.x < x ? 25 : -25)); 
	}
	public void morid() {
		timer.stop();
		dibujablesAtraidos.clear();
		super.morid();
	}
	public void atraed(Dibujable atraido) {
		if(timer == null) {
			iniciarTimer();
		}
		if(atraido.getPosicion().distance(this.getPosicion()) < 50) {
			atraido.morid();
			if(atraido == Juego.miNave) {
				Insets ins = Juego.entorno.getInsets();
		        Juego.entorno.setSize(800 + ins.left + ins.right, 600 + ins.bottom + ins.top);
		        Juego.entorno.setLocationRelativeTo(null);
				Juego.perdido = true;
				morid();
			}
			return;
		}
		Point2D.Double dir;
		if(atraido == Juego.miNave) {
			Juego.miNave.setVelocidad(1.3);
			hazLoQueQuieras();
			
			if(Math.abs(atraido.x - x) > 120) {
				atraido.rotacion = getRotacionDeAtraido(atraido);
				if(fuego1 != null)
					fuego1.x = fuego2.x = atraido.x + (atraido.x < x ? 25 : -25);
			}else {
				atraido.rotacion = Herramientas.radianes(180);
			}
			if (!Juego.miNave.colisionado && !Juego.miNave.estaMuriendo() && (entorno.estaPresionada(entorno.TECLA_ABAJO) || entorno.estaPresionada('S'))) {
				dir = (Colisionable.alejarse(atraido.x, atraido.y, x, y));
				dir.y *= 0.1;
				dir.x *= 0.1;
			}else {
				
				if(Juego.miNave.estaMuriendo() || Juego.miNave == null) {
					Insets ins = Juego.entorno.getInsets();
			        Juego.entorno.setSize((int)Juego.entorno.ancho() + ins.left + ins.right, (int)Juego.entorno.alto() + ins.bottom + ins.top);
			        Juego.entorno.setLocationRelativeTo(null);
					morid();
					return;
				}
				dir = (Colisionable.acercarse(atraido.x, atraido.y, x, y));
			}
			if (!Juego.miNave.estaMuriendo() && (entorno.estaPresionada(entorno.TECLA_DERECHA) || entorno.estaPresionada('D'))) {
				Juego.miNave.x += 2;
			} else if (!Juego.miNave.estaMuriendo() && (entorno.estaPresionada(entorno.TECLA_IZQUIERDA) || entorno.estaPresionada('A'))) {
				Juego.miNave.x -= 2;
			}
		}else {
			dir = (Colisionable.acercarse(atraido.x, atraido.y, x, y));
		}
		double velocidad = 0.01;
		atraido.x += dir.x * velocidad;
		atraido.y += dir.y * velocidad;
		atraido.escala =  (atraido.ancho * atraido.y) / (atraido.ancho * 700);
	}
	
	private void hazLoQueQuieras() {
		Nave nave = Juego.miNave;
		nave.radioDeColision = 0.3;
		nave.categorias.add(Categoria.INVULNERABLE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		nave.sobrevivirMasAllaDelBordeInferior = true;
		if(nave.getY() >= screenSize.getHeight()) {
			Insets ins = Juego.entorno.getInsets();
	        Juego.entorno.setSize(800 + ins.left + ins.right, 600 + ins.bottom + ins.top);
	        Juego.entorno.setLocationRelativeTo(null);
			Juego.ganado = true;
			this.morid();
			return;
		}
		try {
			if(nave.y >= 600 && !Juego.ganado && !Juego.perdido) {
				Juego.entorno.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
				Juego.entorno.setLocationRelativeTo(null);
				if(noHechoAun("acomodar_dibujables")) {
					for(Dibujable d : Dibujable.instancias) {
						d.x += (screenSize.getWidth() - 800) / 2;
					}
				}
				if(noHechoAun("agrandar_fondo")) {
					Field f = Juego.class.getDeclaredField("fondo");
					f.setAccessible(true);
					f.set(Juego.juego, ((Image)f.get(Juego.juego)).getScaledInstance( (int) screenSize.getWidth(), (int) screenSize.getHeight(), Image.SCALE_DEFAULT));
				}
			}
			Method method = Nave.class.getDeclaredMethod("cargarSonido", String.class);
			method.setAccessible(true);
			Field field = Nave.class.getDeclaredField("sonidoDisparo");
			field.setAccessible(true);
			field.set(nave, method.invoke(nave, "src/recursos/sonidos/turbo.wav"));
			
			field = Nave.class.getDeclaredField("proyectil");
			field.setAccessible(true);
			ProyectilPj p = (ProyectilPj) field.get(nave);
			
			if(p != null) {
				
				p.setVelocidadProyectil(-20);
				p.visible = false;
				nave.setVelocidad(nave.getVelocidad());
				if(p.getY() > screenSize.height) {
					p.morid();
					field.set(nave, null);
				}
				else {
				}
				nave.setY(nave.getY() + 4);
				if(noHechoAun("instanciar_fuegos")) {
					fuego1 = new DibujableBasico(Herramientas.cargarImagen("recursos/fuego1.png"), nave.getX(), nave.getY() - nave.getRadio(), 25, 80);
					fuego2 = new DibujableBasico(Herramientas.cargarImagen("recursos/fuego2.png"), nave.getX(), nave.getY() - nave.getRadio(), 25, 80);
					fuego1.sobrevivirMasAllaDelBordeInferior = fuego2.sobrevivirMasAllaDelBordeInferior = true;
					fuego1.zIndex = fuego2.zIndex = 2;
				}
				if(rand.nextBoolean()) {
					fuego1.visible = false;
					fuego2.visible = true;
				}else {
					fuego1.visible = true;
					fuego2.visible = false;
				}
				fuego1.x = fuego2.x = nave.x;
				fuego1.y = fuego2.y = nave.y - nave.getRadio();
				fuego1.rotacion = fuego2.rotacion = nave.rotacion + Herramientas.radianes(180);
			}
			else {
				if(fuego1 != null) {
					fuego1.visible = false;
					fuego2.visible = false;
				}
			}
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			System.out.println(e.getMessage());
		}
		DisenioDeNivel.puntaje--;
	}
	
	private boolean noHechoAun(String s) {
		if(!notDoneYetVar.contains(s)) {
			notDoneYetVar += s + ", ";
			return true;
		}
		return false;
	}
	public void mostrarIntro() {
		long tiempoTranscurrido = System.currentTimeMillis() - startAt - retraso;
		if(tiempoTranscurrido < 1500 && tiempoTranscurrido > 0) {
			entorno.dibujarCirculo(150, 150, 30, new Color(255,255,255));
			entorno.dibujarCirculo(150, 150, 29, new Color(0,0,0));
			entorno.dibujarCirculo(150, 150, 22, new Color(255,255,255));
			entorno.dibujarCirculo(150, 150, 21, new Color(0,0,0));
			entorno.dibujarCirculo(150, 150, 15, new Color(255,255,255));
			entorno.dibujarCirculo(150, 150, 14, new Color(0,0,0));
			entorno.dibujarImagen(Herramientas.cargarImagen("recursos/star.png"), 150, 150, 0, 0.0005 * tiempoTranscurrido);
		}
		if(tiempoTranscurrido > 1500 && tiempoTranscurrido < 1650) {
			if(!soundsPlayed.contains("estrella.wav")) {
				Herramientas.play("recursos/nodriza/sonido/estrella.wav");
				soundsPlayed += ", estrella.wav";
			}
			
			entorno.dibujarImagen(Herramientas.cargarImagen("recursos/star.png"), 150, 150, 0, 2.5);
		}
		if(tiempoTranscurrido > 1750 && tiempoTranscurrido < 3400) {
			entorno.dibujarCirculo(150, 150, 2, new Color(255,255,255));
		}
		if(tiempoTranscurrido > 4000 && tiempoTranscurrido < 19500) {
			if(!soundsPlayed.contains("suspenso.wav")) {
				Herramientas.play("recursos/nodriza/sonido/suspenso.wav");
				soundsPlayed += ", suspenso.wav";
			}
			entorno.dibujarImagen(Herramientas.cargarImagen("recursos/nodriza/2.png"), 150 + comba * 0.2, 150, 0, 0.000006 * tiempoTranscurrido - 0.02);
			if(tiempoTranscurrido > 18500 &&  !soundsPlayed.contains("motor.wav")) {
				Herramientas.play("recursos/nodriza/sonido/motor.wav");
				soundsPlayed += ", motor.wav";
			}
		}
		if(tiempoTranscurrido > 19500 && tiempoTranscurrido < 20070) {
			comba += 55;
			entorno.dibujarImagen(Herramientas.cargarImagen("recursos/nodriza/2.png"), 150 + comba*1.8, 150 + comba*2.5, 0, 0.6);
		}
		if(tiempoTranscurrido > 20070) {
			bossInicializado = true;
		}
	}
	// Crea un Timer para que ejecute actionPerformed cada 200 milisegundos
	private void iniciarTimer() {
		timer = new Timer(200, this);
        timer.start();    
	}
	// Crea los asteroides cada el tiempo especificado al instanciar el Timer
	@Override
	public void actionPerformed(ActionEvent e) {
		Asteroide a = new Asteroide(rand.nextInt(10) < 2 ? Juego.miNave.x : rand.nextInt(4000) - 2000, 1300, 0d, 0d);
		a.categorias.add(Categoria.BLANDO);
		Point2D.Double dir = Colisionable.acercarse(a.x, a.y, this.x, this.y);
		a.sobrevivirMasAllaDelBordeInferior = true;
		for(int i = 0; i < 15; i++) {
			double d = rand.nextDouble(0.7)-0.35;
			Asteroide c = new Asteroide(a.x +dir.x*d, a.y-a.getRadio()+dir.y*d-0.5, 10, 10, 0,-1);
			c.sobrevivirMasAllaDelBordeInferior = true;
			c.colisionesDeshabilitadas = true;
			dibujablesAtraidos.add(c);
		}
		dibujablesAtraidos.add(a);
	}
}
