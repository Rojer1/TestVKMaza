package com.example.makar.testvkmaza;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKApiGetMessagesResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VkAudioArray;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] scope = new String[]{VKScope.MESSAGES, VKScope.AUDIO, VKScope.FRIENDS, VKScope.WALL};
    private ListView listView;
    private Button showMessage;
    private Button showAudios;
    private String LOG_TAG = "mylogs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VKSdk.login(this, scope);


        showMessage = (Button) findViewById(R.id.showMessage);
        showAudios = (Button) findViewById(R.id.showAudios);
        listView = (ListView) findViewById(R.id.listView);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.showMessage:
                        Log.d(LOG_TAG, "Кнопка просмотра сообщений сработала");
                        VKRequest request = VKApi.messages().get(VKParameters.from(VKApiConst.COUNT, 12));
                        request.executeWithListener(new VKRequest.VKRequestListener() {
                            @Override
                            public void onComplete(VKResponse response) {
                                super.onComplete(response);

                                VKApiGetMessagesResponse vkApiGetMessagesResponse = (VKApiGetMessagesResponse) response.parsedModel;
                                VKList<VKApiMessage> listMsg = vkApiGetMessagesResponse.items;
                                ArrayList<String> lestBodyMessages = new ArrayList<>();

                                for (VKApiMessage message : listMsg) {
                                    lestBodyMessages.add(message.body);
                                }

                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this,
                                        android.R.layout.simple_list_item_1, lestBodyMessages);
                                listView.setAdapter(arrayAdapter);
                            }

                            @Override
                            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                super.attemptFailed(request, attemptNumber, totalAttempts);
                            }

                            @Override
                            public void onError(VKError error) {
                                super.onError(error);
                            }

                            @Override
                            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                                super.onProgress(progressType, bytesLoaded, bytesTotal);
                            }
                        });
                        break;
                    case R.id.showAudios:
                        Log.d(LOG_TAG, "Кнопка просмотра аудио сработала");
                        VKRequest request1 = VKApi.audio().get();
                        request1.executeWithListener(new VKRequest.VKRequestListener() {
                            @Override
                            public void onComplete(VKResponse response) {
                                super.onComplete(response);
                                final VkAudioArray array = (VkAudioArray) response.parsedModel;
                                ArrayList<String> listTrack = new ArrayList<String>();
                                final MediaPlayer mediaPlayer = new MediaPlayer();


                                for (VKApiAudio track : array) {
                                    listTrack.add(track.url);

                                }
                                Log.d(LOG_TAG, "Аудиофайлы добавлены");

                                ArrayAdapter<VKApiAudio> arrayAdapter = new ArrayAdapter<>(MainActivity.this,
                                        android.R.layout.simple_list_item_1, array);
                                listView.setAdapter(arrayAdapter);


                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
                                                + id);
                                        Log.d(LOG_TAG, String.valueOf(array.get(position).url));
                                        String url = String.valueOf(array.get(position).url); // your URL here

                                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                        try {
                                            if (mediaPlayer.isPlaying()) {
                                                mediaPlayer.reset();
                                            }
                                                mediaPlayer.setDataSource(url);
                                                mediaPlayer.prepare(); // might take long! (for buffering, etc)



                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        mediaPlayer.start();
                                    }
                                });

                            }

                            @Override
                            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                super.attemptFailed(request, attemptNumber, totalAttempts);
                            }

                            @Override
                            public void onError(VKError error) {
                                super.onError(error);
                            }

                            @Override
                            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                                super.onProgress(progressType, bytesLoaded, bytesTotal);
                            }
                        });
                        break;

                }

            }
        };
        showMessage.setOnClickListener(listener);
        showAudios.setOnClickListener(listener);



/*
        Блок для получения отпечатков сертификата
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        System.out.println(Arrays.asList(fingerprints));
*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*      Летающая кнопка
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(getApplicationContext(), "Good", Toast.LENGTH_LONG).show();

                VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name,bdate"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        VKList listFriends = (VKList) response.parsedModel;

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_list_item_1, listFriends);
                        listView.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                        super.attemptFailed(request, attemptNumber, totalAttempts);
                    }

                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                    }

                    @Override
                    public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                        super.onProgress(progressType, bytesLoaded, bytesTotal);
                    }
                });

                // User passed Authorization
            }

            @Override
            public void onError(VKError error) {
                // User didn't pass Authorization
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
