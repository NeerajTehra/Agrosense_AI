package com.example.agrosense.utils;

import android.content.Context;

import com.example.agrosense.data.database.AgroSenseDatabase;
import com.example.agrosense.data.entity.Disease;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DemoDataSeeder {

    public static void seedDiseases(Context context) {
        AgroSenseDatabase db = AgroSenseDatabase.getInstance(context);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        executor.execute(() -> {
            if (db.diseaseDao().getAllDiseases().isEmpty()) {
                addDisease(db, "Late Blight", "Fungal disease", "Dark water-soaked spots on leaves", "Apply fungicide, improve drainage", "Cool, wet conditions", "Potato, Tomato");
                addDisease(db, "Early Blight", "Fungal disease", "Dark circular lesions with rings", "Remove affected leaves, use fungicide", "High humidity, moderate temp", "Tomato, Potato");
                addDisease(db, "Septoria Leaf Spot", "Fungal disease", "Small circular spots with gray centers", "Avoid overhead watering, apply copper-based spray", "Warm, wet weather", "Tomato");
                addDisease(db, "Bacterial Spot", "Bacterial disease", "Small, dark, water-soaked spots", "Use certified disease-free seeds", "Splashing rain, high humidity", "Tomato, Pepper");
                addDisease(db, "Target Spot", "Fungal disease", "Concentric rings resembling a target", "Improve air circulation", "Long periods of leaf wetness", "Tomato, Cucumber");
                addDisease(db, "Yellow Leaf Curl Virus", "Viral disease", "Stunted growth, yellowing, upward curling", "Control whiteflies, remove infected plants", "Whitefly transmission", "Tomato");
            }
        });
    }

    private static void addDisease(AgroSenseDatabase db, String name, String desc, String symptoms, String treatment, String cause, String crops) {
        Disease d = new Disease();
        d.name = name;
        d.description = desc;
        d.symptoms = symptoms;
        d.treatmentRecommendation = treatment;
        d.causativeAgent = cause; // Using this as 'Cause' for demo
        d.prevention = "Maintain field hygiene and use resistant varieties.";
        d.cropApplicability = crops;
        db.diseaseDao().insert(d);
    }
}
