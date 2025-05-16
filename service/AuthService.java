package service;

import core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AuthService {
    // Data sementara (nanti bisa pindah ke StorageService)
    public static final List<Pengguna> databaseUser = new ArrayList<>();

    static {
        // Akun dosen default (dibuat admin)
        databaseUser.add(new Dosen("dosen1", "pass123"));
    }

    // Login untuk semua role
    public static Pengguna login(Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        List<Pengguna> users = FileStorageService.loadUsers();
        return users.stream()
                .filter(user -> user.login(username, password))
                .findFirst()
                .orElse(null);
    }

    // Sign up khusus mahasiswa
    public static Mahasiswa signUpMahasiswa(Scanner scanner) {
        System.out.println("\n=== FORM SIGN UP MAHASISWA ===");

        // Validasi input
        String namaLengkap = validateNamaLengkap(scanner);
        String password = validatePassword(scanner);

        // Generate username
        String username = generateUsername(namaLengkap);

        // Cek username unik
        if (FileStorageService.loadUsers().stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(username))) {
            throw new IllegalArgumentException("Username sudah digunakan!");
        }

        // Buat dan simpan mahasiswa baru
        Mahasiswa mhs = new Mahasiswa(username, password);
        mhs.setNamaLengkap(namaLengkap);
        FileStorageService.saveUser(mhs);

        System.out.println("\n✅ Pendaftaran berhasil!");
        System.out.println("Username Anda: " + username);
        return mhs;
    }

    // ===== METHOD BANTU =====
    private static String validateNamaLengkap(Scanner scanner) {
        while (true) {
            System.out.print("Nama Lengkap: ");
            String nama = scanner.nextLine().trim().toLowerCase();

            if (nama.isEmpty()) {
                System.out.println("❌ Nama tidak boleh kosong");
            } else if (nama.matches(".*\\d.*")) {
                System.out.println("❌ Nama tidak boleh mengandung angka");
            } else if (nama.length() < 2) {
                System.out.println("❌ Nama terlalu pendek");
            } else {
                return nama;
            }
        }
    }

    private static String validatePassword(Scanner scanner) {
        while (true) {
            System.out.print("Password (min 8 karakter, huruf+angka): ");
            String password = scanner.nextLine();

            if (password.length() < 8) {
                System.out.println("❌ Password minimal 8 karakter");
            } else if (!password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) {
                System.out.println("❌ Harus kombinasi huruf dan angka");
            } else {
                return password;
            }
        }
    }

    private static String generateUsername(String nama) {
        // Contoh: "asep sunarya" -> "asepsunarya"
        return nama.replaceAll("\\s+", "");
    }

    private static boolean isUsernameExists(String username) {
        return databaseUser.stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }
}