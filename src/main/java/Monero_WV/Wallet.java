/* 
 * Copyright (C) 2018 R.Harkes & M.van Gorcum
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package Monero_WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import keccak.KeccakSponge;

public class Wallet {
    public boolean valid;
    public String errormessage;
    private final String StrValue;
    private static final String Alphabet = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    
    public Wallet(String str){
        StrValue = str;
        int error = Validate(StrValue);
        switch (error){
            case 0:valid = true;
            break;
            case 1:valid = false;
            errormessage = "incorrect length";
            break;
            case 2:valid = false;
            errormessage = "incorrect characters";
            break;
            case 3:valid = false;
            errormessage = "incorrect byte signature";
            break;
            case 4:valid = false;
            errormessage = "hash failed";
            break;               
        }
    }
    
    public String GetString(){
        return StrValue;
    }
    
    private static int Validate(String str){
        if (str.length()!=95){return 1;} //length
        Pattern p = Pattern.compile("[^"+Alphabet+"]");
        Matcher m = p.matcher(str);
        if (m.matches()){return 2;}
        byte[] BytValue = Wallet2Byte(str);
        if (BytValue[0]!=18){return 3;}
        byte[] toHash = new byte[65];
        System.arraycopy(BytValue, 0, toHash, 0, 65);
        byte[] HashRes = Hash(toHash);
        for (int i = 0;i<4;i++){
            if (HashRes[i] != BytValue[65+i]){return 4;}
        }
        return 0;
    }
    
    private static byte[] Hash(byte[] values){
        KeccakSponge spongeFunction = new KeccakSponge(1088, 512, "", 256);
        byte[] hashBytes = spongeFunction.apply(values);
        byte[] out = new byte[4];
        System.arraycopy(hashBytes, 0, out, 0, out.length);
        return out;
    }
    
    private static byte[] Wallet2Byte(String str){
        byte[] BytValue = new byte[69];
        long[] factors = new long[11]; //58**11, 58**11 etc.
        for (int i=0;i<factors.length;i++){
            factors[i] = pow(58,factors.length-i-1);
        }
        for(int i=0;i<8;i++){ //first eight blocks of 11 base 58 characters
            String substr = str.substring(i*11, (i+1)*11);
            long val = 0; //signed int64
            for (int j=0;j<11;j++){
                val += factors[j]*Alphabet.indexOf(substr.charAt(j));
            }
            byte[] valB = longToBytes(val);
            System.arraycopy(valB, 0, BytValue, i*8, 8);
        }
        //last block of 7 base 58 caracters
        String substr = str.substring(88, 95);
        long val = 0;
        for (int j = 0;j<7;j++){
            val += factors[j+4]*Alphabet.indexOf(substr.charAt(j));
        }
        byte[] valB = longToBytes(val);
        for (int j = 0;j<5;j++){
            BytValue[64+j]=valB[3+j];
        }
        return BytValue;
    }
    
    private static long pow (long a, int b)
    {
        if ( b == 0)        return 1;
        if ( b == 1)        return a;
        if ((b%2)==0)       return     pow ( a * a, b/2); //even a=(a^2)^b/2
        else                return a * pow ( a * a, b/2); //odd  a=a*(a^2)^b/2
    }
    
    private static byte[] longToBytes(long l) {
    byte[] result = new byte[8];
    for (int i = 7; i >= 0; i--) {
        result[i] = (byte)(l & 0xFF);
        l >>= 8;
    }
    return result;
}
}