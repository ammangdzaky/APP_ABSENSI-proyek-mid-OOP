package entity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MataKuliah {
    private String nama;
    private String hari; // disimpan lowercase
    private LocalTime waktuMulai;
    private LocalTime waktuSelesai;
    private String token;

    public MataKuliah(String nama, String hari, String waktuMulaiStr,
        String waktuSelesaiStr, String token) {
        this.nama = nama;
        setHari(hari);
        this.token = token;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        try {
            this.waktuMulai = LocalTime.parse(waktuMulaiStr, formatter);
            this.waktuSelesai = LocalTime.parse(waktuSelesaiStr, formatter);

            // Validasi waktu selesai harus setelah waktu mulai
            if (this.waktuSelesai.isBefore(this.waktuMulai)) {
                throw new IllegalArgumentException("Waktu selesai harus setelah waktu mulai");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format waktu harus H:mm (contoh: 8:00 atau 13:30)");
        }
    }

    // --- Validasi ---
    public boolean isWaktuSekarangValid() {
        LocalTime sekarang = LocalTime.now();
        return !sekarang.isBefore(waktuMulai) && !sekarang.isAfter(waktuSelesai);
    }

    public boolean isHariDanWaktuValid() {
        return isHariValid() && isWaktuSekarangValid();
    }

    public boolean isHariValid() {
        return convertHariToDayOfWeek(this.hari).equals(LocalDate.now().getDayOfWeek());
    }

    private DayOfWeek convertHariToDayOfWeek(String hari) {
        return switch (hari.toLowerCase()) {
            case "senin" -> DayOfWeek.MONDAY;
            case "selasa" -> DayOfWeek.TUESDAY;
            case "rabu" -> DayOfWeek.WEDNESDAY;
            case "kamis" -> DayOfWeek.THURSDAY;
            case "jumat" -> DayOfWeek.FRIDAY;
            case "sabtu" -> DayOfWeek.SATURDAY;
            case "minggu" -> DayOfWeek.SUNDAY;
            default -> throw new IllegalArgumentException(
                    "Hari harus: Senin, Selasa, Rabu, Kamis, Jumat, Sabtu, Minggu"
            );
        };
    }

    // --- Getter & Setter ---
    public String getNama() { return nama; }
    public String getHari() { return hari; }
    public String getHariFormatted() {
        return hari.substring(0, 1).toUpperCase() + hari.substring(1);
    }
    public LocalTime getWaktuMulai() { return waktuMulai; }
    public LocalTime getWaktuSelesai() { return waktuSelesai; }
    public String getToken() { return token; }

    public void setHari(String hari) {
        try {
            this.hari = hari.trim().toLowerCase();
            convertHariToDayOfWeek(this.hari); // Validasi
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Hari harus: Senin, Selasa, Rabu, Kamis, Jumat, Sabtu, Minggu"
            );
        }
    }

    public void setToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token tidak boleh kosong");
        }
        this.token = token;
    }
}