package com.syscow.dictionary;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class AppTest {
    
	@Test
	public void testSimpleWordLine()
    {	
		//given
    	String line = "your attention";
    	List<String> wordsExceptedWords = Arrays.asList("your", "attention");
    	
    	//when
    	List<String> words = OpenReader.readWordsFromText(line);
    	
    	//then
    	assertEquals(wordsExceptedWords, words);
    }
	
	@Test
	public void testHtmlLineWordLine()
    {	
		//when
    	String line = "<font color=\"#E5E5E5\">your attention</font>";

    	List<String> wordsExceptedWords = Arrays.asList("your", "attention");
    	String text = OpenReader.html2text(line);
    	List<String> words = OpenReader.readWordsFromText(text);

    	System.out.println(words);
    	//then
    	assertEquals(wordsExceptedWords, words);
    }
	
}
