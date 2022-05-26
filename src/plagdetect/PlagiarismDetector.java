package plagdetect;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.*;

public class PlagiarismDetector implements IPlagiarismDetector {
	private int n=3;
	private Map<String, Map<String, Integer>> results;
	private Map<String, Set<String>> ngram;

	public PlagiarismDetector(int n) {
		results = new HashMap<String, Map<String, Integer>>();
		ngram = new HashMap<String, Set<String>>();
	}
	
	@Override
	public int getN() {
		return n;
	}

	@Override
	public Collection<String> getFilenames() {
		return ngram.keySet();
	}

	@Override
	public Collection<String> getNgramsInFile(String filename) {
		return ngram.get(filename);
	}

	@Override
	public int getNumNgramsInFile(String filename) {
		return ngram.get(filename).size();
	}

	@Override
	public Map<String, Map<String, Integer>> getResults() {
		return results;
	}

	@Override
	public void readFile(File file) throws IOException {
		Set<String> ngramDoc = new HashSet<>();
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] lineArr = line.split(" ");
			StringBuilder sb = new StringBuilder();
			int start = 0;
			for (int end=0; end<lineArr.length; end++) {
				sb.append(lineArr[end]);
				if (end >= n-1) {
					ngramDoc.add(sb.toString());
					sb.delete(start, start+1);
					start++;
				}
			}
		}
		scanner.close();
		ngram.put(file.getName(), ngramDoc);
	}

	@Override
	public int getNumNGramsInCommon(String file1, String file2) {
		return results.get(file1).get(file2);
	}

	@Override
	public Collection<String> getSuspiciousPairs(int minNgrams) {
		Set<String> susPairs = new HashSet<>();
		for (Map.Entry<String, Map<String, Integer>> result : results.entrySet()) {
			for (Map.Entry<String, Integer> r : result.getValue().entrySet()) {
				if (r.getValue() >= minNgrams && r.getKey() != result.getKey()) {
					susPairs.add(String.format("u1=%s u2=%s u3=%s", result.getKey(), r.getKey(), result.getValue().toString()));
				}
			}
		}
		return susPairs;
	}

	@Override
	public void readFilesInDirectory(File dir) throws IOException {
		// delegation!
		// just go through each file in the directory, and delegate
		// to the method for reading a file
		for (File f : dir.listFiles()) {
			readFile(f);
		}
	}
}
