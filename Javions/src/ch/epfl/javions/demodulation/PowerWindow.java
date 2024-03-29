package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


/**
 * Represents a window of fixed size on a sequence of samples produced by <code>PowerComputer</code>
 * @author Eva Mangano 345375
 */
public final class PowerWindow {

    private static final int SAMPLE_SIZE = 1 << 16;
    private final PowerComputer powerComputer;
    private final int windowSize;
    private int[] tab1;
    private int[] tab2;
    private int position;
    private int positionInTab;
    private int samplesLeft;


    /**
     * Constructor. Builds an instance of <code>PowerWindow</code>
     * @param stream     input stream containing the bytes from the AirSpy radio
     * @param windowSize size of the window on the sequence of samples that we want to look at
     * @throws IOException if there is an input or output error
     * @author Eva Mangano 345375
     */
    public PowerWindow(InputStream stream, int windowSize) throws
                                                           IOException {
        Preconditions.checkArgument( ( windowSize > 0 ) && ( windowSize <= SAMPLE_SIZE ) );

        this.powerComputer = new PowerComputer( stream, SAMPLE_SIZE );
        this.windowSize = windowSize;
        this.tab1 = new int[SAMPLE_SIZE];
        this.tab2 = new int[SAMPLE_SIZE];
        this.position = 0;
        this.positionInTab = 0;

        samplesLeft = powerComputer.readBatch( tab1 );
    }


    /**
     * Getter for <code>windowSize</code>
     * @return the value of <code>windowSize</code>
     * @author Eva Mangano 345375
     */
    public int size() {
        return windowSize;
    }


    /**
     * Getter for the position of the window on the stream
     * @return the value of <code>position</code>
     * @author Eva Mangano 345375
     */
    public long position() {
        return position;
    }


    /**
     * Checks if the window is full (the end of the stream happens before the end of the window)
     * @return true if the window is full (contains as many samples as its size)
     * @author Eva Mangano 345375
     */
    public boolean isFull() {
        return ( windowSize <= samplesLeft );
    }


    /**
     * Gets the sample at the index <code>i</code> of the window
     * @param i index
     * @return the sample at the index of the window
     * @author Eva Mangano 345375
     */
    public int get(int i) {

        Objects.checkIndex( i, windowSize );

        int j = positionInTab + i;

        if ( j < SAMPLE_SIZE ) {
            return tab1[j];
        }

        return tab2[j - SAMPLE_SIZE];
    }


    /**
     * Advances the window of 1 sample in the stream
     * @throws IOException if there is an input or output error
     * @author Eva Mangano 345375
     */
    public void advance() throws
                          IOException {

        positionInTab++;
        position++;
        samplesLeft--;

        if ( windowSize + positionInTab - 1 == SAMPLE_SIZE ) {
            samplesLeft += powerComputer.readBatch( tab2 );
        }

        if ( positionInTab == tab1.length ) {

            positionInTab = 0;
            int[] temp = tab1;
            tab1 = tab2;
            tab2 = temp;
        }
    }


    /**
     * Advances the window of <code>offset</code> samples in the stream
     * @param offset the number of samples pass which to advance the window
     * @throws IOException if there is an input or output error
     * @author Eva Mangano 345375
     */
    public void advanceBy(int offset) throws
                                      IOException {
        Preconditions.checkArgument( offset >= 0 );

        for ( int i = 0 ; i < offset ; i++ ) {
            advance();
        }
    }
}
