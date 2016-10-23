package com.example.sarah.multithread;

import android.app.ProgressDialog;
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
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    String MY_FILE_NAME;

    Button createButton;
    Button loadButton;
    Button clearButton;
    Button progressBtn;
    ProgressDialog progressBar;
    private int progressBarStat = 0;
    private Handler progressBarHandler = new Handler();

    List<String> total;
    Thread thread1;
    Thread thread2;

    public int filesize = 0;

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

                // progress bar dialog set up
                progressBar = new ProgressDialog(v.getContext());
                progressBar.setCancelable(true);
                progressBar.setMessage("File Currently Loading.");
                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();

                // reset the status
                progressBarStat = 0;
                // reset the file
                filesize = 0;

                // set up a background thread to manipulate the GUI
                thread1 = new Thread(new Runnable() {
                    public void run() {
                        while (progressBarStat < 100) {
                            // process the tasks
                            progressBarStat = createFile(v);

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            progressBarHandler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressBarStat);
                                } // end run
                            });
                        } // end while
                        if (progressBarStat >= 100) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            // no more progress bar dialog!
                            progressBar.dismiss();
                        } // end if
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                arrayAdapter.notifyDataSetChanged();
                            }
                        });
                    } // end run
                });
                thread1.start();
                Toast.makeText(getBaseContext(), "File saved successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        // used final to access View
        loadButton.setOnClickListener(new View.OnClickListener() {
        // used final to access View
            public void onClick(final View v) {

                progressBar = new ProgressDialog(v.getContext());
                progressBar.setCancelable(true);
                progressBar.setMessage("File currently downloading.");
                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();

                // reset again
                progressBarStat = 0;
                filesize = 0;

                     thread2 = new Thread (new Runnable() {
                        public void run () {
                            while (progressBarStat < 100) {
                                progressBarStat =  ReadBtn(v);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                progressBarHandler.post(new Runnable() {
                                    public void run() {
                                        progressBar.setProgress(progressBarStat);
                                    }
                                });
                            }// end while

                            if (progressBarStat >= 100) {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                progressBar.dismiss();
                            } // end if
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            });
                        }// end of run
                     }); // end thread
                     thread2.start();
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

    public int createFile(View v) {
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
                progressBar.setProgress(filesize*10);
                filesize++;
            }
            // close the file
            fileos.close();

            //IOexception catches every thing!!!
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 100;
    }// end createFile

    // Read text from file
    public int ReadBtn(View v) {
        String total;
        //reading text from file
        try {
            // open the file
            FileInputStream fileIn = openFileInput(MY_FILE_NAME);
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            BufferedReader bufferedReader = new BufferedReader(InputRead);

            String line;
            // setting the line
            while ((line = bufferedReader.readLine()) !=null) {
                total.add(line);
                System.out.println(filesize);
                Thread.sleep(250);
            }
            progressBar.setProgress(filesize*10);
            filesize++;
            // always need to close the read
            InputRead.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 100;
    }// end ReadBtn
} // end of class
    //
//http://codetheory.in/android-saving-files-on-internal-and-external-storage/
    // reading and writing to a file
//http://www.androidinterview.com/android-internal-storage-read-and-write-text-file-example/