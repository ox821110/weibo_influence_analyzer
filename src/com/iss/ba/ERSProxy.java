package com.iss.ba;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;

public class ERSProxy {
	private String m_uri = null;
	private String m_bundleUri = null;
	private String m_connectionId = null;
	private String m_authenticationInfo = null;

	private void doConnect() throws IOException {
		int i = 0;
		byte[] arrayOfByte = new byte[32767];
		InputStream localInputStream = null;
		if (this.m_uri == null)
			return;
		String str1 = this.m_uri + "/ba/cre/connection/bundleid";
		URL localURL = new URL(str1);
		HttpURLConnection localHttpURLConnection = (HttpURLConnection) localURL
				.openConnection();
		localHttpURLConnection.setRequestMethod("POST");
		localHttpURLConnection.setInstanceFollowRedirects(false);
		if (this.m_authenticationInfo != null)
			localHttpURLConnection
					.setRequestProperty(
							"Authorization",
							"Basic "
									+ DatatypeConverter
											.printBase64Binary(this.m_authenticationInfo
													.getBytes()));
		if (this.m_connectionId != null)
			localHttpURLConnection.setRequestProperty("ERS-ConnectionId",
					this.m_connectionId);
		String str2 = "{ \"bundleUri\":\"" + this.m_bundleUri + "\"}";
		localHttpURLConnection.setRequestProperty("Content-Type",
				"application/json");
		localHttpURLConnection.setRequestProperty("Content-Length",
				Integer.toString(str2.length()));
		localHttpURLConnection.setDoOutput(true);
		ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(
				str2.getBytes());
		OutputStream localOutputStream = localHttpURLConnection
				.getOutputStream();
		i = 0;
		while ((i = localByteArrayInputStream.read(arrayOfByte)) > 0)
			localOutputStream.write(arrayOfByte, 0, i);
		localOutputStream.close();
		try {
			localHttpURLConnection.connect();
			int j = 200;
			try {
				j = localHttpURLConnection.getResponseCode();
			} catch (Exception localException) {
			}
			localInputStream = j >= 400 ? localHttpURLConnection
					.getErrorStream() : localHttpURLConnection.getInputStream();
			if (localInputStream != null) {
				ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
				i = 0;
				while ((i = localInputStream.read(arrayOfByte)) > 0)
					localByteArrayOutputStream.write(arrayOfByte, 0, i);
				try {
					JSONObject localJSONObject = new JSONObject(
							localByteArrayOutputStream.toString());
					this.m_connectionId = ((String) localJSONObject
							.get("connectionId"));
				} catch (JSONException localJSONException) {
					System.out.println(localJSONException.getMessage());
				}
			}
		} finally {
			if (localInputStream != null)
				localInputStream.close();
		}
	}

	private void doDisconnect() throws IOException {
		int i = 0;
		byte[] arrayOfByte = new byte[32767];
		InputStream localInputStream = null;
		if (this.m_uri == null)
			return;
		String str = this.m_uri + "/ba/cre/connection/bundleid/"
				+ this.m_connectionId;
		URL localURL = new URL(str);
		HttpURLConnection localHttpURLConnection = (HttpURLConnection) localURL
				.openConnection();
		localHttpURLConnection.setRequestMethod("DELETE");
		localHttpURLConnection.setInstanceFollowRedirects(false);
		if (this.m_authenticationInfo != null)
			localHttpURLConnection
					.setRequestProperty(
							"Authorization",
							"Basic "
									+ DatatypeConverter
											.printBase64Binary(this.m_authenticationInfo
													.getBytes()));
		try {
			localHttpURLConnection.connect();
			int j = 200;
			try {
				j = localHttpURLConnection.getResponseCode();
			} catch (Exception localException) {
			}
			localInputStream = j >= 400 ? localHttpURLConnection
					.getErrorStream() : localHttpURLConnection.getInputStream();
			if (localInputStream != null) {
				ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
				i = 0;
				while ((i = localInputStream.read(arrayOfByte)) > 0)
					localByteArrayOutputStream.write(arrayOfByte, 0, i);
			}
		} finally {
			if (localInputStream != null)
				localInputStream.close();
			this.m_connectionId = null;
		}
	}

	@SuppressWarnings("rawtypes")
	private int doVerb(String paramString,
			HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse)
			throws ServletException, IOException {
		int i = 200;
		InputStream localInputStream = null;
		if ((this.m_uri == null) || (this.m_authenticationInfo == null))
			return i;
		String str1 = this.m_uri + paramHttpServletRequest.getPathInfo();
		String str2 = paramHttpServletRequest.getQueryString();
		if (str2 != null)
			str1 = str1 + "?" + str2;
		URL localURL = new URL(str1);
		HttpURLConnection localHttpURLConnection = (HttpURLConnection) localURL
				.openConnection();
		localHttpURLConnection.setRequestMethod(paramString);
		localHttpURLConnection.setInstanceFollowRedirects(false);
		Object localObject1 = paramHttpServletRequest.getHeaderNames();
		Object localObject2;
		while (((Enumeration) localObject1).hasMoreElements()) {
			localObject2 = (String) ((Enumeration) localObject1).nextElement();
			String str4 = paramHttpServletRequest
					.getHeader((String) localObject2);
			if (((String) localObject2).startsWith("Cookie"))
				str4 = str4.replaceAll("ERS-", "");
			localHttpURLConnection.addRequestProperty((String) localObject2,
					str4);
		}
		if (this.m_authenticationInfo != null)
			localHttpURLConnection
					.setRequestProperty(
							"Authorization",
							"Basic "
									+ DatatypeConverter
											.printBase64Binary(this.m_authenticationInfo
													.getBytes()));
		if (this.m_connectionId != null)
			localHttpURLConnection.setRequestProperty("ERS-ConnectionId",
					this.m_connectionId);
		byte[] arrayOfByte;
		if (paramHttpServletRequest.getContentLength() > 0) {
			localHttpURLConnection.setDoOutput(true);
			localObject1 = paramHttpServletRequest.getInputStream();
			localObject2 = localHttpURLConnection.getOutputStream();
			int k = 0;
			arrayOfByte = new byte[32767];
			while ((k = ((ServletInputStream) localObject1).read(arrayOfByte)) > 0)
				((OutputStream) localObject2).write(arrayOfByte, 0, k);
			((OutputStream) localObject2).close();
		}
		try {
			localHttpURLConnection.connect();
			try {
				i = localHttpURLConnection.getResponseCode();
			} catch (Exception localException) {
			}
			paramHttpServletResponse.setStatus(localHttpURLConnection
					.getResponseCode());
			String str3 = null;
			for (int j = 1; (str3 = localHttpURLConnection.getHeaderFieldKey(j)) != null; j++) {
				String str5 = localHttpURLConnection.getHeaderField(j);
				if (str3.equals("Set-Cookie"))
					str5 = "ERS-" + str5;
				paramHttpServletResponse.addHeader(str3, str5);
			}
			localInputStream = i >= 400 ? localHttpURLConnection
					.getErrorStream() : localHttpURLConnection.getInputStream();
			if (localInputStream != null) {
				ServletOutputStream localServletOutputStream = paramHttpServletResponse
						.getOutputStream();
				int m = 0;
				arrayOfByte = new byte[32767];
				while ((m = localInputStream.read(arrayOfByte)) > 0)
					localServletOutputStream.write(arrayOfByte, 0, m);
			}
		} finally {
			if (localInputStream != null)
				localInputStream.close();
		}
		return i;
	}

	public ERSProxy(String paramString) {
		this.m_uri = paramString;
	}

	public void setAuthenticationInfo(String paramString1, String paramString2) {
		this.m_authenticationInfo = (paramString1 + ":" + paramString2);
	}

	public void connect(String paramString) throws IOException {
		this.m_bundleUri = paramString;
		doConnect();
	}

	public void disconnect() throws IOException {
		doDisconnect();
	}

	public void doGet(HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse)
			throws ServletException, IOException {
		if (doVerb("GET", paramHttpServletRequest, paramHttpServletResponse) == 404) {
			doConnect();
			doVerb("GET", paramHttpServletRequest, paramHttpServletResponse);
		}
	}

	public void doDelete(HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse)
			throws ServletException, IOException {
		if (doVerb("DELETE", paramHttpServletRequest, paramHttpServletResponse) == 404) {
			doConnect();
			doVerb("DELETE", paramHttpServletRequest, paramHttpServletResponse);
		}
	}
}
