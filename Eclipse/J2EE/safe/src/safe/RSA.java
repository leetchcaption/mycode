package safe;
import java.security.*;
import java.security.spec.*;
import java.security.interfaces.*;
import java.text.BreakIterator;

import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;
import java.io.*;
import java.math.BigInteger;
public class RSA {
	
	static String string;
	public RSA(String mString) {
		this.string = mString;
	}
	
	public static void getKey(){
		try{
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");//返回指定算法的KeyPairGenerator的密钥对对象
			kpg.initialize(1024);//初始化密钥对大小的密钥生成器
			KeyPair kp = kpg.generateKeyPair();//公钥和私钥的持有者
			PublicKey pubkp = kp.getPublic();
			PrivateKey prikp = kp.getPrivate();
			
			FileOutputStream pub_fos = new FileOutputStream("Public_Key.txt");  //存储公钥
			ObjectOutputStream pub_oos = new ObjectOutputStream(pub_fos);
			pub_oos.writeObject(pubkp);
			
			FileOutputStream pri_fos = new FileOutputStream("Private_Key.txt"); //存储私钥
			ObjectOutputStream pri_oos =new ObjectOutputStream(pri_fos);
			pri_oos.writeObject(prikp);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static String enCypt() throws Exception{
		//获取公钥及计算参数e,n;
		FileInputStream pub_fis = new FileInputStream("Public_Key.txt");
		ObjectInputStream pub_ois = new ObjectInputStream(pub_fis);
		RSAPublicKey rsaPublicKey = (RSAPublicKey) pub_ois.readObject();
		BigInteger e = rsaPublicKey.getPublicExponent();
		BigInteger n = rsaPublicKey.getModulus();
		System.out.println("参数e="+e);
		//System.out.println("n="+n);
		//获取明文
		byte ptext[] = string.getBytes("UTF-8");
		BigInteger m = new BigInteger(ptext);
		//对明文进行加密，计算密文
		BigInteger cBigInteger=m.modPow(e, n);
		System.out.println("密文是:"+cBigInteger);
		
		//保存密文
		String cString = cBigInteger.toString();
		BufferedWriter bWriter = 
				new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream("加密后.dat")));
		bWriter.write(cString,0,cString.length());
		bWriter.close();
		return null;      //加密	
	}
	public static void deCypt(){		//解密
		try {
			BufferedReader bReader = 
					new BufferedReader(
							new InputStreamReader(new FileInputStream("加密后.dat")));
			String cString = bReader.readLine();
			BigInteger c= new BigInteger(cString);
			//读取私钥
			FileInputStream pri_key = new FileInputStream("Private_Key.txt");
			ObjectInputStream ois = new ObjectInputStream(pri_key);
			RSAPrivateKey prKey = (RSAPrivateKey)ois.readObject();
			//获取私钥参数解密
			BigInteger d = prKey.getPrivateExponent();
			BigInteger n = prKey.getModulus();
			System.out.println("参数d = " +d);
			System.out.println("参数n = " +n);
			//解密
			BigInteger mBigInteger = c.modPow(d,n);
			//System.out.println("m = "+mBigInteger);
			byte [] mt = mBigInteger.toByteArray();
			System.out.println("加密前文档内容：");
				for(int i=0;i<mt.length;i++){
					System.out.print((char) mt[i]);
				} 
		} catch (Exception e) {e.printStackTrace();	}
	}
	public static void main(String arg[]){
		try{
			BufferedReader mingwen = 
					new BufferedReader(
							new InputStreamReader(
									new FileInputStream("明文.txt")));
			String subString,mString="";
			int i=0;
			while ((subString = mingwen.readLine())!= null){
				mString = mString+"\n"+subString;
			}
			//System.out.println(mString);
			RSA go = new RSA(mString);
			go.getKey();
			go.enCypt();
			go.deCypt(); 
			
			
		}catch(Exception e){e.printStackTrace();}
	}
}
