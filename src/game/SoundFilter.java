package game;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Nikolaos Kouroumalos on 11/04/2018
 *
 * @author Nikolaos Kouroumalos
 * @version 0.1
 */
public class SoundFilter extends FilterInputStream {

    public SoundFilter(InputStream in) {
        super(in);
    }
//
//
//    public short getSample(byte[] buffer, int position)
//    {
//        return (short) (((buffer[position+1] & 0xff) << 8) |
//                (buffer[position] & 0xff));
//    }
//
//    public void setSample(byte[] buffer, int position, short sample)
//    {
//        buffer[position] = (byte)(sample & 0xFF);
//        buffer[position+1] = (byte)((sample >> 8) & 0xFF);
//    }
//
//    public int read(byte [] sample, int offset, int length) throws IOException
//    {
//        int bytesRead = super.read(sample,offset,length);
//
//        short amp=0;
//        float smoothing = 3;
//        byte newSample = sample[0];
//
//        for (int p=0; p<bytesRead; p = p + 2)
//        {
//            amp = getSample(sample,p);
//
//            /**
//             * Tried to create a low-pass filter with no success.
//             */
//            for (int i=0; i<sample.length; i++)
//            {
//                byte currentValue = sample[i];
//                newSample += (currentValue - newSample) / smoothing;
//                sample[i] = newSample;
//
//            }
//            setSample(sample,p,amp);
//        }
//        return length;
//
//    }
}


