package com.rainbow.gray.framework.parser.xml.dom4j;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class Dom4JWriter {
    public static Document createDocument() {
        return DocumentHelper.createDocument();
    }

    public static String getText(Document document) throws IOException, UnsupportedEncodingException {
        return getText(document, Dom4JConstant.ENCODING_UTF_8);
    }

    public static String getText(Document document, String charset) throws IOException, UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputFormat outputFormat = new OutputFormat("  ", true, charset);

        try {
            XMLWriter writer = new XMLWriter(baos, outputFormat);
            writer.write(document);

            baos.flush();
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            if (baos != null) {
                baos.close();
            }
        }

        return baos.toString(charset);
    }
}