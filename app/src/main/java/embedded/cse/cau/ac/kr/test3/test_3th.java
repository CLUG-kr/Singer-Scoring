package embedded.cse.cau.ac.kr.test3;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.TimerTask;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.PipedAudioStream;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class test_3th extends AppCompatActivity {
    long initTime = System.currentTimeMillis();
    String file_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final shapeview temp = new shapeview(this);

        //file_name = Environment.getDataDirectory().getAbsolutePath() + "/test.txt";
        file_name = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/data.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)));
            String line = null;
            int note_size = 0;

            while((line = reader.readLine()) != null){
                String[] parser = line.split(",");
                Point backup_ponint;
                backup_ponint = new Point(temp.width + note_size, Integer.parseInt(parser[0]));
                note_size += Integer.parseInt(parser[1]);
                temp.length.add(Integer.parseInt(parser[1]));
                temp.pitch.add(backup_ponint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult res, AudioEvent e){
                final float pitchInHz = res.getPitch();

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // 헤르츠 무시
                        if(pitchInHz <80 && pitchInHz >2000){
                            temp.hz = 0;
                        }
                        else {
                            temp.hz = pitchInHz;
                        }
                        long time_temp = (System.currentTimeMillis() - initTime);

                        if(1065 - ((int)time_temp/10) <= 30){
                            initTime = System.currentTimeMillis();
                            temp.time=0;
                        }
                        else {
                            temp.time = ((int) time_temp) / 10;  // 흐르는 속도
                        }

                        setContentView(temp);
                    }
                });
            }
        };

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);
        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);

        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();

    }
}
