package com.adspostx

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.DialogFragment
import com.adspostx.databinding.FragmentDialogAdpostBinding
import com.adspostx.util.AdpostxDialogDismissListener
import com.adspostx.util.DialogParams


class AdpostxDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogAdpostBinding
    var dialogParams: DialogParams? = null
    var updatedUrl = ""

    companion object {
        const val KEY_PARAMS = "params"
        var adpostxDialogDismissListener: AdpostxDialogDismissListener? = null

        fun newInstance(
            dialogParams: DialogParams?,
            listener: AdpostxDialogDismissListener?
        ): AdpostxDialogFragment {
            adpostxDialogDismissListener = listener
            val adpostxDialogFragment = AdpostxDialogFragment()
            val args = Bundle()
            args.putSerializable(KEY_PARAMS, dialogParams)
            adpostxDialogFragment.arguments = args
            return adpostxDialogFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dialogParams = arguments?.getSerializable(KEY_PARAMS) as DialogParams?

        if (dialogParams?.token.isNullOrEmpty().not()) {
            updatedUrl = "${dialogParams?.url}?from=${dialogParams?.token}"
        }

        setMenuVisibility(true)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogAdpostBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(updatedUrl)

        binding.toolbar.title = dialogParams?.title
        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.buttonClose) {
                dialog?.dismiss()
            }
            return@setOnMenuItemClickListener true
        }
    }

    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        adpostxDialogDismissListener?.dismissListener()
    }
}