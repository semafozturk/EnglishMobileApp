package com.htcmmss.englishproje

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.htcmmss.englishproje.databinding.ActivityGirisBinding

class GirisActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityGirisBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityGirisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        //Kullanıcı yeni kayıt yapıp giriş yapa tıkladığında giriş sayfasına uğramadan profil sayfasına geçer.
        //İf 'i yorum satırı yaparsak direk giriş sayfasına gider kullanıcı bilgilerini tekrar yazar ve öyle giriş yapar.
        var currentUser = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this@GirisActivity,ProfilActivity::class.java))
            finish()
            }

            //Giriş yap butonuna tıklandığında gerekli kontroller
            binding.girisyapButton.setOnClickListener {
                var girisEmail = binding.girisEmail.text.toString()
                var girisParola = binding.girisParola.text.toString()
                if (TextUtils.isEmpty(girisEmail)){
                    binding.girisEmail.error = "Lütfen E-mail adresinizi yazınız"
                    return@setOnClickListener
                }
                else if (TextUtils.isEmpty(girisParola)){
                    binding.girisParola.error = "Lütfen Parolanızı yazınız"
                    return@setOnClickListener
                }

                //Giriş bilgilerini doğrulama
                auth.signInWithEmailAndPassword(girisEmail,girisParola)
                    .addOnCompleteListener(this){
                        if (it.isSuccessful){
                            intent = Intent(applicationContext,ProfilActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            Toast.makeText(applicationContext,"Giriş hatalı, Lütfen tekrar deneyiniz.",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            //Yeni üyelik sayfasına gitmek için
            binding.girisKayitOl.setOnClickListener {
                intent = Intent(applicationContext, UyeActivity::class.java)
                startActivity(intent)
                finish()
            }

             //Şifremi unuttum sayfasına gitmek için
             binding.girisParolaUnuttum.setOnClickListener {
                intent = Intent(applicationContext, PSifirlaActivity::class.java)
                startActivity(intent)
                finish()
        }

    }
}