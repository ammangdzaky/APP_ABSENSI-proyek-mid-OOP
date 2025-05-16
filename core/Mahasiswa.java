package core;

import core.Pengguna;

public class Mahasiswa extends Pengguna {
    private String namaLengkap;

    // Constructor
    public Mahasiswa(String username, String password) {
        super(username, password.toLowerCase()); // Simpan password lowercase
    }

    @Override
    public boolean login(String inputUsername, String inputPassword) {
        // Login case insensitive
        return this.username.equalsIgnoreCase(inputUsername)
                && this.password.equals(inputPassword.toLowerCase());
    }

    // Getter & Setter
    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap.toLowerCase();
    }
}