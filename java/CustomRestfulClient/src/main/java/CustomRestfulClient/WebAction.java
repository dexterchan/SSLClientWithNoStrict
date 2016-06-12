package CustomRestfulClient;

import java.util.List;

public class WebAction {
	String httpMethod;
	String webLink;
	
	List jsonParaFromEnv;
	
	public String getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	public String getWebLink() {
		return webLink;
	}
	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}
	public List getJsonParaFromEnv() {
		return jsonParaFromEnv;
	}
	public void setJsonParaFromEnv(List jsonParaFromEnv) {
		this.jsonParaFromEnv = jsonParaFromEnv;
	}
	
}
