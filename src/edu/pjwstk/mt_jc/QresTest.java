package edu.pjwstk.mt_jc;

import edu.pjwstk.jps.result.ISingleResult;
import edu.pjwstk.mt_jc.interpreter.qres.QResStack;
import edu.pjwstk.mt_jc.result.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Małgorzata on 15.04.14.
 */
public class QresTest {

    public static void main(String[] args){

        SolveQRESQuery1();
    }


    //  (struct(1, 2+1), (bag("test", „Ala”) as nazwa));
    private static void SolveQRESQuery1(){
        QResStack qres = new QResStack();



        qres.push(new IntegerResult(2));
        qres.push(new IntegerResult(1));

        IntegerResult plusRight = (IntegerResult) qres.pop(); // 1
        IntegerResult plusLeft = (IntegerResult) qres.pop(); // 2
        IntegerResult plusRes = new IntegerResult(plusLeft.getValue() + plusRight.getValue());
        qres.push(new IntegerResult(1)); // 1
        qres.push(plusRes); // 2 + 1

        IntegerResult structRight = (IntegerResult) qres.pop(); // 3
        IntegerResult structLeft = (IntegerResult) qres.pop(); // 1
        List<ISingleResult> structList = new ArrayList<>();
        structList.add(structLeft);
        structList.add(structRight);
        StructResult structRes = new StructResult(structList);

        qres.push(structRes); // struct(1, 3)

        qres.push(new StringResult("test"));
        qres.push(new StringResult("Ala"));

        StringResult commaRight = (StringResult) qres.pop(); // "Ala"
        StringResult commaLeft = (StringResult) qres.pop(); // "test"
        List bagList = new ArrayList<>();

        bagList.add(commaLeft);
        bagList.add(commaRight);
        BagResult bagRes = new BagResult(bagList);

        qres.push(new StringResult("nazwa")); // "nazwa"
        qres.push(bagRes); // bag("test", „Ala”)
        BagResult rightAs = (BagResult) qres.pop();
        StringResult leftAs = (StringResult) qres.pop();
        bagList = new ArrayList<>();
        for (ISingleResult b: rightAs.getElements()) {
            bagList.add(new BinderResult(b, leftAs.getValue()));
        }
        BagResult asRes = new BagResult(bagList);
        qres.push(asRes);

        BagResult comma2Right = (BagResult) qres.pop();
        StructResult comma2Left = (StructResult) qres.pop();

        bagList = new ArrayList<>();


        for (ISingleResult b: comma2Right.getElements()) {
            structList = new ArrayList<>();
            structList.addAll(comma2Left.elements());
            structList.add(b);
            bagList.add(new StructResult(structList));
        }
        BagResult commaRes = new BagResult(bagList);
        qres.push(commaRes);


        Log("Result from QRESQuery1:  (struct(1, 2+1), (bag(\"test\", „Ala”) as nazwa)); ");
        Log(qres.pop());
    }

    public static void Log(Object o){
        System.out.println(o);
    }
}
