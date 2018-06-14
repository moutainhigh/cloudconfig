package com.kuangchi.sdd.util.pdf;

import java.io.File;

/**
 * Created by ccjjxl on 2014/11/1.
 */
public class ImageToPdfProcessor extends AbstractProcessor<File> {

    private String img;

    public ImageToPdfProcessor(String name, String img) {
        super(name);
        this.img = img;
    }
    @Override
    protected File action() throws Exception {

        return PdfUtil.getFile(img);
    }

   

}
