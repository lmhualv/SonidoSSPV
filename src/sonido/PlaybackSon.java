/*
 * @author Luis M. Huesca 
Clase encargada de la reproduccion del audio binario
 */

package sonido;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class PlaybackSon {
    
    //variables globales de sonido
    //Source -> Input
    private static SourceDataLine lineIn;           //fs=30000, sampleSize=8, channels=1, unsigned, bigEndian
    private static AudioFormat formatIn = new AudioFormat(30000, 8, 1, false, true); // format para input
    private static DataLine.Info datalineInfoIn = new DataLine.Info(SourceDataLine.class, formatIn);
    
    /**
     * Método <code>private</code> que toma un <code>byte[]</code> con informacion del audio binario y lo reproduce en la salida default de línea del sistema. -LMHA
     */
    public static void playLine(byte[] audioBuf) {
        //System.out.println("Ejecutando..."); //DEBUG
        //las variables propias de audio eran locales a la clase, pero se volvieron globales para refinar los metdodos 18-May-17
        
        
        if(!AudioSystem.isLineSupported(datalineInfoIn))
            System.err.println("Error Line, no disponible");
            
        try {
            
            lineIn = (SourceDataLine) AudioSystem.getLine(datalineInfoIn); //in
            lineIn.open(formatIn,1024); //aquí abre la linea y está listo para reproducir
            //byte[] audioBuffer=generadorAudioBuffer(); //3600 seg = 1 hr; fs=30000
            byte[] audioBuffer=audioBuf; //3600 seg = 1 hr; fs=30000
            lineIn.write(audioBuffer, 0, audioBuffer.length);
           
            lineIn.start();
            
            
           // if(lineIn.isActive()||lineIn.isRunning()) System.out.print("Activa"); //DEBUG
            
            lineIn.drain();
            lineIn.close();
            
            
        } catch (Exception ex) 
        {
            System.err.println("Error Line, no soportada. Detalles: \n"+ex);
            ex.printStackTrace();
        }
    }//fin de PlayLine
    
    
}
