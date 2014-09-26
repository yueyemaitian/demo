package com.tmall.buy.tomcat.study;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.tomcat.util.digester.Digester;
import org.xml.sax.InputSource;
  
public class MyDigester {  
//  
    private MyServer myServer;  
  
    public MyServer getMyServer() {  
        return myServer;  
    }  
  
    public void setMyServer(MyServer myServer) {  
        this.myServer = myServer;  
    }  
  
    private Digester createStartDigester() {  
        // ʵ����һ��Digester����  
        Digester digester = new Digester();  
  
        // ����Ϊfalse��ʾ����xmlʱ����Ҫ����DTD�Ĺ���У��  
        digester.setValidating(false);  
  
        // �Ƿ���нڵ����ù���У��,���xml����Ӧ�ڵ�û�����ý���������ڿ���̨��ʾ��ʾ��Ϣ  
        digester.setRulesValidation(true);  
  
        // ��xml�ڵ��е�className��Ϊ�����ԣ����ص���Ĭ�ϵ�setter������һ��Ľڵ������ڽ���ʱ����������ֵ��Ϊ��ε��øýڵ���Ӧ�����setter��������className���Ե���������ʾ�������ø����Ե�ֵ��ʵ��������  
        HashMap<Class<?>, List<String>> fakeAttributes = new HashMap<Class<?>, List<String>>();  
        ArrayList<String> attrs = new ArrayList<String>();  
        attrs.add("className");  
        fakeAttributes.put(Object.class, attrs);  
        digester.setFakeAttributes(fakeAttributes);  
  
        // addObjectCreate��������˼������xml�ļ��е�Server�ڵ��򴴽�һ��MyStandardServer����  
        digester.addObjectCreate("Server",  
        		MyStandardServer.class.getName(), "className");  
        // ����Server�ڵ��е�������Ϣ������Ӧ���Ե�setter�������������xml�ļ�Ϊ��������setPort��setShutdown��������ηֱ���8005��SHUTDOWN  
        digester.addSetProperties("Server");  
        // ��Server�ڵ��Ӧ�Ķ�����Ϊ��ε���ջ�������setMyServer�����������ջ�����������digester.push���������õĵ�ǰ��Ķ���this������˵����MyDigester���setMyServer����  
        digester.addSetNext("Server", "setMyServer",  
                MyServer.class.getName());  
  
        // ����xml��Server�ڵ��µ�Listener�ڵ�ʱȡclassName���Ե�ֵ��Ϊʵ������ʵ����һ������  
        digester.addObjectCreate("Server/Listener", null, "className");  
        digester.addSetProperties("Server/Listener");  
        digester.addSetNext("Server/Listener", "addLifecycleListener",  
                "org.apache.catalina.LifecycleListener");  
  
        digester.addObjectCreate("Server/Service",  
        		MyService.class.getName(), "className");  
        digester.addSetProperties("Server/Service");  
        digester.addSetNext("Server/Service", "setMyService",  
                MyService.class.getName());  
//        digester.pop();
//        digester.pop();
        
        digester.addObjectCreate("Server/Service/Listener", null, "className");  
        digester.addSetProperties("Server/Service/Listener");  
        digester.addSetNext("Server/Service/Listener", "addLifecycleListener",  
                "org.apache.catalina.LifecycleListener");  
        return digester;  
    }  
  
    public MyDigester() {  
        Digester digester = createStartDigester();  
  
        InputSource inputSource = null;  
        InputStream inputStream = null;  
        try {  
            String configFile = "myServer.xml";  
            inputStream = getClass().getClassLoader().getResourceAsStream(  
                    configFile);  
            inputSource = new InputSource(getClass().getClassLoader()  
                    .getResource(configFile).toString());  
  
            inputSource.setByteStream(inputStream);  
            digester.push(this);  
            digester.parse(inputSource);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                inputStream.close();  
            } catch (IOException e) {  
                // Ignore  
            }  
        }  
  
//        getMyServer().setMyDigester(this);  
    }  
  
    public static void main(String[] agrs) {  
        MyDigester md = new MyDigester();  
//        Assert.assertNotNull(md.getMyServer());  
    }  
} 