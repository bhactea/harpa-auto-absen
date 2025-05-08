package com.bhakti.hook

import android.webkit.WebView
import android.webkit.WebViewClient
import android.util.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.harpamobilehr") return

        Log.d("HOOK", "Harpa Mobile HR loaded!")

        XposedHelpers.findAndHookMethod(
            WebView::class.java,
            "loadUrl",
            String::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val webView = param.thisObject as WebView
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
