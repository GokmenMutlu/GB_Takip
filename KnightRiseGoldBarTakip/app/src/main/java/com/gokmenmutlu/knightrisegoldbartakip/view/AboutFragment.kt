package com.gokmenmutlu.knightrisegoldbartakip.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gokmenmutlu.knightrisegoldbartakip.databinding.FragmentAboutBinding


class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Versiyon numarasını çek ve göster
        val versionName = requireContext().packageManager
            .getPackageInfo(requireContext().packageName, 0).versionName

        binding.txtVersion.text = "Versiyon: $versionName"

        binding.btnSend.setOnClickListener {


            val message = binding.etMessage.text.toString().trim()

            if (message.isNotEmpty()) {
                // E-posta gönderim Intent'i oluşturuluyor
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.type = "message/rfc822"  // Bu tür, sadece e-posta uygulamalarıyla uyumludur

                // E-posta alıcısı, konu ve mesaj içerikleri
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("destek.goldbartakip@gmail.com"))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Uygulama İletişim")
                emailIntent.putExtra(Intent.EXTRA_TEXT, message)

                // Mail uygulamasını seçmesi için kullanıcıya göster
                try {
                    startActivity(Intent.createChooser(emailIntent, "Mail uygulaması seçin"))
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(requireContext(), "E-posta uygulamanız bulunamadı.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Mesaj boş ise kullanıcıya uyarı
                Toast.makeText(requireContext(), "Lütfen mesajınızı yazın.", Toast.LENGTH_SHORT).show()
            }


        }

        binding.imgLogo.setOnClickListener {
            Toast.makeText(requireContext(),"İletişim: 'destek.goldbartakip@gmail.com'",Toast.LENGTH_LONG).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}