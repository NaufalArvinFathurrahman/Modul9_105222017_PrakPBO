class Pasien {
    private String nama;
    private int umur;

    public Pasien(String nama, int umur) {
        this.nama = nama;
        this.umur = umur;
    }

    public String getNama() {
        return nama;
    }

    public int getUmur() {
        return umur;
    }
}

class Dokter {
    private String nama;
    private String spesialisasi;

    public Dokter(String nama, String spesialisasi) {
        this.nama = nama;
        this.spesialisasi = spesialisasi;
    }

    public String getNama() {
        return nama;
    }

    public String getSpesialisasi() {
        return spesialisasi;
    }

    public void periksa(Pasien pasien) {
        System.out.println("--- Rekam Jejak Pemeriksaan ---");
        System.out.println("Dokter yang bertugas : " + this.nama);
        System.out.println("Spesialisasi         : " + this.spesialisasi);
        System.out.println("Sedang menangani");
        System.out.println("Nama Pasien          : " + pasien.getNama());
        System.out.println("Umur Pasien          : " + pasien.getUmur() + " tahun");
        System.out.println("-------------------------------\n");
    }
}

class Ruangan {
    private String nomorRegistrasi;
    private int kapasitasMaksimal;

    public Ruangan(String nomorRegistrasi, int kapasitasMaksimal) {
        this.nomorRegistrasi = nomorRegistrasi;
        this.kapasitasMaksimal = kapasitasMaksimal;
    }

    public String getNomorRegistrasi() {
        return nomorRegistrasi;
    }

    public int getKapasitasMaksimal() {
        return kapasitasMaksimal;
    }
}

class RumahSakit {
    private String nama;
    private Ruangan[] daftarRuangan;
    private Dokter[] daftarDokter;

    public RumahSakit(String nama) {
        this.nama = nama;
        this.daftarRuangan = new Ruangan[2];
        this.daftarRuangan[0] = new Ruangan("R-01", 4);
        this.daftarRuangan[1] = new Ruangan("R-02", 6);
    }

    public void tugaskanDokter(Dokter[] daftarDokter) {
        this.daftarDokter = daftarDokter;
    }

    public void cetakDaftarRuangan() {
        System.out.println("=== Daftar Infrastruktur Ruangan " + this.nama + " ===");
        for (int i = 0; i < daftarRuangan.length; i++) {
            System.out.println("- Ruangan " + (i + 1));
            System.out.println("  Nomor Registrasi : " + daftarRuangan[i].getNomorRegistrasi());
            System.out.println("  Kapasitas Maks   : " + daftarRuangan[i].getKapasitasMaksimal() + " pasien");
        }
        System.out.println("===================================================\n");
    }

    public void cetakDaftarDokter() {
        System.out.println("=== Daftar Dokter Bertugas di " + this.nama + " ===");
        if (daftarDokter != null) {
            for (int i = 0; i < daftarDokter.length; i++) {
                System.out.println("- " + daftarDokter[i].getNama() + " (" + daftarDokter[i].getSpesialisasi() + ")");
            }
        } else {
            System.out.println("Belum ada dokter yang ditugaskan.");
        }
        System.out.println("====================================================\n");
    }
}

public class App {
    public static void main(String[] args) {
        Dokter dokter1 = new Dokter("Dr. Andi", "Spesialis Penyakit Dalam");
        Dokter dokter2 = new Dokter("Dr. Budi", "Spesialis Anak");
        
        Pasien pasien1 = new Pasien("Citra", 25);
        Pasien pasien2 = new Pasien("Doni", 8);

        dokter1.periksa(pasien1);
        dokter2.periksa(pasien2);
        
        RumahSakit rsSehatSelalu = new RumahSakit("RS Sehat Selalu");
        rsSehatSelalu.cetakDaftarRuangan();

        Dokter[] dokterTugas = { dokter1, dokter2 };
        rsSehatSelalu.tugaskanDokter(dokterTugas);
        rsSehatSelalu.cetakDaftarDokter();
    }
}
