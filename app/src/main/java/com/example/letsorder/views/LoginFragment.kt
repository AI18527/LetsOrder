package com.example.letsorder.views

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.letsorder.R
import com.example.letsorder.util.Event
import com.example.letsorder.databinding.FragmentLoginBinding
import com.example.letsorder.viewmodel.LoginViewModel
import com.example.letsorder.views.admin.AdminMain
import com.example.letsorder.views.waiter.WaiterMain
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    val viewModel: LoginViewModel by viewModels()

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
            buttonLogin.setOnClickListener {
                if (inputEmail.text.toString() != "" || inputPassword.text.toString() != "") {
                    viewModel.isAdmin(
                        inputEmail.text.toString(),
                        inputPassword.text.toString()
                    )
                    viewModel.isAdmin.observe(viewLifecycleOwner) { isAdmin: Event<Boolean> ->
                        val admin = isAdmin.handle()
                        admin?.let {
                            if (admin) {
                                navigateToAdmin()
                            } else {
                                viewModel.isWaiter(
                                    inputEmail.text.toString(),
                                    inputPassword.text.toString()
                                )
                                viewModel.isWaiter.observe(viewLifecycleOwner) { isWaiter: Event<Boolean> ->
                                    val waiter = isWaiter.handle()
                                    waiter?.let {
                                        if (waiter) {
                                            navigateToWaiter()
                                        } else {
                                            Snackbar.make(
                                                view.findViewById(R.id.loginFragment),
                                                "You have not been added by the admin.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                        }
                    }
                    viewModel.rightLogin.observe(viewLifecycleOwner) { rightLogin: Event<Boolean> ->
                        val valid = rightLogin.handle()
                        valid?.let {
                            if (!valid) {
                                Snackbar.make(
                                    view.findViewById(R.id.loginFragment),
                                    "Wrong password. Try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    Snackbar.make(
                        view,
                        "You should enter email and password.",
                        Snackbar.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.removeListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToAdmin() {
        startActivity(Intent(activity, AdminMain::class.java))
    }

    private fun navigateToWaiter() {
        startActivity(Intent(activity, WaiterMain::class.java))
    }
}