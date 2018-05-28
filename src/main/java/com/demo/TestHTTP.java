package com.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TestHTTP {

  @Test
  public void testCase() {
    String result = TestHTTP.httpRequest("http://localhost:8765/test/h1", "POST","{\"name\":\"a\", \"value\":\"bb\"}");
    System.out.println(result);
  }

  public static String httpRequest(String requestUrl, String requestMethod,String outputStr) {
    
    StringBuffer buffer = new StringBuffer();
    InputStream inputStream = null;
    try {
      URL url = new URL(requestUrl);
      HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
      httpUrlConn.setDoOutput(true);
      httpUrlConn.setDoInput(true);
      httpUrlConn.setRequestProperty("Content-Type","application/json");
      httpUrlConn.setRequestMethod(requestMethod);
      if ("GET".equalsIgnoreCase(requestMethod))
        httpUrlConn.connect();

      if (null != outputStr) {
        OutputStream outputStream = httpUrlConn.getOutputStream();
        outputStream.write(outputStr.getBytes("UTF-8"));
        outputStream.close();
      }
      inputStream = httpUrlConn.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

      String str = null;
      while ((str = bufferedReader.readLine()) != null) {
        buffer.append(str);
      }
      bufferedReader.close();
      inputStreamReader.close();
      inputStream.close();
      inputStream = null;
      httpUrlConn.disconnect();
      
    } catch (ConnectException ce) {
      ce.printStackTrace();
      System.out.println("Weixin server connection timed out");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("http request error:{}");
    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return buffer.toString();
  }

  /**
   * 发送GET请求
   * 
   * @param url
   *          目的地址
   * @param parameters
   *          请求参数，Map类型。
   * @return 远程响应结果
   */
  public static String sendGet(String url, Map<String, String> parameters) {
    String result = "";
    BufferedReader in = null;// 读取响应输入流
    StringBuffer sb = new StringBuffer();// 存储参数
    String params = "";// 编码之后的参数
    try {
      // 编码请求参数
      if (parameters.size() == 1) {
        for (String name : parameters.keySet()) {
          sb.append(name).append("=").append(java.net.URLEncoder.encode(parameters.get(name), "UTF-8"));
        }
        params = sb.toString();
      } else {
        for (String name : parameters.keySet()) {
          sb.append(name).append("=").append(java.net.URLEncoder.encode(parameters.get(name), "UTF-8")).append("&");
        }
        String temp_params = sb.toString();
        params = temp_params.substring(0, temp_params.length() - 1);
      }
      String full_url = url + "?" + params;
      System.out.println(full_url);
      java.net.URL connURL = new java.net.URL(full_url);
      java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL.openConnection();
      httpConn.setRequestProperty("Accept", "*/*");
      httpConn.setRequestProperty("Connection", "Keep-Alive");
      httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
      httpConn.connect();
      Map<String, List<String>> headers = httpConn.getHeaderFields();
      for (String key : headers.keySet()) {
        System.out.println(key + "\t：\t" + headers.get(key));
      }
      in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
      String line;
      while ((line = in.readLine()) != null) {
        result += line;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return result;
  }

  /**
   * 发送POST请求
   * 
   * @param url
   *          目的地址
   * @param parameters
   *          请求参数，Map类型。
   * @return 远程响应结果
   */
  public static String sendPost(String url, Map<String, String> parameters) {
    String result = "";// 返回的结果
    BufferedReader in = null;// 读取响应输入流
    PrintWriter out = null;
    StringBuffer sb = new StringBuffer();// 处理请求参数
    String params = "";// 编码之后的参数
    try {
      // 创建URL对象
      java.net.URL connURL = new java.net.URL(url);
      // 打开URL连接
      java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL.openConnection();
      // 设置通用属性
      httpConn.setRequestProperty("Accept", "*/*");
      httpConn.setRequestProperty("Connection", "Keep-Alive");
      httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
      // 设置POST方式
      httpConn.setDoInput(true);
      httpConn.setDoOutput(true);
      // 获取HttpURLConnection对象对应的输出流
      out = new PrintWriter(httpConn.getOutputStream());
      // 发送请求参数
      out.write(params);
      // flush输出流的缓冲
      out.flush();
      // 定义BufferedReader输入流来读取URL的响应，设置编码方式
      in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
      String line;
      // 读取返回的内容
      while ((line = in.readLine()) != null) {
        result += line;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return result;
  }

}