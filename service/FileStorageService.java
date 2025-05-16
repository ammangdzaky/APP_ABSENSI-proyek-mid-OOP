package service;

import core.*;
import entity.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

public class FileStorageService {
    private static final String DATA_DIR = "data";
    private static final String USER_FILE = DATA_DIR + "/users.txt";
    private static final String MATKUL_FILE = DATA_DIR + "/matkul.txt";
    private static final String ABSENSI_FILE = DATA_DIR + "/absensi.txt";

    static {
        // Buat folder data jika belum ada
        new File(DATA_DIR).mkdirs();
    }

    // ======================= USERS =======================
    public static List<Pengguna> loadUsers() {
        try {
            if (!Files.exists(Paths.get(USER_FILE))) {
                return new ArrayList<>();
            }

            return Files.readAllLines(Paths.get(USER_FILE)).stream()
                    .map(line -> {
                        String[] parts = line.split("\\|");
                        if (parts[0].equals("dosen")) {
                            return new Dosen(parts[1], parts[2]);
                        } else {
                            Mahasiswa mhs = new Mahasiswa(parts[1], parts[2]);
                            if (parts.length > 3) mhs.setNamaLengkap(parts[3]);
                            return mhs;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveUser(Pengguna user) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(USER_FILE),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            String line = (user instanceof Dosen ? "dosen" : "mahasiswa") + "|"
                    + user.getUsername() + "|"
                    + user.getPassword();

            if (user instanceof Mahasiswa) {
                line += "|" + ((Mahasiswa) user).getNamaLengkap();
            }

            writer.write(line + "\n");
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    }

    // ======================= MATA KULIAH =======================
    public static List<MataKuliah> loadMataKuliah() {
        try {
            if (!Files.exists(Paths.get(MATKUL_FILE))) {
                return new ArrayList<>();
            }

            return Files.readAllLines(Paths.get(MATKUL_FILE)).stream()
                    .map(line -> {
                        String[] parts = line.split("\\|");
                        return new MataKuliah(
                                parts[0], // nama
                                parts[1], // hari
                                parts[2], // waktuMulai
                                parts[3], // waktuSelesai
                                parts[4]  // token
                        );
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error loading mata kuliah: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveMataKuliah(MataKuliah matkul) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(MATKUL_FILE),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            writer.write(String.format("%s|%s|%s|%s|%s\n",
                    matkul.getNama(),
                    matkul.getHari(),
                    matkul.getWaktuMulai(),
                    matkul.getWaktuSelesai(),
                    matkul.getToken()
            ));
        } catch (IOException e) {
            System.err.println("Error saving mata kuliah: " + e.getMessage());
        }
    }

    // ======================= ABSENSI =======================
    public static List<Absensi> loadAbsensi() {
        try {
            if (!Files.exists(Paths.get(ABSENSI_FILE))) {
                return new ArrayList<>();
            }

            return Files.readAllLines(Paths.get(ABSENSI_FILE)).stream()
                    .map(line -> {
                        String[] parts = line.split("\\|");
                        // Cari mahasiswa dan matkul yang sudah ada
                        Mahasiswa mhs = findMahasiswaByUsername(parts[0]);
                        MataKuliah matkul = findMataKuliahByNama(parts[1]);

                        return new Absensi(
                                mhs != null ? mhs : new Mahasiswa(parts[0], ""),
                                matkul != null ? matkul : new MataKuliah(parts[1], "", "", "", ""),
                                parts[2], // status
                                LocalDate.parse(parts[3]) // tanggal dari file
                        );
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error loading absensi: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveAbsensi(Absensi absensi) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(ABSENSI_FILE),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            writer.write(String.format("%s|%s|%s|%s\n",
                    absensi.getMahasiswa().getUsername(),
                    absensi.getMataKuliah().getNama(),
                    absensi.getStatus(),
                    absensi.getTanggal()
            ));
        } catch (IOException e) {
            System.err.println("Error saving absensi: " + e.getMessage());
        }
    }

    // ======================= HELPER METHODS =======================
    private static Mahasiswa findMahasiswaByUsername(String username) {
        return loadUsers().stream()
                .filter(u -> u instanceof Mahasiswa && u.getUsername().equals(username))
                .map(u -> (Mahasiswa) u)
                .findFirst()
                .orElse(null);
    }

    private static MataKuliah findMataKuliahByNama(String nama) {
        return loadMataKuliah().stream()
                .filter(m -> m.getNama().equalsIgnoreCase(nama))
                .findFirst()
                .orElse(null);
    }

    // ======================= UTILITIES =======================
    public static void clearAllData() {
        try {
            Files.deleteIfExists(Paths.get(USER_FILE));
            Files.deleteIfExists(Paths.get(MATKUL_FILE));
            Files.deleteIfExists(Paths.get(ABSENSI_FILE));
        } catch (IOException e) {
            System.err.println("Error clearing data: " + e.getMessage());
        }
    }

    public static void clearAllMatkul() throws IOException {
        Files.deleteIfExists(Paths.get(MATKUL_FILE));
    }

    public static void simpanSemuaMatkul(List<MataKuliah> matkulList) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(MATKUL_FILE),
                StandardOpenOption.TRUNCATE_EXISTING)) {

            for (MataKuliah m : matkulList) {
                writer.write(String.format("%s|%s|%s|%s|%s\n",
                        m.getNama(), m.getHari(),
                        m.getWaktuMulai(), m.getWaktuSelesai(),
                        m.getToken()));
            }
        } catch (IOException e) {
            System.err.println("Gagal menyimpan mata kuliah: " + e.getMessage());
        }
    }
}