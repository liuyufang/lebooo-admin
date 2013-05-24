package com.lebooo.admin.service.lebooo;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 探测文本字符集.
 * 用法: <br/>
 * CharsetDetector charDect = new CharsetDetector(); <br/>
 * String charset = charDect.detectAllCharset(new ByteArrayInputStream(byteArray)); <br/>
 * @author liuwei
 */
public class CharsetDetector {
	private boolean found = false;
	private String result;
	private static Logger logger = LoggerFactory.getLogger(CharsetDetector.class);

	public String detectChineseCharset(InputStream in) throws IOException {
		return detectCharset(in, nsPSMDetector.CHINESE)[0];
	}

	public String detectAllCharset(InputStream in) throws IOException {
		// 测试发现即使探测图片,返回的数组也有一个编码,所以不用判断是否为空和length是否大于0了.
		return detectCharset(in, nsPSMDetector.ALL)[0];
	}

	private String[] detectCharset(InputStream in, int lang) throws IOException {
		String[] prob;
		// Initalize the nsDetector() ;
		nsDetector det = new nsDetector(lang);
		// Set an observer...
		// The Notify() will be called when a matching charset is found.

		det.Init(new nsICharsetDetectionObserver() {
			@SuppressWarnings("synthetic-access")
			public void Notify(String charset) {
				found = true;
				result = charset;
			}
		});
		BufferedInputStream imp = new BufferedInputStream(in);
		byte[] buf = new byte[1024];
		int len;
		boolean isAscii = true;
		while ((len = imp.read(buf, 0, buf.length)) != -1) {
			// Check if the stream is only ascii.
			if (isAscii)
				isAscii = det.isAscii(buf, len);
			// DoIt if non-ascii and not done yet.
			if (!isAscii) {
				if (det.DoIt(buf, len, false))
					break;
			}
		}
		imp.close();
		in.close();
		det.DataEnd();
		if (isAscii) {
			found = true;
			prob = new String[] { "ASCII" };
		} else if (found) {
			prob = new String[] { result };
		} else {
			prob = det.getProbableCharsets();
		}
		return prob;
	}

	/**
	 * 获取输入流内容,关闭输入流.
	 */
	public static String getContentAndClose(InputStream input) {
		if (input == null) {
			return "";
		}
		try {
			// 读到字节数组
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = -1;
			byte[] buff = new byte[4096];
			while ((len = input.read(buff, 0, buff.length)) != -1) {
				baos.write(buff, 0, len);
			}
			byte[] allBytes = baos.toByteArray();

			// 探测编码
			CharsetDetector charsetDetector = new CharsetDetector();
			String charset = charsetDetector.detectAllCharset(new ByteArrayInputStream(allBytes));
			logger.debug("charset: " + charset);

			return new String(allBytes, charset);
		} catch (Exception e) {
			logger.error("fetch remote content  error", e);
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				// 忽略
			}
		}

		return "";
	}
}