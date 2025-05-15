package core;

public abstract class Pengguna {
    // Attribute (protected agar bisa diakses child class)
    protected String username;
    protected String password;

    // Constructor
    public Pengguna(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Abstract method (wajib diimplementasikan child class)
    public abstract boolean login(String inputUsername, String inputPassword);

    // Getter (Encapsulation)
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}