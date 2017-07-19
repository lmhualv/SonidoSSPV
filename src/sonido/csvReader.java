
package sonido;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class csvReader {

        final int bufferSize = 1025;
        
        String csvFile = null;
        String cvsSplitBy = ",";
        
        BufferedReader br = null;
        String line = "";
        
        double [] contenedordouble = new double[bufferSize];
        int [] contenedorint = new int[bufferSize];
        byte [] contenedorbyte = new byte[bufferSize];
        
        String[] temp= new String[bufferSize];
        
        
        /**
     * Método <b>publico</b> que toma una URL de archivo de texto, y un caracter separador, para realizar un parseo a <code>double</code>.
     * @param URLarchivo URL absoluta del archivo <code>String</code>
     * @param separador caracter <code>String</code> que separa los datos
     * @return contenedodouble <code>double[]</code> que contiene los datos leidos como double
     */
        public double[] leeHaciaDouble(String URLarchivo, String separador){
        try {
            this.csvFile=URLarchivo;
            this.cvsSplitBy=separador;
            
            
            
            br = new BufferedReader(new FileReader(csvFile));
            
            while ((line = br.readLine()) != null) {
                // use comma as separator
                temp = line.split(cvsSplitBy);
                System.out.println("length: "+temp.length);
                 //solo se hace una vez porque el archivo está llenito :(
            }
            
            for (int j = 0; j < temp.length; j++) {
                contenedordouble[j] = Double.parseDouble(temp[j]);
                System.out.println("Pos["+j+"]= "+contenedordouble[j]+"\n");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
            return contenedordouble;
        }
        
        
        /**
     * Método <b>publico</b> que toma un <code>byte[]</code> previamente leido y lo despliega en pantalla. -LMHA
     * @param contenedorbyte  <code>byte[]</code> que contiene la informacion leida del archivo y que se desplegara en pantalla
     */
        public void muestraDatos(byte[] contenedorbyte){
        for (int i = 0; i < contenedorbyte.length; i++) {
            System.out.print("\n["+i+"]= "+contenedorbyte[i]);
        }
        }
        //////////////
        
        
        ////////////////metodo LeeHaciaUnsignedByte
     /**
     * Método <b>publico</b> que toma una URL de archivo de texto, y un caracter separador, para realizar un parseo a <code>integer</code>. -LMHA
     * @param URLarchivo URL absoluta del archivo <code>String</code>
     * @param separador caracter <code>String</code> que separa los datos
     * @return contenedorbyte <code>byte[]</code> que contiene los datos leidos como un byte sin signo
     */
         public byte[] leeHaciaUByte(String URLarchivo, String separador){
        try {
            this.csvFile=URLarchivo;
            this.cvsSplitBy=separador;
            
            
            br = new BufferedReader(new FileReader(csvFile));
            
            while ((line = br.readLine()) != null) {
                // use comma as separator
                temp = line.split(cvsSplitBy);
                System.out.println("length: "+temp.length);
                 //solo se hace una vez porque el archivo está llenito :(
            }
            
            System.out.println("Bytes\n");
            for (int j = 0; j < temp.length; j++) {
               //contenedorbyte[j] = (byte)((Integer.parseInt(temp[j])&0xFF));
               //modificado pruebas
               Integer integermi;
               integermi = (Integer.parseInt(temp[j])&0xFF);
               contenedorbyte[j] = integermi.byteValue();
               //////////////// 
                //System.out.println("Pos["+j+"]= "+contenedorbyte[j]+"\nBinario: "+Integer.toBinaryString(integermi)+"\nDato a escribir UNSIGNED: "+Integer.toUnsignedString(integermi)+"\n----------\n");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
            return contenedorbyte;
        }
        
        
        ///////////////////////////////////////////////LeeHaciaUnsignedByte
         
         
     /**
     * Método <b>publico</b> que toma una URL de archivo de texto, y un caracter separador, para realizar un parseo a <code>integer</code>. -LMHA
     * @param URLarchivo URL absoluta del archivo <code>String</code>
     * @param separador caracter <code>String</code> que separa los datos
     * @return contenedorint <code>int[]</code> que contiene los datos leidos
     */
        public int[] leeHaciaInt(String URLarchivo, String separador){
        try {
            this.csvFile=URLarchivo;
            this.cvsSplitBy=separador;
            
            
            
            br = new BufferedReader(new FileReader(csvFile));
            
            while ((line = br.readLine()) != null) {
                // usar ',' como separador
                temp = line.split(cvsSplitBy);
                //System.out.println("CSVReader - length: "+temp.length);
                 //solo se hace una vez porque el archivo está llenito :(
            }
            
            for (int j = 0; j < temp.length; j++) {
                contenedorint[j] = Integer.parseInt(temp[j]);
                System.out.println("Pos["+j+"]= "+contenedorint[j]+"\n");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
            return contenedorint;
        }

}