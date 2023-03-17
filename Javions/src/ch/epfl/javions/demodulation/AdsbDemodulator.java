package ch.epfl.javions.demodulation;

import ch.epfl.javions.adsb.RawMessage;

import java.io.IOException;
import java.io.InputStream;

//  package ch.epfl.javions.demodulation;
//
//  import ch.epfl.javions.ByteString; import ch.epfl.javions.adsb.RawMessage;
//  import java.io.IOException; import java.io.InputStream;
//  public final class AdsbDemodulator {
//  private final long timeStampNs; private InputStream stream; private byte[] samples; private int index = 0; private
//  RawMessage rawMessage;
//
//  public AdsbDemodulator(InputStream samplesStream) throws IOException { this.samples = new byte[1200];
//  this.timeStampNs = timeStampNs;
//  this.rawMessage = new RawMessage( timeStampNs, samples. ); }
//
//  public RawMessage nextMessage() throws IOException { for ( int i = 0 ; i <= 1200 ; i++ ) {
//  if ( i == 1200 ) { return null; }
//  byte a = samples[i]; } } }



//I couldn't get my head into the code so I started again from scratch to see if I understood what to do, but I got
// nowhere

public final class AdsbDemodulator {

    private final PowerWindow powerWindow;

    public AdsbDemodulator(InputStream samplesStream) throws IOException {
        powerWindow = new PowerWindow(samplesStream, 1200);
    }

    public RawMessage nextMessage() throws IOException {
        while (!powerWindow.isFull()) {
            powerWindow.advance();
        }

        int[] samples = new int[powerWindow.size()];
        for (int i = 0; i < powerWindow.size(); i++) {
            samples[i] = powerWindow.get(i);
        }
        powerWindow.advanceBy(8);

        return new RawMessage(samples, powerWindow.position() - powerWindow.size());
    }
}



    /**
     * Size of the window
     */
    /*public static final int ADS_B_SAMPLE_SIZE = 1200;

    private PowerWindow powerWindow;


    public AdsbDemodulator(InputStream samplesStream) throws IOException {

        this.powerWindow = new PowerWindow( samplesStream, ADS_B_SAMPLE_SIZE );
    }

     */
}