package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyFileReader {

	public static String[] getFile(String path) {
		List<String> list = new ArrayList<String>();
		String[] codes = new String[list.size()];
		File f = new File(path);
		// 创建文件字符流
		// 缓存流必须建立在一个存在的流的基础上
		try (FileReader fr = new FileReader(f); BufferedReader br = new BufferedReader(fr);) {
			while (true) {
				// 一次读一行
				String line = br.readLine();

				if (null == line)
					break;
				// 删除空格
//				line = line.replaceAll("\\s*", "");
				list.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list.toArray(codes);

	}
}