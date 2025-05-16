# 📘 APP-ABSENSI

## 📝 Deskripsi Singkat Proyek
Aplikasi ini merupakan sistem absensi sederhana berbasis Java yang dirancang untuk dua jenis pengguna, yaitu **Dosen** dan **Mahasiswa**. Proyek ini mensimulasikan proses absensi perkuliahan dengan menggunakan **token** sebagai alat verifikasi kehadiran.
Setiap dosen dapat menambahkan mata kuliah, membuat token absensi, serta mengelola jadwal dan melihat data kehadiran mahasiswa. Mahasiswa, di sisi lain, dapat melakukan registrasi akun, login, dan mengisi absensi sesuai jadwal menggunakan token yang diberikan oleh dosen.
Aplikasi ini dikembangkan dengan pendekatan pemrograman berorientasi objek, serta menerapkan struktur folder yang terorganisasi seperti `core`, `entity`, `service`, dan `util`, agar mudah dipelajari dan dikembangkan lebih lanjut. Selain itu, aplikasi ini menggunakan penyimpanan file `.txt` sebagai simulasi pengganti database agar tetap ringan dan mudah digunakan untuk keperluan pembelajaran.
an **token** sebagai alat verifikasi kehadiran.

## 👥 Fitur Utama

### 👨‍🏫 Untuk Dosen:
- Login menggunakan akun dosen.
- Membuat dan mengatur mata kuliah.
- Menentukan token untuk absensi.
- Melihat rekap data kehadiran mahasiswa.

### 👨‍🎓 Untuk Mahasiswa:
- Sign up dan login.
- Melihat daftar mata kuliah.
- Melakukan absensi (Hadir, Sakit, Izin) menggunakan token.
- Melihat riwayat absensi.


Tujuan dari proyek ini adalah untuk memberikan gambaran sederhana tentang bagaimana sistem manajemen absensi berbasis peran pengguna dapat diimplementasikan menggunakan bahasa pemrograman **Java**.
---

## 🏗️ Struktur Class
```
APP-ABSENSI/
├── core/
│   ├── Dosen.java             # Class turunan dari Pengguna khusus untuk dosen
│   ├── Mahasiswa.java         # Class turunan dari Pengguna khusus untuk mahasiswa
│   └── Pengguna.java          # Class dasar untuk pengguna (dosen & mahasiswa)
├── data/
│   ├── absensi.txt            # Penyimpanan data kehadiran
│   ├── matkul.txt             # Penyimpanan data mata kuliah
│   └── users.txt              # Penyimpanan akun pengguna
├── entity/
│   ├── Absensi.java           # Representasi data absensi
│   └── MataKuliah.java        # Representasi data mata kuliah
├── main/
│   └── Main.java              # Titik masuk aplikasi
├── service/
│   ├── AbsensiService.java     # Layanan untuk pengelolaan absensi
│   ├── AuthService.java        # Layanan untuk autentikasi login
│   ├── DosenService.java       # Layanan khusus dosen
│   ├── FileStorageService.java # Layanan baca/tulis file
│   ├── IInfoAbsensi.java       # Interface untuk informasi absensi
│   └── MahasiswaService.java   # Layanan khusus mahasiswa
├── util/
│   └── AppUtils.java           # Utilitas umum aplikasi
```

## ▶️ Cara Menjalankan Program
1. Pastikan Java Development Kit (JDK) sudah terinstal di komputermu.
2. Buka proyek ini di IDE seperti **IntelliJ IDEA** atau **NetBeans**
3. Jalankan file `Main.java` yang berada di dalam folder `main`
4. Ikuti instruksi pada terminal untuk login sebagai **Dosen** atau **Mahasiswa**

---

## 👥 Pembagian Tugas per Anggota

| Nama Anggota                    | Tugas yang Dikerjakan                         |
|---------------------------------|-----------------------------------------------|
| Abdurrahman Dzaky Safrullah     | Semua layanan pada folder `service/`          |
| Akram Alfandi Tamir             | Class pengguna pada folder `core/`            |
| Natasya                         | Implementasi `Main.java` dan membuat README.md|
| Sita Rasmi Raihana              | Class dan logika pada folder `entity/`        |
---

## 📌 Catatan
- Program ini berbasis **CLI (Command Line Interface)**, sehingga interaksi pengguna dilakukan melalui terminal. Semua input dan output dilakukan melalui teks, tanpa tampilan grafis.
- Data pengguna, absensi, dan mata kuliah disimpan dalam file `.txt` sederhana untuk keperluan simulasi. Ini memungkinkan penyimpanan data secara permanen meskipun aplikasi ditutup.
- Setiap peran pengguna (Dosen dan Mahasiswa) memiliki menu dan fungsionalitas yang berbeda, sehingga logika program terbagi sesuai peran.
- Program tidak menggunakan database atau framework eksternal agar tetap ringan dan mudah dipahami, cocok untuk pembelajaran dasar pemrograman Java.
- Struktur folder yang modular membantu pemisahan tanggung jawab, sehingga memudahkan pengembangan dan pemeliharaan kode.
