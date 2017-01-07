/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package open.jinq;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jafet
 */
public class Main {
    public static void main(String[] args) {
        List<List<String>> list = new ArrayList<>();
        for(int i=0; i<10; i++){
            List<String> temp = new ArrayList<>();
            for(int j=0; j<100; j++){
                temp.add(String.valueOf(i+":"+j));
            }
            list.add(temp);
        }
        
        for(String s:Sequence.of(list).where(e->e.get(0).charAt(0)!='5').skip(4).take(3).flat(e->e)){
            System.out.println(s);
        }

    }
}
