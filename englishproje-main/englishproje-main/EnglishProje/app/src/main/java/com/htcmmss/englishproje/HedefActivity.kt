package com.htcmmss.englishproje

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.htcmmss.englishproje.databinding.ActivityHedefBinding

class HedefActivity : AppCompatActivity() {
    lateinit var binding: ActivityHedefBinding
    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    var database: FirebaseDatabase?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHedefBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")


        var hedefTurkce = resources.getStringArray(R.array.Turkce)
        var hedefIngilizce = resources.getStringArray(R.array.Ingilizce)

        var secilenAnaDil =intent.getStringExtra("anadil")



        if (binding.hedefSpinner != null){
            if (secilenAnaDil=="Türkçe"){
                val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,hedefIngilizce)
                binding.hedefSpinner.adapter = adapter
                binding.hedefSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        binding.hedefGizliText.text = hedefIngilizce[position]
                        //binding.dilOgrenmekGizliText.isVisible= false

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }


            }
            else if (secilenAnaDil=="İngilizce"){
                val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,hedefTurkce)
                binding.hedefSpinner.adapter = adapter
                binding.hedefSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        binding.hedefGizliText.text = hedefTurkce[position]
                        //binding.dilOgrenmekGizliText.isVisible= false

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }


            }

        }


        var currentUser = auth.currentUser
        //Kullanıcı Id sini alıp o ID adı altında adımızı ve soyadımızı kaydedelim. Bu adımlar standart.
        var currentUserDb = currentUser?.let { it1 -> databaseReference?.child(it1.uid) }
        //currentUserDb?.child("dil")?.setValue(binding.dilAnaGizliText.text.toString())
        //Toast.makeText(this@DilActivity, "Kayıt Başarılı", Toast.LENGTH_LONG).show()

        //Kaydet butonu ile kaydetme adımları
        binding.hedefKayitOlButton.setOnClickListener {
            var dilhedef= binding.hedefGizliText.text.toString()

            //Realtime database deki ID ye ulaşıp altındaki childların içindeki veriyi sayfaya aktarma
            var userReference = databaseReference?.child(currentUser?.uid!!)
            userReference?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    currentUserDb?.child("dilhedef")?.setValue(binding.hedefGizliText.text.toString())
                    Toast.makeText(this@HedefActivity, "Kayıt Başarılı", Toast.LENGTH_LONG).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

            intent = Intent(applicationContext,GirisActivity::class.java)
            startActivity(intent)
            finish()

        }



    }
}