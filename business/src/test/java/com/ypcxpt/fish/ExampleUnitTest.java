package com.ypcxpt.fish;

import com.ypcxpt.fish.app.repository.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static DataSource mDS;

    @BeforeClass
    public static void init() {
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test1() {
        int code = 0x02;
        System.out.println(Integer.toHexString(code & 0x1));
        System.out.println(Integer.toHexString(code & 0x2));
        System.out.println(Integer.toHexString(code & 0x4));
        System.out.println(Integer.toHexString(code & 0x8));
        System.out.println(Integer.toHexString(code & 0x10));
        System.out.println(Integer.toHexString(code & 0x20));
        System.out.println(Integer.toHexString(code & 0x40));
        System.out.println(Integer.toHexString(code & 0x80));
//        0x01, 02, 04, 08, 10, 20, 40, 80
    }

    @Test
    public void test2() {
        int i = 8;
        String format = String.format("%02d", 0);
        System.out.println(format);
    }

    @Test
    public void test3() {
        String s = "-12";
        int i = Integer.parseInt(s);
        System.out.println(i+"");
    }

    @Test
    public void test5(){
        try {
            Date date = DateFormat.getDateInstance().parse("2018-09-20");
//            Date date = DateFormat.getDateInstance().parse("2018-09-20T10:33:49.378Z");
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}