package com.bhakti.hook

import android.webkit.WebView
import android.webkit.WebViewClient
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import android.util.Log

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.harpamobilehr") return

        XposedHelpers.findAndHookConstructor(
            WebViewClient::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val client = param.thisObject as WebViewClient

                    XposedHelpers.findAndHookMethod(
                        client.javaClass,
                        "onPageFinished",
                        WebView::class.java,
                        String::class.java,
                        object : XC_MethodHook() {
                            override fun afterHookedMethod(param: MethodHookParam) {
                                val webView = param.args[0] as WebView
                                webView.evaluateJavascript(
                                    "(function() { return document.documentElement.outerHTML; })();"
                                ) { html ->
                                    Log.d("HTML_DUMP", html ?: "HTML kosong")
                                }
                            }
                        }
                    )
                }
            }
        )
    }
}
