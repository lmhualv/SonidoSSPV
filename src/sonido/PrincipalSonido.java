package sonido;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
 

/**
 *
 * @author Luis M. Huesca
 */
public class PrincipalSonido {

    
        public static long startMideTiempo(){
            long startTime = 0;
            return startTime = System.nanoTime();
        }
        
        public static void stopDispTiempo(long startTime){
        long difference = System.nanoTime() - startTime;
        //Sytsem.out.println("Total execution time: "+String.format("%d min, %d sec",TimeUnit.NANOSECONDS.toHours(difference),TimeUnit.NANOSECONDS.toSeconds(difference) - TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(difference))));
        }
        
        
    
    private static final String OUTPUT_FILE_NAME = "C:/Audios/AudioTests_";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, IOException, WAVFileException {
        
        for (int i = 0; i < 100; i++) {
        
        long startTime = PrincipalSonido.startMideTiempo();
            
            
        csvReader csvr = new csvReader();
        String urlcsv = "C:/Users/pc07/Documents/NetBeansProjects/JavaWAV/AUDIO.csv";
        String separador = ",";
        byte [] arreglobyte = new byte[1024];
    
        arreglobyte = csvr.leeHaciaUByte(urlcsv, separador);
        
        //csvr.muestraDatos(arreglobyte);  //DEBUG
        
        PlaybackSon.playLine(arreglobyte);
        
        //parametros de audio de archivo WAV
         int sampleRate = 30000;		
        double duration = 0.034133333333333333333;		
        int bits = 8;
	long numFrames = (long)(duration * sampleRate);
        
        //para añadir fecha y hora al final del archivo
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss_");
        Date date = new Date();
        String fecha_hora = dateFormat.format(date);
        String OutputFileName = OUTPUT_FILE_NAME.concat(fecha_hora+"fs"+sampleRate+"_"+bits+"bits.wav");
        EscritorWAV escWAV = EscritorWAV.newWavFile(new File(OutputFileName), 1, numFrames, bits, sampleRate);  //modificado        
        byte[] buffer = new byte[100];
                      
	long frameCounter = 0;

        //System.out.println("Valores:\nnumframes= "+numFrames+"\n");
        
                        
        // Loop until all frames written
        while (frameCounter < numFrames)
	{
	// Determine how many frames to write, up to a maximum of the buffer size
	long remaining = escWAV.getFramesRemaining();
	int toWrite = (remaining > 100) ? 100 : (int) remaining;
                                    
	// Fill the buffer, one tone per channel
	for (int s=0 ; s<toWrite ; s++, frameCounter++)
	{
                buffer[s] = arreglobyte[s];
	}

	// Write the buffer
	escWAV.writeFrames(buffer, toWrite);
                                
        //DEBUG
        //System.out.println("\ntoWrite= "+toWrite);
        //System.out.println("\nframeCounter= "+frameCounter);
                     
                                
        }//escritura de frames
                        
        //escWAV.display(); //DEBUG
                        
        // Close the wavFile
	escWAV.close();
        
       stopDispTiempo(startTime);
    } //del for
        
    }//fin de clase main
    
    
    
    
}//fin de clase principalsonido
