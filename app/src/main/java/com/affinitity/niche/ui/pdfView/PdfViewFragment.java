package com.affinitity.niche.ui.pdfView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


import com.affinitity.niche.MainActivity;
import com.affinitity.niche.R;

import java.net.URL;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;


public class PdfViewFragment extends Fragment {



    private MainActivity mainActivity;
    String pdfFileName;
    Integer pageNumber = 0;
    Uri uri;
Context context;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pdf_view, container, false);
final   WebView webView;
 webView = (WebView) view.findViewById(R.id.webview);


         Bundle bundle = null;
        mainActivity      = (MainActivity) getActivity();
        mainActivity.getSupportActionBar().setTitle(getArguments().getString("filename"));
        String  urlString  = getArguments().getString("urlString");


        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        String url = "http://beaconsintl.com/api/lorem-ipsum.pdf";
 String finalUrl =   "https://docs.google.com/viewer?embedded=true&url=" + urlString;

        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setBuiltInZoomControls(true);
   //

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        webView.loadUrl(finalUrl);





        return  view;
    }

}