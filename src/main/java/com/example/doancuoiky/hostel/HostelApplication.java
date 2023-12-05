package com.example.doancuoiky.hostel;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class HostelApplication {
	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException{
		GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(
				"hostelappdoancuoinam-firebase-adminsdk-okx2g-5b720483cb.json").getInputStream());
		FirebaseOptions firebaseOptions = FirebaseOptions.builder().setCredentials(googleCredentials).build();
		FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions,"HostelAppDoanCuoiNam");
		return FirebaseMessaging.getInstance(app);
	}

	public static void main(String[] args) {
		SpringApplication.run(HostelApplication.class, args);
	}

}
