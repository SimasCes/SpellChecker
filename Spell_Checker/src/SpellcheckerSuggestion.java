//Just to point out, if something is capital and you would like it to be capital again or different from
//that saved in the dictionary e.g. snake is saved but you would like Snake; you must specify that you would
//like to enter a replacement and enter Snake for the capital to be kept (if a whole new word is entered
//not in the dictionary then this will be checked again)


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


public class SpellcheckerSuggestion
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
		//Will also allow users to choose a blank sometimes if the levenshtein algorithm allows
		wholeDictionary.add("");
		//Will sort the dictionary if it is not already sorted
		wholeDictionary.sort(String::compareToIgnoreCase);
	
		
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
	public static void checkWords(ArrayList<String> toCheckNoPunct, ArrayList<String> wholeDictionary, String args) throws IOException
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
				System.out.println("");
				
				//Create an array to store the suggestions
				ArrayList<String> levSuggestions = new ArrayList<String>();
				
				//Loops through the dictionary
				//The dictionary will always create and empty space at the top due to how 
				//I have coded this (does not affect anything)
				//This loop starts from 1 to ignore that (will also work if dictionary does not have that
				//space as it is added to the dictionary before this loop and is sorted in the main function)
				for(int i = 1; i < wholeDictionary.size(); i++)
				{
					//If the word that is wrong is the longest levenshtein distance of 2 it will
					//be displayed
					if(levenshtein(toCheckNoPunct.get(k), wholeDictionary.get(i)) <= 2)
					{
						//Add all the strings to the levSuggestions array
						levSuggestions.add(wholeDictionary.get(i));
					}
					
				}
				
				//To write numbers next to the suggestions
				int levCount = 1;
				
				//If there are no suggestions this is not printed
				if(levSuggestions.size() != 0)
				{
					System.out.println("You might have meant: ");
					
					//Will print out all of the suggestions
					for(int j = 0; j < levSuggestions.size(); j++)
					{
						System.out.println("[" + levCount + "]" + " " + levSuggestions.get(j));
						//Count updated so the numbers next to the list go up
						levCount++;
					}
				}
				//If there are no suggestions this is then printed
				else
				{
					System.out.println("Sorry there were no suggestions found in the dictionary!");
				}
				
				//Will print instructions on what user can do next
				System.out.println("");
				System.out.println("Type 1 if you would like to enter a replacement word");
				System.out.println("Type 2 to add this spelling to the dictionary");
				if(levSuggestions.size() != 0)
				{
					System.out.println("Type 3 if you would like to accept a word from the list");
				}

				//A user can enter what they would like to happen
				String choice = userChoice.next();
				
				//Will allow user to choose different options depending if there are suggested words
				if(levSuggestions.size() != 0)
				{
					//Until a user enter 1, 2, or 3 the question will repeat
					while(((choice.equalsIgnoreCase("1") || choice.equalsIgnoreCase("2") || choice.equalsIgnoreCase("3"))) == false)
					{
						System.out.println("Please type in 1 to accept word from list, 2 for replacement word");
						System.out.println("or 3 to add spelling to diacitonary");
						choice = userChoice.next();
					}
				}
				else
				{
					//Until a user enter 1, or 2 he question will repeat
					while(((choice.equalsIgnoreCase("1") || choice.equalsIgnoreCase("2"))) == false)
					{
						System.out.println("Please type in 1 to accept word from list, 2 for replacement word");
						choice = userChoice.next();
					}
				}
				
				//If they choose 1 then they will have to enter a replacement
				if(choice.equalsIgnoreCase("1"))
				{
					System.out.println("Enter the replacement word");
					//Will set the word the user types in as the replacement word
					toCheckNoPunct.set(k, userChoice.next());
					//To check if the word is in the dictionary again
					k--;
				}
				//If they choose 2 the word will be added to the dictionary
				else if(choice.equalsIgnoreCase("2"))
				{
					//Will add that word to the dictionary ArrayList
					//Will make all words lower-case
					wholeDictionary.add(toCheckNoPunct.get(k));
					System.out.println("The word was added!");
					System.out.println("");
				}
				//If the user enters 3, they are able to choose from the list of words that was shown
				else
				{
					//used to loop through the try and catch loop (if errors are caught)
					boolean error = false;
					while(error == false)
					{
						try
						{
							System.out.println("");
							//Will print the list again
							System.out.println("The list of available words was: ");
							for(int m = 0; m < levSuggestions.size(); m++)
							{
								System.out.println("[" + (m + 1) + "]" + levSuggestions.get(m));
							}
							
							System.out.println("");
							System.out.println("Plese enter the number of the word you would like to select: ");
								
							//Will convert the string to a number
							int num = Integer.parseInt(userChoice.next());
							
							//Will set the word that is not spelled correctly
							//as the users choice from the list
							toCheckNoPunct.set(k, levSuggestions.get(num - 1));
							
							//Will stop looping
							error = true;
						}
						//Will catch any errors with entering wrong values
						catch(java.lang.NumberFormatException | java.lang.IndexOutOfBoundsException e)
						{
							System.out.println("");
							System.out.println("Please only select numbers form the list!");
							System.out.println("");
						}
					}
					
					
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
		
		//Will update the dictionary file
		for(int i = 0; i < wholeDictionary.size(); i++)
		{
			dictionaryLine.println(wholeDictionary.get(i));
		}
		
		
		dictionaryLine.close();
	}
	
		
	//Returns an array with the words and punctuation separated of the file you want to check
	//All the white space is there for when you will create a new text file later with this
	//It will also return the list of words with punctuation as -2	
	public static ArrayList<String> fileToCheckToArray(String args) throws IOException
	{
		//Used for checking if the char elements are in the alphabet (to make strings form char elements)
		//Also has apostrophes as these will be ignored so work for words like don't
		char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
						   'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 
						   'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 
						   'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
						   '\''};
		
		
		//Creates a buffered reader object (which used the file reader object( which uses a file description
		//entered as the command line argument, for the file you need to spell check))
		BufferedReader fileBuffRead = new BufferedReader(new FileReader(new File(args)));
		
		
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
		while((i = fileBuffRead.read()) != -1)
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
		fileBuffRead.close();

		//Will add every element of the non-punctuation array list to the end of the stored punctuation list
		storedToEdit.addAll(noPunctuation);	
		
		//Returns the ArrayList's concatenated
		return storedToEdit;
		
	}
 
    
	//Will use the Levenshtein algorithms to calculate Levenshtein distance(measures 
	//amount of differences between two strings)
    public static int levenshtein(String word1, String word2) 
    {
        //Convert input strings to array of characters (char)
        char[] word1Array = word1.toCharArray();
        char[] word2Array = word2.toCharArray();
        
        //These instances are used a lot so this simplifies having to type .length()
        int word1Length = word1.length();
        int word2Length = word2.length();

        //Create a two dimensional array/matrix to store the Levenshtein distances
        int[][] deepMatrix = new int[word2Length + 1][word1Length + 1];

        //This position is always equal to zero as you compare empty string with empty string
        deepMatrix[0][0] = 0;

        //Will fill the row with the index elements from 1 to length of word1
        for (int k = 1; k <= word1Length; k++) 
        {
            deepMatrix[0][k] = k;
        }
        
        //Will fill the column with the index elements from 1 to length of word2
        for (int l = 1; l <= word2Length; l++) 
        {
            deepMatrix[l][0] = l;
        }

        //To keep track of replacements
        int replaced;

        //Loop through the 2D array (deepMatrix) and checks positions
        for (int i = 1; i <= word2Length ; i++) 
        {
            for (int j = 1; j <= word1Length ; j++) 
            {
            	//If the characters are the same, there is no replacement
                if(word2Array[i - 1] == word1Array[j - 1])
                {
                	replaced = 0;
                }  
                else
                {
                	//There is replacement so it is incremented
                	replaced = 1;
                }
                
                //You find the minimum of the 3 operations possible (deletion, insertion, or substitution)
            	//and set that as the deepMatrix element you are at
                deepMatrix[i][j] = Math.min(Math.min(deepMatrix[i - 1][j] + 1,  //deletion
                									 deepMatrix[i][j - 1] + 1),  //insertion
                        							 deepMatrix[i - 1][j - 1] + replaced); //substitution
                   
            }
        }
        //Return the total shortest length (Levenshtein distance)
        return (deepMatrix[word2Length][word1Length]);
    }
}




















