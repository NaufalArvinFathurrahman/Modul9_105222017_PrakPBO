import java.util.Scanner;

// INTERFACE (Abstraksi)
interface Otorisasi {
    boolean verifikasiPIN(String inputPin);
}

// KELAS BUKU MUTASI (Untuk simulasi Komposisi)
class BukuMutasi {
    public void catat(String aktivitas) {
        System.out.println("[Mutasi Log] " + aktivitas);
    }
}

// ABSTRACT CLASS
abstract class Rekening implements Otorisasi {
    // ENKAPSULASI: Properti di-set private agar tidak bisa diubah langsung dari luar
    private String nomorRekening;
    private String namaPemilik;
    private double saldo;
    private String pin;
    
    // KOMPOSISI: BukuMutasi menjadi bagian internal Rekening (terikat kuat)
    protected BukuMutasi mutasiInternal;

    public Rekening(String nomorRekening, String namaPemilik, double saldoAwal, String pin) {
        this.nomorRekening = nomorRekening;
        this.namaPemilik = namaPemilik;
        this.saldo = saldoAwal;
        this.pin = pin;
        
        // KOMPOSISI: Objek BukuMutasi diinstansiasi secara otomatis dan internal tepat 
        // saat Rekening dibangun. Jika entitas Rekening hancur (di-set null), objek 
        // mutasiInternal tidak punya referensi lain dan akan ikut musnah otomatis.
        this.mutasiInternal = new BukuMutasi();
        this.mutasiInternal.catat("Rekening diterbitkan. Saldo Awal: Rp" + saldoAwal);
    }

    public String getNomorRekening() {
        return nomorRekening;
    }

    public String getNamaPemilik() {
        return namaPemilik;
    }

    public double getSaldo() {
        return saldo;
    }

    // Metode internal untuk mengontrol manipulasi saldo (Enkapsulasi ketat)
    protected void kurangiSaldo(double jumlah) {
        this.saldo -= jumlah;
    }

    protected void tambahSaldo(double jumlah) {
        this.saldo += jumlah;
    }

    @Override
    public boolean verifikasiPIN(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public void setor(double jumlah) {
        if (jumlah > 0) {
            tambahSaldo(jumlah);
            System.out.println("Setor tunai berhasil: Rp" + jumlah);
            mutasiInternal.catat("Setor Rp" + jumlah + " | Saldo Akhir: Rp" + this.saldo);
        } else {
            System.out.println("Jumlah setor tidak valid.");
        }
    }

    // Metode abstrak untuk menerapkan Polimorfisme pada kelas turunan
    public abstract void tarik(double jumlah);
}

// ENTITAS KONKRET: Rekening Reguler
class RekeningReguler extends Rekening {
    private static final double BIAYA_ADMIN = 2500.0;

    public RekeningReguler(String nomorRekening, String namaPemilik, double saldoAwal, String pin) {
        super(nomorRekening, namaPemilik, saldoAwal, pin);
    }

    @Override
    public void tarik(double jumlah) {
        double totalPotongan = jumlah + BIAYA_ADMIN;
        if (jumlah <= 0) {
            System.out.println("Jumlah tarik tidak valid.");
        } else if (getSaldo() >= totalPotongan) {
            kurangiSaldo(totalPotongan);
            System.out.println("Tarik Tunai Reguler Berhasil: Rp" + jumlah + " (Terpotong Admin: Rp" + BIAYA_ADMIN + ")");
            mutasiInternal.catat("Tarik Reguler Rp" + jumlah + " | Admin Rp" + BIAYA_ADMIN + " | Saldo Akhir: Rp" + getSaldo());
        } else {
            System.out.println("Transaksi Gagal: Saldo tidak mencukupi untuk penarikan dan biaya admin.");
        }
    }
}

// ENTITAS KONKRET: Rekening Prioritas
class RekeningPrioritas extends Rekening {
    private static final double MIN_TARIK = 500000.0;

    public RekeningPrioritas(String nomorRekening, String namaPemilik, double saldoAwal, String pin) {
        super(nomorRekening, namaPemilik, saldoAwal, pin);
    }

    @Override
    public void tarik(double jumlah) {
        if (jumlah < MIN_TARIK) {
            System.out.println("Transaksi Gagal: Rekening Prioritas memiliki batas minimum penarikan Rp" + MIN_TARIK);
        } else if (getSaldo() >= jumlah) {
            kurangiSaldo(jumlah);
            System.out.println("Tarik Tunai Prioritas Berhasil: Rp" + jumlah + " (Bebas Biaya Admin)");
            mutasiInternal.catat("Tarik Prioritas Rp" + jumlah + " | Bebas Admin | Saldo Akhir: Rp" + getSaldo());
        } else {
            System.out.println("Transaksi Gagal: Saldo tidak mencukupi.");
        }
    }
}

// ENTITAS CUSTOMER SERVICE (Untuk simulasi Asosiasi)
class CustomerService {
    private String namaCS;

    public CustomerService(String namaCS) {
        this.namaCS = namaCS;
    }

    public void terimaKeluhan(Nasabah n, String pesan) {
        System.out.println("\n[Customer Service - " + this.namaCS + "]: Menerima laporan...");
        System.out.println("Dari Nasabah : " + n.getNama());
        System.out.println("Keluhan      : \"" + pesan + "\"");
        System.out.println("Respon       : Terima kasih atas laporannya, kami akan segera menginvestigasi hal ini.");
    }
}

// ENTITAS NASABAH
class Nasabah {
    private String nama;
    
    // AGREGASI: Menampung maksimal 3 rekening secara loose-coupling (ikatan lemah)
    private Rekening[] daftarRekening;
    private int jumlahRekening;

    public Nasabah(String nama) {
        this.nama = nama;
        this.daftarRekening = new Rekening[3];
        this.jumlahRekening = 0;
    }

    public String getNama() {
        return this.nama;
    }

    // Menambahkan rekening yang sudah jadi (eksistensi independen) ke profil (Agregasi)
    public void tambahRekening(Rekening rek) {
        if (jumlahRekening < 3) {
            daftarRekening[jumlahRekening] = rek;
            jumlahRekening++;
            System.out.println("=> Rekening " + rek.getNomorRekening() + " berhasil ditautkan ke profil nasabah " + this.nama);
        } else {
            System.out.println("=> Gagal: Profil nasabah sudah mencapai batas maksimal 3 rekening.");
        }
    }

    public void tampilkanRekening() {
        System.out.println("\n=== Portofolio Rekening Milik " + this.nama + " ===");
        if (jumlahRekening == 0) {
            System.out.println("Belum ada rekening yang ditautkan.");
        } else {
            for (int i = 0; i < jumlahRekening; i++) {
                System.out.println((i+1) + ". Norek: " + daftarRekening[i].getNomorRekening() + " | Saldo: Rp" + daftarRekening[i].getSaldo());
            }
        }
        System.out.println("=========================================");
    }

    public Rekening getRekening(int indeks) {
        if (indeks >= 0 && indeks < jumlahRekening) {
            return daftarRekening[indeks];
        }
        return null;
    }

    // ASOSIASI: Nasabah berinteraksi mandiri dengan CustomerService tanpa kepemilikan
    public void laporKeluhan(CustomerService cs, String pesan) {
        cs.terimaKeluhan(this, pesan);
    }
}

// KELAS UTAMA & ANTARMUKA PENGGUNA
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Objek utama dalam simulasi
        Nasabah nasabahAktif = null;
        CustomerService csNeoBank = new CustomerService("Mbak Dita");
        
        // Array Bank Data Pusat untuk mendemonstrasikan bahwa rekening adalah entitas mandiri (loose-coupling) 
        // terhadap profil nasabah, membuktikan teori Agregasi.
        Rekening[] bankDataPusat = new Rekening[10];
        int totalRekeningBank = 0;

        int pilihan = 0;
        
        System.out.println("==================================================");
        System.out.println("       SELAMAT DATANG DI SISTEM NEOBANK");
        System.out.println("==================================================");

        do {
            System.out.println("\n[ MENU UTAMA ]");
            System.out.println("1. Registrasi Profil Nasabah Baru");
            System.out.println("2. Buka & Tautkan Rekening Baru");
            System.out.println("3. Lihat Portofolio Rekening");
            System.out.println("4. Transaksi (Setor / Tarik Tunai)");
            System.out.println("5. Hubungi Customer Service (Uji Asosiasi)");
            System.out.println("6. Hancurkan Profil Akun (Uji Agregasi & Komposisi)");
            System.out.println("7. Keluar");
            System.out.print("Pilih menu (1-7): ");
            pilihan = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (pilihan) {
                case 1:
                    if (nasabahAktif == null) {
                        System.out.print("Masukkan nama lengkap Anda: ");
                        String nama = scanner.nextLine();
                        nasabahAktif = new Nasabah(nama);
                        System.out.println("=> Registrasi berhasil. Profil nasabah atas nama " + nama + " aktif.");
                    } else {
                        System.out.println("=> Error: Anda sedang login dengan profil " + nasabahAktif.getNama());
                    }
                    break;

                case 2:
                    if (nasabahAktif != null) {
                        System.out.println("\n-- OPSI JENIS REKENING --");
                        System.out.println("1. Rekening Reguler (Biaya Admin per penarikan: Rp2.500)");
                        System.out.println("2. Rekening Prioritas (Bebas Admin, Min. Tarik Rp500.000)");
                        System.out.print("Pilih tipe rekening: ");
                        int jenis = scanner.nextInt();
                        scanner.nextLine();
                        
                        System.out.print("Buat Nomor Rekening: ");
                        String norek = scanner.nextLine();
                        System.out.print("Buat PIN Keamanan (6 Digit): ");
                        String pin = scanner.nextLine();
                        System.out.print("Nominal Setoran Awal: Rp");
                        double setorAwal = scanner.nextDouble();
                        scanner.nextLine();

                        Rekening rekBaru = null;
                        if (jenis == 1) {
                            rekBaru = new RekeningReguler(norek, nasabahAktif.getNama(), setorAwal, pin);
                        } else if (jenis == 2) {
                            rekBaru = new RekeningPrioritas(norek, nasabahAktif.getNama(), setorAwal, pin);
                        }

                        if (rekBaru != null) {
                            // 1. Catat ke Bank Data Pusat sebagai entitas independen
                            bankDataPusat[totalRekeningBank++] = rekBaru;
                            // 2. Tautkan (Agregasi) ke profil Nasabah
                            nasabahAktif.tambahRekening(rekBaru);
                        }
                    } else {
                        System.out.println("=> Akses ditolak: Silakan registrasi profil nasabah terlebih dahulu.");
                    }
                    break;

                case 3:
                    if (nasabahAktif != null) {
                        nasabahAktif.tampilkanRekening();
                    } else {
                        System.out.println("=> Akses ditolak: Silakan registrasi profil nasabah terlebih dahulu.");
                    }
                    break;

                case 4:
                    if (nasabahAktif != null) {
                        nasabahAktif.tampilkanRekening();
                        System.out.print("Pilih nomor urut rekening untuk transaksi: ");
                        int idx = scanner.nextInt() - 1;
                        scanner.nextLine();
                        
                        Rekening rek = nasabahAktif.getRekening(idx);
                        if (rek != null) {
                            // Simulasi Proses Login (Otorisasi Interface)
                            System.out.print("Masukkan PIN rekening " + rek.getNomorRekening() + " Anda: ");
                            String inputPin = scanner.nextLine();
                            
                            if (rek.verifikasiPIN(inputPin)) {
                                System.out.println("=> Otorisasi Berhasil. Akses Terbuka.");
                                System.out.println("1. Setor Tunai");
                                System.out.println("2. Tarik Tunai");
                                System.out.print("Pilihan Transaksi: ");
                                int trxD = scanner.nextInt();
                                System.out.print("Masukkan Nominal: Rp");
                                double nom = scanner.nextDouble();
                                scanner.nextLine();
                                
                                if (trxD == 1) {
                                    rek.setor(nom);
                                } else if (trxD == 2) {
                                    // POLIMORFISME: Tarik tunai dieksekusi dengan aturan sesuai jenis kelas konkretnya
                                    rek.tarik(nom);
                                } else {
                                    System.out.println("Pilihan transaksi tidak tersedia.");
                                }
                            } else {
                                System.out.println("=> ERROR: PIN Salah! Akses ditolak.");
                            }
                        } else {
                            System.out.println("=> ERROR: Rekening tidak ditemukan pada profil Anda.");
                        }
                    } else {
                        System.out.println("=> Akses ditolak: Silakan registrasi profil nasabah terlebih dahulu.");
                    }
                    break;

                case 5:
                    if (nasabahAktif != null) {
                        System.out.print("Tulis keluhan atau laporan Anda: ");
                        String pesan = scanner.nextLine();
                        // ASOSIASI: Nasabah dan CS berinteraksi tanpa adanya unsur kepemilikan
                        nasabahAktif.laporKeluhan(csNeoBank, pesan);
                    } else {
                        System.out.println("=> Akses ditolak: Silakan registrasi profil nasabah terlebih dahulu.");
                    }
                    break;

                case 6:
                    if (nasabahAktif != null) {
                        System.out.println("\n[!]--- SKENARIO PENUTUPAN AKUN SECARA PAKSA ---[!]");
                        System.out.println("Menghancurkan Profil Nasabah " + nasabahAktif.getNama() + " dari memori lokal...");
                        
                        // Menghapus objek Nasabah (Memicu pembuktian teori PBO)
                        nasabahAktif = null;
                        System.out.println("=> SUCCESS: Profil nasabah telah dihancurkan secara permanen (null).");

                        System.out.println("\n[!] BUKTI ANALITIS TEORI PBO [!]");
                        System.out.println("1. PEMBUKTIAN AGREGASI (Nasabah & Rekening):");
                        System.out.println("   Walaupun entitas Nasabah telah dihancurkan tanpa sisa, data Rekening tidak ikut hilang.");
                        System.out.println("   Hal ini dikarenakan ikatan keduanya adalah Agregasi (Loose-Coupling).");
                        if (totalRekeningBank > 0 && bankDataPusat[0] != null) {
                            System.out.println("   => BUKTI: Rekening " + bankDataPusat[0].getNomorRekening() + " masih utuh dan tersimpan di Bank Data Pusat NeoBank.");
                        }

                        System.out.println("\n2. PEMBUKTIAN KOMPOSISI (Rekening & BukuMutasi):");
                        System.out.println("   Berbeda dengan agregasi, BukuMutasi diciptakan persis di dalam constructor Rekening (Strong-Coupling).");
                        System.out.println("   Sekarang, mari kita hancurkan entitas Rekening tersebut dari Bank Data Pusat...");
                        
                        if (totalRekeningBank > 0 && bankDataPusat[0] != null) {
                            String destroyedNorek = bankDataPusat[0].getNomorRekening();
                            bankDataPusat[0] = null; // Menghancurkan entitas Rekening
                            System.out.println("   => Rekening " + destroyedNorek + " telah dilebur/ditutup secara paksa.");
                            System.out.println("   => HASIL: Objek BukuMutasi yang terikat di dalamnya secara logis lenyap tanpa sisa dan terhapus oleh Garbage Collection Java.");
                        }

                        pilihan = 7; // Memaksa keluar program setelah skenario ini berakhir
                    } else {
                        System.out.println("=> Akses ditolak: Silakan registrasi profil nasabah terlebih dahulu.");
                    }
                    break;

                case 7:
                    System.out.println("\nTerima kasih telah mempercayakan transaksi Anda pada NeoBank. Sistem Dimatikan.");
                    break;
                    
                default:
                    System.out.println("=> Pilihan menu tidak valid.");
            }
        } while (pilihan != 7);
        
        scanner.close();
    }
}

// =========================================================================
// KOMEN PENGGANTI LAPORAN PRAKTIKUM
// =========================================================================
// 1. ABSTRAKSI:
//    Diwujudkan melalui Interface `Otorisasi` untuk standardisasi keamanan, 
//    dan dipadukan dengan Abstract Class `Rekening` sebagai blueprint utama 
//    rekening bank agar tidak bisa diinstansiasi secara langsung.
// 
// 2. ENKAPSULASI:
//    Properti super krusial seperti `saldo` dan `pin` diatur menjadi private. 
//    Mereka tidak dapat dirusak dari luar, karena saldo hanya bertambah lewat 
//    `setor()` atau berkurang lewat validasi internal algoritma `tarik()`.
// 
// 3. INHERITANCE (Pewarisan) & POLIMORFISME:
//    Kelas turunan konkrit `RekeningReguler` dan `RekeningPrioritas` mewarisi 
//    `Rekening`. Polimorfisme bekerja dinamis melalui metode Overriding 
//    `tarik()`, di mana satu metode yang sama diakses dari menu namun memicu
//    aturan yang jauh berbeda (ada tidaknya potongan admin & batas minimum penarikan).
// 
// 4. KOMPOSISI (Strong-Coupling):
//    `Rekening` memiliki hubungan komposisi dengan `BukuMutasi`. 
//    BukuMutasi DIBUAT (new BukuMutasi()) SECARA INTERNAL DI DALAM CONSTRUCTOR 
//    Rekening. Karena tidak ada referensi eksternalnya, ketika Rekening 
//    dimusnahkan/ditutup, BukuMutasinya otomatis hancur karena siklus 
//    hidupnya bergantung sepenuhnya pada eksistensi Rekening.
// 
// 5. AGREGASI (Loose-Coupling):
//    `Nasabah` hanya menampung objek `Rekening` yang sudah eksis di memori 
//    (bank data pusat) ke dalam array-nya. Oleh karena itu, saat profil 
//    nasabah tersebut dihapus (null), rekening-rekeningnya tetap hidup karena 
//    bisa dilepas pasang dan eksis secara mandiri.
// 
// 6. ASOSIASI UMUM:
//    Terlihat dari metode `laporKeluhan(CustomerService cs)` pada Nasabah. 
//    Kedua objek sama-sama independen dan utuh, dan hanya berinteraksi 
//    saat method dijalankan untuk saling lempar data tanpa ada rasa kepemilikan.
// =========================================================================
