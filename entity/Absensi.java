package entity;

import core.Mahasiswa;
import java.time.LocalDate;

public class Absensi {
    private Mahasiswa mahasiswa;
    private MataKuliah mataKuliah;
    private String status;  // "Hadir", "Sakit", "Izin"
    private LocalDate tanggal;

    // Constructor untuk absensi baru (tanggal = hari ini)
    public Absensi(Mahasiswa mahasiswa, MataKuliah mataKuliah, String status) {
        this(mahasiswa, mataKuliah, status, LocalDate.now());
    }

    // Constructor untuk load dari file (dengan tanggal spesifik)
    public Absensi(Mahasiswa mahasiswa, MataKuliah mataKuliah, String status, LocalDate tanggal) {
        this.mahasiswa = mahasiswa;
        this.mataKuliah = mataKuliah;
        this.status = status;
        this.tanggal = tanggal;
    }

    // Validasi token (khusus status "Hadir")
    public boolean isValidToken(String inputToken) {
        if (!status.equalsIgnoreCase("Hadir")) {
            return true;  // Tidak perlu token jika bukan "Hadir"
        }
        return mataKuliah.getToken().equals(inputToken);
    }

    // Getter
    public String getStatus() {
        return status;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public MataKuliah getMataKuliah() {
        return mataKuliah;
    }

    public Mahasiswa getMahasiswa() {
        return mahasiswa;
    }
}