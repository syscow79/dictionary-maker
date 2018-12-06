package com.syscow.dictionary;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OpenReader {

	private Reader reader;

	URL url;
	static String host;
	static String text = "";

	public OpenReader() throws IOException {
		setReader(new FileReader("src/main/resources/com_maven_maven_overview.htm"));
	}

	public OpenReader(String url) throws IOException {
		System.out.println(url);
		this.url = new URL(url);
		host = this.url.getHost();
		setReader(new InputStreamReader(this.url.openStream()));
	}

	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		Set<String> links = new HashSet<>();

		//text = readAllText(new OpenReader("https://confluence.atlassian.com/agile/jira-agile-user-s-guide/jira-agile-tutorials"));
		//links.addAll(getLinks(text));

		text = readAllText(new OpenReader("http://www.vogella.com/tutorials/Git/article.html"));
		System.out.println(text);
		links.addAll(getLinks(text));

		text = "";
		links.parallelStream().forEach(OpenReader::readTextFromLink);

		Map<String, Integer> wordMap = collectMultipleWords(text);

		WordComparator wc = new WordComparator(wordMap);
		Map<String, Integer> sorted_map = new TreeMap<>(wc);
		sorted_map.putAll(wordMap);

		Set<String> keySet = new LinkedHashSet<String>(sorted_map.keySet());
		int totalCount = 0;
		for (String word : keySet) {
			totalCount += wordMap.get(word);
			System.out.println(word + " : " + wordMap.get(word));
			// System.out.println(word);
		}

		System.out.println("Words: " + keySet.size());
		System.out.println("TotalCount: " + totalCount);

		System.out.println("Time: " + (System.currentTimeMillis() - startTime) + "ms");
	}

	public static String readTextFromLink(String link){
		String html2text = "";
		try {
			html2text = html2text(readAllText(new OpenReader("https://" + host + link)));
		} catch (IOException e) {
			System.out.println("Error : " + link);
			e.printStackTrace();
		}
		text += html2text;
		return html2text;
	}

	public static Map<String, Integer> collectMultipleWords(String texts) {
		Map<String, Integer> wordMap = new HashMap<>();
		List<String> words = readWordsFromText(texts);
		WordQueue doubleWordQueue = new WordQueue(1);

		for (String word : words) {
			doubleWordQueue.push(word);
			String doubleWord = doubleWordQueue.getAllWords();
			if (wordMap.containsKey(doubleWord)) {
				wordMap.put(doubleWord, wordMap.get(doubleWord) + 1);
			} else {
				wordMap.put(doubleWord, 1);
			}
		}
		return wordMap;
	}

	private static String readAllText(OpenReader reader) {
		String line, text = "";
		try (BufferedReader br = new BufferedReader(reader.getReader())) {
			while ((line = br.readLine()) != null) {
				text += line + "\n";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	public static String html2text(String html) {
		return Jsoup.parse(Jsoup.parse(html).text()).text();
	}

	public static Set<String> getLinks(String html) {
		Document doc = Jsoup.parse(html);
		Elements links = doc.select("a[href]");
		Set<String> linkSet = new HashSet<>();

		for (Element link : links) {
			String linkhref = link.attr("href");
			if (linkhref.startsWith("/")) {
				if (linkhref.indexOf('#') > 0) {
					linkhref = linkhref.substring(0, linkhref.indexOf('#'));
				}
				linkSet.add(linkhref);
			}
		}

		return linkSet;
	}

	public static List<String> readWordsFromText(String line) {
		line = line.replaceAll("[,:;(){}=*/@]", " ");
		line = line.replaceAll("\\s+", " ");
		line = line.replaceAll("\\s", " ");
		line = line.replaceAll(" +", " ");
		Pattern pattern = Pattern.compile(" +");
		String[] words = pattern.split(line);
		List<String> returnWords = new ArrayList<String>();
		for (String word : words) {
			if (containOnlyAZ(word)) {
				returnWords.add(word.toLowerCase());
			}
		}
		return returnWords;
	}

	private static boolean containOnlyAZ(String word) {
		return word.matches("[A-Za-z]+");
	}

	public Reader getReader() {
		return reader;
	}

	public void setReader(Reader reader) {
		this.reader = reader;
	}

}
