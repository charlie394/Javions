package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.Units;
import ch.epfl.javions.aircraft.IcaoAddress;

import java.util.Objects;

/**
 * Represents an <code>AirborneVelocityMessage</code>
 * @param timeStampNs    the horodatage, in nanoseconds
 * @param icaoAddress    the ICAO address of the sender of the message
 * @param speed          the speed of the sender of the message (ground speed or airspeed), in meters per second
 * @param trackOrHeading the track the angle the direction of the aircraft does with the North, in radians
 * @author Nagyung Kim (339628)
 * @author Eva Mangano 345375
 */
public record AirborneVelocityMessage(long timeStampNs, IcaoAddress icaoAddress, double speed, double trackOrHeading)
        implements Message {


    private static final int HEADING_START = 11;
    private static final int HEADING_SIZE = 10;
    private static final int AIR_SPEED_START = 0;
    private static final int AIR_SPEED_SIZE = 10;
    private static final int CAP_AVAILABILITY_INDEX = 11;
    private static final int DEW_START = 21;
    private static final int DIRECTION_SIZE = 1;
    private static final int VEW_START = 11;
    private static final int VELOCITY_SIZE = 10;
    private static final int DNS_START = 10;
    private static final int VNS_START = 0;
    private static final int ST_START = 48;
    private static final int ST_SIZE = 3;
    private static final int DEPENDENT_BITS_START = 21;
    private static final int DEPENDENT_BITS_SIZE = 22;
    private static final int GROUND_SPEED_SUBSONIC = 1;
    private static final int GROUND_SPEED_SUPERSONIC = GROUND_SPEED_SUBSONIC + 1;
    private static final int AIR_SPEED_SUBSONIC = 3;
    private static final int AIR_SPEED_SUPERSONIC = AIR_SPEED_SUBSONIC + 1;


    /**
     * Constructor. Builds an instance of <code>AirborneVelocityMessage</code>
     * @param timeStampNs    the horodatage, in nanoseconds
     * @param icaoAddress    the ICAO address of the sender of the message
     * @param speed          the speed of the sender of the message (ground speed or airspeed), in meters per second
     * @param trackOrHeading the track the angle the direction of the aircraft does with the North, in radians
     * @throws NullPointerException     if the ICAO address is null
     * @throws IllegalArgumentException if the horodatage, the speed or the angle is negative
     * @author Eva Mangano 345375
     * @author Nagyung Kim (339628)
     */
    public AirborneVelocityMessage {
        Objects.requireNonNull( icaoAddress );
        Preconditions.checkArgument( timeStampNs >= 0 );
        Preconditions.checkArgument( speed >= 0 );
        Preconditions.checkArgument( trackOrHeading >= 0 );
    }


    /**
     * Builds an instance of <code>AirborneVelocityMessage</code> from the <code>RawMessage</code>
     * @param rawMessage the raw message sent by the aircraft
     * @return an instance of <code>AirborneVelocityMessage</code> from the raw message
     * @author Nagyung Kim (339628)
     * @author Eva Mangano 345375
     */
    public static AirborneVelocityMessage of(RawMessage rawMessage) {
        long payload = rawMessage.payload();

        int subType = Bits.extractUInt( payload, ST_START, ST_SIZE );
        int stDependentBits = Bits.extractUInt( payload, DEPENDENT_BITS_START, DEPENDENT_BITS_SIZE );

        switch ( subType ) {
            case GROUND_SPEED_SUBSONIC, GROUND_SPEED_SUPERSONIC -> {
                return velocityMessageGroundSpeed( rawMessage, subType, stDependentBits );
            }
            case AIR_SPEED_SUBSONIC, AIR_SPEED_SUPERSONIC -> {
                return velocityMessageAirSpeed( rawMessage, subType, stDependentBits );
            }
        }
        return null;
    }


    private static AirborneVelocityMessage velocityMessageGroundSpeed(RawMessage rawMessage, int subType,
                                                                      int stDependentBits) {
        int directionEW = Bits.extractUInt( stDependentBits, DEW_START, DIRECTION_SIZE );
        int velocityEW = Bits.extractUInt( stDependentBits, VEW_START, VELOCITY_SIZE ) - 1;
        int directionNS = Bits.extractUInt( stDependentBits, DNS_START, DIRECTION_SIZE );
        int velocityNS = Bits.extractUInt( stDependentBits, VNS_START, VELOCITY_SIZE ) - 1;

        if ( velocityNS == -1 || velocityEW == -1 ) {
            return null;
        }
        velocityEW = velocitySign( directionEW, velocityEW );
        velocityNS = velocitySign( directionNS, velocityNS );

        double speed = Math.hypot( velocityNS, velocityEW );

        double angle = Math.atan2( velocityEW, velocityNS );
        if ( angle < 0 ) {
            angle += Units.Angle.TURN;
        }

        speed = convertSpeedToKnot( speed, subType, GROUND_SPEED_SUPERSONIC );

        return new AirborneVelocityMessage( rawMessage.timeStampNs(), rawMessage.icaoAddress(), speed, angle );
    }


    private static AirborneVelocityMessage velocityMessageAirSpeed(RawMessage rawMessage, int subType,
                                                                   int stDependentBits) {
        int heading = Bits.extractUInt( stDependentBits, HEADING_START, HEADING_SIZE );
        int airSpeed = Bits.extractUInt( stDependentBits, AIR_SPEED_START, AIR_SPEED_SIZE ) - 1;

        if ( capIsAvailable( stDependentBits ) || airSpeed == 0 ) {
            return null;
        }

        double angle = Units.convertFrom( Math.scalb( (double)heading, -10 ), Units.Angle.TURN );
        double speed;

        speed = convertSpeedToKnot( airSpeed, subType, AIR_SPEED_SUPERSONIC );

        return new AirborneVelocityMessage( rawMessage.timeStampNs(), rawMessage.icaoAddress(), speed, angle );
    }


    private static int velocitySign(int direction, int velocity) {
        return direction == 0
               ? velocity
               : -velocity;
    }


    private static boolean capIsAvailable(int stDependentBits) {
        return Bits.testBit( stDependentBits, CAP_AVAILABILITY_INDEX );
    }


    private static double convertSpeedToKnot(double speed, int subType, int fourKnotsUnitSubType) {
        speed = subType == fourKnotsUnitSubType
                ? 4. * speed
                : speed;
        return Units.convertFrom( speed, Units.Speed.KNOT );
    }
}
