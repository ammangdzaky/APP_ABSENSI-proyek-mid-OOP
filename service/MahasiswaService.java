package service;

import core.Mahasiswa;
import entity.*;
import util.AppUtils;

import java.time.LocalDate;
import java.util.List;

public class MahasiswaService implements IInfoAbsensi{

    private Mahasiswa currentMahasiswa;  // Menyimpan mahasiswa yang login

    // Constructor untuk inject mahasiswa yang login
    public MahasiswaService(Mahasiswa mahasiswa) {
        this.currentMahasiswa = mahasiswa;
    }

    // === FITUR ABSENSI DENGAN PILIHAN ANGKA ===
    public static void absensi(Mahasiswa mahasiswa) {
        System.out.println("\n=== ABSENSI ===");
        int pilihan = showAbsensiMenu();
        String status = convertToStatus(pilihan);

        System.out.print("Token Matkul: ");
        String token = AppUtils.scanner.nextLine();

        List<MataKuliah> matkulList = FileStorageService.loadMataKuliah();
        MataKuliah matkul = matkulList.stream()
                .filter(m -> m.getToken().equalsIgnoreCase(token))
                .findFirst()
                .orElse(null);

        if (matkul == null) {
            System.out.println("❌ Token tidak valid!");
            return;
        }

        // Pengecekan absensi ganda
        boolean sudahAbsen = FileStorageService.loadAbsensi().stream()
                .anyMatch(a -> a.getMahasiswa().getUsername().equals(mahasiswa.getUsername()) &&
                        a.getMataKuliah().getNama().equals(matkul.getNama()) &&
                        a.getTanggal().equals(LocalDate.now()));

        if (sudahAbsen) {
            System.out.println("⚠️ Anda sudah absen untuk mata kuliah ini hari ini!");
            AppUtils.pressEnterToContinue();
            return;
        }

        if (status.equals("Hadir")) {
            if (!matkul.isHariDanWaktuValid()) {
                System.out.println("⚠️ Absensi hanya bisa dilakukan pada " +
                        matkul.getHariFormatted() + " jam " +
                        matkul.getWaktuMulai() + "-" + matkul.getWaktuSelesai());
                return;
            }
        }

        Absensi absensi = new Absensi(mahasiswa, matkul, status);
        FileStorageService.saveAbsensi(absensi);
        System.out.println("✅ Absensi " + status + " untuk " + matkul.getNama() + " berhasil!");
    }

    private static int showAbsensiMenu() {
        System.out.println("""
            Pilih Status:
            1. Hadir
            2. Sakit
            3. Izin
            """);
        return AppUtils.getValidIntInput("Pilihan (1-3): ", 1, 3);
    }

    private static String convertToStatus(int choice) {
        return switch (choice) {
            case 1 -> "Hadir";
            case 2 -> "Sakit";
            case 3 -> "Izin";
            default -> throw new IllegalArgumentException("Pilihan tidak valid");
        };
    }

    // === FITUR INFO ABSENSI ===
    @Override
    public void infoAbsensi(String token) {
        // Delegasi ke DosenService jika perlu
        DosenService dosenService = new DosenService();
        dosenService.infoAbsensi(token);
    }

    @Override
    public void infoAbsensi() {
        System.out.println("\n=== ABSENSI SAYA ===");

        // Pakai currentMahasiswa yang sudah disimpan
        FileStorageService.loadAbsensi().stream()
                .filter(a -> a.getMahasiswa().getUsername()
                        .equals(currentMahasiswa.getUsername()))
                .forEach(a -> {
                    System.out.println("Mata Kuliah: " + a.getMataKuliah().getNama());
                    System.out.println("Status    : " + a.getStatus());
                    System.out.println("Tanggal   : " + a.getTanggal());
                    System.out.println("---------------------------------");
                });

        long totalHadir = FileStorageService.loadAbsensi().stream()
                .filter(a -> a.getMahasiswa().getUsername()
                        .equals(currentMahasiswa.getUsername()))
                .filter(a -> a.getStatus().equalsIgnoreCase("hadir"))
                .count();

        System.out.println("TOTAL HADIR: " + totalHadir);
    }
}