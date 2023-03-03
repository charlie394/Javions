package ch.epfl.javions;

import org.junit.jupiter.api.Test;

import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Crc24Test {

    @Test
    void Crc24Crc_bitwiseReturnsCorrectCrc() {
        String[] messages = {"8D392AE499107FB5C00439", "8D4D2286EA428867291C08", "8D3950C69914B232880436",
                             "8D4B17E399893E15C09C21", "8D4B18F4231445F2DB63A0", "8D495293F82300020049B8"};
        String[] crc24s = {"035DB8", "EE2EC6", "BC63D3", "9FC014", "DEEB82", "111203"};
        for ( int i = 0 ; i < messages.length ; i++ ) {
            Crc24 crc24 = new Crc24( Crc24.GENERATOR );
            String mS = messages[i];
            String cS = crc24s[i];
            int c = Integer.parseInt( cS, 16 );

            byte[] mAndC = HexFormat.of().parseHex( mS + cS );
            assertEquals( 0, crc24.crc( mAndC ) );

            byte[] mOnly = HexFormat.of().parseHex( mS );
            assertEquals( c, crc24.crc( mOnly ) );
        }
    }


}