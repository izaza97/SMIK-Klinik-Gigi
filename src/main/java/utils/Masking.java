package utils;
public class Masking{
    public static String nik(String nik){
        if(nik==null||nik.length()<8) return "****";
        return nik.substring(0,4)+"********"+nik.substring(nik.length()-4);
    }
    public static String hp(String hp){
        if(hp==null||hp.length()<5) return "****";
        return hp.substring(0,3)+"****"+hp.substring(hp.length()-3);
    }
}
