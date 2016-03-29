package com.ex.ak.barleybreak;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView text2;
    private TextView text3;
    private TextView text4;

    private final static int COLOR_RED = 2000;
    private final static int COLOR_GREEN = 2005;
    private final static int COLOR_BLUE = 2010;

    private final static String KEY_BARLEY  = "keyBarley";
    private final static String KEY_MOVES   = "keyMoves";
    private final static String KEY_COLOR   = "keyColor";

    private int movesCount  = 0;                                //  Counter of Moves in Game
    private int color; // red = 1 ; green = 2 ; blue = 3;       //  Number of Color

    private int []      arrValues   = new int[16];              // Array of Buttons Values
    private Button []   arrButtons  = new Button [16];          // Array of Vidgets Buttons
    private int []      arrId       = {R.id.b1,R.id.b2,R.id.b3,R.id.b4,R.id.b5,R.id.b6,R.id.b7,R.id.b8,
                                       R.id.b9,R.id.b10,R.id.b11,R.id.b12,R.id.b13,R.id.b14,R.id.b15,R.id.b16};


    /**
     *  Save our Values to Bundle
     */
    @Override
    public void onSaveInstanceState (Bundle B)
    {
        super.onSaveInstanceState(B);

        B.putIntArray(MainActivity.KEY_BARLEY, this.arrValues);
        B.putInt(KEY_MOVES,this.movesCount);
        B.putInt(KEY_COLOR,this.color);

        String textInfo = this.text4.getText().toString();
        B.putString("TextVievColorInfo", textInfo);

        int c = this.text4.getCurrentTextColor();
        B.putInt("Color",c);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         *  Initialization of Vidgets
         */
        for (int i = 0; i < 16; i++)
        {
            arrButtons[i] = (Button) this.findViewById(arrId[i]);
        }

        this.text2  = (TextView)this.findViewById(R.id.tv2);
        this.text3  = (TextView)this.findViewById(R.id.tv3);    // TextView3 have Context Menu for Color Selection
        this.registerForContextMenu(text3);
        this.text4  = (TextView)this.findViewById(R.id.tv4);

        /**
         *   Restoring from Bundle
         *   if Bundle == null  Starting New Game
         */
        if(savedInstanceState != null)
        {
            this.arrValues      = savedInstanceState.getIntArray(MainActivity.KEY_BARLEY);
            this.movesCount     = savedInstanceState.getInt(MainActivity.KEY_MOVES);
            this.color          = savedInstanceState.getInt(MainActivity.KEY_COLOR);
            this.text4.setText(savedInstanceState.getString("TextViewColorInfo"));
            this.text4.setTextColor(savedInstanceState.getInt("Color"));

            MainActivity.this.setValues();
        }
        else
        {
            MainActivity.this.newGame();
        }
    }


    /**
     *   Start New Game
     */
    public void newGame()
    {
        this.movesCount = 0; // === Set to 0 counter of moves in Game

        /**
         *  Fill array Random Values
         */
        boolean isRepeat = false;
        int a = 0;

        while (a != 16)
        {
            int rnd = (int) ((Math.random() * 16) + 1);

            for (int i = 0; i < a; i++)
            {
                if (this.arrValues[i] == rnd)
                {
                    isRepeat = true;
                }
            }

            if (isRepeat)
            {
                isRepeat = false;
            }
            else
            {
                this.arrValues[a] = rnd;
                a++;
            }
        }
        MainActivity.this.setValues();
    }


    /**
     *   Set Values && Colors to Buttons
     */
    public void setValues()
    {
        for (int i = 0; i < this.arrId.length; i++)
        {
            if(this.arrValues[i] != 16)
            {
                this.arrButtons[i].setText(String.valueOf(this.arrValues[i]));
                /**
                 * From 1 to 15 buttons set Color & Values
                 */
                switch(this.color)
                {
                    case 1:
                        this.arrButtons[i].setBackgroundColor(Color.rgb(247, 109, 109));  // red
                        break;
                    case 2:
                        this.arrButtons[i].setBackgroundColor(Color.rgb(146, 204, 0));    // green
                        break;
                    case 3:
                    default:
                        this.arrButtons[i].setBackgroundColor(Color.rgb(87, 167, 253));   // blue
                        break;
                }
            }
            else
            {
                /**
                 *  To 16 button set Inner Color & No Value
                 */
                this.arrButtons[i].setBackgroundColor(Color.rgb(185,185,185));
                this.arrButtons[i].setText("");
            }
        }

        /**
         *  Set to Counter Of Moves Value
         */
        this.text2.setText(String.valueOf(this.movesCount));
    }


    /**
     *   Checking Game For Win
     *   comparing our array of Buttons Values (arrValues) to the reference array
     */
    public boolean chkWin()
    {
        int cnt = 0;
        boolean isWin = false;

        do
        {
            for(int i = 1; i < 16; i++)
            {
                if (arrValues[cnt] < arrValues[i])
                {
                    cnt++;
                }
            }
            if(cnt == 15)
            {
                isWin = true;
            }
        }
        while(cnt == 14);

        return isWin;
    }


    /**
     *  Moves of buttons
     */
    @Override
    public void onClick(View v)
    {
        int empty = 0;
        int touch = 0;
        for (int i = 0; i < 16; i++)
        {
            if(this.arrValues[i] == 16)
            {
                empty = i;
            }

            if(this.arrButtons[i].getId() == v.getId())
            {
                touch = i;
            }
        }

        boolean checkMove = false;

        for (int i = 0; i < 13; i+=4)
        {
            for (int j = 0; j < 4 ; j++)
            {
                if (checkMove)
                {
                    break;
                }
                else
                if( (i+j) == touch)
                {
                    if( ((i+j)- 1) == empty && ((i+j)- 1) >= (i))
                    {
                        checkMove = true;
                        break;
                    }
                    if( ((i+j)+ 1) == empty && ((i+j)+ 1) <= (i+3))
                    {
                        checkMove = true;
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 16; j += 4)
            {
                if (checkMove)
                {
                    break;
                }
                else
                if( (i+j) == touch)
                {
                    if( ((i+j)- 4) == empty && ((i+j)- 4) >= (0))
                    {
                        checkMove = true;
                        break;
                    }
                    if( ((i+j)+ 4) == empty && ((i+j)+ 4) <= (15) )
                    {
                        checkMove = true;
                        break;
                    }
                }
            }
        }

        /**
         *  Checking for opportunity to move
         */
        if (checkMove)
        {
            if ((int)Math.abs(empty - touch) < 4)
            {
                if(empty < touch)
                {
                    int tmp = arrValues[empty + 1];
                    arrValues [empty + 1] = arrValues[empty];
                    arrValues [empty] = tmp;
                    empty += 1;

                }
                else
                {
                    int tmp = arrValues[empty - 1];
                    arrValues [empty - 1] = arrValues[empty];
                    arrValues [empty] = tmp;
                    empty -= 1;
                }
            }
            else
            {
                if(empty < touch)
                {
                    int tmp = arrValues[empty + 4];
                    arrValues [empty + 4] = arrValues[empty];
                    arrValues [empty] = tmp;
                    empty += 4;
                }
                else
                {
                    int tmp = arrValues[empty - 4];
                    arrValues [empty - 4] = arrValues[empty];
                    arrValues [empty] = tmp;
                    empty -= 4;
                }
            }

            this.movesCount ++;
            this.text2.setText(String.valueOf(this.movesCount));
        }
        else
        {
            Toast.makeText(this, "Impossible Move!", Toast.LENGTH_SHORT).show();
        }

        this.setValues();

        /**
         *  Message When End of Game
         */
        if (this.chkWin())
        {
            Toast.makeText(this, "You Win!!!", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "You result is : " + movesCount, Toast.LENGTH_LONG).show();
            this.newGame();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     *  Menu Items
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.action_startNewBarley)
        {
            MainActivity.this.newGame();
            return true;
        }
        if(id == R.id.action_exitBarley)
        {
            System.exit(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     *  Context Menu Calling by Long Tap on text "color"
     *  It is intended to change Color of Buttons
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        menu.add(Menu.NONE,COLOR_RED,10,"Red");
        menu.add(Menu.NONE,COLOR_GREEN,20,"Green");
        menu.add(Menu.NONE,COLOR_BLUE,30,"Blue");
    }


    /**
     *  Context Menu Items
     */
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        TextView tv4 = (TextView)this.findViewById(R.id.tv4);

        switch (item.getItemId())
        {
            case MainActivity.COLOR_RED:
                tv4.setText("Red");
                tv4.setTextColor(Color.rgb(255, 0, 0));
                this.color = 1;
                this.setValues();
                break;
            case MainActivity.COLOR_GREEN:
                tv4.setText("Green");
                tv4.setTextColor(Color.rgb(0, 255, 0));
                this.color = 2;
                this.setValues();
                break;
            case MainActivity.COLOR_BLUE:
                tv4.setText("Blue");
                tv4.setTextColor(Color.rgb(0, 0, 255));
                this.color = 3;
                this.setValues();
                break;
        }
        return true;
    }
}
