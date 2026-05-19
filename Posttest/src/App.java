class Ban {
    private String merk;
    private int ukuranRing;

    public Ban(String merk, int ukuranRing) {
        this.merk = merk;
        this.ukuranRing = ukuranRing;
    }

    public String getMerk() {
        return merk;
    }

    public int getUkuranRing() {
        return ukuranRing;
    }
}

class Mesin {
    private String nomorSeri;
    private int kapasitasCC;

    public Mesin(String nomorSeri, int kapasitasCC) {
        this.nomorSeri = nomorSeri;
        this.kapasitasCC = kapasitasCC;
    }

    public String getNomorSeri() {
        return nomorSeri;
    }

    public int getKapasitasCC() {
        return kapasitasCC;
    }
}

class Mobil {
    private String merkMobil;
    private String warna;
    private Mesin mesinInternal;
    private Ban[] setBan;

    public Mobil(String merkMobil, String warna, String nomorSeriMesin, int kapasitasCC) {
        this.merkMobil = merkMobil;
        this.warna = warna;
        this.mesinInternal = new Mesin(nomorSeriMesin, kapasitasCC);
        this.setBan = new Ban[4]; 
    }

    public void pasangSetBan(Ban[] banBaru) {
        if (banBaru.length <= 4) {
            this.setBan = banBaru;
        } else {
            System.out.println("Gagal memasang ban: Kapasitas maksimal hanya 4 ban.");
        }
    }

    public void tampilkanSpesifikasi() {
        System.out.println("=== Spesifikasi Mobil ===");
        System.out.println("Merk Mobil   : " + this.merkMobil);
        System.out.println("Warna        : " + this.warna);
        System.out.println("--- Spesifikasi Mesin ---");
        System.out.println("Nomor Seri   : " + this.mesinInternal.getNomorSeri());
        System.out.println("Kapasitas CC : " + this.mesinInternal.getKapasitasCC() + " CC");
        System.out.println("--- Spesifikasi Ban ---");
        if (this.setBan[0] != null) {
            for (int i = 0; i < setBan.length; i++) {
                System.out.println("Ban " + (i+1) + " : " + setBan[i].getMerk() + " (Ring " + setBan[i].getUkuranRing() + ")");
            }
        } else {
            System.out.println("Ban belum terpasang.");
        }
        System.out.println("=========================\n");
    }
}

class Montir {
    private String idMontir;
    private String nama;

    public Montir(String idMontir, String nama) {
        this.idMontir = idMontir;
        this.nama = nama;
    }

    public void lakukanQualityControl(Mobil m) {
        System.out.println(">> Montir " + this.nama + " (ID: " + this.idMontir + ") sedang melakukan Quality Control...");
        if (m != null) {
            System.out.println(">> Pengecekan berhasil! Berikut spesifikasinya:");
            m.tampilkanSpesifikasi();
        } else {
            System.out.println(">> ERROR: Mobil tidak ditemukan atau sudah dilebur.");
        }
    }
}

public class App {
    public static void main(String[] args) {
        Ban[] daftarBan = new Ban[4];
        daftarBan[0] = new Ban("Bridgestone", 18);
        daftarBan[1] = new Ban("Bridgestone", 18);
        daftarBan[2] = new Ban("Bridgestone", 18);
        daftarBan[3] = new Ban("Bridgestone", 18);

        Mobil mobilSport = new Mobil("Honda Civic", "Hitam", "ENG-X991", 1500);

        mobilSport.pasangSetBan(daftarBan);

        Montir montirAhli = new Montir("MTR-07", "Joko");
        montirAhli.lakukanQualityControl(mobilSport);

        System.out.println("!---- SKENARIO MOBIL GAGAL UJI KELAYAKAN ----!");
        System.out.println("Mobil dinyatakan cacat produksi dan harus dilebur...");
        mobilSport = null; 
        System.out.println("Mobil telah dilebur (mobilSport = null).\n");

        System.out.println("=== BUKTI EKSISTENSI SETELAH PELEBURAN ===");
        
        System.out.println("1. Mengecek Ban di Gudang (Agregasi):");
        if (daftarBan[0] != null) {
            System.out.println("   -> Ban masih utuh dan bisa dipakai untuk mobil lain.");
            System.out.println("   -> Bukti: Ban 1 merk " + daftarBan[0].getMerk() + " ukuran Ring " + daftarBan[0].getUkuranRing() + " masih eksis.");
        }

        System.out.println("\n2. Mengecek Mesin (Komposisi):");
        System.out.println("   -> Mesin tidak bisa diakses dari Main Class (tidak ada variabel independennya).");
        System.out.println("   -> Karena objek `mobilSport` tempat ia diinstansiasi telah dihancurkan, referensinya");
        System.out.println("      hilang mutlak, sehingga objek Mesin tersebut musnah tanpa jejak di memori.");
    }
}
