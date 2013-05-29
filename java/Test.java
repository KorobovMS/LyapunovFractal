import org.slf4j.*;

import org.opencv.core.*;
import org.opencv.highgui.*;
import org.opencv.imgproc.*;

import java.util.*;

class Test {
	private static final Logger logger = LoggerFactory.getLogger(Test.class);
	private static final String destination = "LyapunovFractal.png";
	private static final int WIDTH = 4000;
	private static final int HEIGHT = 4000;
	
	static {
		System.loadLibrary("opencv_java245");
	}
	
	public static void main(String[] args) {
		Mat fractal = new Mat(HEIGHT, WIDTH, CvType.CV_64FC4);
		CycledLetterSequence S = new CycledLetterSequence();
		Map<Character, Double> r = new HashMap<Character, Double>();
		
		final double left = 0.0;
		final double right = 4.0;
		final double aStep = (right - left) / HEIGHT;
		final double bStep = (right - left) / WIDTH;
		for (double a = left; a <= right; a += aStep) {
			for (double b = left; b <= right; b += bStep) {
				r.put(Character.valueOf('A'), Double.valueOf(a));
				r.put(Character.valueOf('B'), Double.valueOf(b));
				double lambda = lyapunovExponent(900, r, S);
				int x = (int)(a*HEIGHT/(right-left));
				int y = (int)(b*WIDTH/(right-left));
				fractal.put(x, y,
					Math.abs(lambda)*255.0, 
					Math.abs(lambda)*255.0, 
					Math.abs(lambda)*255.0, 
					255.0);
			}
		}
		
		logger.info(S.toString());
		Highgui.imwrite(destination, fractal);
	}	
	
	static double lyapunovExponent(int N, Map<Character, Double> r, 
			CycledLetterSequence S) {
		double sum = 0.0;
		double xn = 0.5;
		for (int i = 1; i <= N; i++) {
			Character nextChar = Character.valueOf(S.charAtIndex(i));
			double rn = r.get(nextChar).doubleValue();
			xn = rn * xn * (1 - xn);
			
			sum += Math.log(Math.abs(rn * (1 - 2 * xn)));
		}
		return sum / N;
	}
}


class CycledLetterSequence {
	private static final char[] letters = {'A', 'B'};
	
	private String seq = null;
	
	public CycledLetterSequence() {
		StringBuilder builder = new StringBuilder();
		Random random = new Random();
		int size = 1 + random.nextInt(60);
		
		for (int i = 1; i <= size; i++) {
			int number = random.nextInt(2);
			builder.append(letters[number]);
		}
		
		seq = builder.toString();
	}
	
	public CycledLetterSequence(String str) {
		seq = str;
	}
	
	public char charAtIndex(int index) {
		int trueIndex = index % seq.length();
		return seq.charAt(trueIndex);
	}
	
	public String toString() {
		return seq;
	}
}


