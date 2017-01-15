package com.khinekhant.crimereporter.database;

/**
 * Created by ${KhineKhant} on 0030,08/30/16.
 */
public class CrimedbSchema {
    public static final class CrimeTable{
        public static final String NAME="crimes";

        public static final class Cols{
            public static final String UUID="uuid";
            public static final String TITLE="title";
            public static final String DATE="date";
            public static final String SOLVED="solved";
            public static final String SUSPECT="suspect";
            public static final String CONTACT_ID="conatctid";
        }
    }
}
