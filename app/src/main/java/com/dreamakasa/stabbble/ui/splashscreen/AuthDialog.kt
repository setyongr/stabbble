package com.dreamakasa.stabbble.ui.splashscreen

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.dreamakasa.stabbble.R
import com.dreamakasa.stabbble.common.base.BaseInjectedDialog
import com.dreamakasa.stabbble.data.remote.AuthenticationService
import com.dreamakasa.stabbble.data.remote.Config
import com.dreamakasa.stabbble.injection.component.ActivityComponent
import com.dreamakasa.stabbble.ui.main.MainActivity
import kotlinx.android.synthetic.main.dialog_auth.*
import javax.inject.Inject

class AuthDialog: BaseInjectedDialog(), AuthView{
    @Inject lateinit var presenter: AuthPresenter

    var progress: ProgressDialog? = null

    override fun getLayout(): Int = R.layout.dialog_auth
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)

        web_view.settings.useWideViewPort = true
        web_view.settings.loadWithOverviewMode = true
        web_view.settings.setSupportZoom(true)
        web_view.settings.builtInZoomControls = true
        web_view.settings.displayZoomControls = false
        web_view.isVerticalScrollBarEnabled = true
        web_view.isHorizontalScrollBarEnabled = true

        web_view.setWebChromeClient(object : WebChromeClient(){})
        web_view.setWebViewClient(object : WebViewClient(){

            fun handleUrl(uri: Uri): Boolean{
                if(uri.toString().startsWith(Config.REDIRECT_URI)) {
                    //Get code
                    val code: String? = uri.getQueryParameter("code")
                    if(code != null){
                        progress = ProgressDialog.show(context, "Loading...", "Please wait...", true, false)
                        presenter.getToken(code)
                    }else if(uri.getQueryParameter("error") != null){
                        //Show Error
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                    return true
                }

                return false
            }

            @Suppress("OverridingDeprecatedMember")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean =
                    handleUrl(Uri.parse(url))

            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean =
                    handleUrl(request.url)
        })

        web_view.loadUrl(AuthenticationService.ENDPOINT + "authorize?client_id=${Config.CLIENT_ID}")

        btn_close.setOnClickListener {
            dismiss()
        }
    }

    override fun injectModule(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
        presenter.attachView(this)
    }
    override fun loginSuccess() {
        startActivity(Intent(context, MainActivity::class.java))
        activity.finish()
    }

    override fun onStart() {
        super.onStart()
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onPause() {
        super.onPause()
        presenter.clearDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.clearDisposable()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        presenter.clearDisposable()
        progress?.dismiss()
    }

}