package lzstring.streaming;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStream {

    private InputStream is;

    private int pos = 128;
    private byte thisByte = 0;

    public BitInputStream(InputStream is) throws IOException {

        this.is = is;
        thisByte = (byte) is.read();
    }

    public void close() throws IOException{

        is.close();
    }

    public int readBits(int numBits) throws IOException {

        int returnVal = 0;

        if(numBits > 16)
            numBits = 16;

        for(int i = 0; i < numBits; i++){

            returnVal |= (1 << i) * (((thisByte & pos) == 0)? 0 : 1);

            pos >>= 1;

            if(pos == 0){

                thisByte = (byte) is.read();
                pos = 128;
            }
        }

        return returnVal;
    }
}
