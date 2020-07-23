package com.example.mindhlju.file;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mindhlju.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

public class PDFDetailActivity extends AppCompatActivity implements OnLoadCompleteListener, OnPageChangeListener {

    private PDFView pdfView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfdetail);
        initPDFView();
    }

    private void initPDFView() {
        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.fromAsset("think.pdf")
                .defaultPage(0)
                .onPageChange(PDFDetailActivity.this)
                .enableAnnotationRendering(true)
                .onLoad(PDFDetailActivity.this)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .load();
    }

    @Override
    public void loadComplete(int nbPages) {
        Toast.makeText(this, "欢迎阅读《思考·快与慢》", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        getSupportActionBar().setTitle("思考·快与慢 ");
        getSupportActionBar().setSubtitle("当前进度： " + page + "/" + pageCount);
    }

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, PDFDetailActivity.class);
        activity.startActivity(intent);
    }
}
