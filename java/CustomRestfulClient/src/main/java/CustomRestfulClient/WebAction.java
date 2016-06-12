package CustomRestfulClient;

import java.util.List;

public class WebAction {
	String httpMethod;
	String webLink;
	
	List<String> jsonParaFromEnv;
	
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
	public List<String> getJsonParaFromEnv() {
		return jsonParaFromEnv;
	}
	public void setJsonParaFromEnv(List<String> jsonParaFromEnv) {
		this.jsonParaFromEnv = jsonParaFromEnv;
	}
	
	
}
