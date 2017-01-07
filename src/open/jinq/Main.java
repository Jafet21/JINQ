/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package open.jinq;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author jafet
 */
public class Main {

    public static void main(String[] args) {
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < 1500; i++) {
            List<String> temp = new ArrayList<>();
            for (int j = 0; j < 10000; j++) {
                temp.add(String.valueOf(i + ":" + j));
            }
            list.add(temp);
        }
        long time = 0;
        for (int i = 0; i < 100; i++) {
            time = System.nanoTime();
            Sequence.of(list).where(e -> e.get(0).charAt(0) != '5').skip(4).take(3).flat(e -> e).toList();
            System.out.println("a:" + (System.nanoTime() - time));

            time = System.nanoTime();
            list.stream().filter(e -> e.get(0).charAt(0) != '5').skip(4).limit(3).flatMap(e -> e.stream()).collect(Collectors.toList());
            System.out.println("b: " + (System.nanoTime() - time));

        }
    }
}
