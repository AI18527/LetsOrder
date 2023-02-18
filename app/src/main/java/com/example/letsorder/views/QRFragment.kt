package com.example.letsorder.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.letsorder.R
import com.example.letsorder.util.Event
import com.example.letsorder.util.RestaurantInfo
import com.example.letsorder.databinding.FragmentQRBinding
import com.example.letsorder.viewmodel.TableStatusViewModel
import com.example.letsorder.views.client.ClientMain
import com.google.android.material.snackbar.Snackbar


class QRFragment : Fragment() {
    private val viewModel: TableStatusViewModel by viewModels()

    //private lateinit var cameraExecutor: ExecutorService

    private var _binding: FragmentQRBinding? = null
    private val binding get() = _binding!!

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //cameraExecutor = Executors.newSingleThreadExecutor()
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQRBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //startCamera()

        /*val selector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
            .build()
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext()), QRAnalyzer{ result ->
            var code = result
        })*/

        /*val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                //val intent = result.data
                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, ImageAnalyzer())
                    }
            }
            else{
                Log.d("Error", "Camera did not open")
            }
        }
        startForResult.launch(camera)
    }*/
        binding.buttonMenu.setOnClickListener {
            if (binding.restaurantId.text.toString() != "" &&
                binding.tableNumber.text.toString() != ""
            ) {
                viewModel.getTable(
                    binding.restaurantId.text.toString().toInt(),
                    binding.tableNumber.text.toString().toInt()
                )
                viewModel.tableExists.observe(viewLifecycleOwner) { tableExists ->
                    val exists = tableExists.handle()
                    exists?.let {
                        if (exists) {
                            viewModel.isTableFree()
                            viewModel.isFree.observe(viewLifecycleOwner) { isFree: Event<Boolean> ->
                                val free = isFree.handle()
                                free?.let {
                                    TableStatusViewModel.FREE_TABLE = free
                                    navigate()
                                }
                            }
                        } else {
                            Snackbar.make(
                                view,
                                "Wrong input. Check and enter again",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Snackbar.make(
                    view,
                    "You should enter table's number and restaurant's number",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        binding.buttonLogin.setOnClickListener {
            if (binding.restaurantId.text.toString() != "") {
                RestaurantInfo.restaurantId = binding.restaurantId.text.toString().toInt()
                findNavController().navigate(R.id.action_QRFragment_to_loginFragment)
            } else {
                Snackbar.make(
                    view,
                    "You should enter restaurant's number",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        //cameraExecutor.shutdown()
    }

    private fun navigate() {
        startActivity(Intent(activity, ClientMain::class.java))
        viewModel.removeListeners()
    }
}

/*private fun startCamera() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(binding.previewView.createSurfaceProvider())
            }

        val imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, QRAnalyzer())
            }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageAnalyzer
            )
        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }, ContextCompat.getMainExecutor(requireContext()))

    // Image analyzer
    /* val imageAnalysis = ImageAnalysis.Builder()
             .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
             .build()

         imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext()), QRAnalyzer{ result ->
             var code = result
             Log.d("Tag", "$code")
         })
         val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

         try {
             cameraProvider.unbindAll()
             cameraProvider.bindToLifecycle(
                 this, cameraSelector, preview, imageAnalysis
             )

         } catch (exc: Exception) {
             exc.printStackTrace()
         }
     }, ContextCompat.getMainExecutor(requireContext()))
 }
}*/
}*/
