# Seminar Registration App - UTS Pemrograman Mobile I

**Nama:** Hilvan Yadhisnata  
**NIM:** 24552011163  
**Kelas:** TIF K 24A  
**Dosen:** Erryck Norrys, S.Kom.  

---

## 📱 Alur Aplikasi

```
LoginActivity → HomeActivity (3 Tab) → FormActivity → ResultActivity
                     ↕
              RegisterActivity → SuccessActivity
```

## 🎯 Fitur yang Diimplementasikan

### 1. Halaman Login (`LoginActivity`)
- Form email + password dengan TextInputLayout Material
- Real-time validation (TextWatcher)
- Hardcoded credentials:
  - `user@email.com` / `123456`
  - `hilvan@email.com` / `hilvan123`
  - `admin@utb.ac.id` / `admin2025`
- Animasi fade-in staggered
- Navigasi ke RegisterActivity via tombol Sign Up

### 2. Halaman Register (`RegisterActivity`) ← dari project sebelumnya
- Form lengkap: Nama, Email, Password, Confirm Password
- RadioGroup gender, CheckBox hobi, Spinner kota
- Real-time validation, AlertDialog konfirmasi

### 3. Dashboard/Beranda (`HomeActivity`)
- **Tab 1: Beranda** — Hero card, statistik, jadwal seminar, tombol Daftar Seminar
- **Tab 2: Seminar** — Daftar 3 seminar dengan CardView, CTA daftar
- **Tab 3: Profil** — Info user, data universitas, tombol Logout
- Menerima nama user dari LoginActivity via Intent

### 4. Form Pendaftaran (`FormActivity`) ← **[BARU - UTS]**
- Input: Nama, Email, Nomor HP, Jenis Kelamin, Pilihan Seminar, Checkbox Persetujuan
- **Real-time validation** dengan TextWatcher:
  - Nama: tidak boleh kosong
  - Email: harus mengandung `@`
  - Nomor HP: hanya angka, 10-13 digit, diawali `08`
- Spinner dengan 8 pilihan seminar (hardcode)
- AlertDialog konfirmasi sebelum submit

### 5. Halaman Hasil (`ResultActivity`) ← **[BARU - UTS]**
- Pesan "Pendaftaran Berhasil" dengan animasi
- CardView summary: Nama, Email, No HP, Gender, Seminar
- Tombol kembali ke HomeActivity

---

## 🎨 Design System

| Elemen | Nilai |
|--------|-------|
| Primary | `#5C6BC0` (Indigo) |
| Secondary | `#FFB300` (Amber) |
| Success | `#00897B` (Teal) |
| Error | `#E53935` (Red) |
| Background | `#F5F6FA` |
| Font | Poppins (Regular, SemiBold, Bold) |
| Corner Radius | 12dp – 24dp |
| Card Elevation | 4dp – 12dp |

---

## 📂 Struktur File Baru (UTS)

```
app/src/main/
├── java/com/example/registerapp/
│   ├── LoginActivity.kt        ← Halaman Login
│   ├── RegisterActivity.kt     ← Halaman Register (dari tugas sebelumnya)
│   ├── SuccessActivity.kt      ← Sukses Register
│   ├── HomeActivity.kt         ← Dashboard + 3 Fragment Tab (BARU)
│   ├── FormActivity.kt         ← Form Pendaftaran Seminar (BARU)
│   └── ResultActivity.kt       ← Hasil Pendaftaran (BARU)
└── res/
    ├── layout/
    │   ├── activity_login.xml
    │   ├── activity_home.xml       ← TabLayout + ViewPager2 (BARU)
    │   ├── fragment_beranda.xml    ← Tab Beranda (BARU)
    │   ├── fragment_seminar_list.xml ← Tab Seminar (BARU)
    │   ├── fragment_profil.xml     ← Tab Profil (BARU)
    │   ├── activity_form.xml       ← Form Seminar (BARU)
    │   └── activity_result.xml     ← Hasil (BARU)
    ├── drawable/  ← semua icon dan shape
    ├── font/      ← Poppins (perlu download dari Google Fonts)
    └── values/
        ├── colors.xml
        └── themes.xml
```

---

## ⚙️ Setup Font Poppins

Download dari [Google Fonts - Poppins](https://fonts.google.com/specimen/Poppins) dan letakkan di `app/src/main/res/font/`:
- `poppins_regular.ttf`
- `poppins_medium.ttf`
- `poppins_semibold.ttf`
- `poppins_bold.ttf`

---

## 🎬 Video Penjelasan

> **[TAMBAHKAN LINK VIDEO DI SINI]**  
> Link Google Drive / YouTube: `https://...`

Video mencakup:
1. Penjelasan Halaman Login
2. Penjelasan Halaman Register
3. Penjelasan Dashboard (3 Tab)
4. Penjelasan Form Pendaftaran
5. Penjelasan Validasi Real-time
6. Penjelasan Dialog Konfirmasi
7. Penjelasan Halaman Hasil
8. Penjelasan Kode
