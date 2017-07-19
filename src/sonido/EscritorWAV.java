
/*
Esta clase escribe datos como archivos en formato WAV
CLASE BASADA EN EL ARCHIVO:
Wav file IO class
A.Greensted
http://www.labbookpages.co.uk

File format is based on the information from
http://www.sonicspot.com/guide/wavefiles.html
http://www.blitter.com/~russtopia/MIDI/~jglatt/tech/wave.ht

Version 1.0 

y basado en el formato WAV de 
http://soundfile.sapp.org/doc/WaveFormat/
*/

package sonido;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Luis M. Huesca
 */
public class EscritorWAV {
    
    
    private enum IOState {ESCRIBIENDO, CERRADO}; //estados del archivo
	
        ///constantes
        private final static int BUFFER_SIZE = 4096;
	private final static int FMT_CHUNK_ID = 0x20746D66;
	private final static int DATA_CHUNK_ID = 0x61746164;
	private final static int RIFF_CHUNK_ID = 0x46464952;
	private final static int RIFF_TYPE_ID = 0x45564157;

	private File file;					// archivo que se escribir'a
	private IOState ioState;				// Especifica el estado del archivo
	private int bytesPerSample;                             //bits en cada sample
	private long numFrames;					// Numero de frames en la seccion 'data'
	private FileOutputStream oStream;                       // Output stream para escribir datos				
	private boolean wordAlignAdjust;                        // Se especifica en caso de requerir alineacio'n al final del data chunk

	/////////Header WAV
	private int numChannels;				// 2 bytes unsigned 0x0001 (1) hasta 0xFFFF (65535)
	private long sampleRate;				// 4 bytes unsigned 0x00000001 (1) hasta 0xFFFFFFFF (4294967295) //int en java es de 4 bytes, por tanto se usa long
	private int blockAlign;					// 2 bytes unsigned 0x0001 (1) hasta 0xFFFF (65535)
	private int validBits;					// 2 bytes unsigned 0x0002 (2) hasta 0xFFFF (65535)

	// Buffering
	private byte[] buffer;					// Buffer local
	private int bufferPointer;				// Apunta a la posicio'n del buffer
	private long frameCounter;				// numero de frames escritos

	// no instanciar WavFile directamente, usar newWavFile() 
	private EscritorWAV()
	{
		buffer = new byte[BUFFER_SIZE];
	}

	public int getNumChannels()
	{
		return numChannels;
	}

	public long getNumFrames()
	{
		return numFrames;
	}

	public long getFramesRemaining()
	{
		return numFrames - frameCounter;
	}

	public long getSampleRate()
	{
		return sampleRate;
	}

	public int getValidBits()
	{
		return validBits;
	}
        
        public int getBlockAlign(){
            return blockAlign;
        }
        
        public int getBytesPerSample(){
            return bytesPerSample;
        }
        

        public static EscritorWAV newWavFile(File file, int numChannels, long numFrames, int validBits, long sampleRate) throws IOException, WAVFileException
	{
		// instancia de escritor wav
		EscritorWAV escritorWAV = new EscritorWAV();
		escritorWAV.file = file;
		escritorWAV.numChannels = numChannels;
		escritorWAV.numFrames = numFrames;
		escritorWAV.sampleRate = sampleRate;
		escritorWAV.bytesPerSample = (validBits + 7) / 8;
		escritorWAV.blockAlign = escritorWAV.bytesPerSample * numChannels;
		escritorWAV.validBits = validBits;

		// rule check 
		if (numChannels < 1 || numChannels > 65535) throw new WAVFileException("Numero invalido de canales: 1 < Channels <65536");
		if (numFrames < 0) throw new WAVFileException("Numero de frames negativo");
		if (validBits < 2 || validBits > 65535) throw new WAVFileException("Numero invalido de bits: 2 < numbits < 65536");
		if (sampleRate < 0) throw new WAVFileException("frecuencia de muestreo no puede ser negativa");

		// crear el outputstream para write
		escritorWAV.oStream = new FileOutputStream(file);

		// calcular tamaño de los chunks
		long headerChunkSize = escritorWAV.blockAlign * numFrames;
		long mainChunkSize =	4 +	// RIFF
					8 +	// chunkSize y WAVE
                                        16 +	// fmt y data
					8 + 	// Data ID y su tamaño
                                        headerChunkSize;

		// Chunks deben ser word aligned, si es número impar de audio data, debe ajustarse
		if (headerChunkSize % 2 == 1) {
			mainChunkSize += 1;
			escritorWAV.wordAlignAdjust = true;
		}
		else {
			escritorWAV.wordAlignAdjust = false;
		}

		// setea el tamaño del chunk principal
		putLE(RIFF_CHUNK_ID,	escritorWAV.buffer, 0, 4);
		putLE(mainChunkSize,	escritorWAV.buffer, 4, 4);
		putLE(RIFF_TYPE_ID,	escritorWAV.buffer, 8, 4);

		//escribe el header
		escritorWAV.oStream.write(escritorWAV.buffer, 0, 12);

		// poniendo el data de formato en el buffer
		long byteRate = sampleRate * escritorWAV.blockAlign;

		putLE(FMT_CHUNK_ID,				escritorWAV.buffer, 0, 4);		// Chunk1ID
		putLE(16,					escritorWAV.buffer, 4, 4);		// Chunk1Size
		putLE(1,					escritorWAV.buffer, 8, 2);		// PCM
		putLE(numChannels,				escritorWAV.buffer, 10, 2);		// NumChannels
		putLE(sampleRate,				escritorWAV.buffer, 12, 4);		// SampleRate
		putLE(byteRate,                                 escritorWAV.buffer, 16, 4);		// ByteRate
		putLE(escritorWAV.blockAlign,                   escritorWAV.buffer, 20, 2);		// BlockAlign
		putLE(validBits,                                escritorWAV.buffer, 22, 2);		// NumnBits

		// escribe Chunk1
		escritorWAV.oStream.write(escritorWAV.buffer, 0, 24);

		// empieza el chunk de data
		putLE(DATA_CHUNK_ID,				escritorWAV.buffer, 0, 4);		// Chunk2ID
		putLE(headerChunkSize,				escritorWAV.buffer, 4, 4);		// Chunk2Size

		// escribe Chunk2
		escritorWAV.oStream.write(escritorWAV.buffer, 0, 8);


		// setea el estado
		escritorWAV.bufferPointer = 0;
		escritorWAV.frameCounter = 0;
		escritorWAV.ioState = escritorWAV.ioState.ESCRIBIENDO;

		return escritorWAV;
	}
        
        //volteando para que sean LittleEndian
        private static void putLE(long val, byte[] buffer, int pos, int numBytes)
	{
		for (int b=0 ; b<numBytes ; b++)
		{
			buffer[pos] = (byte) (val & 0xFF);
			val >>= 8;
			pos ++;
		}
	}

	// Sample Writing ;)
	private void writeSample(long val) throws IOException
	{
		for (int b=0 ; b<bytesPerSample ; b++)
		{
			if (bufferPointer == BUFFER_SIZE)
			{
				oStream.write
        
        (buffer, 0, BUFFER_SIZE);
				bufferPointer = 0;
			}

			buffer[bufferPointer] = (byte) (val & 0xFF);
			val >>= 8;
			bufferPointer ++;
		}
	}
       
        ///////METODOS BYTE :)
        
        public int writeFrames(byte[] sampleBuffer, int offset, int numFramesToWrite) throws IOException, WAVFileException
	{
            
                //System.out.println("A escribir en bytes\n"); /////DEBUG ONLY!!
		if (ioState != IOState.ESCRIBIENDO) throw new IOException("No se puede escribir en la instancia escritorWav");

		for (int f=0 ; f<numFramesToWrite ; f++)
		{
			if (frameCounter == numFrames) return f;

			for (int c=0 ; c<numChannels ; c++)
			{
				writeSample(sampleBuffer[offset]);
				offset ++;
			}

			frameCounter ++;
		}

		return numFramesToWrite;
	}
        
        public int writeFrames(byte[] sampleBuffer, int numFramesToWrite) throws IOException, WAVFileException
	{
		return writeFrames(sampleBuffer, 0, numFramesToWrite);
	}
        
        public void close() throws IOException
	{
		
		if (oStream != null) 
		{
			// escribe lo que quede en el buffer local
			if (bufferPointer > 0) oStream.write(buffer, 0, bufferPointer);

			// si se necesita un bit extra para alignment, lo añade
			if (wordAlignAdjust) oStream.write(0);

			// cierra el stream y setea en null
			oStream.close();
			oStream = null;
		}

		// indica que se ha cerrado
		ioState = IOState.CERRADO;
	}
        
        public void display()
	{
		System.out.print("File:"+file+"\nChannels: "+numChannels+". Frames: "+numFrames+
                        "\nIO State:"+ioState+"\nSample Rate:" +sampleRate+ "Block Align:"+blockAlign+
                        "\nValid Bits:"+validBits+"Bytes per sample: "+bytesPerSample+"\n");
	}
        
        
        
        
        
        
}
