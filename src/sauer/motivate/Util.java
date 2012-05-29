package sauer.motivate;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.util.Log;
import android.util.Pair;

public class Util {

  private static final String UTF_8 = "UTF-8";
  private static final String TAG = Util.class.getName();

  static String post(String url, String body, List<Pair<String, String>> headers) {
    try {
      URL u = new URL(url);
      Log.i(TAG, "url: " + u);

      HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
      urlConnection.setDoOutput(true);
      urlConnection.setDoInput(true);
      urlConnection.setChunkedStreamingMode(0);
      urlConnection.setRequestMethod("POST");
      
      for (Pair<String, String> header : headers) {
        urlConnection.addRequestProperty(header.first, header.second);
      }

      OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream(), UTF_8);
      InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream(), UTF_8);

      Log.i(TAG, "-> " + body);
      writer.write(body);

      String result = read(reader);
      Log.i(TAG, "<- " + result);

      urlConnection.disconnect();
      return result;
    } catch (Exception e) {
      Log.i(TAG, "EXECPTION" + e);
      throw new RuntimeException(e);
    }
  }

  private static String read(InputStreamReader reader) throws IOException {
    char[] chars = new char[1024];
    StringBuffer buf = new StringBuffer();
    int len;
    while ((len = reader.read(chars)) != -1) {
      buf.append(chars, 0, len);
    }
    return buf.toString();
  }

}
