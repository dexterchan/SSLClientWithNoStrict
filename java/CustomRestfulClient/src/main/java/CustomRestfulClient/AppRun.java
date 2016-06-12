package CustomRestfulClient;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@EnableAutoConfiguration
@Component
@ComponentScan
public class AppRun {
	private static final Logger log = LoggerFactory.getLogger(AppRun.class);
	enum MODE{FREE, TEMPLATE};
	MODE myMode;
	WebActionTemplate myTemplate=null;
	public static void main(String args[]) throws Exception{
//		AppRun r = new AppRun();
//        int i= r.run(args);
//        System.exit(i);
        
		//Testing
		
    	
		
        ConfigurableApplicationContext ctx = SpringApplication.run(AppRun.class, args);
        
        AppRun mainObj = ctx.getBean(AppRun.class);

        int i= mainObj.run(args);
        System.exit(i);
    }
	
	
    public int run(String args[]) throws Exception {
    	this.myMode = MODE.FREE;
		//Read the argument
		boolean checkEnv=false;
		boolean checkMode=false;
		boolean checkURL=false;
		String strEnvVar="";
		String Mode="";
		String URL="";
		String templateFile="";
		String Action="";
		int maxTry=10;
		for ( int i=0;i<args.length;i++){
			if(args[i].equals("-RUNTEMPL")){
				if(i<args.length-1){
					templateFile=args[i+1];
					checkEnv=true;
					i++;
					log.info("Use TemplateFile:"+templateFile);
					myMode = MODE.TEMPLATE;
				}else{
					checkEnv=false;
				}
			}else if(args[i].equals("-ACTION")){
				if(i<args.length-1){
					Action=args[i+1];
					checkEnv=true;
					i++;
					log.info("Action:"+Action);
				}else{
					checkEnv=false;
				}
			}else if(args[i].equals("-ENV_VAR")){
				if(i<args.length-1){
					strEnvVar=args[i+1];
					checkEnv=true;
					i++;
					log.info("EnvVar:"+strEnvVar);
				}else{
					checkEnv=false;
				}
			}else if(args[i].equals("-MODE")){
				if(i<args.length-1){
					if(i<args.length-1){
						Mode=args[i+1];
						checkMode=true;
						i++;
						log.info("Mode:"+Mode);
					}else{
						checkMode=false;
					}
				}
			}else if(args[i].equals("-URL")){
				if(i<args.length-1){
					if(i<args.length-1){
						URL=args[i+1];
						checkURL=true;
						i++;
						log.info("URL:"+URL);
					}else{
						checkURL=false;
					}
				}
			}else if(args[i].equals("-MAXTRY")){
				if(i<args.length-1){
					if(i<args.length-1){
						String sMaxTry=args[i+1];
						try{
							maxTry = Integer.parseInt(sMaxTry);
						}catch(NumberFormatException ne){
							log.error("Max Try is not valid numerical value");
							maxTry=10;
						}
						i++;
						log.info("MaxTry:"+maxTry);
					}
				}
			}else if(args[i].equals("-H")){
				System.out.println("A Restful client to send request by JSON");
				System.out.println("Free Mode");
				System.out.println("-URL : URL");
				System.out.println("-ENV_VAR : SYSTEM varialble to contain value to form JSON requestm, example: TITLE,DESCRIPTION");
				System.out.println("Tool will search env variable of TITLE and DESCRIPTION from env variable and form JSON");
				System.out.println("-MODE : GET,POST,PUT,DELETE");
				System.out.println("-MAXTRY : MAX TRY before failure, default = 10");
				System.out.println("-H : HELP for this screen");
				System.out.println("Template Mode");
				System.out.println("-RUNTEMPL <action template defined in Spring App Context>");
				System.out.println("-ACTION <action in tempalte>");
				return -1;
			}
		}
		
		//Further parameterized the input 
		Map<String,String> h=null;
		RestfulClient cl=RestfulClient.getInstance();
		RestfulClient.ClientReturn ret=null;
		if(this.myMode==MODE.FREE){
			if(!checkEnv){
				throw new Exception("No argument -ENV_VAR defined");
			}
			if(!checkMode){
				throw new Exception("No argument -MODE defined");
			}
			if(!checkURL){
				throw new Exception("No argument -URL defined");
			}
			h = prepareEnvInputFromString(strEnvVar);
			ret=cl.connectServer(URL, h,Mode,maxTry);
			System.out.println(ret.getResponse());
		}else{
			if(!checkEnv){
				throw new Exception("Template action is incomplete");
			}
			
			ApplicationContext context = new ClassPathXmlApplicationContext("SpringAppContext.xml");
			this.myTemplate = (WebActionTemplate)context.getBean(templateFile);
			h = prepareEnvInputFromWebActionTemplate(this.myTemplate,Action);
			
			WebAction webaction=myTemplate.actionWebLink.get(Action);
	    	if(webaction==null){
	    		throw new Exception("Not action defined");
	    	}
			
			maxTry = this.myTemplate.getMaxTryEachServer();
			int SecondaryInstance=0;
			boolean primaryFail=false;
			boolean runContinue=true;
			
			while(runContinue && SecondaryInstance<this.myTemplate.getSecondaryHostPort().size()){
				try{
					if(!primaryFail){
						URL=this.myTemplate.getPrimaryHostPort()+webaction.getWebLink();
						ret=cl.connectServer(URL, h,webaction.getHttpMethod(),maxTry);
						System.out.println(ret.getResponse());
						runContinue=false;
					}else{
						URL=this.myTemplate.getSecondaryHostPort().get(SecondaryInstance)+webaction.getWebLink();
						ret=cl.connectServer(URL, h,webaction.getHttpMethod(),maxTry);
						System.out.println(ret.getResponse());
						runContinue=false;
					}
					
				}catch(Exception e){
					if(primaryFail){
						SecondaryInstance++;
					}else{
						primaryFail=true;
					}
				}
			}
			
		}
		if(ret!=null){
			return ret.getStatus();
		}else return -1;
	}
    
    Map<String,String> prepareEnvInputFromWebActionTemplate(WebActionTemplate template,String action)throws Exception{
    	HashMap<String,String> h = new HashMap<String,String>();
    	WebAction webaction=template.actionWebLink.get(action);
    	if(webaction==null){
    		throw new Exception("Not action defined");
    	}
    	
		for(String k : webaction.getJsonParaFromEnv()){
			String kl = k.trim();
			String value = System.getenv(kl);
			if(value!=null){
				h.put(kl, value);
			}
		}
		return h;
    }
    
    Map<String,String> prepareEnvInputFromString(String strEnvVar){
    	HashMap<String,String> h = new HashMap<String,String>();
		String[] keylst = strEnvVar.split(",");
		for(String k : keylst){
			String kl = k.trim();
			String value = System.getenv(kl);
			if(value!=null){
				h.put(kl, value);
			}
		}
		return h;
    }
}
