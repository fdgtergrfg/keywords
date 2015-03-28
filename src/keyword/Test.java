package keyword;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
//import java.util.Dictionary;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Test {
	public static void main(String[] args) throws IOException {
		Reader text = new StringReader("hello word a");
		// Dictionary dic = new Dictionary(null);

		IKSegmenter iks = new IKSegmenter(text, true);

		// 创建分词对象
		// Analyzer anal = new IKAnalyzer(true);
		// isStopWord()
		// StringReader reader = new StringReader(text);
		// // 分词
		// TokenStream ts = anal.tokenStream("", reader);
		// CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
		// // 遍历分词数据
		// ts.reset();
		// while (ts.incrementToken()) {
		// System.out.print(term.toString() + "|");
		// }
		Lexeme term = null;
		while ((term = iks.next()) != null) {
			System.out.print(term.getLexemeText() + "|");
		}
		// reader.close();
		System.out.println();
	}

}