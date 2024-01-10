package dibujables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import entorno.Entorno;
import entorno.Herramientas;
import juego.Juego;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

public abstract class Dibujable{
	protected final String IMG_DIR = "recursos/";
	protected String simpleClassName;
	// Posición
	protected double x;
	protected double y;
	// Tamaño
	protected double ancho;
	protected double alto;
	// Lista con todas las instancias de las subclases
	protected static ArrayList<Dibujable> instancias = new ArrayList<Dibujable>();
	// Listas auxiliares para evitar error por modificar al recorrer la lista
	protected static ArrayList<Dibujable> nuevasInstancias = new ArrayList<Dibujable>();
	protected static ArrayList<Dibujable> removeInstancias = new ArrayList<Dibujable>();
	// Imagen que representa al objeto
	protected Image sprite;
	// Porcentaje del tamaño de la imagen original con el que se mostrará en pantalla
	protected double escala = 1d;
	// Rotación de la imagen
	protected double rotacion = 0;
	// Puntos no-transparentes de la imagen
	protected ArrayList<Point> superficie = new ArrayList<Point>();
	// Determina si se muestra o no en pantalla la imagen
	protected boolean visible = true;
	// Dirección en la que variará la posición
	protected double dx;
	protected double dy;
	// Punto con coordenadas tipo double para enviar y recibir de los métodos la dirección con mayor comodidad
	protected Point2D.Double direccion;
	// Para saber si está siendo colisionado
	protected boolean colisionado = false;
	// Pueden pertenecer categorias que determinen su relación con los de las distintas categorias.
	protected ArrayList<Categoria> categorias = new ArrayList<Categoria>();
	// Una coordenada a la que se dirigirá el objeto. No utilizado actualmente.
	protected Point2D.Double destino;
	// Un índice que determina el orden en el que serán dibujados los objetos en pantalla. Los de menor índice se dibujan primero.
	protected int zIndex = 10;
	// Marca de tiempo para controlar las animaciones de muerte
	protected long muertoAl;
	protected double duracionDeMuerte = 400;
	// Para evitar colisiones al estar mostrando la animación de muerte
	protected boolean colisionesDeshabilitadas;
	// 
	protected boolean sobrevivirMasAllaDelBordeInferior;
	
	// Setea la posición, la imagen por defecto, y agrega la nueva instancia a la lista de clase.
	public Dibujable(double x, double y) {
		this.simpleClassName = getClass().getSimpleName();
		this.setX(x);
		this.y = y;
		try {
			this.sprite = Herramientas.cargarImagen("recursos/"+ simpleClassName +".png");
			if (sprite != null) {
				this.ancho = sprite.getWidth(null);
				this.alto = sprite.getHeight(null);
			}
		}
		catch(Exception e) {
//			System.out.println(e.getMessage());
		}
		nuevasInstancias.add(this);
	}

	// Lo mismo que el anterior pero luego setea tamaño
	public Dibujable(double x, double y, double ancho, double alto) {
		this(x, y);
		if(sprite != null) {
			this.sprite = sprite.getScaledInstance((int)ancho, (int)alto, Image.SCALE_DEFAULT);
		}
		this.ancho = ancho;
		this.alto = alto;
	}

	// Método que implementarán las instancias que se ejecutará al llamar a actualizarTodo
	public abstract void actualizad();
	
	// Contar instancias de determinada clase
	public static int getCantidadDeInstanciasDe(String className) {
		int cnt = 0;
		for(Dibujable d : Dibujable.instancias) {
			if(d.simpleClassName.equals(className)) {
				cnt++;
			}
		}
		return cnt;
	}

	// Método que se ejecuta en cada ciclo del temporizador.
	// Primero manda a revisar las colisiones y luego llama a actualizad() y a dibujad() de cada instancia
	// Si la posición atraviesa el borde inferior de la pantalla, desreferencia al objeto con el método morid()
	// Finalmente actualiza la lista de instancias
	public static void actualizarTodo(Entorno entorno) {
		// Intento de solucionar posible acceso de múltiples hilos simultaneamente al método
//		if (!removeInstancias.isEmpty()) {	// asteroides_orig hacía que sea true porque llamaba a su metodo morid sin estar dentro de este método 
//			return;
//		}
		Colisionable.actualizarTodo();
		for (Dibujable d : Dibujable.instancias) {
			if (!d.sobrevivirMasAllaDelBordeInferior && d.getBordeSuperior() >= 600) {
				d.morid();
			}
			d.actualizad();
			if (d.visible) {
				d.dibujad();
			}
		}
	    actualizarListasDeInstancias();
	}

	// Agrega y quita las referencias de instancias de las subclases que corresponda
	private static void actualizarListasDeInstancias() {
		instancias.addAll(nuevasInstancias);
		instancias.removeAll(removeInstancias);
		// Ordena el resultado dependiendo de sus zIndex
		Collections.sort(instancias, new Comparator<Dibujable>() {
	        @Override
	        public int compare(Dibujable d1, Dibujable d2) {
	        	if(d1.zIndex == d2.zIndex) {
	    			return 0;
	    		}
	            return d1.zIndex < d2.zIndex ? -1 : 1;
	        }
	    });
		Colisionable.actualizarListaDeInstancias(instancias);
		nuevasInstancias.clear();
		removeInstancias.clear();
	}

	// Agrega su propia referencia a la lista de las que serán eliminadas
	protected void morid() {
		removeInstancias.add(this);
	}

	// Método por defecto que muestra la imagen almacenada en 'sprite' en pantalla
	public void dibujad() {
		dibujad(this.getRotacion(), this.escala);
	}

	public void dibujad(double angulo, double escala) {
		Entorno e = Juego.entorno;
		if (getSprite() != null) {
			e.dibujarImagen(getSprite(), getX(), getY(), angulo, escala);
		} else {
			e.dibujarRectangulo(x, y, ancho, alto, 0, new Color(255, 255, 255));
		}
	}
	
	// Limpia por completo la lista de instancias
	public static void eliminarTodos() {
	    instancias.clear();
	    nuevasInstancias.clear();
	    removeInstancias.clear();
	    Colisionable.actualizarListaDeInstancias(instancias);
	}

	// Para obtener cómodamente los bordes a partir de la posición y el tamaño
	public double getBordeDerecho() {
		return this.x + this.ancho / 2;
	}

	public double getBordeInferior() {
		return this.y + this.alto / 2;
	}

	public double getBordeSuperior() {
		return this.y - this.alto / 2;
	}

	public double getBordeIzquierdo() {
		return this.x - this.ancho / 2;
	}

	// Agregar destino. No utilizado actualmente
	protected void idA(Double destino) {
		if (this.destino == null) {
			this.destino = destino;
		}
	}
	// Comprobar si llegó al destino. No utilizado actualmente
	protected boolean destinoAlcanzado() {
		return x == destino.x && y == destino.y;
	}

	public Dibujable hide() {
		this.visible = false;
		return this;
	}
	
	// getters y setters
	public Point2D.Double getDireccion() {
		return direccion;
	}

	public void setDireccion(Point2D.Double direccion) {
		this.direccion = direccion;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public double getAncho() {
		return ancho;
	}

	public void setAncho(double d) {
		this.ancho = (int) d;
	}

	public double getAlto() {
		return alto;
	}

	public void setAlto(double d) {
		this.alto = (int) d;
	}

	public static ArrayList<Dibujable> getInstancias() {
		return instancias;
	}

	public static void setInstancias(ArrayList<Dibujable> instancias) {
		Dibujable.instancias = instancias;
	}

	public Image getSprite() {
		return sprite;
	}

	public void setSprite(Image image) {
		this.sprite =  image;
	}

	public double getEscala() {
		return escala;
	}

	public void setEscala(double escala) {
		this.escala = escala;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Point2D.Double getPosicion() {
		return new Point2D.Double(x, y);
	}

	public double getRadio() {
		return ancho * escala / 2;
	}

	public void setColisionado(boolean b) {
		colisionado = b;
	}

	public ArrayList<Categoria> getCategorias() {
		return this.categorias;
	}
	public double getRotacion() {
		return rotacion;
	}

	public void setRotacion(double rotacion) {
		this.rotacion = rotacion;
	}

	public int getzIndex() {
		return zIndex;
	}

	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}
	
	public boolean estaMuriendo() {
		return  muertoAl != 0;
	}
	public boolean terminoLaAnimacionDeMuerte() {
		return estaMuriendo() && muertoAl + duracionDeMuerte < System.currentTimeMillis();
	}
}