package Hello;




import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class);
    }

    
    @Override
    public void run(String... args) throws Exception {
    	
    	TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] certificate, String authType) {
                return true;
            }
        };
        
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();
        SSLSocketFactory csf = new SSLSocketFactory(new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] certificate, String authType) {
                return true;
            }
        });
        
        csf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        
        

        HttpClientBuilder cb = HttpClients.custom();
        cb=cb.setSSLSocketFactory(csf);
        cb=cb.setSSLHostnameVerifier(new NoopHostnameVerifier());
        
        CloseableHttpClient httpClient = cb
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);

        
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        //Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
        
        //String urlString="http://localhost:8080/todo/api/v1.0/tasks";
        String urlString="https://localhost:443/todo/api/v1.0/tasks";
        TasksLst quote = restTemplate.getForObject(urlString, TasksLst.class);
        log.info(quote.toString());
        
        
        Task t = new Task();
        t.setTitle("Test ResfulTemplate");
        t.setDescription("new task test");
        
        //1. Convert object to JSON string
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(t);
        
     // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(jsonRequest, headers);
        
     // send request and parse result
        
        
        ResponseEntity<String> loginResponse = restTemplate
          .exchange(urlString, HttpMethod.POST, entity, String.class);
        if (loginResponse.getStatusCode() == HttpStatus.OK) {
          log.info( loginResponse.getBody());
        }else if(loginResponse.getStatusCode() == HttpStatus.CREATED) {
        	log.info( loginResponse.getBody());
        	//Logic to get response
        	String msgBody =  loginResponse.getBody();
        	JsonObject element = gson.fromJson (msgBody, JsonObject.class);
        	
        	JsonElement jo_task=element.get("task");
        	if(jo_task!=null){
	        	
	        	Task tt = gson.fromJson(jo_task, Task.class);
	        	log.info(tt.toString());
        	}
        	
    	}else if (loginResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) {
          // nono... bad credentials
        }
        
        
    }
}
