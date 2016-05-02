package CustomRestfulClient;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;


public class AppRun {
	private static final Logger log = LoggerFactory.getLogger(AppRun.class);
	
	public static void main(String args[]) throws Exception{
		AppRun r = new AppRun();
        int i= r.run(args);
        System.exit(i);
    }
	
	
    public int run(String args[]) throws Exception {
		//Read the argument
		boolean checkEnv=false;
		boolean checkMode=false;
		boolean checkURL=false;
		String strEnvVar="";
		String Mode="";
		String URL="";
		int maxTry=10;
		for ( int i=0;i<args.length;i++){
			if(args[i].equals("-ENV_VAR")){
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
				System.out.println("-URL : URL");
				System.out.println("-ENV_VAR : SYSTEM varialble to contain value to form JSON requestm, example: TITLE,DESCRIPTION");
				System.out.println("Tool will search env variable of TITLE and DESCRIPTION from env variable and form JSON");
				System.out.println("-MODE : GET,POST,PUT,DELETE");
				System.out.println("-MAXTRY : MAX TRY before failure, default = 10");
				System.out.println("-H : HELP for this screen");
				return -1;
			}
		}
		if(!checkEnv){
			throw new Exception("No argument -ENV_VAR defined");
		}
		if(!checkMode){
			throw new Exception("No argument -MODE defined");
		}
		if(!checkURL){
			throw new Exception("No argument -URL defined");
		}
		//Further parameterized the input 
		HashMap<String,String> h = new HashMap<String,String>();
		String[] keylst = strEnvVar.split(",");
		for(String k : keylst){
			String kl = k.trim();
			String value = System.getenv(kl);
			if(value!=null){
				h.put(kl, value);
			}
		}
		RestfulClient cl=RestfulClient.getInstance();
		
		RestfulClient.ClientReturn ret=cl.connectServer(URL, h,Mode,maxTry);
		System.out.println(ret.getResponse());
		return ret.getStatus();
	}
}
