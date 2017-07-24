
package sonido;

import java.io.FileOutputStream;
import java.io.IOException;

/*
 * @author Luis M. Huesca
 */
public class Array2BinFileWriter {
//main para pruebas
    /*
    public static void main(String[] args) {
    byte[] data = { 0, 89, 82, 120, 0, 109, 103, 20, 0, 59 };
    Array2BinFileWriter.escribe(data);
  }//del main
    */
    
    String fileName = "C:/Audios/sspv/buffer_audio.dat";

public static void escribe(byte[] data, String fileName){
    try {
      FileOutputStream file = new FileOutputStream(fileName);
      for (int i = 0; i < data.length; i++)
        file.write(data[i]);
      file.close();
    } catch (IOException e) {
      System.out.println("Error - " + e.toString());
    }//del trycatch
}


}//de la clase
