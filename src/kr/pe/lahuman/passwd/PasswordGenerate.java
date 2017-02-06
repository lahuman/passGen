package kr.pe.lahuman.passwd;

public class PasswordGenerate {

	public static void main(String[] args) throws Exception {
		PasswordGenerate pg =  new PasswordGenerate();
		int minSize = 10, maxSize = 10, useUper = 4, useNumber = 1, useSpecial = 2;
		String pw = pg.generate(minSize, maxSize, useUper, useNumber, useSpecial);
		
		System.out.println(pw);
	}

	//14개
	private final char[] specialChar = {'!','@','#','$','%','^','&','*','(',')','_','+','-','='};
	private final boolean isDebug = true;
	public String generate(int minSize, int maxSize, int useUper,
			int useNumber, int useSpecial) throws Exception {
		
		//validate
		if(useUper + useNumber + useSpecial > minSize){
			throw new Exception("Checked your Option");
		}
		
		int currentPasswdLength = 0;
		while (currentPasswdLength < minSize){
			currentPasswdLength = getRandomNumber(maxSize)+1;
		}
		
		/**
		 * 0 : 소문자
		 * 1 : 대문자
		 * 2 : 숫자
		 * 3 : 특수문자 
		 */
		char[] passwd = new char[currentPasswdLength];
		//set random 소문자
		for(int i=0; i<passwd.length; i++){
			passwd[i] = getRandomEnglish(false);
		}
		
		print(currentPasswdLength);
		print("**************************");
		
		
		setFill(currentPasswdLength, passwd, useUper, 1); 
		setFill(currentPasswdLength, passwd, useNumber, 2);
		setFill(currentPasswdLength, passwd, useSpecial, 3);

		String resultPasswd = "";
		for(int i=0; i<passwd.length; i++){
			resultPasswd += passwd[i];
		}
		
		
		
		return resultPasswd;
	}

	private int setFill(int currentPasswdLength, char[] passwd, int used,
			int type) {
		while(used > 0){
			int whereFill = getRandomNumber(currentPasswdLength);
			int valOfNum = (int)passwd[whereFill] ;
			//소문자의 경우만
			if ( valOfNum>= 97 && valOfNum <= 122 ){
				print(whereFill);
				switch (type) {
				case 1:
					passwd[whereFill] = getRandomEnglish(true);
					break;
				case 2:
					passwd[whereFill] = (""+getRandomNumber(10)).toString().charAt(0);
					break;
				case 3:
					passwd[whereFill] = specialChar[getRandomNumber(14)];
					break;

				default:
					break;
				}
				used--;
			}
		}
		return used;
	}

	private char getRandomEnglish(boolean isUper){
		if(isUper){
			return (char)((Math.random()*26)+65);
		}else{
			return (char)((Math.random()*26)+97);
		}
	}
	private int getRandomNumber(int maxSize) {
		return (int) Math.floor(Math.random()*maxSize);
	}
	
	private void print(Object msg){
		if(isDebug){
			System.out.println(msg);
		}
	}

}
