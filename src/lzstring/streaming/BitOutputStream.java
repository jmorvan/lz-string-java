package lzstring.streaming;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BitOutputStream {

  ByteArrayOutputStream bos = new ByteArrayOutputStream();
  
  private int pos = 0, bytesWritten = 0;
  private byte thisByte = 0;
  
  public byte[] getContent() throws IOException {
  
    while(thisByte != 0)
      writeBits(1, 0);
    
    if(bytesWritten % 2 != 0)
      bos.write((byte)0);
    
    return bos.toByteArray();
  }
  
  public void writeBits(int numBits, int value) throws IOException {
    
    for(int i = 0; i < numBits; i++){
      
      thisByte = (byte) ((thisByte << 1) | (value & 1));
      
      if(++pos % 8 == 0){
        bos.write(thisByte);
        pos = 0;
      }
      
      value >>= 1;
    }
    
    bytesWritten += numBits;
  }
}
