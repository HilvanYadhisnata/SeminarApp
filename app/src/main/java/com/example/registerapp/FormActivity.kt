package com.example.registerapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * FormActivity - Form Pendaftaran Seminar
 *
 * Fitur:
 * - [01] Form input: Nama, Email, No HP, Jenis Kelamin, Seminar, CheckBox
 * - [02] Real-time validation dengan TextWatcher pada setiap field
 * - [03] Validasi Nomor HP: angka saja, 10-13 digit, awali "08"
 * - [04] Validasi Email: harus mengandung "@" dan "."
 * - [05] AlertDialog konfirmasi sebelum submit (ringkasan data)
 * - [06] Navigasi ke ResultActivity membawa data via Intent
 */
class FormActivity : AppCompatActivity() {

    // ─── Views: Text Inputs ───────────────────────────────────────────────────
    private lateinit var tilNama: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPhone: TextInputLayout
    private lateinit var etNama: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPhone: TextInputEditText

    // ─── Views: Selection ────────────────────────────────────────────────────
    private lateinit var rgGender: RadioGroup
    private lateinit var rbLaki: RadioButton
    private lateinit var rbPerempuan: RadioButton
    private lateinit var spinnerSeminar: Spinner
    private lateinit var cbSetuju: MaterialCheckBox

    // ─── Views: Lainnya ──────────────────────────────────────────────────────
    private lateinit var btnSubmit: MaterialButton
    private lateinit var btnBack: View
    private lateinit var rootView: View

    // ─── Data ─────────────────────────────────────────────────────────────────
    /**
     * Daftar seminar yang tersedia - hardcode sesuai ketentuan UTS (minimal 5)
     */
    private val listSeminar = listOf(
        "— Pilih Seminar —",
        "Artificial Intelligence & Machine Learning",
        "Cybersecurity & Ethical Hacking",
        "Mobile App Development with Kotlin",
        "Cloud Computing & DevOps",
        "UI/UX Design Thinking",
        "Data Science & Big Data Analytics",
        "Blockchain & Web3 Technology",
        "Internet of Things (IoT) Engineering"
    )

    // Data user dari HomeActivity (opsional, untuk pre-fill)
    private var userName: String  = ""
    private var userEmail: String = ""

    // ─────────────────────────────────────────────────────────────────────────

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_form)

        // Ambil data user yang login (untuk pre-fill form)
        userName  = intent.getStringExtra("USER_NAME")  ?: ""
        userEmail = intent.getStringExtra("USER_EMAIL") ?: ""

        initViews()
        setupSpinner()
        prefillUserData()
        setupRealTimeValidation()   // [02]
        setupClickListeners()
    }

    private fun initViews() {
        rootView       = findViewById(R.id.root_form)
        tilNama        = findViewById(R.id.til_nama)
        tilEmail       = findViewById(R.id.til_email_form)
        tilPhone       = findViewById(R.id.til_phone)
        etNama         = findViewById(R.id.et_nama)
        etEmail        = findViewById(R.id.et_email_form)
        etPhone        = findViewById(R.id.et_phone)
        rgGender       = findViewById(R.id.rg_gender)
        rbLaki         = findViewById(R.id.rb_laki)
        rbPerempuan    = findViewById(R.id.rb_perempuan)
        spinnerSeminar = findViewById(R.id.spinner_seminar)
        cbSetuju       = findViewById(R.id.cb_setuju)
        btnSubmit      = findViewById(R.id.btn_submit)
        btnBack        = findViewById(R.id.btn_back)
    }

    /**
     * Pre-fill nama & email jika user sudah login (data dari HomeActivity)
     */
    private fun prefillUserData() {
        if (userName.isNotEmpty())  etNama.setText(userName)
        if (userEmail.isNotEmpty()) etEmail.setText(userEmail)
    }

    /**
     * Setup Spinner dengan data seminar hardcode
     */
    private fun setupSpinner() {
        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            listSeminar
        ).also { it.setDropDownViewResource(R.layout.spinner_dropdown_item) }

        spinnerSeminar.adapter = adapter
    }

    // =========================================================================
    // [02] REAL-TIME VALIDATION - Setiap field punya TextWatcher sendiri
    // =========================================================================

    private fun setupRealTimeValidation() {

        // ── Validasi Nama ──────────────────────────────────────────────────
        etNama.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val nama = s.toString().trim()
                tilNama.error = when {
                    nama.isEmpty()  -> "Nama tidak boleh kosong"
                    nama.length < 3 -> "Nama minimal 3 karakter"
                    else            -> null
                }
            }
        })

        // ── [04] Validasi Email - harus mengandung "@" ─────────────────────
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString().trim()
                tilEmail.error = when {
                    email.isEmpty()       -> "Email tidak boleh kosong"
                    !email.contains("@") -> "Email harus mengandung karakter '@'"
                    !email.contains(".") -> "Email tidak valid"
                    else                 -> null
                }
            }
        })

        // ── [03] Validasi Nomor HP ─────────────────────────────────────────
        // Aturan: hanya angka | panjang 10-13 digit | awali "08"
        etPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val phone = s.toString().trim()
                tilPhone.error = when {
                    phone.isEmpty()               -> "Nomor HP tidak boleh kosong"
                    !phone.all { it.isDigit() }  -> "Nomor HP hanya boleh berisi angka"
                    !phone.startsWith("08")       -> "Nomor HP harus diawali dengan '08'"
                    phone.length < 10             -> "Nomor HP minimal 10 digit"
                    phone.length > 13             -> "Nomor HP maksimal 13 digit"
                    else                          -> null
                }
            }
        })
    }

    private fun setupClickListeners() {
        // Tombol back di header
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Tombol submit
        btnSubmit.setOnClickListener {
            if (validateAllFields()) {
                showConfirmationDialog() // [05]
            }
        }
    }

    // =========================================================================
    // VALIDASI FINAL saat tombol Submit ditekan
    // =========================================================================

    /**
     * Validasi lengkap semua field sebelum dialog konfirmasi ditampilkan.
     * @return true jika semua input valid
     */
    private fun validateAllFields(): Boolean {
        var isValid = true

        // ── Validasi Nama ──────────────────────────────────────────────────
        val nama = etNama.text.toString().trim()
        when {
            nama.isEmpty()  -> { tilNama.error = "Nama tidak boleh kosong"; isValid = false }
            nama.length < 3 -> { tilNama.error = "Nama minimal 3 karakter"; isValid = false }
        }

        // ── Validasi Email ─────────────────────────────────────────────────
        val email = etEmail.text.toString().trim()
        when {
            email.isEmpty()       -> { tilEmail.error = "Email tidak boleh kosong"; isValid = false }
            !email.contains("@") -> { tilEmail.error = "Email harus mengandung '@'"; isValid = false }
            !email.contains(".") -> { tilEmail.error = "Email tidak valid"; isValid = false }
        }

        // ── Validasi Nomor HP ──────────────────────────────────────────────
        val phone = etPhone.text.toString().trim()
        when {
            phone.isEmpty()              -> { tilPhone.error = "Nomor HP tidak boleh kosong"; isValid = false }
            !phone.all { it.isDigit() } -> { tilPhone.error = "Nomor HP hanya boleh angka"; isValid = false }
            !phone.startsWith("08")     -> { tilPhone.error = "Nomor HP harus diawali '08'"; isValid = false }
            phone.length < 10           -> { tilPhone.error = "Nomor HP minimal 10 digit"; isValid = false }
            phone.length > 13           -> { tilPhone.error = "Nomor HP maksimal 13 digit"; isValid = false }
        }

        // ── Validasi Jenis Kelamin ─────────────────────────────────────────
        if (rgGender.checkedRadioButtonId == -1) {
            showSnackbar("⚠️  Pilih jenis kelamin terlebih dahulu")
            isValid = false
        }

        // ── Validasi Spinner Seminar ───────────────────────────────────────
        if (spinnerSeminar.selectedItemPosition == 0) {
            showSnackbar("⚠️  Pilih seminar yang ingin Anda ikuti")
            isValid = false
        }

        // ── [Validasi CheckBox Persetujuan] ────────────────────────────────
        // Jika checkbox belum dicentang: tampilkan pesan peringatan
        if (!cbSetuju.isChecked) {
            Snackbar.make(
                rootView,
                "⚠️  Centang persetujuan data sebelum melanjutkan",
                Snackbar.LENGTH_LONG
            ).setAction("OK") { cbSetuju.requestFocus() }
                .setBackgroundTint(getColor(R.color.error))
                .setTextColor(getColor(R.color.white))
                .setActionTextColor(getColor(R.color.white))
                .show()
            isValid = false
        }

        return isValid
    }

    // =========================================================================
    // [05] DIALOG KONFIRMASI
    // =========================================================================

    /**
     * Tampilkan AlertDialog Material dengan ringkasan data.
     * Tombol "Ya" → navigasi ke ResultActivity
     * Tombol "Tidak" → tutup dialog, tetap di form
     */
    private fun showConfirmationDialog() {
        val jk             = if (rbLaki.isChecked) "Laki-laki" else "Perempuan"
        val seminarDipilih = spinnerSeminar.selectedItem.toString()

        val message = buildString {
            append("Pastikan data berikut sudah benar:\n\n")
            append("👤 Nama     :  ${etNama.text.toString().trim()}\n")
            append("📧 Email    :  ${etEmail.text.toString().trim()}\n")
            append("📱 No. HP   :  ${etPhone.text.toString().trim()}\n")
            append("🚻 Kelamin  :  $jk\n")
            append("🎓 Seminar  :  $seminarDipilih\n\n")
            append("Lanjutkan pendaftaran?")
        }

        AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setTitle("Konfirmasi Pendaftaran")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Ya, Daftar!") { _, _ ->
                navigateToResult() // [06]
            }
            .setNegativeButton("Tidak, Kembali") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // =========================================================================
    // [06] NAVIGASI KE RESULT
    // =========================================================================

    /**
     * Kirim semua data form ke ResultActivity via Intent extras
     */
    private fun navigateToResult() {
        val jk = if (rbLaki.isChecked) "Laki-laki" else "Perempuan"

        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("NAMA",    etNama.text.toString().trim())
            putExtra("EMAIL",   etEmail.text.toString().trim())
            putExtra("PHONE",   etPhone.text.toString().trim())
            putExtra("GENDER",  jk)
            putExtra("SEMINAR", spinnerSeminar.selectedItem.toString())
        }
        startActivity(intent)
    }

    // ─── Helper ───────────────────────────────────────────────────────────────

    private fun showSnackbar(message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(getColor(R.color.primary_dark))
            .setTextColor(getColor(R.color.white))
            .show()
    }
}
