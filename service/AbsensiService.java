package service;

import core.Mahasiswa;
import entity.*;

import java.time.LocalDate;
import java.util.Scanner;

public class AbsensiService {
    public static void handleAbsensi(Mahasiswa mhs, MataKuliah matkul, Scanner scanner) {
        System.out.print("Status (Hadir/Sakit/Izin): ");
        String status = scanner.nextLine();

        String token = null;
        if (status.equalsIgnoreCase("Hadir")) {
            System.out.print("Token: ");
            token = scanner.nextLine();

            if (!matkul.isWaktuSekarangValid()) {
                System.out.println("⚠️ Diluar jam kuliah!");
                return;
            }

            if (!matkul.isHariValid()) {
                System.out.println("⚠️ Bukan hari kuliah!");
                return;
            }
        }

        // Cek apakah mahasiswa sudah absen hari ini
        boolean sudahAbsen = FileStorageService.loadAbsensi().stream()
                .anyMatch(a -> a.getMahasiswa().getUsername().equals(mhs.getUsername()) &&
                        a.getMataKuliah().getNama().equals(matkul.getNama()) &&
                        a.getTanggal().equals(LocalDate.now()));

        if (sudahAbsen) {
            System.out.println("⚠️ Anda sudah absen hari ini!");
            return;
        }

        Absensi absensi = new Absensi(mhs, matkul, status);
        if (absensi.isValidToken(token)) {
            FileStorageService.saveAbsensi(absensi);  // Ganti dengan FileStorageService
            System.out.println("✅ Absensi berhasil!");
        } else {
            System.out.println("❌ Token salah/tidak diperlukan!");
        }
    }
}