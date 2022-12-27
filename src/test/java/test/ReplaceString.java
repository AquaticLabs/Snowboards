package test;

import me.extremesnow.snowboard.utils.StringUtil;
import org.apache.commons.lang.StringUtils;

/**
 * @Author: extremesnow
 * On: 8/19/2022
 * At: 20:50
 */
public class ReplaceString {



    private static final String test1Str = "TEST1";
    private static final String test2Str = "TEST2";
    private static final String test3Str = "TEST3";
    private static final String test4Str = "TEST4";
    private static final String test5Str = "TEST5";
    private static final String test6Str = "TEST6";
    private static final String test7Str = "TEST7";
    private static final String test8Str = "TEST8";
    private static final String test9Str = "TEST9";
    private static final String test10Str = "TEST10";
    private static final String test11Str = "TEST11";
    private static final String test12Str = "TEST12";

    public static void main(String[] args) {


        long timeStart = System.currentTimeMillis();

        String test = "This is a test replacement: %test1%, %test2%, %test3%, %test4%, %test5%, %test6%, %test7%, %test8%, %test9%, %test10%, %test11%, %test12%, ";

        test3Replace(test);

        long timeEnd = System.currentTimeMillis();

        System.out.println("Time: " + (timeEnd-timeStart) + "ms");



    }


    private static void test1Replace(String t) {
        t = t
                .replace("%test1%", test1Str)
                .replace("%test2%", test2Str)
                .replace("%test3%", test3Str)
                .replace("%test4%", test4Str)
                .replace("%test5%", test5Str)
                .replace("%test6%", test6Str)
                .replace("%test7%", test7Str)
                .replace("%test8%", test8Str)
                .replace("%test9%", test9Str)
                .replace("%test10%", test10Str)
                .replace("%test11%", test11Str)
                .replace("%test12%", test12Str)
                .replace("%test13%", test12Str)
                .replace("%test14%", test8Str)
                .replace("%test15%", test8Str)
                .replace("%test16%", test8Str);

        System.out.println(t);
    }

    private static void test2Replace(String t) {

        String[] replacements = new String[] {
                "%test1%",
                "%test2%",
                "%test3%",
                "%test4%",
                "%test5%",
                "%test6%",
                "%test7%",
                "%test8%",
                "%test9%",
                "%test10%",
                "%test11%",
                "%test12%",
                "%test13%",
        };
        String[] replacewith = new String[] {
                test1Str,
                test2Str,
                test3Str,
                test4Str,
                test5Str,
                test6Str,
                test7Str,
                test8Str,
                test9Str,
                test10Str,
                test11Str,
                test12Str,
                test12Str,
        };

        StringUtils.replace(t, "%test1%", "tdasdadagfa");
        StringUtils.replace(t, "%test2%", "tdasdadagfa");
        StringUtils.replace(t, "%test3%", "tdasdadagfa");
        StringUtils.replace(t, "%test4%", "tdasdadagfa");
        StringUtils.replace(t, "%test5%", "tdasdadagfa");
        StringUtils.replace(t, "%test6%", "tdasdadagfa");
        StringUtils.replace(t, "%test7%", "tdasdadagfa");
        StringUtils.replace(t, "%test8%", "tdasdadagfa");
        StringUtils.replace(t, "%test9%", "tdasdadagfa");
        StringUtils.replace(t, "%test10%", "tdasdadagfa");
        StringUtils.replace(t, "%test11%", "tdasdadagfa");
        StringUtils.replace(t, "%test12%", "tdasdadagfa");
        StringUtils.replace(t, "%test13%", "tdasdadagfa");
        t = StringUtils.replaceEach(t, replacements, replacewith);
        System.out.println(t);
    }


    private static void test3Replace(String message) {
        StringUtil util = new StringUtil();
        util.addReplacement("%test1%", test1Str);
        util.addReplacement("%test2%", test2Str);
        util.addReplacement("%test3%", test3Str);
        util.addReplacement("%test4%", test4Str);
        util.addReplacement("%test5%", test5Str);
        util.addReplacement("%test6%", test6Str);
        util.addReplacement("%test7%", test7Str);
        util.addReplacement("%test8%", test8Str);
        util.addReplacement("%test9%", test9Str);
        util.addReplacement("%test10%", test10Str);
        util.addReplacement("%test11%", test11Str);
        util.addReplacement("%test12%", test12Str);
        util.addReplacement("%test1123123%", test1Str);
        util.addReplacement("%test1asa%", test1Str);
        util.addReplacement("%test11sda%", test1Str);
        util.addReplacement("%test11fgas%", test1Str);
        util.addReplacement("%test112gha%", test1Str);
        util.addReplacement("%test131gha%", test1Str);
        util.addReplacement("%test11gha%", test1Str);
        util.addReplacement("%test1411gha%", test1Str);
        util.addReplacement("%test11gha%", test1Str);
        util.addReplacement("%tes12t11gha%", test1Str);
        util.addReplacement("%tes3t11gha%", test1Str);
        util.addReplacement("%tes23t11gha%", test1Str);
        util.addReplacement("%tes1t11gha%", test1Str);
        util.addReplacement("%te321st11gha%", test1Str);
        util.addReplacement("%te5st11gha%", test1Str);
        util.addReplacement("%t6est11gha%", test1Str);
        util.addReplacement("%t4est11gha%", test1Str);


        System.out.println(util.getReplaced(null, message));


    }
}
