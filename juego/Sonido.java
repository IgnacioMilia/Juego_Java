package juego;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Sonido {
	private static Clip fondo;

	// Método para cargar el sonido
	public static void cargarSonido(String ruta) {
		try {
			File archivoSonido = new File(ruta); // Crear un objeto File a partir de la ruta del archivo de sonido
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(archivoSonido); // Obtener el flujo de
																								// entrada de audio a
																								// partir del archivo
			AudioFormat audioFormat = audioInputStream.getFormat(); // Obtener el formato de audio del archivo
			DataLine.Info info = new DataLine.Info(Clip.class, audioFormat); // Crear un objeto Info para especificar el
																				// tipo de línea de datos de audio
			fondo = (Clip) AudioSystem.getLine(info); // Obtener una instancia de Clip para reproducir el sonido
			fondo.open(audioInputStream); // Abrir el Clip y cargar el audio en él
		} catch (Exception e) {
			e.printStackTrace(); // Imprimir información de error en caso de excepción
		}
	}

	// Método para reproducir el sonido de fondo en bucle
	public static void reproducirSonido() {
		try {
			fondo.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Método para detener el sonido de fondo
	public static void detenerSonido() {
		try {
			fondo.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}