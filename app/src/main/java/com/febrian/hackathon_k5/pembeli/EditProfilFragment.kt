package com.febrian.hackathon_k5.pembeli

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.febrian.hackathon_k5.MainActivity
import com.febrian.hackathon_k5.PedagangAtauPembeli
import com.febrian.hackathon_k5.R
import com.febrian.hackathon_k5.databinding.FragmentEditProfilBinding
import com.febrian.hackathon_k5.pedagang.Register2Activity
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EditProfilFragment : Fragment() {

    private lateinit var binding: FragmentEditProfilBinding

    private lateinit var database: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditProfilBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = view.context.getSharedPreferences(
            MainActivity.KEYLOGIN,
            AppCompatActivity.MODE_PRIVATE
        )

        binding.btnSignOut.setOnClickListener {

            sharedPreferences.edit().clear().apply()

            val intent = Intent(view.context, PedagangAtauPembeli::class.java)
            startActivity(intent)
            activity?.finish()
        }

        val name = sharedPreferences.getString(LoginPembeli2Activity.KEY_NAME, "")

        database =
            FirebaseDatabase.getInstance().reference.child("UsersPembeli").child(name.toString())

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.nama.setText(snapshot.child("username").value.toString())

                binding.nama.setText(snapshot.child("username").value.toString())
                binding.password.setText(snapshot.child("password").value.toString())

                if (snapshot.child("url_image").value != null)
                    Glide.with(view.context).load(snapshot.child("url_image").value.toString())
                        .into(binding.ivPhoto)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(view.context, error.message, Toast.LENGTH_LONG).show()
            }

        })

    }
}