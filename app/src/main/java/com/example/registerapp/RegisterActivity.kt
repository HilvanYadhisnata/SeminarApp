package com.example.registerapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * RegisterActivity menangani seluruh alur registrasi pengguna.
 *
 * Fitur yang diimplementasikan:
 * - [01] Complete Form: TextInputLayout dengan Nama, Email, Password, Confirm Password
 * - [02] Advanced Validation: Real-time validation, format email, password match
 * - [03] Selection Controls: RadioGroup gender + Checkbox hobi (min 3)
 * - [04] Spinner & Dialog: Spinner kota + AlertDialog konfirmasi submit
 * - [05] Gesture Interaction: Long Press pada tombol untuk aksi tambahan
 * - [06] GitHub: Struktur siap upload ke GitHub/GitLab
 *
 * @author Student
 * @version 1.0
 */
class RegisterActivity : AppCompatActivity() {

    // ─── Views: Text Inputs ───────────────────────────────────────────────────
    private lateinit var tilNama: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tilConfirmPassword: TextInputLayout

    private lateinit var etNama: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText

    // ─── Views: Selection Controls ────────────────────────────────────────────
    private lateinit var rgGender: RadioGroup
    private lateinit var rbLakiLaki: RadioButton
    private lateinit var rbPerempuan: RadioButton

    private lateinit var cbOlahraga: MaterialCheckBox
    private lateinit var cbMusik: MaterialCheckBox
    private lateinit var cbMembaca: MaterialCheckBox
    private lateinit var cbMemasak: MaterialCheckBox
    private lateinit var cbTraveling: MaterialCheckBox
    private lateinit var cbGaming: MaterialCheckBox

    // ─── Views: Spinner ───────────────────────────────────────────────────────
    private lateinit var spinnerKota: Spinner
    private lateinit var tvSpinnerLabel: TextView

    // ─── Views: Buttons ───────────────────────────────────────────────────────
    private lateinit var btnDaftar: MaterialButton
    private lateinit var btnReset: MaterialButton
    private lateinit var tvLoginLink: TextView

    // ─── Data: Spinner ────────────────────────────────────────────────────────
    /** Daftar kota untuk Spinner */
    private val listKota = listOf(
        "-- Pilih Kota --",
        "Jakarta",
        "Bandung",
        "Surabaya",
        "Medan",
        "Semarang",
        "Makassar",
        "Palembang",
        "Tangerang",
        "Depok",
        "Bekasi"
    )

    /** Index kota yang dipilih (0 = belum pilih) */
    private var selectedKotaIndex = 0

    // ─── State ────────────────────────────────────────────────────────────────
    /** Apakah form pernah di-submit (untuk menampilkan error) */
    private var isSubmitAttempted = false

    // ═════════════════════════════════════════════════════════════════════════
    // LIFECYCLE
    // ═════════════════════════════════════════════════════════════════════════

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_register)

        initViews()
        setupSpinner()
        setupRealTimeValidation()
        setupButtonListeners()
    }

    // ═════════════════════════════════════════════════════════════════════════
    // INIT
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Menginisialisasi semua view dari layout.
     */
    private fun initViews() {
        // TextInputLayout
        tilNama = findViewById(R.id.tilNama)
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword)

        // EditText
        etNama = findViewById(R.id.etNama)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)

        // RadioGroup & RadioButton
        rgGender = findViewById(R.id.rgGender)
        rbLakiLaki = findViewById(R.id.rbLakiLaki)
        rbPerempuan = findViewById(R.id.rbPerempuan)

        // Checkboxes hobi
        cbOlahraga = findViewById(R.id.cbOlahraga)
        cbMusik = findViewById(R.id.cbMusik)
        cbMembaca = findViewById(R.id.cbMembaca)
        cbMemasak = findViewById(R.id.cbMemasak)
        cbTraveling = findViewById(R.id.cbTraveling)
        cbGaming = findViewById(R.id.cbGaming)

        // Spinner
        spinnerKota = findViewById(R.id.spinnerKota)
        tvSpinnerLabel = findViewById(R.id.tvSpinnerLabel)

        // Buttons
        btnDaftar = findViewById(R.id.btnDaftar)
        btnReset = findViewById(R.id.btnReset)
        tvLoginLink = findViewById(R.id.tvLoginLink)
    }

    // ═════════════════════════════════════════════════════════════════════════
    // SETUP: SPINNER
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Menyiapkan Spinner kota dengan data custom dan listener perubahan.
     */
    private fun setupSpinner() {
        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            listKota
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerKota.adapter = adapter

        spinnerKota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedKotaIndex = position
                // Validasi ulang setelah pilih kota (jika sudah pernah submit)
                if (isSubmitAttempted) {
                    validateKota()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedKotaIndex = 0
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // SETUP: REAL-TIME VALIDATION
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Memasang TextWatcher pada setiap field untuk validasi real-time.
     * Error akan muncul saat pengguna mengetik (setelah submit pertama kali).
     */
    private fun setupRealTimeValidation() {
        etNama.addTextChangedListener(createWatcher { validateNama() })
        etEmail.addTextChangedListener(createWatcher { validateEmail() })
        etPassword.addTextChangedListener(createWatcher {
            validatePassword()
            // Validasi ulang confirm password saat password berubah
            if (etConfirmPassword.text?.isNotEmpty() == true) validateConfirmPassword()
        })
        etConfirmPassword.addTextChangedListener(createWatcher { validateConfirmPassword() })
    }

    /**
     * Helper untuk membuat TextWatcher dengan callback sederhana.
     * @param afterChanged Fungsi yang dipanggil setelah teks berubah
     */
    private fun createWatcher(afterChanged: () -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isSubmitAttempted) afterChanged()
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // SETUP: BUTTON LISTENERS
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Menyiapkan semua listener untuk tombol dan interaksi gesture.
     */
    private fun setupButtonListeners() {

        // ── Tombol Daftar: Click biasa ─────────────────────────────────────
        btnDaftar.setOnClickListener {
            isSubmitAttempted = true
            if (validateAllFields()) {
                showConfirmationDialog()
            }
        }

        // ── Tombol Daftar: Long Press (Gesture Interaction) ────────────────
        // [05] Gesture Interaction: Long Press untuk preview data sebelum submit
        btnDaftar.setOnLongClickListener { view ->
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            showPreviewDialog()
            true // Menandakan event sudah ditangani
        }

        // ── Tombol Reset: Click ────────────────────────────────────────────
        btnReset.setOnClickListener {
            showResetConfirmationDialog()
        }

        // ── Link Login ─────────────────────────────────────────────────────
        tvLoginLink.setOnClickListener {
            val intent = android.content.Intent(this, LoginActivity::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // VALIDATION
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Menjalankan seluruh validasi form.
     * @return true jika semua field valid, false jika ada yang tidak valid
     */
    private fun validateAllFields(): Boolean {
        val namaOk = validateNama()
        val emailOk = validateEmail()
        val passwordOk = validatePassword()
        val confirmOk = validateConfirmPassword()
        val genderOk = validateGender()
        val hobiOk = validateHobi()
        val kotaOk = validateKota()

        return namaOk && emailOk && passwordOk && confirmOk && genderOk && hobiOk && kotaOk
    }

    /**
     * Validasi field Nama: tidak boleh kosong, minimal 3 karakter.
     */
    private fun validateNama(): Boolean {
        val nama = etNama.text.toString().trim()
        return when {
            nama.isEmpty() -> {
                tilNama.error = "Nama tidak boleh kosong"
                false
            }
            nama.length < 3 -> {
                tilNama.error = "Nama minimal 3 karakter"
                false
            }
            else -> {
                tilNama.error = null
                tilNama.isErrorEnabled = false
                true
            }
        }
    }

    /**
     * Validasi field Email: tidak kosong + format email valid.
     */
    private fun validateEmail(): Boolean {
        val email = etEmail.text.toString().trim()
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS
        return when {
            email.isEmpty() -> {
                tilEmail.error = "Email tidak boleh kosong"
                false
            }
            !emailPattern.matcher(email).matches() -> {
                tilEmail.error = "Format email tidak valid"
                false
            }
            else -> {
                tilEmail.error = null
                tilEmail.isErrorEnabled = false
                true
            }
        }
    }

    /**
     * Validasi field Password: tidak kosong, minimal 8 karakter,
     * harus mengandung huruf dan angka.
     */
    private fun validatePassword(): Boolean {
        val password = etPassword.text.toString()
        return when {
            password.isEmpty() -> {
                tilPassword.error = "Password tidak boleh kosong"
                false
            }
            password.length < 8 -> {
                tilPassword.error = "Password minimal 8 karakter"
                false
            }
            !password.any { it.isLetter() } -> {
                tilPassword.error = "Password harus mengandung huruf"
                false
            }
            !password.any { it.isDigit() } -> {
                tilPassword.error = "Password harus mengandung angka"
                false
            }
            else -> {
                tilPassword.error = null
                tilPassword.isErrorEnabled = false
                true
            }
        }
    }

    /**
     * Validasi Confirm Password: harus sama dengan password.
     */
    private fun validateConfirmPassword(): Boolean {
        val password = etPassword.text.toString()
        val confirm = etConfirmPassword.text.toString()
        return when {
            confirm.isEmpty() -> {
                tilConfirmPassword.error = "Konfirmasi password tidak boleh kosong"
                false
            }
            confirm != password -> {
                tilConfirmPassword.error = "Password tidak cocok"
                false
            }
            else -> {
                tilConfirmPassword.error = null
                tilConfirmPassword.isErrorEnabled = false
                true
            }
        }
    }

    /**
     * Validasi Gender: harus memilih salah satu.
     */
    private fun validateGender(): Boolean {
        return if (rgGender.checkedRadioButtonId == -1) {
            Toast.makeText(this, "⚠ Pilih jenis kelamin terlebih dahulu", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    /**
     * Validasi Hobi: minimal 3 checkbox harus dicentang.
     */
    private fun validateHobi(): Boolean {
        val selectedHobi = getSelectedHobi()
        return if (selectedHobi.size < 3) {
            Toast.makeText(this, "⚠ Pilih minimal 3 hobi", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    /**
     * Validasi Kota: harus memilih kota (bukan placeholder).
     */
    private fun validateKota(): Boolean {
        return if (selectedKotaIndex == 0) {
            Toast.makeText(this, "⚠ Pilih kota asal terlebih dahulu", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Mengambil daftar hobi yang dipilih pengguna.
     * @return List nama hobi yang dicentang
     */
    private fun getSelectedHobi(): List<String> {
        val hobi = mutableListOf<String>()
        if (cbOlahraga.isChecked) hobi.add("Olahraga")
        if (cbMusik.isChecked) hobi.add("Musik")
        if (cbMembaca.isChecked) hobi.add("Membaca")
        if (cbMemasak.isChecked) hobi.add("Memasak")
        if (cbTraveling.isChecked) hobi.add("Traveling")
        if (cbGaming.isChecked) hobi.add("Gaming")
        return hobi
    }

    /**
     * Mengambil gender yang dipilih dari RadioGroup.
     * @return String gender atau "Belum dipilih"
     */
    private fun getSelectedGender(): String {
        return when (rgGender.checkedRadioButtonId) {
            R.id.rbLakiLaki -> "Laki-laki"
            R.id.rbPerempuan -> "Perempuan"
            else -> "Belum dipilih"
        }
    }

    /**
     * Mereset seluruh form ke kondisi awal.
     */
    private fun resetForm() {
        // Reset EditText
        etNama.text?.clear()
        etEmail.text?.clear()
        etPassword.text?.clear()
        etConfirmPassword.text?.clear()

        // Reset error
        tilNama.error = null
        tilEmail.error = null
        tilPassword.error = null
        tilConfirmPassword.error = null

        // Reset RadioGroup
        rgGender.clearCheck()

        // Reset Checkboxes
        cbOlahraga.isChecked = false
        cbMusik.isChecked = false
        cbMembaca.isChecked = false
        cbMemasak.isChecked = false
        cbTraveling.isChecked = false
        cbGaming.isChecked = false

        // Reset Spinner
        spinnerKota.setSelection(0)
        selectedKotaIndex = 0

        // Reset state
        isSubmitAttempted = false

        Toast.makeText(this, "✓ Form berhasil direset", Toast.LENGTH_SHORT).show()
    }

    // ═════════════════════════════════════════════════════════════════════════
    // DIALOGS
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * [04] Menampilkan AlertDialog konfirmasi sebelum submit data.
     * Menampilkan ringkasan data yang akan didaftarkan.
     */
    private fun showConfirmationDialog() {
        val nama = etNama.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val gender = getSelectedGender()
        val hobi = getSelectedHobi().joinToString(", ")
        val kota = listKota[selectedKotaIndex]

        val message = """
            Konfirmasi Data Registrasi:
            
            👤  Nama     : $nama
            📧  Email    : $email
            ⚧   Gender   : $gender
            🏙️  Kota     : $kota
            🎯  Hobi     : $hobi
            
            Apakah data sudah benar?
        """.trimIndent()

        AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setTitle("📋 Konfirmasi Registrasi")
            .setMessage(message)
            .setPositiveButton("✓ Daftar Sekarang") { _, _ ->
                navigateToSuccess()
            }
            .setNegativeButton("✗ Periksa Kembali") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    /**
     * [05] Menampilkan preview data dari Long Press gesture.
     * Memperlihatkan data yang sudah diisi tanpa submit.
     */
    private fun showPreviewDialog() {
        val nama = etNama.text.toString().trim().ifEmpty { "(belum diisi)" }
        val email = etEmail.text.toString().trim().ifEmpty { "(belum diisi)" }
        val gender = getSelectedGender()
        val hobi = getSelectedHobi().joinToString(", ").ifEmpty { "(belum dipilih)" }
        val kota = if (selectedKotaIndex > 0) listKota[selectedKotaIndex] else "(belum dipilih)"

        val message = """
            Preview data yang sedang diisi:
            
            👤  Nama     : $nama
            📧  Email    : $email
            ⚧   Gender   : $gender
            🏙️  Kota     : $kota
            🎯  Hobi     : $hobi
            
            💡 Tip: Tekan tombol sekali untuk mendaftar
        """.trimIndent()

        AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setTitle("👁 Preview Data")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    /**
     * Menampilkan dialog konfirmasi sebelum mereset form.
     */
    private fun showResetConfirmationDialog() {
        AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setTitle("⚠ Reset Form")
            .setMessage("Semua data yang sudah diisi akan dihapus.\nYakin ingin mereset form?")
            .setPositiveButton("Ya, Reset") { _, _ -> resetForm() }
            .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    // ═════════════════════════════════════════════════════════════════════════
    // NAVIGATION
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Navigasi ke SuccessActivity setelah registrasi berhasil.
     * Membawa data pengguna sebagai extras.
     */
    private fun navigateToSuccess() {
        val intent = Intent(this, SuccessActivity::class.java).apply {
            putExtra(SuccessActivity.EXTRA_NAMA, etNama.text.toString().trim())
            putExtra(SuccessActivity.EXTRA_EMAIL, etEmail.text.toString().trim())
            putExtra(SuccessActivity.EXTRA_GENDER, getSelectedGender())
            putExtra(SuccessActivity.EXTRA_KOTA, listKota[selectedKotaIndex])
            putExtra(SuccessActivity.EXTRA_HOBI, getSelectedHobi().joinToString(", "))
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
