package de.chdrescher.piegong.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import de.chdrescher.piegong.model.AudioModel;
import de.chdrescher.piegong.repo.AudioModelRepository;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class AudioService {

	private static Logger log = Logger.getLogger(AudioService.class);
		
	private static Player player = null;

    @Value("${mp3filesbasepath}")
    private String mp3filesbasepath;

    @Autowired
    private AudioModelRepository audioModelRepository;

	public void playMp3(int audioModelId){
        AudioModel audioModel = audioModelRepository.findOne(audioModelId);
        play(mp3filesbasepath  + audioModel.getFilename());
	}

    private void play(String filepath){
        try {
            InputStream is = new FileInputStream(filepath);
            play(is, filepath);

        } catch (Exception e) {
            log.error("Exeption while reading mp3 file from storage", e);
        }

    }

    public void playFallback() throws URISyntaxException {
        play(this.getClass().getClassLoader().getResourceAsStream("fallback.mp3"), "fallback.mp3");
    }

    private void play(InputStream is, String name) {
        try {
            BufferedInputStream bis = new BufferedInputStream(is);
            player = new Player(bis);

        } catch (Exception e) {
            log.error("Exeption while reading mp3 file from storage", e);
        }

        try {
            player.play();
            log.info("play file " + name);
            while(!player.isComplete()){
                Thread.sleep(500);
            }
        } catch (JavaLayerException e) {
            log.error("Exeption while playing mp3 file", e);
        } catch (InterruptedException e) {
            log.error("Thread interrupted while waiting for player to end", e);
        }
    }

}
