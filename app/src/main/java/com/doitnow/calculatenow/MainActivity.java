package com.doitnow.calculatenow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView mathArea;
    private TextView resultArea;
    private Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b0,bDot,
            addBtn,subtractionBtn,multiplyBtn,divisionBtn,leftBrace,rightBrace,
            equalToBtn,clearBtn,allClearBtn;
    private boolean error;
    private String previousMathArea = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeFields();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert("1");
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert("2");
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert("3");
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert("4");
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert("5");
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert("6");
            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert("7");
            }
        });

        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert("8");
            }
        });

        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert("9");
            }
        });

        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert("0");
            }
        });

        bDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert(".");
            }
        });

        subtractionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert(" - ");

            }
        });

        multiplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert(" x ");
            }
        });

        divisionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert(" / ");
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert(" + ");
            }
        });

        leftBrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert(" ( ");
            }
        });

        rightBrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert(" ) ");
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuffer afterClear = new StringBuffer(mathArea.getText().toString());
                if(!TextUtils.isEmpty(afterClear)) {
                    if (afterClear.charAt(afterClear.length() - 1) == ' ') {
                        afterClear.delete(afterClear.length() - 3, afterClear.length());
                    } else {
                        afterClear.deleteCharAt(afterClear.length() - 1);
                    }
                    if (TextUtils.isEmpty(afterClear)) {
                        mathArea.setText("");
                    } else {
                        mathArea.setText(afterClear.toString());
                    }
                }

            }
        });

        allClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(mathArea.getText().toString())){
                    resultArea.setText("0.0");
                }
                mathArea.setText("");
                previousMathArea = "";
            }
        });

        equalToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!previousMathArea.equals(mathArea.getText().toString())) {
                    if (!TextUtils.isEmpty(mathArea.getText().toString())) {
                        previousMathArea = mathArea.getText().toString();
                        char[] preparedExpression = prepareExpression(mathArea.getText().toString());
                        Double finalResult = evaluate(preparedExpression);
                        if (!error) {
                            resultArea.setText(finalResult.toString());
                            error = false;
                        } else {
                            resultArea.setText("Invalid Input");
                        }
                    } else {
                        if (!(TextUtils.equals("Division by Zero", resultArea.getText().toString())
                                || TextUtils.equals("Invalid Input", resultArea.getText().toString()))) {
                            mathArea.setText(resultArea.getText().toString());
                        }
                    }
                }else {
                    mathArea.setText(resultArea.getText().toString());
                }

            }
        });


    }

    private char[] prepareExpression(String rawExpression) {
        char rawCharExp[] = rawExpression.toCharArray();

        //if an expression ended with any operator other than ")" then that operator was removed
        if(rawExpression.charAt(rawExpression.length()-1)==' '){

            if(rawExpression.length()>5){

                if(!(rawExpression.charAt(rawExpression.length()-2)==')')){
                    StringBuffer endCharExpression = new StringBuffer(rawExpression);
                    endCharExpression.delete(rawCharExp.length-2,rawCharExp.length);
                    rawExpression = endCharExpression.toString();
                    rawCharExp = rawExpression.toCharArray();
                }

            }
        }
        int size = rawCharExp.length;
        char preCharExp[] = new char[size+size];

        //if there was "(" after number then a multiplication(*) operator was added in between number and "(".
        //to resolve expressions like 2(3+1) to 2*(3+1)
        for (int i = 0,k = 0; i<rawCharExp.length;i++,k++){
            if(rawCharExp[i] == '(' && i>1 ){

                if(rawCharExp[i-2]>='0' && rawCharExp[i-2]<='9' || rawCharExp[i-3] == ')'){
                    preCharExp[k++] = 'x';
                    preCharExp[k++] =' ';
                    preCharExp[k++] = rawCharExp[i];
                    if(rawCharExp[i+3]=='-'){
                        preCharExp[k++] = ' ';
                        preCharExp[k++] = '0';
                    }
                }
                else if (rawCharExp[i-3] == 'x' || rawCharExp[i-3] == '+'
                        || rawCharExp[i-3] == '-' || rawCharExp[i-3] == '/'){
                    preCharExp[k++] = rawCharExp[i];
                    if(rawCharExp[i+3]=='-'){
                        preCharExp[k++] = ' ';
                        preCharExp[k++] = '0';
                    }
                }

            }
            //to resolve expression like 2(3)6 to 2(3)*6
            else if(rawCharExp[i] == ')' && i+2<size){
               if(rawCharExp[i+2]>='0' && rawCharExp[i+2]<='9'){
                   preCharExp[k++] = rawCharExp[i];
                   preCharExp[k++] = rawCharExp[++i];
                   preCharExp[k++] = ' ';
                   preCharExp[k++] = 'x';
                   preCharExp[k++] = ' ';
               }else {
                   preCharExp[k++] = rawCharExp[i];
               }
            }
            else {
                preCharExp[k] = rawCharExp[i];
            }
        }


        return preCharExp;
    }


    private void initializeFields() {

        mathArea = (TextView) findViewById(R.id.math_area);
        resultArea = (TextView) findViewById(R.id.result_area);

        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        b5 = (Button) findViewById(R.id.button5);
        b6 = (Button) findViewById(R.id.button6);
        b7 = (Button) findViewById(R.id.button7);
        b8 = (Button) findViewById(R.id.button8);
        b9 = (Button) findViewById(R.id.button9);
        b0 = (Button) findViewById(R.id.button0);
        bDot = (Button) findViewById(R.id.button_dot);

        addBtn = (Button) findViewById(R.id.add_btn);
        multiplyBtn = (Button) findViewById(R.id.multiply_btn);
        subtractionBtn = (Button) findViewById(R.id.subtraction_btn);
        divisionBtn = (Button) findViewById(R.id.division_btn);
        leftBrace = (Button) findViewById(R.id.left_brace);
        rightBrace = (Button) findViewById(R.id.right_brace);

        equalToBtn = (Button) findViewById(R.id.equal_to_btn);
        clearBtn = (Button) findViewById(R.id.clear);
        allClearBtn = (Button) findViewById(R.id.all_clear);


    }

    public void insert(String s1){
        if(mathArea.getText().toString().equals("Division by Zero")){
            mathArea.setText("");
        }
        mathArea.append(s1);

        //to not allow operator at the start
        if(mathArea.getText().charAt(0)==' '){
            if(!(mathArea.getText().charAt(1)=='(')){
                mathArea.setText("");
            }
        }

        //to not allow two operators side by side
        if(mathArea.length()>5){
        char last = mathArea.getText().toString().charAt(mathArea.getText().length()-2);
        char lastBtOne = mathArea.getText().toString().charAt(mathArea.getText().length()-5);
        if((last=='+'||last=='-'||last=='/'||last=='x')&& (lastBtOne=='+'||lastBtOne=='-'||lastBtOne=='/'||lastBtOne=='x')){
            StringBuffer newExpression = new StringBuffer(mathArea.getText().toString());
            newExpression.delete(mathArea.getText().length()-6,mathArea.getText().length()-1);
            mathArea.setText(newExpression.toString());
            mathArea.append(last+" ");

        }
        }

    }


    public Double evaluate(char[] expression) {

        try {
            error = false;
        if (expression.length > 0) {

            char[] tokens = expression;

            // Stack for numbers: 'values'
            Stack<Double> values = new Stack<Double>();

            // Stack for Operators: 'ops'
            Stack<Character> ops = new Stack<Character>();

            for (int i = 0; i < tokens.length; i++) {
                // Current token is a whitespace, skip it
                if (tokens[i] == ' ')
                    continue;

                // Current token is a number, push it to stack for numbers
                if (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.') {
                    StringBuffer sbuf = new StringBuffer();
                    // There may be more than one digits in number
                    while ((i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') || (i < tokens.length && tokens[i] == '.'))
                        sbuf.append(tokens[i++]);
                    values.push(Double.parseDouble(sbuf.toString()));
                }

                // Current token is an opening brace, push it to 'ops'
                else if (tokens[i] == '(')
                    ops.push(tokens[i]);

                    // Closing brace encountered, solve entire brace
                else if (tokens[i] == ')') {
                    while (ops.peek() != '(') {
                        if (!ops.empty()) {
                            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                        } else {
                            break;
                        }

                    }

                    ops.pop();
                }

                // Current token is an operator.
                else if (tokens[i] == '+' || tokens[i] == '-' ||
                        tokens[i] == 'x' || tokens[i] == '/') {
                    // While top of 'ops' has same or greater precedence to current
                    // token, which is an operator. Apply operator on top of 'ops'
                    // to top two elements in values stack
                    while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));

                    // Push current token to 'ops'.
                    ops.push(tokens[i]);
                }
            }

            // Entire expression has been parsed at this point, apply remaining
            // ops to remaining values
            while (!ops.empty())
                values.push(applyOp(ops.pop(), values.pop(), values.pop()));

            // Top of 'values' contains result, return it
            if (values.empty()) {
                return 0.0;
            }
            return round(values.pop(),2);

        }

    }catch (Exception e){
            error = true;
        }

        return 0.0;
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    // Returns true if 'op2' has higher or same precedence as 'op1',
    // otherwise returns false.
    public boolean hasPrecedence(char op1, char op2)
    {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == 'x' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }

    // A utility method to apply an operator 'op' on operands 'a'
    // and 'b'. Return the result.
    public Double applyOp(char op, Double b, Double a)
    {
        switch (op)
        {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case 'x':
                return a * b;
            case '/':
                if (b == 0)
                    mathArea.setText("Division by Zero");
                return a / b;
        }
        return 0.0;
    }


}
