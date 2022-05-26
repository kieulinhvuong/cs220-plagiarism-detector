package junit;

import java.io.File;
import java.util.Collection;

import plagdetect.IPlagiarismDetector;
import plagdetect.PlagiarismDetector;

class TestAllDocs {
	
	static final String DOCS = "docs/alldocs";

    public static IPlagiarismDetector makeDetector(int n) throws Exception {
        // n is the size of an n-gram
        IPlagiarismDetector detector = new PlagiarismDetector(n);
        detector.readFilesInDirectory(new File(DOCS));
        return detector;
    }
    
	public static void main(String[] args) throws Exception {
		
		// how long does it take to create a detector?
		long start = System.currentTimeMillis();
		IPlagiarismDetector detector = makeDetector(3);
		long total = System.currentTimeMillis() - start;
		System.out.printf("It took %.1f seconds to read the documents\n", total/1000.0);
		
		// how long does it take to search for suspicious pairs?
		start = System.currentTimeMillis();
		Collection<String> pairs = detector.getSuspiciousPairs(50);
		for (String pair : pairs) {
			System.out.println(pair);
		}
		total = System.currentTimeMillis() - start;
		System.out.printf("It took %.1f seconds to check for suspicious pairs in the documents\n", total/1000.0);
	}
	
	

}
