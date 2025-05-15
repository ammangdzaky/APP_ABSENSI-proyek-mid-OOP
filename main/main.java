package main;

import core.*;
import entity.*;
import service.*;
import util.AppUtils;

import java.io.IOException;
import java.util.Scanner;

import static service.MahasiswaService.absensi;
import static util.AppUtils.scanner;

public class Main {
    private static Pengguna userLoggedIn = null;

    public static void main(String[] args) throws IOException {
        showMainMenu();
    }

    private static void showMainMenu() throws IOException {
        while (true) {
            AppUtils.clearScreen();
            System.out.println("""
            \n\n=== APLIKASI ABSENSI ===
            1. Login Dosen
            2. Mahasiswa
            3. Keluar
            """);

            int choice = AppUtils.getValidIntInput("Pilih menu: ");
            switch (choice) {
                case 1 -> handleDosenLogin();
                case 2 -> handleMahasiswaAuth(); // Arahkan ke menu mahasiswa
                case 3 -> System.exit(0);
                default -> System.out.println("Pilihan tidak valid!");
            }
        }
    }

    // ===== DOSEN HANDLER =====
    private static void handleDosenLogin() throws IOException {
        AppUtils.clearScreen();
        System.out.println("=== LOGIN DOSEN ===");
        userLoggedIn = AuthService.login(scanner);

        if (userLoggedIn instanceof Dosen) {
            showDosenMenu();
        } else {
            System.out.println("Login gagal! Cek kredensial.");
            AppUtils.pressEnterToContinue();
        }
    }

    private static void showDosenMenu() throws IOException {
//        DosenService dosenService = new DosenService(); // Buat instance
        IInfoAbsensi absensiService = new DosenService();

        while (true) {
            AppUtils.clearScreen();
            System.out.println("""
            \n\n=== MENU DOSEN ===
            1. Tambah Mata Kuliah
            2. Lihat Daftar Matkul
            3. Lihat Absensi
            4. Update Token Matkul
            5. Logout
            """);

            int choice = AppUtils.getValidIntInput("Pilih menu: ");
            switch (choice) {
                case 1 -> DosenService.setMataKuliah(); // Tetap static
                case 2 -> { DosenService.infoMatkul(); AppUtils.pressEnterToContinue(); }
                case 3 -> absensiService.infoAbsensi(); // Panggil via instance
                case 4 -> DosenService.updateTokenMatkul();
                case 5 -> { return; }
                default -> System.out.println("Pilihan tidak valid!");
            }
        }
    }
}

    // ===== MAHASISWA HANDLER =====
    private static void handleMahasiswaAuth() {
        while (true) {
            AppUtils.clearScreen();
            System.out.println("""
            \n\n=== MAHASISWA ===
            1. Login
            2. Sign Up
            3. Kembali
            """);

            int choice = AppUtils.getValidIntInput("Pilih menu: ");
            switch (choice) {
                case 1 -> {
                    handleMahasiswaLogin();
                    return; // Kembali ke main menu setelah login
                }
                case 2 -> {
                    handleMahasiswaSignUp();
                    // Tetap di menu ini setelah sign up
                }
                case 3 -> { return; } // Kembali ke main menu
                default -> System.out.println("Pilihan tidak valid!");
            }
        }
    }

