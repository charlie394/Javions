package ch.epfl.javions.aircraft;

public class AircraftDatabaseTest {
/*
    @Test
    void AircraftDatabaseWorksForValidInput() throws IOException {
        assertEquals( new AircraftData( new AircraftRegistration( "HB-JDC" ), new AircraftTypeDesignator( "A20N" ),
                                        "AIRBUS A-320neo", new AircraftDescription( "L2J" ),
                                        WakeTurbulenceCategory.of( "M" ) ), new AircraftDatabase(
                "C:\\Users\\Eva Mangano\\Downloads\\javions_skeleton (1)" + "\\Javions\\resources\\aircraft.zip" ).get(
                new IcaoAddress( "4B1814" ) ) );
    }


    @Test
    void AircraftDatabaseWorksForInValidInput() throws IOException {
        assertNull( new AircraftDatabase(
                "C:\\Users\\Eva Mangano\\Downloads\\javions_skeleton (1)\\Javions\\resources\\aircraft.zip" ).get(
                new IcaoAddress( "4B9912" ) ) );
    }


    private AircraftDatabase getDatabase() {
        // Try to get the database from the resources
        var aircraftResourceUrl = getClass().getResource(
                "C:\\Users\\Eva Mangano\\OneDrive\\Documents\\EPFL\\4 - BA2\\PROJET\\Javions\\resources\\aircraft" );
        //if ( aircraftResourceUrl != null ) {
            return new AircraftDatabase( URLDecoder.decode( aircraftResourceUrl.getFile(), UTF_8 ) );
        //}
        // Try to get the database from the JAVIONS_AIRCRAFT_DATABASE environment variable
        // (only meant to simplify testing of several projects with a single database)
        //        var aircraftFileName = System.getenv( "JAVIONS_AIRCRAFT_DATABASE" );
        //        if ( aircraftFileName != null ) {
        //            return new AircraftDatabase( aircraftFileName );
        //        }
        //
        //        throw new Error( "Could not find aircraft database" );

    }


    @Test
    void aircraftDatabaseGetReturnsNullWhenAddressDoesNotExist() throws IOException {
        var aircraftDatabase = getDatabase();
        assertNull( aircraftDatabase.get( new IcaoAddress( "123456" ) ) );
    }


    @Test
    void aircraftDatabaseGetWorksWithFirstLineOfFile() throws IOException {
        var aircraftDatabase = getDatabase();
        var aircraftData = aircraftDatabase.get( new IcaoAddress( "0086AB" ) );
        assertNotNull( aircraftData );
        assertEquals( new AircraftRegistration( "ZS-CNA" ), aircraftData.registration() );
    }


    @Test
    void aircraftDatabaseGetWorksWithLastLineOfFile() throws IOException {
        var aircraftDatabase = getDatabase();
        var aircraftData = aircraftDatabase.get( new IcaoAddress( "E808C0" ) );
        assertNotNull( aircraftData );
        assertEquals( new AircraftRegistration( "CC-DAW" ), aircraftData.registration() );
    }


    @Test
    void aircraftDatabaseGetWorksWithAddressGreaterThanLastOneOfFile() throws IOException {
        var aircraftDatabase = getDatabase();
        var aircraftData = aircraftDatabase.get( new IcaoAddress( "FFFF01" ) );
        assertNull( aircraftData );
    }


    @Test
    void aircraftDatabaseGetReturnsCorrectData() throws IOException {
        var aircraftDatabase = getDatabase();
        var aircraftData = aircraftDatabase.get( new IcaoAddress( "4B1805" ) );
        assertNotNull( aircraftData );
        assertEquals( new AircraftRegistration( "HB-JCN" ), aircraftData.registration() );
        assertEquals( new AircraftTypeDesignator( "BCS3" ), aircraftData.typeDesignator() );
        assertEquals( "AIRBUS A220-300", aircraftData.model() );
        assertEquals( new AircraftDescription( "L2J" ), aircraftData.description() );
        assertEquals( WakeTurbulenceCategory.MEDIUM, aircraftData.wakeTurbulenceCategory() );
    }


    @Test
    void aircraftDatabaseGetWorksWithEmptyColumns() throws IOException {
        var aircraftDatabase = getDatabase();
        var aircraftData = aircraftDatabase.get( new IcaoAddress( "AAAAAA" ) );
        assertNotNull( aircraftData );
        assertEquals( new AircraftRegistration( "N787BK" ), aircraftData.registration() );
        assertEquals( new AircraftTypeDesignator( "" ), aircraftData.typeDesignator() );
        assertEquals( "", aircraftData.model() );
        assertEquals( new AircraftDescription( "" ), aircraftData.description() );
        assertEquals( WakeTurbulenceCategory.UNKNOWN, aircraftData.wakeTurbulenceCategory() );
    }*/
}