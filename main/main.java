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
}