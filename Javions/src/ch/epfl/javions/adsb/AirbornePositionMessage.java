package ch.epfl.javions.adsb;
/*
 *  Author :        Mangano Eva
 *  Date :          21/03/2023
 */


import ch.epfl.javions.Bits;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.aircraft.IcaoAddress;

public record AirbornePositionMessage(long timeStampNs, IcaoAddress icaoAddress, double altitude, int parity, double x,
                                      double y) implements Message {


    private static final int ME_SIZE = 12;


    public AirbornePositionMessage {
        if ( icaoAddress == null ) {
            throw new NullPointerException();
        }
        Preconditions.checkArgument(
                timeStampNs >= 0 && ( parity == 0 || parity == 1 ) && x >= 0 && x < 1 && y >= 0 && y < 1 );
    }


    public static AirbornePositionMessage of(RawMessage rawMessage) {

        int attributeALT = Bits.extractUInt( rawMessage.payload(), 36, 12 );
        int q = Bits.extractUInt( rawMessage.payload(), 40, 1 );
        int alt;

        if ( q == 1 ) {
            int left = Bits.extractUInt( attributeALT, 5, 7 );
            int right = Bits.extractUInt( attributeALT, 0, 4 );
            int a = right | ( left << 4 );
            alt = -1000 + a * 25;
        }
        else {
            int untangled = untangle( attributeALT );

            int multipleOf500 = Bits.extractUInt( untangled, 3, 9 );
            int multipleOf100 = Bits.extractUInt( untangled, 0, 3 );

            int msBits = decodeGrey( multipleOf500, 9 );
            int lsBits = decodeGrey( multipleOf100, 3 );

            //if (  )
        }

        return null;
    }


    public static int decodeGrey(int value, int n) {

        int binaryValue = value;

        for ( int i = 1 ; i < n ; i++ ) {
            binaryValue = binaryValue ^ ( value >> i );
        }

        return binaryValue;
    }


    private static int untangle(int attributeALT) {
        int[] tangled = new int[]{Bits.extractUInt( attributeALT, 2, 1 ), Bits.extractUInt( attributeALT, 0, 1 ),
                                  Bits.extractUInt( attributeALT, 10, 1 ), Bits.extractUInt( attributeALT, 8, 1 ),
                                  Bits.extractUInt( attributeALT, 6, 1 ), Bits.extractUInt( attributeALT, 5, 1 ),
                                  Bits.extractUInt( attributeALT, 3, 1 ), Bits.extractUInt( attributeALT, 1, 1 ),
                                  Bits.extractUInt( attributeALT, 11, 1 ), Bits.extractUInt( attributeALT, 9, 1 ),
                                  Bits.extractUInt( attributeALT, 7, 1 )};

        int untangled = 0;
        for ( int i = ME_SIZE - 2 ; i > 0 ; i -= 1 ) {
            untangled = ( untangled ) | ( tangled[i] << ( i - 1 ) );
        }
        return untangled;
    }
}