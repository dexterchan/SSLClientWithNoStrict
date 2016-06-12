package CustomRestfulClient;

import java.util.List;
import java.util.Map;

public class WebActionTemplate {
	String primaryHostPort;
	
	List secondaryHostPort;
	
	Map<String,WebAction> actionWebLink;
	
	int maxTryEachServer;
	int minTrySwitch2Secondary;
	
	public String getPrimaryHostPort() {
		return primaryHostPort;
	}

	public void setPrimaryHostPort(String primaryHostPort) {
		this.primaryHostPort = primaryHostPort;
	}

	

	public List getSecondaryHostPort() {
		return secondaryHostPort;
	}

	public void setSecondaryHostPort(List secondaryHostPort) {
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

	public int getMinTrySwitch2Secondary() {
		return minTrySwitch2Secondary;
	}

	public void setMinTrySwitch2Secondary(int minTrySwitch2Secondary) {
		this.minTrySwitch2Secondary = minTrySwitch2Secondary;
	}
	
	
}
