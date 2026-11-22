package utils;

import java.time.LocalDate;

public class TanggalIndo {

    public static String hariIni() {
        return hariDari(LocalDate.now());
    }

    public static String hariDari(LocalDate d) {
        switch (d.getDayOfWeek()) {
            case MONDAY:
                return "Senin";
            case TUESDAY:
                return "Selasa";
            case WEDNESDAY:
                return "Rabu";
            case THURSDAY:
                return "Kamis";
            case FRIDAY:
                return "Jumat";
            case SATURDAY:
                return "Sabtu";
            default:
                return "Minggu";
        }
    }
}
