package core;

public class Dosen extends Pengguna {
    // Constructor
    public Dosen(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean login(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }

    // Method khusus dosen
    public void setMataKuliah(String namaMatkul) {
        System.out.println("Mata kuliah " + namaMatkul + " berhasil diset!");
    }
}