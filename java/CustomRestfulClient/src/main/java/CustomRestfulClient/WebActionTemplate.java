package CustomRestfulClient;

import java.util.List;
import java.util.Map;

public class WebActionTemplate {
	String primaryHostPort;
	
	List<String> secondaryHostPort;
	
	Map<String,WebAction> actionWebLink;
	
	int maxTryEachServer;
	
	
	public String getPrimaryHostPort() {
		return primaryHostPort;
	}

	public void setPrimaryHostPort(String primaryHostPort) {
		this.primaryHostPort = primaryHostPort;
	}

	public List<String> getSecondaryHostPort() {
		return secondaryHostPort;
	}

	public void setSecondaryHostPort(List<String> secondaryHostPort) {
		this.secondaryHostPort = secondaryHostPort;
	}

	public Map<String, WebAction> getActionWebLink() {
		return actionWebLink;
	}

	public void setActionWebLink(Map<String, WebAction> actionWebLink) {
		this.actionWebLink = actionWebLink;
	}

	public int getMaxTryEachServer() {
		return maxTryEachServer;
	}

	public void setMaxTryEachServer(int maxTryEachServer) {
		this.maxTryEachServer = maxTryEachServer;
	}

	
	
	
}
