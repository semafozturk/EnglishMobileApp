package com.htcmmss.englishproje

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.htcmmss.englishproje.databinding.ActivityProfilBinding

class ProfilActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfilBinding
    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    var database: FirebaseDatabase?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        var currentUser = auth.currentUser
        binding.profilEmail.text = "E-mail: "+currentUser?.email

        //Realtime database deki ID ye ulaşıp altındaki childların içindeki veriyi sayfaya aktarma
        var userReference = databaseReference?.child(currentUser?.uid!!)
        userReference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.profilAd.text = "Ad: "+snapshot.child("adi").value.toString()
                binding.profilSoyad.text = "Soyad: "+snapshot.child("soyadi").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        //Bilgilerimi güncelle butonu
        binding.profilBilgilerimiGuncelle.setOnClickListener {
            startActivity(Intent(this@ProfilActivity,GuncelleActivity::class.java))
            finish()
        }

        //Çıkış yap butonu
        binding.profilCikisYap.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@ProfilActivity,GirisActivity::class.java))
            finish()
        }

        //Hesabımı Sil butonu
        binding.profilHesabimiSil.setOnClickListener {
            currentUser?.delete()?.addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(applicationContext, "Hesabınız silindi", Toast.LENGTH_LONG).show()
                    auth.signOut()
                    startActivity(Intent(this@ProfilActivity,GirisActivity::class.java))
                    finish()
                    var currentUserDb = currentUser?.let { it1 -> databaseReference?.child(it1.uid) }
                    currentUserDb?.removeValue()
                }
            }

        }

    }
}