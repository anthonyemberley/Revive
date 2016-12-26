package com.pink2016.revive;

import com.pink2016.revive.Models.Arrhythmia;
import com.pink2016.revive.Models.ECGRecording;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by emil on e911contacted/04/16.
 */
public class ArrhythmiaPTApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initializes the database and registers Arrhythmia and ECGRecording as ParseObjects
        ParseObject.registerSubclass(Arrhythmia.class);
        ParseObject.registerSubclass(ECGRecording.class);
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "7wop8ncD1stVBTcbPaCgDa1ZRHeiwkZulGYuV0nE", "RY7qjP9NJDwV81dEh3ZHVrMfy3ZU7qP9fKJ9QZOQ");
    }
}
