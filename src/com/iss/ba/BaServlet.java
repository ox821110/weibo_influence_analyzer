package com.iss.ba;

import java.io.IOException;
import java.io.FileInputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.json.JSONArray;
import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;

import com.iss.ba.ERSProxy;

public class BaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static ERSProxy m_ersProxy = null;

	public BaServlet() {
		super();
		System.out.println("Begin BaServlet");
		getServiceInfo();
		System.out.println("End BaServlet");
	}
	
	private void getServiceInfo() {
		if (m_ersProxy == null) {
			String uri = null;
			String userid = null;
			String password = null;
			String bundleUri = null;
			JSONObject service = null;
			JSONObject credentials = null;
			JSONArray serviceArray = null;

			Map<String, String> env = System.getenv();
			String vcap = env.get("VCAP_SERVICES");
			if (vcap == null) {
				System.out.println("No VCAP_SERVICES found");
			} else {
				try {
					JSONObject services = new JSONObject(vcap);

					try {
						serviceArray = (JSONArray) services
								.get("erservice-beta1");
						service = (JSONObject) serviceArray.get(0);
						credentials = (JSONObject) service.get("credentials");

						uri = (String) credentials.get("url");
						userid = (String) credentials.get("userid");
						password = (String) credentials.get("password");
					} catch (JSONException e) {
					}

					try {
						serviceArray = (JSONArray) services.get("mongolab");
						service = (JSONObject) serviceArray.get(0);
						credentials = (JSONObject) service.get("credentials");
						bundleUri = (String) credentials.get("uri");
					} catch (JSONException e) {
					}

					if (bundleUri == null) {
						try {
							serviceArray = (JSONArray) services
									.get("mongodb-2.2");
							service = (JSONObject) serviceArray.get(0);
							credentials = (JSONObject) service
									.get("credentials");
							bundleUri = (String) credentials.get("url");
						} catch (JSONException e) {
						}
					}

					if (uri == null) {
						System.err.println("No reporting service found");
						return;
					}

					if (bundleUri == null) {
						System.err.println("No mongodb service found");
						return;
					}

					synchronized (this) {
						m_ersProxy = new ERSProxy(uri);
						m_ersProxy.setAuthenticationInfo(userid, password);
						try {
							m_ersProxy.connect(bundleUri);
						} catch (IOException e) {
						}
					}
				} catch (JSONException e) {
				}
			}
		}
	}

	private void loadStaticPage(String page, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String template = getServletContext().getRealPath("/") + "/WEB-INF/"
				+ page;
		FileInputStream in = new FileInputStream(template);
		ServletOutputStream out = response.getOutputStream();

		int read = 0;
		byte[] buffer = new byte[32767];
		while ((read = in.read(buffer)) > 0) {
			out.write(buffer, 0, read);
		}
		in.close();

		response.setStatus(200);
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null)
			pathInfo = "/";
		if (pathInfo.compareTo("/") == 0) {
			loadStaticPage("result.html", request, response);
		} else if (pathInfo.startsWith("/ba/cre")) {
			if (m_ersProxy != null) {
				m_ersProxy.doGet(request, response);
			} else {
				response.setStatus(404);
			}
		} else {
			response.setStatus(404);
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo.startsWith("/ba/cre")) {
			if (m_ersProxy != null) {
				m_ersProxy.doDelete(request, response);
			} else {
				response.setStatus(404);
			}
		}
	}

	@Override
	public void destroy() {
		if (m_ersProxy != null) {
			try {
				m_ersProxy.disconnect();
			} catch (Exception e) {
			}
		}
	}
}
