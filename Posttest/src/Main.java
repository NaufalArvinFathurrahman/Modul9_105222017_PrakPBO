// Superclass
class MetodePembayaran {
    String nama;

    MetodePembayaran(String nama) {
        this.nama = nama;
    }

    void bayar(double nominal) {
        System.out.println("Memproses transaksi " + nama + " sebesar Rp" + nominal);
    }
}

// Subclass 1
class EWallet extends MetodePembayaran {

    EWallet() {
        super("E-Wallet");
    }

    @Override
    void bayar(double nominal) {
        System.out.println("Membayar via E-Wallet sebesar Rp" + nominal);
    }

    // Overloading: dengan nomor HP
    void bayar(double nominal, String nomorHP) {
        System.out.println("Membayar via E-Wallet sebesar Rp" + nominal + " dari nomor " + nomorHP);
    }
}

// Subclass 2
class KartuKredit extends MetodePembayaran {

    KartuKredit() {
        super("Kartu Kredit");
    }

    @Override
    void bayar(double nominal) {
        System.out.println("Mencetak tagihan Kartu Kredit sebesar Rp" + nominal);
    }

    void verifikasiPIN() {
        System.out.println("Memverifikasi PIN Kartu Kredit... BERHASIL");
    }
}

public class Main {
    public static void main(String[] args) {

        // Polymorphism - inisialisasi langsung
        MetodePembayaran[] metodePembayaran = { new EWallet(), new KartuKredit() };

        for (MetodePembayaran metode : metodePembayaran) {
            metode.bayar(100000);

            // Downcasting - pakai pattern variable
            if (metode instanceof EWallet ew) {
                ew.bayar(50000, "08123456789");
            }

            if (metode instanceof KartuKredit kk) {
                kk.verifikasiPIN();
            }

            System.out.println();
        }
    }
}