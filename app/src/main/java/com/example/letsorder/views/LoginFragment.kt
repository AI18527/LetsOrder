package com.example.letsorder.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.letsorder.R
import com.example.letsorder.data.Datasource
import com.example.letsorder.databinding.FragmentLoginBinding
import com.example.letsorder.model.Waiter
import com.example.letsorder.views.admin.AdminMain
import com.example.letsorder.views.waiter.WaiterMain
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.apply {
            buttonLogin.setOnClickListener { goToNextScreen(view) }
        }
    }

    private fun goToNextScreen(view: View) {
        if (binding.inputEmail.text.toString() == "Admin") {
            startActivity(Intent(activity, AdminMain::class.java))
        } else {
            auth.signInWithEmailAndPassword(
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString()
            )
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "signInWithEmail:success")
                        val user = auth.currentUser
                        startActivity(Intent(activity, WaiterMain::class.java))
                    } else {
                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                        Snackbar.make(
                            view.findViewById(R.id.loginFragment),
                            "Wrong email or password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}