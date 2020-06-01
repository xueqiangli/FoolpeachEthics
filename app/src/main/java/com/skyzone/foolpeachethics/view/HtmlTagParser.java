package com.skyzone.foolpeachethics.view;

import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.AlignmentSpan;

import org.xml.sax.XMLReader;

/**
 * Created by userdev1 on 3/22/2017.
 */

public class HtmlTagParser implements Html.TagHandler {


    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (tag.equalsIgnoreCase("align")) {
            handleAlign(opening, output, xmlReader);
        } else if (tag.equalsIgnoreCase("center")) {
            handleCenter(opening, output);
        }
    }

    private void handleCenter(boolean opening, Editable output) {
        int len = output.length();
        try {
            if (opening) {
                output.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), len, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                Object object = getLast(output, AlignmentSpan.class, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                int where = output.getSpanStart(object);
                output.removeSpan(object);
                if (where < 0)
                    where = 0;
                if (where != len) {
                    output.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), where, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleAlign(boolean opening, Editable output, XMLReader xmlReader) {
        int len = output.length();
        try {
            if (opening) {
                if (xmlReader.getProperty("align").equals("center")) {
                    output.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), len, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else {
                if (xmlReader.getProperty("align").equals("center")) {
                    Object object = getLast(output, AlignmentSpan.class, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int where = output.getSpanStart(object);
                    output.removeSpan(object);
                    if (where < 0)
                        where = 0;
                    if (where != len) {
                        output.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), where, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private Object getLast(Editable text, Class kind, int mark) {
        Object[] objects = text.getSpans(0, text.length(), kind);
        if (objects.length == 0)
            return null;
        else {
            for (int i = objects.length; i > 0; i--) {
                if (text.getSpanFlags(objects[i - 1]) == mark)
                    return objects[i - 1];
            }
            return null;
        }
    }
}
