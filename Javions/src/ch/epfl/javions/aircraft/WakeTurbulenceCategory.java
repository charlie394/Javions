package ch.epfl.javions.aircraft;

/**
 * Wake turbulence Category
 * @author Nagyung Kim (339628)
 */

public enum WakeTurbulenceCategory {

    LIGHT,
    MEDIUM,
    HEAVY,
    UNKNOWN;


    /**
     * returns textual values in the database converted into items of the enumerated type
     * @param s textual values in the database (L, M, H, UNKNOWN)
     * @return textual values in the database converted into items of the enumerated type
     */

    public static WakeTurbulenceCategory of(String s) {
        switch ( s ) {
            case "L" -> {
                return LIGHT;
            }
            case "M" -> {
                return MEDIUM;
            }
            case "H" -> {
                return HEAVY;
            }
            default -> {
                return UNKNOWN;
            }
        }
    }
}