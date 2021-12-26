package com.htcmmss.englishproje

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.htcmmss.englishproje.databinding.ActivityPsifirlaBinding

class PSifirlaActivity : AppCompatActivity() {
    lateinit var binding: ActivityPsifirlaBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPsifirlaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.psifirlaButton.setOnClickListener {
            var psifirlaemail = binding.psifirlaEmail.text.toString().trim()
            if (TextUtils.isEmpty(psifirlaemail)){
                binding.psifirlaEmail.error = "Lütfen E-mail adresinizi giriniz"
            }
            else{
                auth.sendPasswordResetEmail(psifirlaemail)
                    .addOnCompleteListener(this){ sifirlama ->
                        if (sifirlama.isSuccessful){
                            binding.psifirlaMesaj.text ="E-mail adresinize sıfırlama bağlantısı gönderildi, lütfen kontrol ediniz"

                        }
                        else{
                            binding.psifirlaMesaj.text ="Sıfırlama işlemi başarısız"

                        }

                    }
            }



        }
        //Giriş sayfasına gitmek için
        binding.psifirlaGirisYap.setOnClickListener {
            intent = Intent(applicationContext, GirisActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}