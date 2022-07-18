package com.adspostx

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.adspostx.util.AdpostxDialogDismissListener
import com.adspostx.util.DialogParams
import com.adspostx.util.WebAppInterface
import kotlinx.android.synthetic.main.fragment_dialog_adpost.view.*


class AdpostxDialogFragment : DialogFragment() {

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

//        if (dialogParams?.token.isNullOrEmpty().not()) {
             updatedUrl = "${dialogParams?.url}/embed.html?accountId=6&email=&firstname=&lastname=&mobile=&confirmationref=&amount=&currency=&paymenttype=&ccbin=&zipcode=&country=&language=&close=1"
//        }

        setMenuVisibility(true)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialogTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val dialogLayout = layoutInflater.inflate(R.layout.fragment_dialog_adpost, null);
            builder.setView(dialogLayout)

            dialogLayout.webView.webViewClient = WebViewClient { isLoading ->
                if (isLoading.not()) {
                    dialogLayout.progressBar.visibility = View.GONE
                }
            }
            dialogLayout.webView.settings.javaScriptEnabled = true
            dialogLayout.webView.addJavascriptInterface(WebAppInterface(requireContext()) {
             dismiss()
            },"Android")
            dialogLayout.webView.clearHistory()
            dialogLayout.webView.clearFormData()
            dialogLayout.webView.clearCache(true)
            dialogLayout.webView.loadUrl(updatedUrl)


            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    inner class WebViewClient(var callback: (isLoading: Boolean) -> Unit) :
        android.webkit.WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            callback(false)
            super.onPageFinished(view, url)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        adpostxDialogDismissListener?.dismissListener()
    }
}