package com.example.lab2;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author jamiekujawa
 *
 */
public class MainActivity extends Activity {

    /**
     * A string builder to represent the first number entered in the series of entries
     */
    private StringBuilder expression1;

    /**
     * A string builder to represent the second number entered in the series of entries
     */
    private StringBuilder expression2;

    /**
     * A string to represent the last operator performed
     */
    private char operator;

    /**
     * A boolean to track weather the expression has a decimal
     */
    private boolean hasDecimal;

    /**
     * An enum to track the state of the calculator
     */
    private enum State{ OPERATOR, EQUALS, CLEAR, FIRST, SECOND };

    /**
     * Enum to keep track of last button pressed
     */
    State lastPressed;



    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // declare all buttons used within the layout
        Button zero = (Button) findViewById(R.id.button0);
        Button one = (Button) findViewById(R.id.button1);
        Button two = (Button) findViewById(R.id.button2);
        Button three = (Button) findViewById(R.id.button3);
        Button four = (Button) findViewById(R.id.button4);
        Button five = (Button) findViewById(R.id.button5);
        Button six = (Button) findViewById(R.id.button6);
        Button seven = (Button) findViewById(R.id.button7);
        Button eight = (Button) findViewById(R.id.button8);
        Button nine = (Button) findViewById(R.id.button9);
        Button times = (Button) findViewById(R.id.buttontimes);
        Button clear = (Button) findViewById(R.id.buttonClear);
        Button equal = (Button) findViewById(R.id.buttonEqual);
        Button decimal = (Button) findViewById(R.id.buttonDecimal);
        Button divide = (Button) findViewById(R.id.buttondivide);
        Button add = (Button) findViewById(R.id.buttonplus);
        Button subtract = (Button) findViewById(R.id.buttonminus);

        hasDecimal = false;
        lastPressed = State.CLEAR;

        // declare main text view
        final TextView main = (TextView) findViewById(R.id.CalculatorText);

        // Main Strings to represent the expressions
        expression1 = new StringBuilder();
        expression2 = new StringBuilder();
        //main.setText(expression1.append("0.0"));

		/*
		 * Set up all key listener events
		 */
        zero.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                numberPress(0, main);
            }
        });

        one.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                numberPress(1, main);
            }
        });

        two.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                numberPress(2, main);
            }

        });

        three.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                numberPress(3, main);
            }

        });

        four.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                numberPress(4, main);
            }

        });

        five.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                numberPress(5, main);
            }

        });

        six.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                numberPress(6, main);
            }

        });

        seven.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                numberPress(7, main);
            }

        });

        eight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                numberPress(8, main);
            }

        });

        nine.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                numberPress(9, main);
            }

        });

        clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                hasDecimal = false;
                expression1.delete(0, expression1.length());
                expression2.delete(0, expression2.length());
                main.setText("");
                lastPressed = State.CLEAR;
            }

        });

        equal.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                evaluate(main);
            }

        });

        decimal.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!hasDecimal){
                    hasDecimal = true;
                    if(lastPressed == State.CLEAR || lastPressed == State.FIRST || lastPressed == State.EQUALS){
                        if((expression1.length() <= 10)){
                            expression1.append(".");
                            main.setText(expression1.toString());
                        }

                    }else{
                        if((expression1.length() <= 10)) {
                            expression2.append(".");
                            main.setText(expression2.toString());
                        }
                    }
                }
            }
        });

        divide.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                operatorPressed('/', main);
            }

        });

        times.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                operatorPressed('*', main);
            }

        });

        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                operatorPressed('+', main);
            }

        });

        subtract.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                operatorPressed('-', main);
            }

        });
    }

    /**
     * This method will evaluate an operation using expression1 and expression 2
     */
    public void evaluate(TextView view) {

        if(expression1.length() > 0 && expression2.length() > 0) {
            double val1 = Double.parseDouble(expression1.toString());
            double val2 = Double.parseDouble(expression2.toString());
            double result = 0.0;

            if (operator == '*') {
                result = val1 * val2;
            } else if (operator == '/') {
                result = val1 / val2;
            } else if (operator == '+') {
                result = val1 + val2;
            } else if (operator == '-') {
                result = val1 - val2;
            }

            expression1.delete(0, expression1.length());
            expression2.delete(0, expression2.length());

            view.setText(Double.toString(result));

            lastPressed = State.EQUALS;
        }
    }

    public void operatorPressed(char op, TextView view){
        operator = op;
        if(lastPressed == State.EQUALS)
            expression1.replace(0, expression1.length(), view.getText().toString());

        lastPressed = State.OPERATOR;

    }

    public void numberPress(int val, TextView view){
        if(lastPressed == State.CLEAR || lastPressed == State.FIRST || lastPressed == State.EQUALS){
            if((expression1.length() <= 10)){
                expression1.append(val);
                view.setText(expression1.toString());
                lastPressed = State.FIRST;
            }

        }else{
            if((expression2.length() <= 10)) {
                expression2.append(val);
                view.setText(expression2.toString());
                lastPressed = State.SECOND;
            }
        }
    }

}