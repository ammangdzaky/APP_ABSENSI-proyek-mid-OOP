package service;

import entity.*;
import util.AppUtils;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DosenService implements IInfoAbsensi{
    // === FITUR SET MATA KULIAH & JADWAL ===
    public static void setMataKuliah() {
        Scanner scanner = AppUtils.scanner;
        System.out.println("\n=== TAMBAH MATA KULIAH ===");

        String nama = AppUtils.getNonEmptyInput("Nama Mata Kuliah: ");
        String hari = AppUtils.getNonEmptyInput("Hari (Contoh: Senin): ");
        String waktuMulaiStr = AppUtils.getTimeInput("Waktu Mulai (HH:mm): ");
        String waktuSelesaiStr = AppUtils.getTimeInput("Waktu Selesai (HH:mm): ");
        String token = AppUtils.getNonEmptyInput("Token Absensi: ");

        try {
            // Buat objek matkul dulu untuk validasi
            MataKuliah matkulBaru = new MataKuliah(nama, hari, waktuMulaiStr, waktuSelesaiStr, token);

            // Validasi jadwal
            if (isJadwalTumpangTindih(matkulBaru)) {
                System.out.println("❌ Jadwal bertabrakan dengan mata kuliah lain!");
                return;
            }

            // Jika validasi berhasil, simpan ke file
            FileStorageService.saveMataKuliah(matkulBaru);
            System.out.println("✅ Mata kuliah berhasil ditambahkan!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Gagal: " + e.getMessage());
        }
    }


    // === FITUR INFO MATA KULIAH ===
    public static void infoMatkul() {
        System.out.println("\n=== DAFTAR MATA KULIAH ===");
        List<MataKuliah> matkulList = FileStorageService.loadMataKuliah();

        if (matkulList.isEmpty()) {
            System.out.println("Belum ada mata kuliah terdaftar!");
            return;
        }

        matkulList.forEach(m ->
                System.out.printf(
                        "- %s | Hari: %s | Waktu: %s-%s | Token: %s\n",
                        m.getNama(),
                        m.getHariFormatted(),
                        m.getWaktuMulai(),
                        m.getWaktuSelesai(),
                        m.getToken()
                )
        );
    }

    // === FITUR INFO ABSENSI DENGAN TOKEN ===
    @Override
    public void infoAbsensi(String token) {
        List<Absensi> absensi = FileStorageService.loadAbsensi().stream()
                .filter(a -> a.getMataKuliah() != null &&
                        a.getMataKuliah().getToken().equals(token))
                .collect(Collectors.toList());

        if (absensi.isEmpty()) {
            System.out.println("❌ Token tidak valid atau belum ada absensi!");
            return;
        }

        String namaMatkul = absensi.get(0).getMataKuliah().getNama();
        System.out.println("\n=== REKAP ABSENSI ===");
        System.out.println("MATA KULIAH : " + namaMatkul.toUpperCase());
        System.out.println("TANGGAL     : " + AppUtils.formatDate(absensi.get(0).getTanggal()));
        System.out.println("=================================");

        absensi.forEach(a -> {
            System.out.printf(
                    "%s | STATUS: %s\n",
                    a.getMahasiswa().getUsername(),
                    a.getStatus().toUpperCase()
            );
        });

        System.out.println("=================================");
        System.out.println("TOTAL ABSENSI: " + absensi.size());
    }

    @Override
    public void infoAbsensi() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nMasukkan Token Matkul: ");
        String token = scanner.nextLine();
        infoAbsensi(token);  // Panggil versi dengan parameter
    }

    // fitur update token
    public static void updateTokenMatkul() throws IOException {
        List<MataKuliah> matkulList = FileStorageService.loadMataKuliah();

        if (matkulList.isEmpty()) {
            System.out.println("Belum ada mata kuliah terdaftar!");
            return;
        }

        // Tampilkan list matkul
        System.out.println("\n=== DAFTAR MATA KULIAH ===");
        for (int i = 0; i < matkulList.size(); i++) {
            MataKuliah m = matkulList.get(i);
            System.out.printf("%d. %s | %s | %s-%s | Token: %s\n",
                    i+1, m.getNama(), m.getHariFormatted(),
                    m.getWaktuMulai(), m.getWaktuSelesai(), m.getToken());
        }

        // Pilih matkul
        int pilihan = AppUtils.getValidIntInput(
                "\nPilih mata kuliah (1-" + matkulList.size() + "): ",
                1, matkulList.size());

        MataKuliah matkul = matkulList.get(pilihan-1);

        // Input token baru
        String tokenBaru = AppUtils.getNonEmptyInput("Token baru: ");
        matkul.setToken(tokenBaru);

        // Simpan perubahan
        FileStorageService.clearAllMatkul(); // Hapus semua data lama
        matkulList.forEach(FileStorageService::saveMataKuliah); // Simpan ulang

        System.out.println("✅ Token berhasil diupdate!");
    }

    // cek apakah ada matkul yang tumpang tindih
    private static boolean isJadwalTumpangTindih(MataKuliah matkulBaru) {
        List<MataKuliah> semuaMatkul = FileStorageService.loadMataKuliah();

        return semuaMatkul.stream().anyMatch(matkul ->
                matkul.getHari().equalsIgnoreCase(matkulBaru.getHari()) &&
                        isTimeOverlap(
                                matkul.getWaktuMulai(), matkul.getWaktuSelesai(),
                                matkulBaru.getWaktuMulai(), matkulBaru.getWaktuSelesai()
                        )
        );
    }

    private static boolean isTimeOverlap(LocalTime start1, LocalTime end1,
        LocalTime start2, LocalTime end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }
}