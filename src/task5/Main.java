package task5;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(Arrays.toString(encrypt("Hello")));
        System.out.println(decrypt(new int[]{72, 33, -73, 84, -12, -3, 13, -13, -68}));
        System.out.println(canMove("Rook", "A8", "H8"));
        System.out.println(canComplete("butl", "beautiful"));
        System.out.println(sumDigProd(1, 2, 3, 4, 5, 6));
        System.out.println(sameVowelGroup(new String[]{"toe", "ocelot", "maniac"}));
        System.out.println(validateCard("1234567890123452"));
        System.out.println(numToEng(126));
        System.out.println(numToRus(666));
        //System.out.println(getSha256Hash("password123"));
        System.out.println(correctTitle("jOn SnoW, kINg IN thE noRth."));
        System.out.println(hexLattice(61));
    }

    public static int[] encrypt(String str){
        int[] code = new int[str.length()];
        if(str.length() > 0) {
            int lastCode = str.charAt(0);
            code[0] = lastCode;
            if(str.length() > 1) {
                for (int i = 1; i < str.length(); i++) {
                    int newCode = str.charAt(i);
                    int diff = newCode - lastCode;
                    code[i] = diff;
                    lastCode = newCode;
                }
            }
        }
        return code;
    }

    public static String decrypt(int[] mas){
        String decode = "";
        if(mas.length > 0) {
            decode += (char) mas[0];;
            if(mas.length > 1) {
                int diff = mas[0];
                for (int i = 1; i < mas.length; i++) {
                    diff += mas[i];
                    char newCh = (char) diff;
                    decode += newCh;
                }
            }
        }
        return decode;
    }

    public static boolean canMove(String chess, String startPos, String endPos){
        boolean res = false;
        // A = 65, 1 = 49
        int X1 = startPos.charAt(0) - 64;
        int Y1 = startPos.charAt(1) - 48;
        int X2 = endPos.charAt(0) - 64;
        int Y2 = endPos.charAt(1) - 48;
        int diffX = Math.abs(X1-X2);
        int diffY = Math.abs(Y1-Y2);

        //Пешка, Конь, Слон, Ладья, Королева, Король
        switch (chess){
            case "Pawn":
                if(diffX > 0) break;
                if (Y1 == 2 || Y1 == 7){
                    if(diffY > 0 && diffY < 3) {
                        res = true;
                    }
                }
                else if(diffY == 1){
                    res = true;
                }
                break;

            case "Knight":
                if((diffX == 1 && diffY == 2) || (diffX == 2 && diffY == 1)){
                    res = true;
                }
                break;

            case "Bishop":
                if(diffX == diffY){
                    res = true;
                }
                break;

            case "Rook":
                if((diffX == 0 && diffY != 0) || (diffX != 0 && diffY == 0)){
                    res = true;
                }
                break;

            case "Queen":
                if(diffX == diffY){
                    res = true;
                }
                else if((diffX == 0 && diffY != 0) || (diffX != 0 && diffY == 0)){
                    res = true;
                }
                break;

            case "King":
                if(diffX <= 1 && diffY <= 1){
                    res = true;
                }
                break;

            default:
                break;
        }

        return res;
    }

    public static boolean canComplete(String str1, String str2){
        String regex = "^" + str1.replace("", "(.*)") + "$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str2);
        return matcher.matches();
    }

    public static int sumDigProd(int... args){
        int sum = 0;
        for(int num : args){
            sum += num;
        }
        String str = String.valueOf(sum);
        while (str.length() > 1){
            int pr = 1;
            for(char ch : str.toCharArray()){
                pr *= Integer.valueOf(ch)-48;
            }
            str = String.valueOf(pr);
        }
        return Integer.valueOf(str);
    }

    public static ArrayList<String> sameVowelGroup(String[] strArr){
        ArrayList<String>  newStrArr = new ArrayList<>();
        newStrArr.add(strArr[0]);
        String strCheck = strArr[0].replaceAll("[^aeiou]","");
        for(int i = 1; i < strArr.length; i++){
            String substr = strArr[i].replaceAll("[^aeiou]","");
            boolean check = true;
            for(char ch : substr.toCharArray()){
                if(!strCheck.contains(Character.toString(ch))){
                    check = false;
                    break;
                }
            }
            if(check) newStrArr.add(strArr[i]);
        }

        return newStrArr;
    }

    public static boolean validateCard(String argNum){
        if(argNum.length() < 14 || argNum.length() > 19) return false;
        int check = Integer.parseInt(argNum.substring(argNum.length()-1));
        argNum = argNum.substring(0, argNum.length()-1);
        String str = new StringBuilder(argNum).reverse().toString();
        int sum = 0;
        for(int i = 0; i < str.length(); i++){
            int num = str.charAt(i) - 48;
            if(i%2 == 0){
                num *= 2;
                if(num > 9){
                    num = (num % 10 + num / 10);
                }
            }
            sum += num;
        }
        int res = 10 - sum % 10;
        return res == check;
    }

    public static String numToEng(int num) {
        if(num == 0) return "zero";
        String s = "1:one,2:two,3:three,4:four,5:five,6:six,7:seven,8:eight,9:nine,10:ten," +
                "11:eleven,12:twelve,13:thirteen,14:fourteen,15:fifteen,16:sixteen,17:seventeen,18:eighteen,19:nineteen," +
                "20:twenty,30:thirty,40:forty,50:fifty,60:sixty,70:seventy,80:eighty,90:ninety,100:hundred";
        String[] engStr = s.split(",");
        HashMap<Integer, String> engMap = new HashMap<>();
        for(String str : engStr){
            engMap.put(Integer.valueOf(str.split(":")[0]), str.split(":")[1]);
        }
        String res = "";
        if(num > 99) {
            res += engMap.get(num/100) + " " + engMap.get(100) + " ";
            num = num % 100;
        }
        if(num > 9){
            res += engMap.get((num/10)*10) + " ";
            num = num % 10;
        }
        if(num > 0){
            res += engMap.get(num);
        }
        return res.trim();
    }

    public static String numToRus(int num) {
        if(num == 0) return "ноль";
        String s = "1:один,2:два,3:три,4:четыре,5:пять,6:шесть,7:семь,8:восемь,9:девять,10:десять," +
                "11:одиннадцать,12:двенадцать,13:тринадцать,14:четырнадцать,15:пятнадцать,16:шестнадцать,17:семнадцать,18:восемнадцать,19:девятнадцать," +
                "20:двадцать,30:тридцать,40:сорок,50:пятьдесят,60:шестьдесят,70:семьдесят,80:восемьдесят,90:девяносто,100:сотен";
        String[] engStr = s.split(",");
        HashMap<Integer, String> engMap = new HashMap<>();
        for(String str : engStr){
            engMap.put(Integer.valueOf(str.split(":")[0]), str.split(":")[1]);
        }
        String res = "";
        if(num > 99) {
            res += engMap.get(num/100) + " " + engMap.get(100) + " ";
            num = num % 100;
        }
        if(num > 9){
            res += engMap.get((num/10)*10) + " ";
            num = num % 10;
        }
        if(num > 0){
            res += engMap.get(num);
        }
        return res.trim();
    }

    public static String getSha256Hash(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(str.getBytes(StandardCharsets.UTF_8));
        String res = HexBin.encode(hash);
        return res;
    }

    public static String correctTitle(String str){
        String check = "and the of in";
        String res = "";
        String[] strArr = str.split(" ");
        for(int i = 0; i < strArr.length; i++){
            String substr = strArr[i];
            substr = substr.toLowerCase();
            if(!check.contains(substr)){
                substr = substr.substring(0,1).toUpperCase() + substr.substring(1, substr.length());
            }
            res += substr + " ";
        }
        return res.trim();
    }

    public static String hexLattice(int num){
        if((num - 1) % 6!= 0) return "Invalid";
        //Вырезаем сектор
        int subNum = (num-1)/6;
        //Номер треугольного числа
        int N = (int) ((Math.sqrt(8*subNum+1)-1)/2);
        //Максимальные размеры
        int maxLen = 1 + N*2;
        String str = "";
        for(int i = 0; i < maxLen; i++){
            int k = Math.abs(N - i) + 1;
            int j = maxLen - Math.abs(N - i);
            str += String.join("", Collections.nCopies(k, " "));
            str += String.join("", Collections.nCopies(j, "o "));
            str += String.join("", Collections.nCopies(k-1, " "));
            str += "\n";
        }

        return str;
    }

}
