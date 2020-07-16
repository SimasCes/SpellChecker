//The path for the file to check, dictionary file, and path + name of new file (this is when the file is
//spell checked and fixed if any errors were there) must be entered in the command line argument
//With the path of the file being checked as the 1st argument, dictionary file being the 2nd argument,
//and path and name of checked file being the 3rd


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;



public class Spellchecker 
{
	
	public static void main(String[] args)
	{
		//If a file is missing the error will be caught and display instructions on how to make this program runs
		try 
		{
		//Will use the dictionary file specified in the command line argument
		File dict = new File(args[1]);
		Scanner dictionary = new Scanner(dict);
		

		//Used to store the the whole dictionary in an array list
		ArrayList<String> wholeDictionary = new ArrayList<String>();
		

		//While the is a next word, it is added to the ArrayList (can be used 
		//as there are no commas or punctuation in the dictionary file)
		while(dictionary.hasNext())
		{
			wholeDictionary.add(dictionary.next());
		}
		
		//Scanner is closed
		dictionary.close();
		
		//To get rid of the blank space checking requirement 
		//(when checking if words are in a dictionary)
		wholeDictionary.add("");
	
		
		//Returns an array with the words and punctuation separated of the file you want to check
		//All the white space is there for when you will create a new text file later with this
		//It will also return the list of words with punctuation as -2
		ArrayList<String> toCheck = fileToCheckToArray(args[0]);
		
		
		ArrayList<String> toCheckPunct = new ArrayList<String>();
		//This is used to fill the array with the punctuation included
		for(int i = 0 ; i < toCheck.size() / 2; i++)
		{
			toCheckPunct.add(toCheck.get(i));
		}
		
		
		ArrayList<String> toCheckNoPunct = new ArrayList<String>();
		//This is used to fill the array with no punctuation
		for(int j = toCheck.size() / 2 ; j < toCheck.size(); j++)
		{
			toCheckNoPunct.add(toCheck.get(j));
		}
		

		//Will check the file to be spell checked, and return the dictionary
		checkWords(toCheckNoPunct, wholeDictionary, args[1]);
		
		
		
		//Will update the array with the punctuation with any spelling changes that occurred
		for(int k = 0; k < toCheckNoPunct.size(); k++)
		{
			if(toCheckNoPunct.get(k).equals("-2") == false)
			{
				toCheckPunct.set(k, toCheckNoPunct.get(k));
			}
		}
		
		
		//Will print the correct spelled words to a new file with the 3rd command line argument
		printNewFile(toCheckPunct, args[2]);
		}
		catch(IOException e)
		{
			System.out.println("A file is missing or has the wrong path in the command line argument!");
			System.out.println("Please input path + file to check, path + name of dictionary, path + file of where to save the checked file (and name for this file)");
			System.out.println("The file name for the file to save does not have to exist as it will be created for you (you just need the path and a name of your choosing");
			System.out.println("Also make sure the files end with .txt");
		}
		
		
	}
	
	
	//Will create a new file(with the name and place where you specify as the 3rd command line argument)
	public static void printNewFile(ArrayList<String> toCheckPunct, String args) throws FileNotFoundException
	{
		//Will be able to write a new file with the spell checked words
		PrintWriter p = new PrintWriter(args);
		
		//Prints each line to the new file
		for(int b = 0; b < toCheckPunct.size(); b++)
		{
			p.print(toCheckPunct.get(b));
		}
		
		//Close the stream
		p.close();
		
		System.out.println("New correctly spelled file created!");
	}
	
	
	//Will check all the words that need to be spell checked
	public static void checkWords(ArrayList<String> toCheckNoPunct, ArrayList<String> wholeDictionary, String args) throws FileNotFoundException
	{
		
		//To keep track if the word was found in the dictionary
		boolean found = false;
		
		//A scanner will be used to ask for user input
		Scanner userChoice = new Scanner(System.in);
		
		//Loops through the ArrayList which stores the words to be checked with not punctuation
		for(int k = 0; k < toCheckNoPunct.size(); k++)
		{
			//Loops through the dictionary
			for(int l = 0; l < wholeDictionary.size(); l++)
			{
				//If a words is in the dictionary or is -2(so a punctuation) nothing will happen
				//This will also ignore the case (so capitals or non-capitals work the same)
				if((toCheckNoPunct.get(k).equalsIgnoreCase(wholeDictionary.get(l))) 
					|| (toCheckNoPunct.get(k).equals("-2")))
				{
					//So the parameters below are not activated
					found = true;
					//So you do not need to search more
					break;
				}
				
			}
			//If a word is not found then it will be changed or added to the dictionary
			if(found == false)
			{
				
				System.out.println("'" + toCheckNoPunct.get(k) + "'" + " was not found in the dicitonary");
				
				System.out.println("Would you like to add the word to the dictionary (type in 1) or enter a replacement (type in 0)?");
				
				//A user can enter what they would like to happen
				String choice = userChoice.next();
				
				//Until a user enter 1 or 0, the question will repeat
				while(((choice.equalsIgnoreCase("1") || choice.equalsIgnoreCase("0"))) == false)
				{
					System.out.println("Please type in 1 to add to dictionary, or 0 to type a replacement!");
					choice = userChoice.next();
				}
				
				//If they choose 1 the word will be added to the dictionary
				if(choice.equalsIgnoreCase("1"))
				{
					//Will add that word to the dictionary ArrayList
					//Will make all words lower-case
					wholeDictionary.add(toCheckNoPunct.get(k).toLowerCase());
					System.out.println("The word was added!");
					System.out.println("");
				}
				//If they choose 0 then they will have to enter a replacement
				else
				{
					System.out.println("Enter the replacement word");
					//Will set the word the user types in as the replacement word
					toCheckNoPunct.set(k, userChoice.next());
					//To check if the word is in the dictionary again
					k--;
				}
				
					
			}
			
			//Will be false again so you can keep searching through the words
			found = false;
			
		}
		
		//Will close the scanner (ending the stream)
		userChoice.close();
		
		//Will sort the dictionary alphabetically
		wholeDictionary.sort(String::compareToIgnoreCase);
		
		//Will update the dictionary and add any new words
		updateDictionary(wholeDictionary, args);
					
	}
	
	
	//Updates dictionary with any new elements added
	public static void updateDictionary(ArrayList<String> wholeDictionary, String args) throws FileNotFoundException
	{
		//To update the dictionary
		PrintWriter dictionaryLine = new PrintWriter(args);
		
		
		for(int i = 0; i < wholeDictionary.size(); i++)
		{
			//Will make all words lower-case
			dictionaryLine.println(wholeDictionary.get(i).toLowerCase());
		}
		
		
		dictionaryLine.close();
	}
		
	
	//Returns an array with the words and punctuation separated of the file you want to check
	//All the white space is there for when you will create a new text file later with this
	//It will also return the list of words with punctuation as -2	
	public static ArrayList<String> fileToCheckToArray(String args) throws IOException
	{
		//Used for checking if the char elements are in the alphabet (to make strings form char elements)
		char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
						   'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 
						   'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 
						   'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
		
		
		//Creates a buffered reader object (which used the file reader object( which uses a file description
		//entered as the command line argument, for the file you need to spell check))
		BufferedReader br = new BufferedReader(new FileReader(new File(args)));
		
		
		//A temporary string which will hold characters
		String temporary = "";
		
		//Will store the fully formed characters and any punctuation/spaces
		ArrayList<String> storedToEdit = new ArrayList<String>();
		
		//Will store the fully formed characters with -2 for punctuation
		ArrayList<String> noPunctuation = new ArrayList<String>();
		
		//To keep track of when to store words
		int word;
		
		int i = 0;
		//Can only work with IOException (and this is imported)
		//-1 is the end of a file so will work until it reaches the end of a file
		while((i = br.read()) != -1)
		{
			word = 0;
			
			//Loops through the alphabet
			for(int j = 0; j < alphabet.length; j++)
			{
				//If a character is in the alphabet it will be added to a string
				if((char) i == alphabet[j])
				{
					//A character is stored/added to a string
					//(char) i converts from the value to a character e.g. 116 = t
					temporary = temporary + String.valueOf((char) i);
					word = 1;
				}
		
			}
			//When a character that is not in the alphabet is found:
			if(word != 1)
			{
				//The word is added to the storeToEdit and noPunctuation ArrayList 
				storedToEdit.add(temporary);
				noPunctuation.add(temporary);
				//Temporary is reset to being empty
				temporary = "";
				//Any characters e.g. ; or spaces are added to the file
				storedToEdit.add(String.valueOf((char) i));
				//-2 will be used if there is punctuation
				noPunctuation.add("-2");
			}
							
		}
		
		//If the file ends with a letter, this will make sure the last word is added to the array
		if(temporary != "")
		{
			storedToEdit.add(temporary);
			noPunctuation.add(temporary);
		}
		
		//To close the buffer reader scanner
		br.close();

		//Will add every element of the non-punctuation array list to the end of the stored punctuation list
		storedToEdit.addAll(noPunctuation);	
		
		//Returns the ArrayList's concatenated
		return storedToEdit;
		
	}
		
}































