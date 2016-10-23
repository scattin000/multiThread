package com.example.sarah.multithread;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    Button createButton;
    Button loadButton;
    Button clearButton;
    List<String> total;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createButton = (Button) findViewById(R.id.createBtn);
        loadButton = (Button) findViewById(R.id.loadBtn);
        clearButton = (Button) findViewById(R.id.clear);

        // set the listview to findby ID  of the list view
        lv = (ListView)findViewById(R.id.listvw);
        total = new ArrayList<String>();
        // setting the Array list on the list view (translates)
        final ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,total);
        // set list view to arrayAdapter
        lv.setAdapter(arrayAdapter);

        // deal with clicks here NOT xmL
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                // set up a background thread to manipulate the GUI
                thread = new Thread(new Runnable() {
                    public void run() {
                        createFile(v);
                    } // end run
                });
                thread.start();
                Toast.makeText(getBaseContext(), "File saved successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        // used final to access View
        loadButton.setOnClickListener(new View.OnClickListener() {
        // used final to access View
            public void onClick(final View v) {
                     thread = new Thread (new Runnable() {
                        public void run () {
                            ReadBtn(v);
                        }// end of run
                     }); // end thread
                     thread.start();
                    arrayAdapter.notifyDataSetChanged();
                }// end onClick
            });

        // clear the data once the clear button is selected
        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (arrayAdapter != null) {
                    arrayAdapter.clear();
                    //arrayAdapter.notifyDataSetChanged();
                } // end if
                arrayAdapter.notifyDataSetChanged();

            }// end onClick
        });
    }

    public void createFile(View v) {
        // create file in internal storage area
        String MY_FILE_NAME = "numbers.txt";
        // change this content to be the numbers from the counting class in the group assignment
        String content;
        // Create a new output file stream
        try {
            FileOutputStream fileos = openFileOutput(MY_FILE_NAME, Context.MODE_PRIVATE);

            for(int i= 0; i <= 10; i++) {
               // cast in as a string
                content = Integer.toString(i);
                // write the content to the file
                fileos.write(content.getBytes());
                // this creates a new line
                fileos.write(System.getProperty("line.separator").getBytes());
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // close the file
            fileos.close();

            //IOexception catches every thing!!!
        } catch (IOException e) {
            e.printStackTrace();
        }
    }// end createFile

    // Read text from file
    public void ReadBtn(View v) {
        //reading text from file
        try {
            // open the file
            FileInputStream fileIn = openFileInput("numbers.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            BufferedReader bufferedReader = new BufferedReader(InputRead);

            String line;
            // setting the line
            while ((line = bufferedReader.readLine()) !=null) {
                total.add(line);
                Thread.sleep(250);
            }
            // always need to close the read
            InputRead.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }// end ReadBtn
} // end of class
    //
//http://codetheory.in/android-saving-files-on-internal-and-external-storage/
    // reading and writing to a file
//http://www.androidinterview.com/android-internal-storage-read-and-write-text-file-example/