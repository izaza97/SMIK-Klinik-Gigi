SIM Klinik Gigi - NetBeans 29 + JDK 25.0.2 + XAMPP + Jaspersoft Studio
====================================================================
1. XAMPP: Start Apache+MySQL, buka http://localhost/phpmyadmin
   Import database/db_klinik_gigi.sql

2. NetBeans 29:
   - Tools > Java Platforms > Add Platform > C:\Program Files\Java\jdk-25.0.2
   - Open Project > SIMKlinikGigi > Klik kanan > Build (download mysql 9.1.0 + jasper 6.21.2)
   - Project Properties > Build > Compile > Java Platform = JDK 25
   - Run: com.klinikgigi.Main

3. Login:
   resepsionis/123, dokter/123, kasir/123, admin/admin123

4. Jaspersoft Studio 6.21.2 CE (WAJIB sama dgn library 6.21.2):
   - New Data Adapter > Database JDBC > MySQL
     jdbc:mysql://localhost:3306/db_klinik_gigi  user root pass kosong
     Driver: mysql-connector-j-9.1.0.jar
   - Open .jrxml di src/main/resources/laporan > Design > Preview > Build .jasper
   - Copy .jasper ke folder yang sama. Program bisa compile .jrxml otomatis jika .jasper belum ada.

5. Semua form adalah JFrame Swing.
Status: Menunggu->Dipanggil->Diperiksa->Menunggu bayar->Selesai + Terjadwal
