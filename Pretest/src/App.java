// Penjelasan 3 Relasi Objek:
// 1. Asosiasi (Association): Hubungan struktural atau semantik secara umum antara dua class atau lebih. 
//    Keduanya saling berinteraksi, namun memiliki siklus hidup (lifecycle) masing-masing.
//    Agregasi dan Komposisi pada dasarnya adalah bentuk khusus (lebih spesifik) dari Asosiasi.
//
// 2. Agregasi (Aggregation): Hubungan "has-a" yang lemah (weak "has-a"). Objek bagian (part) 
//    dapat berdiri sendiri meskipun objek utamanya (whole) dihancurkan.
//    Contoh: KomputerServer dengan Monitor. Monitor dihubungkan ke server, namun jika server
//    dihancurkan, Monitor tersebut tetap eksis dan bisa digunakan di tempat lain.
//
// 3. Komposisi (Composition): Hubungan "has-a" yang kuat (strong "has-a"). Objek bagian (part) 
//    sangat bergantung secara eksistensial pada objek utama (whole). 
//    Contoh: KomputerServer dengan Harddisk yang terpasang permanen di dalamnya. 
//    Jika server dihancurkan, maka Harddisk di dalamnya ikut musnah bersama server tersebut.

class Harddisk {
    String kapasitas;
    
    public Harddisk(String kapasitas) {
        this.kapasitas = kapasitas;
    }
}

class Monitor {
    String merk;
    
    public Monitor(String merk) {
        this.merk = merk;
    }
}

class KomputerServer {
    // Komposisi: Harddisk terikat erat dengan siklus hidup KomputerServer
    private Harddisk harddiskInternal;
    
    // Agregasi: Sekumpulan Monitor yang bisa lepas-pasang (menggunakan Array)
    private Monitor[] daftarMonitor;

    // Constructor KomputerServer
    public KomputerServer(String kapasitasHarddisk, Monitor[] daftarMonitor) {
        // Implementasi Komposisi: 
        // Harddisk diciptakan tepat saat KomputerServer diciptakan (di dalam constructor). 
        // Jika instance KomputerServer ini dihancurkan/dihapus dari memori, 
        // maka instance Harddisk ini otomatis ikut hilang.
        this.harddiskInternal = new Harddisk(kapasitasHarddisk);
        
        // Implementasi Agregasi:
        // Monitor sudah dibuat secara mandiri di luar class ini (passed by reference).
        // Jika instance KomputerServer ini dihancurkan, objek Monitor di luar masih tetap aman.
        this.daftarMonitor = daftarMonitor;
    }
}

public class App {
    public static void main(String[] args) {
        // Membuat sekumpulan Monitor secara mandiri (eksistensi independen)
        Monitor[] monitors = new Monitor[2];
        monitors[0] = new Monitor("Asus 24 Inch");
        monitors[1] = new Monitor("Dell 27 Inch");
        
        // Membuat KomputerServer
        // - "2TB" akan diinstansiasi menjadi Harddisk di dalam server (Komposisi)
        // - Array 'monitors' dipasangkan dari luar ke server (Agregasi)
        KomputerServer server = new KomputerServer("2TB", monitors);
        
        System.out.println("Komputer Server berhasil dirakit dengan Komposisi (Harddisk) dan Agregasi (Monitor).");
    }
}
