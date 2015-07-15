
public class Cipher {
	//Table of expected freq values
	static double[] table = {8.2, 1.5, 2.8, 4.3, 12.7, 2.2, 2.0, 6.1, 7.0, 0.2, 0.8, 4.0, 2.4, 6.7,
		7.5, 1.9, 0.1, 6.0, 6.3, 9.1, 2.8, 1.0, 2.4, 0.2, 2.0, 0.1};
	
	//Converts alphabetical char to num
	static int let2nat(char c)
	{
		if(Character.isLowerCase(c))
		{
			return ( c - 'a');
		}
		else
		{
			return -1;
		}
	}
	
	//Converts num into a lowercase alphabetical char
	static char nat2let(int code)
	{
		if(code >= 0 && code <= 25)
		{
			switch(code)
			{
			case 0: return 'a';
			case 1: return 'b';
			case 2: return 'c';
			case 3: return 'd';
			case 4: return 'e';
			case 5: return 'f';
			case 6: return 'g';
			case 7: return 'h';
			case 8: return 'i';
			case 9: return 'j';
			case 10: return 'k';
			case 11: return 'l';
			case 12: return 'm';
			case 13: return 'n';
			case 14: return 'o';
			case 15: return 'p';
			case 16: return 'q';
			case 17: return 'r';
			case 18: return 's';
			case 19: return 't';
			case 20: return 'u';
			case 21: return 'v';
			case 22: return 'w';
			case 23: return 'x';
			case 24: return 'y';
			case 25: return 'z';
			}
		}
		else
		{
			System.out.println("nat2let: Value outside of expected range");
			return '0';
		}
		return '0';
	}
	
	//Receives a char then shifts it over 'x' letters
	static char shift(int shift, char c)
	{
		if(Character.isLowerCase(c))
		{
			//Convert char to num and add shift value
			int num = let2nat(c);
			num += shift;
			//Mod the num to keep it in the range 0 - 25
			num = num % 26;
			//Convert it back into a char
			c = nat2let(num);
			return c;
		}
		//If it's not a lowercase character return the char
		else
		{
			return c;
		}
	}
	
	//Encodes a string according to it's shift value
	static String encode(int shift, String str)
	{
		String newStr = new String();
		
		//For every char in the string shift it and append
		//it to the new string
		for(int i = 0; i < str.length(); i++)
		{
			newStr = newStr + shift(shift, str.charAt(i));
		}
		
		return newStr;
	}
	
	//Inverse operation of 'encode'
	static String decode(int shift, String str)
	{
		String newStr = new String();
		
		//For every char in the string shift it inversely to
		//the shift value (26-shift)
		//26 - shift should never be negative since
		//we will only call it in crack
		for(int i = 0; i < str.length(); i++)
		{
			newStr = newStr + shift(26-shift, str.charAt(i));
		}
		
		return newStr;
	}
	
	//Counts the amount of lowercase letters
	static int lowers(String str)
	{
		int count = 0;
		//For loop checks if char in string is lowercase then
		//increments count
		for(int i = 0; i < str.length(); i++)
		{
			if(Character.isLowerCase(str.charAt(0)))
			{
				count++;
			}
		}
		
		return count;
	}
	
	//Counts how much of one char is in a string
	static int count(char c, String str)
	{
		int count = 0;
		
		//For loop checks if the current char is
		//the one we're looking for then increments if it is
		for(int i = 0; i < str.length(); i++)
		{
			if(str.charAt(i) == c)
			{
				count++;
			}
		}
		return count;
	}
	
	//percent gets the percent ratio of two ints
	static double percent(int num1, int num2)
	{
		//Cast nums as doubles, divide and multiply by 100
		return (double)100*((double)num1/(double)num2);
	}
	
	//Puts letter frequencies in a string into a list of doubles
	static double[] freqs(String str)
	{
		int total = lowers(str);
		double[] freqs = new double[26];
		
		//Iterate through array while assigning the current index
		//the letter frequency of the current letter
		for(int i = 0; i < freqs.length; i++)
		{
			//Calling count with nat2let(i) and str gives us how many times
			//the current letter appears in str
			freqs[i] = percent(count(nat2let(i),str),total);
		}
		
		return freqs;
	}
	
	//'Rotates' a list by n spaces left
	static double[] rotate(int n, double[] list)
	{
		double[] newList = new double[26];
		
		//Iterate through array assigning the members
		//of list to newList except with the shift value n added
		//to list's index
		for(int i = 0; i < list.length; i++)
		{
			newList[i] = list[(i+n)%26];
		}
		
		return newList;
	}
	
	//Outputs chisqr value of a list of doubles
	static double chisqr(double[] os)
	{
		double sum = 0.0;
		
		for(int i = 0; i < os.length; i++)
		{
			//chisqr formula
			sum += ((os[i] - table[i])*(os[i] - table[i])) / table[i];
		}
		
		return sum;
	}
	
	//Returns the index at which a value is encountered
	static int position(double a, double[] list)
	{
		for(int i = 0; i < list.length; i++)
		{
			if(list[i] == a)
			{
				return i;
			}
		}
		return -1;
	}
	
	//By getting a frequency list made from str crack
	//will then figure out the shift value for the minimum
	//chisqr value which will hopefully be the correct shift
	//needed to decode the encoded string
	static String crack(String str)
	{
		double[] list = freqs(str);
		
		//Initialize min to max value
		double min = Double.MAX_VALUE;
		
		int position = 0;
		
		//Iterate through list comparing the chisqr to min
		//and assigning the current index to position if chisqr
		//is less than min
		for(int i = 0; i < 26; i++)
		{
			if( chisqr(rotate(i,list)) < min)
			{
				min = chisqr(rotate(i,list));
				position = i;
			}
		}
		
		return decode(position, str);
	}

	public static void main(String[] args) {
		/*
		***************TEST CODE*********************
		double[] somelist = rotate(1,freqs("haskellisfun"));
		for(int i = 0; i < somelist.length; i++)
		{
			System.out.println(somelist[i]);
		}
		System.out.println();
		System.out.println(encode(3,"haskellisfun"));
		System.out.println(decode(3,"kdvnhoolvixq"));
		***************TEST CODE*********************
		*/
		System.out.println(crack("myxqbkdevkdsyxc yx mywzvodsxq dro ohkw!"));
	}

}
