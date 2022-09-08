package com.affinitity.niche.ui.pdf;


public  class PdfModel {

    String id;
    String filename;
    String filepath;
    String pdfImage;


    public PdfModel(String id, String filename, String filepath,String pdfImage) {
        this.id = id;
        this.filename = filename;
        this.filepath = filepath;
        this.pdfImage = pdfImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getPdfImage() {
        return pdfImage;
    }

    public void  setPdfImage(String pdfImage) {
        this.pdfImage = pdfImage;
    }
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
