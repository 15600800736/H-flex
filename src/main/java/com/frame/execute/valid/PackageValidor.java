package com.frame.execute.valid;

import com.frame.exceptions.ScanException;
import com.frame.execute.scan.BaseContentsScanner;
import com.frame.info.Configuration;
import com.frame.execute.scan.Scanner;
import com.frame.util.ConfigurationReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fdh on 2017/7/5.
 */
public class PackageValidor implements Validor {
    private List<String> pakageList;

    public PackageValidor(List<String> pakageList) {
        this.pakageList = pakageList;
    }

    @Override
    public Boolean valid() {
        Pattern pattern = Pattern.compile("^[a-zA-z_][a-zA-Z0-9_]*(\\.[a-zA-z_][a-zA-Z0-9_]*)*");
        for(String pack : pakageList) {
            Matcher matcher = pattern.matcher(pack);
            if(!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

    public static void main(String...strings) {
        ConfigurationReader reader = null;
        try {
            reader = new ConfigurationReader("G:\\test.xml");
        } catch (ScanException e) {
            e.printStackTrace();
        }
        Configuration configuration = new Configuration();
        Scanner scanner = null;
        if(reader != null) {
             scanner = new BaseContentsScanner();
        }
        configuration.setRoot(reader.getRoot());
        configuration.setAnnotationScan(true);
        if(scanner != null) {
            try {
                scanner.scan(configuration);
            } catch (ScanException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void prepareForExecute() {

    }

    @Override
    public void postProcessForExccute() {

    }
}
