-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 21, 2026 at 01:02 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_klinik_gigi`
--

-- --------------------------------------------------------

--
-- Table structure for table `diagnosis`
--

CREATE TABLE `diagnosis` (
  `kode_diagnosis` varchar(10) NOT NULL,
  `nama_diagnosis` varchar(150) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `diagnosis`
--

INSERT INTO `diagnosis` (`kode_diagnosis`, `nama_diagnosis`) VALUES
('K02.0', 'Karies email'),
('K02.1', 'Karies dentin'),
('K04.7', 'Abses periapikal'),
('K05.0', 'Gingivitis akut'),
('K08.1', 'Gigi hilang');

-- --------------------------------------------------------

--
-- Table structure for table `dokter`
--

CREATE TABLE `dokter` (
  `id_dokter` varchar(10) NOT NULL,
  `nama` varchar(100) DEFAULT NULL,
  `sip` varchar(50) DEFAULT NULL,
  `spesialis` varchar(50) DEFAULT NULL,
  `no_hp` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dokter`
--

INSERT INTO `dokter` (`id_dokter`, `nama`, `sip`, `spesialis`, `no_hp`) VALUES
('D001', 'drg. Sinta Maharani', 'SIP.445/2023', 'Konservasi', '081234567890');

-- --------------------------------------------------------

--
-- Table structure for table `jadwal_dokter`
--

CREATE TABLE `jadwal_dokter` (
  `id_jadwal` int(11) NOT NULL,
  `id_dokter` varchar(10) DEFAULT NULL,
  `hari` enum('Senin','Selasa','Rabu','Kamis','Jumat','Sabtu','Minggu') DEFAULT NULL,
  `jam_mulai` time DEFAULT NULL,
  `jam_selesai` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jadwal_dokter`
--

INSERT INTO `jadwal_dokter` (`id_jadwal`, `id_dokter`, `hari`, `jam_mulai`, `jam_selesai`) VALUES
(1, 'D001', 'Senin', '15:00:00', '20:00:00'),
(2, 'D001', 'Rabu', '15:00:00', '20:00:00'),
(3, 'D001', 'Jumat', '15:00:00', '20:00:00'),
(4, 'D001', 'Sabtu', '15:00:00', '20:00:00'),
(5, 'D001', 'Senin', '15:00:00', '20:00:00'),
(6, 'D001', 'Kamis', '15:00:00', '20:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `janji_temu`
--

CREATE TABLE `janji_temu` (
  `id_janji` int(11) NOT NULL,
  `no_rm` varchar(10) DEFAULT NULL,
  `id_dokter` varchar(10) DEFAULT NULL,
  `tanggal` date DEFAULT NULL,
  `jam` time DEFAULT NULL,
  `keperluan` varchar(100) DEFAULT NULL,
  `status` enum('Terjadwal','Dikonfirmasi','Selesai','Batal') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `kunjungan`
--

CREATE TABLE `kunjungan` (
  `id_kunjungan` int(11) NOT NULL,
  `tanggal` date DEFAULT NULL,
  `no_rm` varchar(10) DEFAULT NULL,
  `id_dokter` varchar(10) DEFAULT NULL,
  `keluhan` text DEFAULT NULL,
  `no_antrean` varchar(10) DEFAULT NULL,
  `jam_daftar` time DEFAULT NULL,
  `jam_panggil` time DEFAULT NULL,
  `status` enum('Menunggu','Dipanggil','Diperiksa','Menunggu bayar','Selesai','Terjadwal','Batal') DEFAULT NULL,
  `jenis` enum('Baru','Lama') DEFAULT NULL,
  `tensi` varchar(20) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `layanan`
--

CREATE TABLE `layanan` (
  `kode_layanan` varchar(10) NOT NULL,
  `nama_layanan` varchar(100) DEFAULT NULL,
  `tarif` int(11) DEFAULT NULL,
  `kategori` varchar(50) DEFAULT 'Umum',
  `keterangan` varchar(200) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `layanan`
--

INSERT INTO `layanan` (`kode_layanan`, `nama_layanan`, `tarif`, `kategori`, `keterangan`) VALUES
('EST01', 'Labial Veneer Direct Composite', 950000, 'Esthetic', 'Per Gigi'),
('EST02', 'Labial Veneer Indirect Porcelain', 3500000, 'Esthetic', 'Per Gigi'),
('EST03', 'Dental Imunisasi - MI Varnish', 350000, 'Esthetic', 'MI Varnish'),
('EST04', 'Paket Dental Whitening Bleaching I - 2x oles', 1500000, 'Esthetic', '2x oles'),
('EST05', 'Paket Dental Whitening Bleaching II - 3x oles', 2000000, 'Esthetic', '3x oles'),
('EST06', 'Paket Dental Whitening Bleaching III - 3x oles + TM', 2500000, 'Esthetic', '3x oles + TM'),
('K01', 'Konsultasi', 300000, 'Konsultasi', 'Konsultasi awal'),
('KSV01', 'Open bur/Selective Grinding', 300000, 'Konservasi', ''),
('KSV02', 'Penambalan Sementara + Obat', 300000, 'Konservasi', ''),
('KSV03', 'Penambalan Permanen Tipe I', 400000, 'Konservasi', ''),
('KSV04', 'Penambalan Permanen Tipe II', 600000, 'Konservasi', ''),
('KSV05', 'Penambalan Permanen Tipe III', 850000, 'Konservasi', ''),
('L01', 'Konsultasi & Pemeriksaan', 50000, 'Umum', ''),
('L03', 'Tambal Resin Komposit', 350000, 'Umum', ''),
('L04', 'Cabut Gigi', 300000, 'Umum', ''),
('OBT01', 'Obat Amoxicillin 500mg', 20000, 'Produk', '500mg (10 tab)'),
('OBT02', 'Obat Clindamycin 300mg', 30000, 'Produk', '300mg (10 tab)'),
('OBT03', 'Obat Asam Mefenamat 500mg', 15000, 'Produk', '500mg (10 tab)'),
('OBT04', 'Obat Kalium Diklofenak 50mg', 20000, 'Produk', '50mg (10 tab)'),
('OBT05', 'Obat Paracetamol 500mg', 15000, 'Produk', '500mg (10 tab)'),
('OBT06', 'Obat Ranitidine 150mg', 15000, 'Produk', '150mg (10 tab)'),
('OBT07', 'Obat Phytomenadione 10mg', 5000, 'Produk', '10mg (koagulan) 1 tab'),
('OBT08', 'Obat Co-Amoxyclav 625mg', 40000, 'Produk', '625mg (6 tab)'),
('OBT09', 'Obat Dexyclav 625mg', 145000, 'Produk', '625mg (paten, 6 tab)'),
('OBT10', 'Obat Orinox 60mg', 130000, 'Produk', '60mg (paten,10 tab)'),
('OBT11', 'Obat Amoxsan 500mg', 65000, 'Produk', '500mg (10 tab)'),
('OBT12', 'Obat Clinex (Clindamycin) 300mg', 145000, 'Produk', '300mg (10 tab)'),
('OBT13', 'Obat Medixon (Methylpred) 16mg', 90000, 'Produk', '16mg (6 tab)'),
('OBT14', 'Obat Tramal (Tramadol Hcl) 50mg', 150000, 'Produk', '50mg (10 caps)'),
('OBT15', 'Obat Mefinal 500mg', 35000, 'Produk', '500mg (10 tab)'),
('OBT16', 'Obat Arcoxia 120mg', 190000, 'Produk', '120mg (5 cap)'),
('OBT17', 'Obat Cataflam 50mg', 35000, 'Produk', '50mg (10 tab)'),
('OBT18', 'Obat Neurobion', 45000, 'Produk', '10 cap'),
('ORT01', 'Bracket Metal', 5000000, 'Orthodontic', ''),
('ORT02', 'Bracket Metal AO', 8500000, 'Orthodontic', ''),
('ORT03', 'Bracket Self Ligating Metal', 9000000, 'Orthodontic', ''),
('ORT04', 'Bracket Sapphire', 12000000, 'Orthodontic', ''),
('ORT05', 'Bracket Self Ligating Zapphire', 15000000, 'Orthodontic', ''),
('ORT06', 'Kontrol Bracket Metal', 300000, 'Orthodontic', 'Cleaning+Ganti karet (2minggu sekali)'),
('ORT07', 'Kontrol Bracket Ceramic', 300000, 'Orthodontic', 'Cleaning+Ganti karet (2minggu sekali)'),
('ORT08', 'Kontrol Bracket Self Ligating/Zapphire', 350000, 'Orthodontic', 'Cleaning (1 bulan sekali)'),
('ORT09', 'Recementasi/Reposisi Bracket', 75000, 'Orthodontic', 'Per Bracket'),
('ORT10', 'Ganti Wire Tipe I', 75000, 'Orthodontic', 'Round'),
('ORT11', 'Ganti Wire Tipe II', 100000, 'Orthodontic', 'Round AO/Recta'),
('ORT12', 'Ganti Wire Tipe III', 150000, 'Orthodontic', 'Recta AO'),
('ORT13', 'Ganti Bracket Metal', 150000, 'Orthodontic', 'Hilang/Rusak (Bracket Metal)'),
('ORT14', 'Ganti Bracket Ceramic', 200000, 'Orthodontic', 'Hilang/Rusak (Bracket Ceramic)'),
('ORT15', 'Ganti Bracket Self Ligating', 250000, 'Orthodontic', 'Hilang/Rusak (Self Ligating)'),
('ORT16', 'Ganti Buccal Tube', 250000, 'Orthodontic', ''),
('ORT17', 'Alat tambahan Ortho', 50000, 'Orthodontic', '(OCS/Stopper/Elastic/Ligature wire)'),
('ORT18', 'Lepas Bracket', 950000, 'Orthodontic', 'Lepas Bracket + Scaling + Polishing'),
('ORT19', 'Retainer Ortho', 850000, 'Orthodontic', 'Per 1 Rahang'),
('ORT20', 'Space Maintainer', 750000, 'Orthodontic', 'Per Gigi'),
('ORT21', 'Pindahan Ortho', 3000000, 'Orthodontic', 'Commitment Fee'),
('ORT22', 'Kontrol Incidental (Perawatan Luar)', 350000, 'Orthodontic', 'Ganti karet O Ring'),
('ORT23', 'Kontrol Incidental Resementasi Bracket', 200000, 'Orthodontic', 'Resementasi per Gigi'),
('ORT24', 'Kontrol Incidental Ganti Bracket', 350000, 'Orthodontic', 'Bracket Metal'),
('ORT25', 'Kontrol Incidental Ganti Wire', 350000, 'Orthodontic', 'Bracket Ceramic'),
('OS01', 'Pencabutan Dewasa Tipe I', 400000, 'Oral Surgery', ''),
('OS02', 'Pencabutan Dewasa Tipe II', 600000, 'Oral Surgery', ''),
('OS03', 'Pencabutan Dewasa Tipe III', 850000, 'Oral Surgery', ''),
('OS04', 'Penjahitan', 200000, 'Oral Surgery', '1 Benang Jahit'),
('OS05', 'Spongostan', 50000, 'Oral Surgery', '1 bh'),
('OS06', 'Pencabutan Anak Tipe I', 300000, 'Oral Surgery', 'Tanpa injeksi'),
('OS07', 'Pencabutan Anak Tipe II', 400000, 'Oral Surgery', 'Tanpa injeksi'),
('OS08', 'Pencabutan Anak Tipe III', 400000, 'Oral Surgery', 'Dengan Injeksi'),
('OS09', 'Pencabutan Anak Tipe IV', 500000, 'Oral Surgery', 'Dengan injeksi'),
('OS10', 'Pencabutan Gigi Bungsu Tipe I', 1800000, 'Oral Surgery', ''),
('OS11', 'Pencabutan Gigi Bungsu Tipe II', 2500000, 'Oral Surgery', 'Odontectomy Klas I'),
('OS12', 'Pencabutan Gigi Bungsu Tipe III', 3500000, 'Oral Surgery', 'Odontectomy Klas II'),
('OS13', 'Pencabutan Gigi Bungsu Tipe IV', 5000000, 'Oral Surgery', 'Odontectomy Klas III'),
('OS14', 'Buka Jahitan', 300000, 'Oral Surgery', ''),
('OS15', 'Frenectomy/Operculectomy/Alveolectomy', 500000, 'Oral Surgery', ''),
('OS16', 'Gingivectomy per regio/gigi', 300000, 'Oral Surgery', ''),
('OS17', 'Tambah Ampul', 50000, 'Oral Surgery', '1 Ampul'),
('OS18', 'Anestesi', 200000, 'Oral Surgery', '1 Ampul'),
('PER01', 'Scaling Tipe I', 400000, 'Periodontal', 'Scaling + Brushing Pasta Pumice'),
('PER02', 'Scaling Tipe II', 600000, 'Periodontal', 'Scaling + Brushing Pasta Pumice'),
('PER03', 'Scaling Tipe III', 750000, 'Periodontal', 'Scaling + Brushing Pasta Pumice'),
('PER04', 'Stain Remover OCO', 100000, 'Periodontal', ''),
('PER05', 'Stain Remover Air Polisher', 200000, 'Periodontal', ''),
('PER06', 'Splinting', 300000, 'Periodontal', 'Per Gigi'),
('PER07', 'Paket Dental Spa Tipe I', 850000, 'Periodontal', 'Scaling Tipe II+Stain Remove+TM'),
('PER08', 'Paket Dental Spa Tipe II', 950000, 'Periodontal', 'Scaling Tipe III+Airflow+TM+Gargle'),
('PRD01', 'Toothmousse', 300000, 'Produk', '1 tube'),
('PRD02', 'Sikat Gigi', 60000, 'Produk', 'GC Product (Rusello, Picella) B30, P30'),
('PRD03', 'Pasta Gigi AP24', 95000, 'Produk', ''),
('PRD04', 'Ortho Kit', 80000, 'Produk', ''),
('PRD05', 'Sikat Gigi Ortho', 30000, 'Produk', 'Merk Systema'),
('PRD06', 'Wax Ortho', 25000, 'Produk', ''),
('PRD07', 'Sikat Interdental', 15000, 'Produk', ''),
('PRO01', 'Jacket Crown PFM', 1800000, 'Prosthodontic', 'Porcelain Fused to Metal'),
('PRO02', 'Jacket Crown All Porcelain', 3500000, 'Prosthodontic', 'Emax/Zirconia'),
('PRO03', 'Jacket Crown Sementara', 300000, 'Prosthodontic', ''),
('PRO04', 'Bridge Fiber 1 Visit', 1500000, 'Prosthodontic', ''),
('PRO05', 'Recementasi Crown', 300000, 'Prosthodontic', 'Crown lepas'),
('PRO06', 'Recementasi Bridge', 500000, 'Prosthodontic', 'Bridge lepas (2 crown)'),
('PRO07', 'Implant termasuk Crown', 15000000, 'Prosthodontic', 'Termasuk Crown'),
('PRO08', 'Bongkar Crown Metal/Porcelain', 300000, 'Prosthodontic', 'Per Gigi'),
('PRO09', 'Gigi Tiruan Lepasan Acrylic Simple', 1800000, 'Prosthodontic', 'Plat + 1 gigi, Per Rahang (Kasus Simple)'),
('PRO10', 'Gigi Tiruan Lepasan Acrylic Complex', 2500000, 'Prosthodontic', 'Plat + 1 gigi, Per Rahang (Kasus Complex)'),
('PRO11', 'Tambahan per gigi Acrylic', 250000, 'Prosthodontic', ''),
('PRO12', 'Gigi Tiruan Lepasan Bioton Simple', 2500000, 'Prosthodontic', 'Plat + 1 gigi, Per Rahang (Kasus Simple)'),
('PRO13', 'Gigi Tiruan Lepasan Bioton Complex', 3500000, 'Prosthodontic', 'Plat + 1 gigi, Per Rahang (Kasus Complex)'),
('PRO14', 'Tambahan per gigi Bioton', 300000, 'Prosthodontic', ''),
('PRO15', 'Plat Frame', 1000000, 'Prosthodontic', 'Plat saja'),
('PRO16', 'Full Denture Acrylic Per Rahang', 6000000, 'Prosthodontic', 'Per Rahang termasuk gigi 14bh'),
('PRO17', 'Full Denture Frame Per Rahang', 7500000, 'Prosthodontic', 'Per Rahang termasuk gigi 14bh'),
('PRO18', 'Reparasi Acrylic', 400000, 'Prosthodontic', ''),
('PRO19', 'Rebasing Acrylic', 600000, 'Prosthodontic', ''),
('PRO20', 'Cetak Alginate', 300000, 'Prosthodontic', 'Selain kasus Gigi Tiruan'),
('PRO21', 'Cetak Double Impression', 400000, 'Prosthodontic', 'Selain kasus Gigi Tiruan'),
('PSA01', 'PSA Paket I - 1 Saluran Akar', 1200000, 'PSA Paket', '(1 Saluran Akar) +-3x kunjungan'),
('PSA02', 'PSA Paket II - 2 Saluran Akar', 1600000, 'PSA Paket', '(2 Saluran Akar) +-3x kunjungan'),
('PSA03', 'PSA Paket III - 3 Saluran Akar', 1850000, 'PSA Paket', '(3 Saluran Akar) +-3x kunjungan'),
('PSA04', 'PSA Paket IV - 4 Saluran Akar', 2200000, 'PSA Paket', '(4 Saluran Akar) +-3x kunjungan'),
('PSA05', 'PSA LSTR Tanpa Tambalan', 350000, 'PSA Paket', 'Tanpa Tambalan'),
('PSN01', 'PSA Per Visit Tipe I - 1 Saluran Akar', 450000, 'PSA Non Paket', '1 Saluran Akar : Per Kunjungan'),
('PSN02', 'PSA Per Visit Tipe II - 2 Saluran Akar', 550000, 'PSA Non Paket', '2 Saluran Akar : Per Kunjungan'),
('PSN03', 'PSA Per Visit Tipe III - 3 Saluran Akar', 650000, 'PSA Non Paket', '3 Saluran Akar : Per Kunjungan'),
('PSN04', 'PSA Per Visit Tipe IV - 4 Saluran Akar', 750000, 'PSA Non Paket', '4 Saluran Akar : Per Kunjungan'),
('PSN05', 'Cavity Liner', 200000, 'PSA Non Paket', ''),
('PSN06', 'Benang Epinefrin', 100000, 'PSA Non Paket', ''),
('PSN07', 'Pin Fiber Post', 350000, 'PSA Non Paket', '');

-- --------------------------------------------------------

--
-- Table structure for table `obat`
--

CREATE TABLE `obat` (
  `kode_obat` varchar(10) NOT NULL,
  `nama_obat` varchar(100) DEFAULT NULL,
  `satuan` varchar(20) DEFAULT NULL,
  `harga` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `obat`
--

INSERT INTO `obat` (`kode_obat`, `nama_obat`, `satuan`, `harga`) VALUES
('O01', 'Amoxicillin 500mg', 'tablet', 2000),
('O02', 'Ibuprofen 400mg', 'tablet', 1500),
('O03', 'Dental Anestesi', 'ampul', 25000);

-- --------------------------------------------------------

--
-- Table structure for table `odontogram`
--

CREATE TABLE `odontogram` (
  `id` int(11) NOT NULL,
  `id_rekam` int(11) DEFAULT NULL,
  `no_gigi` varchar(5) DEFAULT NULL,
  `kondisi` enum('Karies','Tambalan Komposit','Tambalan Amalgam','Gigi Hilang','Sisa Akar') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pasien`
--

CREATE TABLE `pasien` (
  `no_rm` varchar(10) NOT NULL,
  `nik` varchar(16) DEFAULT NULL,
  `nama` varchar(100) DEFAULT NULL,
  `tgl_lahir` date DEFAULT NULL,
  `jk` enum('L','P') DEFAULT NULL,
  `no_hp` varchar(20) DEFAULT NULL,
  `alamat` text DEFAULT NULL,
  `alergi` varchar(100) DEFAULT NULL,
  `tgl_daftar` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pasien`
--

INSERT INTO `pasien` (`no_rm`, `nik`, `nama`, `tgl_lahir`, `jk`, `no_hp`, `alamat`, `alergi`, `tgl_daftar`) VALUES
('RM-000001', '12342313', 'test1', '1997-01-01', 'L', '082631313', '-', 'tidak ada', '2026-10-24'),
('RM-000002', '123442466544', 'jaemin', '2000-08-14', 'L', '098755456778', 'seoul', 'tidak ada', '2026-11-19');

-- --------------------------------------------------------

--
-- Table structure for table `pembayaran`
--

CREATE TABLE `pembayaran` (
  `no_kuitansi` varchar(25) NOT NULL,
  `id_kunjungan` int(11) DEFAULT NULL,
  `total_jasa` int(11) DEFAULT NULL,
  `total_obat` int(11) DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  `metode` enum('Tunai','QRIS','Transfer','EDC') DEFAULT NULL,
  `tgl_bayar` datetime DEFAULT NULL,
  `id_user` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pengguna`
--

CREATE TABLE `pengguna` (
  `id_user` int(11) NOT NULL,
  `nama_lengkap` varchar(100) DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `peran` enum('Resepsionis','Dokter','Kasir','Pemilik') DEFAULT NULL,
  `id_dokter` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pengguna`
--

INSERT INTO `pengguna` (`id_user`, `nama_lengkap`, `username`, `password`, `peran`, `id_dokter`) VALUES
(1, 'Admin Pemilik', 'admin', 'admin123', 'Pemilik', NULL),
(2, 'Resepsionis 1', 'resepsionis', '123', 'Resepsionis', NULL),
(3, 'drg. Sinta', 'dokter', '123', 'Dokter', 'D001'),
(4, 'Kasir 1', 'kasir', '123', 'Kasir', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `rekam_medis`
--

CREATE TABLE `rekam_medis` (
  `id_rekam` int(11) NOT NULL,
  `id_kunjungan` int(11) DEFAULT NULL,
  `subjektif` text DEFAULT NULL,
  `objektif` text DEFAULT NULL,
  `assessment` text DEFAULT NULL,
  `plan` text DEFAULT NULL,
  `kode_diagnosis` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `resep`
--

CREATE TABLE `resep` (
  `id` int(11) NOT NULL,
  `id_kunjungan` int(11) DEFAULT NULL,
  `isi_resep` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tindakan_kunjungan`
--

CREATE TABLE `tindakan_kunjungan` (
  `id` int(11) NOT NULL,
  `id_kunjungan` int(11) DEFAULT NULL,
  `kode_layanan` varchar(10) DEFAULT NULL,
  `no_gigi` varchar(5) DEFAULT NULL,
  `tarif` int(11) DEFAULT NULL,
  `jumlah` int(11) DEFAULT 1,
  `keterangan` varchar(200) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `diagnosis`
--
ALTER TABLE `diagnosis`
  ADD PRIMARY KEY (`kode_diagnosis`);

--
-- Indexes for table `dokter`
--
ALTER TABLE `dokter`
  ADD PRIMARY KEY (`id_dokter`);

--
-- Indexes for table `jadwal_dokter`
--
ALTER TABLE `jadwal_dokter`
  ADD PRIMARY KEY (`id_jadwal`),
  ADD KEY `id_dokter` (`id_dokter`);

--
-- Indexes for table `janji_temu`
--
ALTER TABLE `janji_temu`
  ADD PRIMARY KEY (`id_janji`);

--
-- Indexes for table `kunjungan`
--
ALTER TABLE `kunjungan`
  ADD PRIMARY KEY (`id_kunjungan`),
  ADD KEY `no_rm` (`no_rm`),
  ADD KEY `id_dokter` (`id_dokter`);

--
-- Indexes for table `layanan`
--
ALTER TABLE `layanan`
  ADD PRIMARY KEY (`kode_layanan`);

--
-- Indexes for table `obat`
--
ALTER TABLE `obat`
  ADD PRIMARY KEY (`kode_obat`);

--
-- Indexes for table `odontogram`
--
ALTER TABLE `odontogram`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_rekam` (`id_rekam`);

--
-- Indexes for table `pasien`
--
ALTER TABLE `pasien`
  ADD PRIMARY KEY (`no_rm`),
  ADD UNIQUE KEY `nik` (`nik`);

--
-- Indexes for table `pembayaran`
--
ALTER TABLE `pembayaran`
  ADD PRIMARY KEY (`no_kuitansi`),
  ADD UNIQUE KEY `id_kunjungan` (`id_kunjungan`);

--
-- Indexes for table `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `rekam_medis`
--
ALTER TABLE `rekam_medis`
  ADD PRIMARY KEY (`id_rekam`),
  ADD UNIQUE KEY `id_kunjungan` (`id_kunjungan`),
  ADD KEY `kode_diagnosis` (`kode_diagnosis`);

--
-- Indexes for table `resep`
--
ALTER TABLE `resep`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_kunjungan` (`id_kunjungan`);

--
-- Indexes for table `tindakan_kunjungan`
--
ALTER TABLE `tindakan_kunjungan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_kunjungan` (`id_kunjungan`),
  ADD KEY `kode_layanan` (`kode_layanan`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `jadwal_dokter`
--
ALTER TABLE `jadwal_dokter`
  MODIFY `id_jadwal` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `janji_temu`
--
ALTER TABLE `janji_temu`
  MODIFY `id_janji` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `kunjungan`
--
ALTER TABLE `kunjungan`
  MODIFY `id_kunjungan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `odontogram`
--
ALTER TABLE `odontogram`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `pengguna`
--
ALTER TABLE `pengguna`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `rekam_medis`
--
ALTER TABLE `rekam_medis`
  MODIFY `id_rekam` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `resep`
--
ALTER TABLE `resep`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `tindakan_kunjungan`
--
ALTER TABLE `tindakan_kunjungan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `jadwal_dokter`
--
ALTER TABLE `jadwal_dokter`
  ADD CONSTRAINT `jadwal_dokter_ibfk_1` FOREIGN KEY (`id_dokter`) REFERENCES `dokter` (`id_dokter`);

--
-- Constraints for table `kunjungan`
--
ALTER TABLE `kunjungan`
  ADD CONSTRAINT `kunjungan_ibfk_1` FOREIGN KEY (`no_rm`) REFERENCES `pasien` (`no_rm`),
  ADD CONSTRAINT `kunjungan_ibfk_2` FOREIGN KEY (`id_dokter`) REFERENCES `dokter` (`id_dokter`);

--
-- Constraints for table `odontogram`
--
ALTER TABLE `odontogram`
  ADD CONSTRAINT `odontogram_ibfk_1` FOREIGN KEY (`id_rekam`) REFERENCES `rekam_medis` (`id_rekam`);

--
-- Constraints for table `pembayaran`
--
ALTER TABLE `pembayaran`
  ADD CONSTRAINT `pembayaran_ibfk_1` FOREIGN KEY (`id_kunjungan`) REFERENCES `kunjungan` (`id_kunjungan`);

--
-- Constraints for table `rekam_medis`
--
ALTER TABLE `rekam_medis`
  ADD CONSTRAINT `rekam_medis_ibfk_1` FOREIGN KEY (`id_kunjungan`) REFERENCES `kunjungan` (`id_kunjungan`),
  ADD CONSTRAINT `rekam_medis_ibfk_2` FOREIGN KEY (`kode_diagnosis`) REFERENCES `diagnosis` (`kode_diagnosis`);

--
-- Constraints for table `resep`
--
ALTER TABLE `resep`
  ADD CONSTRAINT `resep_ibfk_1` FOREIGN KEY (`id_kunjungan`) REFERENCES `kunjungan` (`id_kunjungan`) ON DELETE CASCADE;

--
-- Constraints for table `tindakan_kunjungan`
--
ALTER TABLE `tindakan_kunjungan`
  ADD CONSTRAINT `tindakan_kunjungan_ibfk_1` FOREIGN KEY (`id_kunjungan`) REFERENCES `kunjungan` (`id_kunjungan`),
  ADD CONSTRAINT `tindakan_kunjungan_ibfk_2` FOREIGN KEY (`kode_layanan`) REFERENCES `layanan` (`kode_layanan`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
