package com.tmall.buy.reg;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import static java.lang.Thread.currentThread;

/**
 * @author tianmai.fh
 * @date 14-1-22 14-11
 */
public class AutoRegister {
    public static void main(String[] args) throws IOException {
          File dir = new File("com/tmall/buy");
        ClassLoader loader = currentThread().getContextClassLoader();
        Enumeration<URL> urls = loader.getResources("com/tmall/buy");
        while(urls.hasMoreElements()){
            URL url = urls.nextElement();
            File[] files = new File(url.getFile()).listFiles();
            for(File file : files){
                if(file.isFile()){
                    loader.getResource(file.getAbsolutePath());
//                    loader
                }
            }
        }

    }
}
