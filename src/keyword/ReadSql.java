package keyword;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadSql {

	public static String Read(String path){
		String result = null;
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(path);
			InputStreamReader reader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line;
			StringBuffer stringBuffer = new StringBuffer();
			try {
				while((line = bufferedReader.readLine()) != null){
					stringBuffer.append(line);
					stringBuffer.append(" ");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result = stringBuffer.toString();
			try {
				bufferedReader.close();
				reader.close();
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
