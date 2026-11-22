package model;

public class Session {

    public static int id_user;
    public static String nama, username, peran, id_dokter;

    public static void clear() {
        id_user = 0;
        nama = username = peran = id_dokter = null;
    }
}
