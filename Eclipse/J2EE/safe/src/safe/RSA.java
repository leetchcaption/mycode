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
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");//����ָ���㷨��KeyPairGenerator����Կ�Զ���
			kpg.initialize(1024);//��ʼ����Կ�Դ�С����Կ������
			KeyPair kp = kpg.generateKeyPair();//��Կ��˽Կ�ĳ�����
			PublicKey pubkp = kp.getPublic();
			PrivateKey prikp = kp.getPrivate();
			
			FileOutputStream pub_fos = new FileOutputStream("Public_Key.txt");  //�洢��Կ
			ObjectOutputStream pub_oos = new ObjectOutputStream(pub_fos);
			pub_oos.writeObject(pubkp);
			
			FileOutputStream pri_fos = new FileOutputStream("Private_Key.txt"); //�洢˽Կ
			ObjectOutputStream pri_oos =new ObjectOutputStream(pri_fos);
			pri_oos.writeObject(prikp);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static String enCypt() throws Exception{
		//��ȡ��Կ���������e,n;
		FileInputStream pub_fis = new FileInputStream("Public_Key.txt");
		ObjectInputStream pub_ois = new ObjectInputStream(pub_fis);
		RSAPublicKey rsaPublicKey = (RSAPublicKey) pub_ois.readObject();
		BigInteger e = rsaPublicKey.getPublicExponent();
		BigInteger n = rsaPublicKey.getModulus();
		System.out.println("����e="+e);
		//System.out.println("n="+n);
		//��ȡ����
		byte ptext[] = string.getBytes("UTF-8");
		BigInteger m = new BigInteger(ptext);
		//�����Ľ��м��ܣ���������
		BigInteger cBigInteger=m.modPow(e, n);
		System.out.println("������:"+cBigInteger);
		
		//��������
		String cString = cBigInteger.toString();
		BufferedWriter bWriter = 
				new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream("���ܺ�.dat")));
		bWriter.write(cString,0,cString.length());
		bWriter.close();
		return null;      //����	
	}
	public static void deCypt(){		//����
		try {
			BufferedReader bReader = 
					new BufferedReader(
							new InputStreamReader(new FileInputStream("���ܺ�.dat")));
			String cString = bReader.readLine();
			BigInteger c= new BigInteger(cString);
			//��ȡ˽Կ
			FileInputStream pri_key = new FileInputStream("Private_Key.txt");
			ObjectInputStream ois = new ObjectInputStream(pri_key);
			RSAPrivateKey prKey = (RSAPrivateKey)ois.readObject();
			//��ȡ˽Կ��������
			BigInteger d = prKey.getPrivateExponent();
			BigInteger n = prKey.getModulus();
			System.out.println("����d = " +d);
			System.out.println("����n = " +n);
			//����
			BigInteger mBigInteger = c.modPow(d,n);
			//System.out.println("m = "+mBigInteger);
			byte [] mt = mBigInteger.toByteArray();
			System.out.println("����ǰ�ĵ����ݣ�");
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
									new FileInputStream("����.txt")));
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