// Superclass
class PerangkatPintar {
    String nama;

    PerangkatPintar(String nama) {
        this.nama = nama;
    }

    void nyalakan() {
        System.out.println(nama + " dinyalakan.");
    }
}

// Subclass LampuPintar
class LampuPintar extends PerangkatPintar {

    LampuPintar() {
        super("Lampu Pintar");
    }

    @Override
    void nyalakan() {
        System.out.println("Lampu menyala dengan kecerahan normal.");
    }

    // Overloading: tanpa warna
    void aturKecerahan(int tingkat) {
        System.out.println("Kecerahan diatur ke tingkat " + tingkat + "%.");
    }

    // Overloading: dengan warna
    void aturKecerahan(int tingkat, String warna) {
        System.out.println("Kecerahan diatur ke tingkat " + tingkat + "% dengan warna " + warna + ".");
    }
}

// Subclass AcPintar
class AcPintar extends PerangkatPintar {

    AcPintar() {
        super("AC Pintar");
    }

    @Override
    void nyalakan() {
        System.out.println("AC menyala dan mulai mendinginkan ruangan.");
    }

    void aturSuhu(int suhu) {
        System.out.println("Suhu ditetapkan menjadi " + suhu + " derajat.");
    }
}

public class Main {
    public static void main(String[] args) {

        // --- Polymorphism ---
        System.out.println("=== Polymorphism ===");
        PerangkatPintar[] devices = { new LampuPintar(), new AcPintar() };

        for (PerangkatPintar device : devices) {
            device.nyalakan();
        }

        // --- Overloading ---
        System.out.println("\n=== Overloading ===");
        LampuPintar lampu = new LampuPintar();
        lampu.aturKecerahan(70);
        lampu.aturKecerahan(80, "Kuning");

        // --- Downcasting ---
        System.out.println("\n=== Downcasting ===");
        for (PerangkatPintar device : devices) {
            if (device instanceof AcPintar ac) {
                ac.aturSuhu(24);
            }
        }

        // --- Casting LampuPintar ---
        System.out.println("\n=== Casting LampuPintar ===");
        PerangkatPintar alat = new LampuPintar();
        if (alat instanceof LampuPintar l) {
            l.aturKecerahan(75, "Putih");
        }
    }
}