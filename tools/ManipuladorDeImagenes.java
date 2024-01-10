package tools;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

public class ManipuladorDeImagenes {
	
	// Código cortesía de GPT
	public static BufferedImage espejar(Image img) {
		BufferedImage originalImage = toBufferedImage(img);
		BufferedImage mirroredImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
				BufferedImage.TYPE_INT_ARGB);

		// Obtener los objetos Graphics2D de ambas imágenes
		Graphics2D originalGraphics = originalImage.createGraphics();
		Graphics2D mirroredGraphics = mirroredImage.createGraphics();

		// Espejar la imagen horizontalmente
		AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
		transform.translate(-originalImage.getWidth(), 0);
		mirroredGraphics.drawImage(originalImage, transform, null);
		return mirroredImage;
	}

	// Método auxiliar para convertir Image a BufferedImage
	private static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		// Crea una BufferedImage a partir de una Image
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		return bufferedImage;
	}

	public static BufferedImage removedMargen(Image image) {
		BufferedImage bufferedImage = toBufferedImage(image);
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		int top = 0;
		int bottom = height - 1;
		int left = 0;
		int right = width - 1;

		// Busca el borde superior
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (bufferedImage.getRGB(x, y) != 0) {
					top = y;
					break;
				}
			}
			if (top != 0) {
				break;
			}
		}

		// Busca el borde inferior
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				if (bufferedImage.getRGB(x, y) != 0) {
					bottom = y;
					break;
				}
			}
			if (bottom != height - 1) {
				break;
			}
		}

		// Busca el borde izquierdo
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (bufferedImage.getRGB(x, y) != 0) {
					left = x;
					break;
				}
			}
			if (left != 0) {
				break;
			}
		}

		// Busca el borde derecho
		for (int x = width - 1; x >= 0; x--) {
			for (int y = 0; y < height; y++) {
				if (bufferedImage.getRGB(x, y) != 0) {
					right = x;
					break;
				}
			}
			if (right != width - 1) {
				break;
			}
		}

		// Crea una imagen recortada sin los bordes transparentes
		int newWidth = right - left + 1;
		int newHeight = bottom - top + 1;
		BufferedImage croppedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = croppedImage.createGraphics();
		g2d.drawImage(bufferedImage, 0, 0, newWidth, newHeight, left, top, right + 1, bottom + 1, null);
		g2d.dispose();
		return croppedImage;
	}

	// Devuelve una lista con los puntos que no son transparentes del PNG almacenado
	// en 'path'
	public static ArrayList<Point> getPuntosDibujadosFromPNG(Image img) {
		BufferedImage image = toBufferedImage(img);
		ArrayList<Point> noTransparentes = new ArrayList<Point>();
		int izq = 0;
		int der = 0;
		for (int i = 0; i < image.getWidth(); i += 2) {
			for (int j = 0; j < image.getHeight(); j += 2) {
				int pixel = image.getRGB(i, j);
				if ((pixel & 0xff000000) != 0) { // Verificar si el pixel NO es transparente
//					if(izq == 0) {
//						izq = j;
//					}
//					der = j;
					noTransparentes.add(new Point(i, j));
				}
			}
//			if(izq > 0) {
//				noTransparentes.add(new Point(i, izq));
//				noTransparentes.add(new Point(i, der));
//			}
//			izq = 0;
//			der = 0;
		}
		return noTransparentes;
	}

	public static BufferedImage resizeImage(Image image, int newWidth, int newHeight) {
		BufferedImage img = toBufferedImage(image);
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, img.getType());
		Graphics2D g = resizedImage.createGraphics();
		AffineTransform transform = AffineTransform.getScaleInstance((double) newWidth / img.getWidth(),
				(double) newHeight / img.getHeight());
		AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
		g.drawImage(operation.filter(img, null), 0, 0, null);
		g.dispose();
		return resizedImage;
	}

	public static BufferedImage rotateImage(Image img, double angle) {
		BufferedImage image = (BufferedImage) img;
		// Calcula el seno y coseno del ángulo
		double sin = Math.abs(Math.sin(angle));
		double cos = Math.abs(Math.cos(angle));
		// Calcula la nueva anchura y altura de la imagen rotada
		int newWidth = (int) Math.round(image.getWidth() * cos + image.getHeight() * sin);
		int newHeight = (int) Math.round(image.getWidth() * sin + image.getHeight() * cos);

		// Crea la nueva imagen
		BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, image.getType());

		// Obtiene el contexto de gráficos para la imagen rotada
		Graphics2D g2d = rotatedImage.createGraphics();

		// Rota la imagen al ángulo especificado
		AffineTransform at = new AffineTransform();
		at.translate((newWidth - image.getWidth()) / 2, (newHeight - image.getHeight()) / 2);
		at.rotate(angle, image.getWidth() / 2, image.getHeight() / 2);
		g2d.setTransform(at);
		g2d.drawImage(image, 0, 0, null);

		// Libera los recursos del contexto de gráficos
		g2d.dispose();

		// Retorna la imagen rotada
		return rotatedImage;
	}
}