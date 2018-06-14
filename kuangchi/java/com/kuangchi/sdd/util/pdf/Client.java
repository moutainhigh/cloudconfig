package com.kuangchi.sdd.util.pdf;

import java.io.File;

/**
 * Created by ccjjxl on 2014/11/2.
 */
public class Client {

    public static void main(String[] args){
        MultiProcessor<File> multiProcessor = new MultiProcessor<File>("imgToPdf");

        String[] imgPath = new String[]{"F:/chucun/workspace/seas-web/src/main/webapp/upload/img/a.jpg",
                "F:/chucun/workspace/seas-web/src/main/webapp/upload/img/b.jpg",
                "F:/chucun/workspace/seas-web/src/main/webapp/upload/img/c.jpg",
                "F:/chucun/workspace/seas-web/src/main/webapp/upload/img/d.jpg"};

        for (int i = 0; i < imgPath.length; i++) {
            ImageToPdfProcessor imageToPdf = new ImageToPdfProcessor("img"+i,imgPath[i]);
            multiProcessor.addProcessor(imageToPdf);
        }
        multiProcessor.start();


    }
}
