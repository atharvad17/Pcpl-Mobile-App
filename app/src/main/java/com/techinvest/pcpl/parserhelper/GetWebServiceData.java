package com.techinvest.pcpl.parserhelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.commonutil.MultipartUtility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class GetWebServiceData {
	
	private static final long CONN_MGR_TIMEOUT = 1000;
	private static final int CONN_TIMEOUT = 16000;
	private static final int SO_TIMEOUT = 190000;
    private static String url = AppSetting.getapiURL();
	/**
	 * Get data from URL
	 * 
	 * @return
	 */
	public static String getWebServiceData(String apiUrl) throws JSONException {
		StringBuilder builder = new StringBuilder();
		String line=null;
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(apiUrl);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200)
			{
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				//String line;
				while ((line = reader.readLine()) != null) 
				{
					builder.append(line);
					line=getJSONString(response);
					 Log.i("GetWebservice", "webdata:"+builder);
				}
				//System.out.println(line); 
				
				
				
			}
			else 
			{
				
				System.out.println("No record found");
			}
		} catch (ClientProtocolException e) {
			//Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
		
	}
	
	public static boolean isNetworkAvailable(Context context) {
		try
		{
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				return true;
			}
			else
				return false;
		}
		catch (Exception ex)
		{
			Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
			return false;
		}
	}
	
	
	public static String getServerResponse(String url,
			List<NameValuePair> nameValuePairs,String data) {
		// TODO Auto-generated method stub
		//Log.d("Request: ", nameValuePairs.get(0).toString());
		HttpResponse httpResponse = null;
		String response = null;
		 System.out.println("Request : " + nameValuePairs.toString());
		HttpPost httppost = new HttpPost(url);

		try {
			if(nameValuePairs!=null)
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
			//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			  System.out.println("Request after utf8 : " + nameValuePairs.toString());
			//HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);
			//httppost.setEntity(entity);
			HttpParams params = new BasicHttpParams();
			ConnManagerParams.setTimeout(params, CONN_MGR_TIMEOUT);
			HttpConnectionParams.setConnectionTimeout(params, CONN_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);

			HttpClient httpclient = new DefaultHttpClient(params);
			httpResponse = httpclient.execute(httppost);
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	

		if (httpResponse != null) {
			response=getJSONString(httpResponse);
			//response = getResponseFromHttpResponse(httpResponse);
			//Log.i("Response: ", response);
		}
		return response;
	}

	/**
	 * get response method 
	 * @param httpResponse
	 * @return
	 */
	public static String getResponseFromHttpResponse(HttpResponse httpResponse) {
		StringBuilder rspnsBuilder = new StringBuilder();
		if (httpResponse != null) {
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(
						httpResponse.getEntity().getContent()));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (bufferedReader != null) {
				String line = "";
				try {
					while ((line = bufferedReader.readLine()) != null) {
						rspnsBuilder.append(line);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return rspnsBuilder.toString();
	}
	
	
	public static String getJSONString(HttpResponse response) {

	    try {
	    	
	         DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	         Document doc = builder.parse(response.getEntity().getContent());
	         NodeList nl = doc.getElementsByTagName("string");
	         Node n = nl.item(0);
	         String str = n.getFirstChild().getNodeValue();
	         System.out.println("Node value : " + str);
	         return str;
	    } catch (ParserConfigurationException e) {
	         e.printStackTrace();
	    } catch (SAXException e) {
	         e.printStackTrace();
	    } catch (IllegalStateException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	 return null;
	}
	
	
	 // For upload profile pic
    public static String MultipartAddProfilePic(String data,String redid, String imagePath) {
        String serResponse = "";
        String charset = "UTF-8";

      //  String urlHex = ServerConnection.toHex(data);
      //  String urlSignature = ServerConnection.getUrlSignature(urlHex, AppConstants.share_key);
      //  Log.d("Before hext", "data" + data + "auth" + urlSignature);

        String requestURL = url +"/upload_images";

        try {
            MultipartUtility multipart = new MultipartUtility(requestURL,
                    charset);

            multipart.addFormField("building_name", data);
            multipart.addFormField("RequestId", redid);
        
            // Log.d("final hex", "data" + urlHex + "auth" + urlSignature);
            System.out.println("Request multipart with Hex : " + "post_data" + multipart.toString());

            if (imagePath != null) {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    multipart.addFilePart("profile_pic", imageFile);
                }
            }
            System.out.println("Request multipart final: " + "post_data" + multipart.toString());
            List<String> response = multipart.finish();
            for (String line : response) {
                serResponse += line;
            }
            //System.out.println("Request multipart with Hex : " + urlHex.toString()+""+urlSignature);
            System.out.println("Response : " + serResponse);
        } catch (IOException ex) {
            System.err.println(ex);
        }
//        }
        return serResponse;
    }

	public static String UploadimageBase64(List<NameValuePair> nameValuePairs,
			String imagePath) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
