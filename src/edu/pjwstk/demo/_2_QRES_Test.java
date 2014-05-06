package edu.pjwstk.demo;

import edu.pjwstk.jps.result.ISingleResult;
import edu.pjwstk.demo.interpreter.qres.QResStack;
import edu.pjwstk.demo.result.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Małgorzata on 15.04.14.
 */
public class _2_QRES_Test {

    @Test
    public void _2_QRES_Test() throws Exception {
        main(null);
    }

    public static void main(String[] args){

        SolveQRESQuery1();

        Log("\n");

        SolveQRESQuery2();

        Log("\n");

        SolveQRESQuery3();
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

    //   (bag("ala"+" ma"+" kota"), bag(8*10, false));
    private static void SolveQRESQuery2(){

        QResStack qres = new QResStack();

        qres.push(new StringResult("ala")); // "ala"
        qres.push(new StringResult(" ma")); // " ma"
        qres.push(new StringResult(" kota")); // " kota"

        StringResult plusRight = (StringResult) qres.pop();
        StringResult plusLeft = (StringResult) qres.pop();

        StringResult plusRes = new StringResult(plusLeft.getValue() + plusRight.getValue());
        qres.push(plusRes); // " ma" + " kota"

        StringResult plus2Right = (StringResult) qres.pop();
        StringResult plus2Left = (StringResult) qres.pop();

        StringResult plus2Res = new StringResult(plus2Left.getValue() + plus2Right.getValue()); //"ala" + " ma" + " kota"
        List bagList = new ArrayList<>();
        bagList.add(plus2Res);
        qres.push(new BagResult(bagList)); // bag("ala" + " ma" + " kota")

        qres.push(new IntegerResult(8)); // 8
        qres.push(new IntegerResult(10)); // 10

        IntegerResult multiRight = (IntegerResult) qres.pop();
        IntegerResult multiLeft = (IntegerResult) qres.pop();

        IntegerResult multiRes = new IntegerResult(multiLeft.getValue() * multiRight.getValue());
        qres.push(multiRes);  // 8*10

        qres.push(new BooleanResult(true)); // true

        BooleanResult bagRight = (BooleanResult) qres.pop();
        IntegerResult bagLeft = (IntegerResult) qres.pop();

        List bag2List = new ArrayList<>();

        bag2List.add(bagLeft);
        bag2List.add(bagRight);

        BagResult bagRes = new BagResult(bag2List);

        qres.push(bagRes); // bag(8*10, false)

        BagResult commaRight = (BagResult) qres.pop();
        BagResult commaLeft = (BagResult) qres.pop();


        bagList = new ArrayList<>();
        for (ISingleResult br: commaRight.getElements()) {
            for (ISingleResult bl: commaLeft.getElements()) {
                List structList = new ArrayList<>();
                structList.add(bl);
                structList.add(br);
                bagList.add(new StructResult(structList));
            }
        }
        qres.push(new BagResult(bagList));

        Log("Result from QRESQuery2:  (bag(\"ala\"+\" ma\"+\" kota\"), bag(8*10, false)); ");
        Log(qres.pop());
    }

    //    ( (bag(1, 2) groupas x), bag(3, 4), 5);
    private static void SolveQRESQuery3(){

        QResStack qres = new QResStack();

        qres.push(new IntegerResult(1)); // 1
        qres.push(new IntegerResult(2)); // 2

        IntegerResult bagRight = (IntegerResult)qres.pop();
        IntegerResult bagLeft = (IntegerResult) qres.pop();
        List bagList = new ArrayList<>();
        bagList.add(bagLeft);
        bagList.add(bagRight);
        BagResult bagRes = new BagResult(bagList); // bag(1, 2)
        qres.push(bagRes);

        qres.push(new StringResult("x")); // "x"

        StringResult groupAsRight = (StringResult) qres.pop();
        BagResult groupAsLeft = (BagResult) qres.pop();

        BinderResult groupAsRes = new BinderResult(groupAsLeft, groupAsRight.getValue());
        qres.push(groupAsRes); // (bag(1, 2) groupas x)

        qres.push(new IntegerResult(3)); // 3
        qres.push(new IntegerResult(4)); // 4

        IntegerResult bag2Right = (IntegerResult) qres.pop();
        IntegerResult bag2Left = (IntegerResult) qres.pop();

        bagList = new ArrayList<>();
        bagList.add(bag2Left);
        bagList.add(bag2Right);

        BagResult bag2Res = new BagResult(bagList); // bag(3, 4)

        qres.push(bag2Res);

        qres.push(new IntegerResult(5)); // 5

        IntegerResult commaRight = (IntegerResult) qres.pop();
        BagResult commaLeft = (BagResult) qres.pop();


        bagList = new ArrayList<>();

        for (ISingleResult b : commaLeft.getElements()) {
            List structList = new ArrayList<>();
            structList.add(b);
            structList.add(commaRight);
            bagList.add(new StructResult(structList));
        }

        BagResult commaRes = new BagResult(bagList);
        qres.push(commaRes);

        BagResult comma2Right = (BagResult) qres.pop();
        BinderResult comma2Left = (BinderResult) qres.pop();

        bagList = new ArrayList<>();

        for (ISingleResult b: comma2Right.getElements()) {
            List structList = new ArrayList<>();
            structList.add(comma2Left);
            structList.add(b);
            bagList.add(new StructResult(structList));
        }

        BagResult bag3Res = new BagResult(bagList);
        qres.push(bag3Res);

        Log("Result from QRESQuery3:  ((bag(1, 2) groupas x), bag(3, 4), 5);");
        // bag(0=struct(binder(name="x",value="bag(0=1,1=2)"),struct(3,5)),1=struct(binder(name="x",value="bag(0=1,1=2)"),struct(4,5)))
        Log(qres.pop());
    }



    public static void Log(Object o){
        System.out.println(o);
    }
}
