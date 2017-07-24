


/*
 * @author Luis M. Huesca
 * Clase que realiza lectura de archivo binario. Posiblemente será reemplazada
 */

package sonido;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;



public class BinFileReader {
    
    public static final int LENTGH = 1024;
    
    /*
    public static void main(String [] args) {
        String fileName = "C:/Audios/brownNoise_1hr_WAV-16B-signed.wav";
        BinFileReader binFileReader = new BinFileReader();
        byte[] buffer = null;
        BinFileReader.readOnlyWAVData(LENTGH, fileName);
    }
    */
    
    
    public static byte[] std_read(String fileName){
    // The name of the file to open.
        fileName = "C:/Audios/brownNoise_1hr_WAV-16B-signed.wav";

        byte[] buffer = new byte[1000];
        
        try {
                                                                                                                                       
            FileInputStream inputStream = 
                new FileInputStream(fileName);

            int total = 0;
            int nRead = 0;
            while((nRead = inputStream.read(buffer))!= -1) {

                total += nRead;
                
                /*
                //imprimir lo leido
                for (int i = 0; i < buffer.length; i++) {
                System.out.println(buffer[i]+", ");
                }*/
            
            }   
            
            inputStream.close();        

            System.out.println("Read " + total + " bytes");
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");  
            ex.printStackTrace();
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                 
            ex.printStackTrace();
        }
        
        return buffer;
    }
    
    /**
     * Obtiene un byte[] de audio a partir de un archivo binario (WAV). Solo lee a partir del inicio de data
     * @param bytesToRead <code>int</code> chunk de bytes que será leído
     * @return un array de <code>byte</code> que contiene  los datos 
     */
    
    public static byte[] readOnlyWAVData(int bytesToRead, String fileName){
        

        byte[] buffer = null;
        
        try {
            
           //buffer = new byte[1000];
           buffer = new byte[bytesToRead]; //buffer y archivo
           FileInputStream inputStream = new FileInputStream(fileName);


            int total = 0;
            int nRead = 0;
            while((nRead = inputStream.read(buffer))!= -1) {

                total += nRead;
                
                /*
                //imprimir lo leido
                for (int i = 0; i < buffer.length; i++) {
                System.out.println(buffer[i]+", ");
                }*/
            
            }   
            
          
            inputStream.close();        

            System.out.println("Read " + total + " bytes");
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");  
            ex.printStackTrace();
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            ex.printStackTrace();
        }
        
        return buffer;
    }
    
    
    
}




