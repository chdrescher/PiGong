package de.chdrescher.piegong.controller;

import de.chdrescher.piegong.model.AudioModel;
import de.chdrescher.piegong.repo.AudioModelRepository;
import de.chdrescher.piegong.service.AudioService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.RequestWrapper;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping("/audio")
public class AudioModelController {

    @Autowired
    AudioModelRepository audioModelRepository;

    @Value("${mp3filesbasepath}")
    private String mp3filesbasepath;

    @Autowired
    AudioService audioService;

    private static Logger log = Logger.getLogger(AudioModelController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity findAll(){
        List<AudioModel> audioModelList = audioModelRepository.findAll();
        return new ResponseEntity(audioModelList, HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public ResponseEntity deleteAudio(@PathVariable("id") int id){
        AudioModel audioModel = audioModelRepository.findOne(id);
        if (audioModel == null){
            return ResponseEntity.notFound().build();
        }

        audioModelRepository.delete(audioModel);
        return ResponseEntity.ok().build();
    }


    @RequestMapping(value="/activate/{id}", method= RequestMethod.PUT)
    public ResponseEntity activateModel(@PathVariable("id") int id){
        AudioModel audioModel = audioModelRepository.findOne(id);
        if (audioModel == null){
            return ResponseEntity.notFound().build();
        }

        audioModel.setActive(true);
        audioModelRepository.save(audioModel);

        return ResponseEntity.ok().build();
    }


    @RequestMapping(value="/deactivate/{id}", method= RequestMethod.PUT)
    public ResponseEntity deactivateModel(@PathVariable("id") int id){
        AudioModel audioModel = audioModelRepository.findOne(id);
        if (audioModel == null){
            return ResponseEntity.notFound().build();
        }
        audioModel.setActive(false);
        audioModelRepository.save(audioModel);

        return ResponseEntity.ok().build();
    }


    @RequestMapping(value="/", method=RequestMethod.POST, produces = "text/plain")
    public ResponseEntity handleFileUpload(@RequestParam("name") String name,
                                                 @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {

            AudioModel audioModel = audioModelRepository.findByName(name);
            if (audioModel != null){
                return new ResponseEntity("audiofile with given name alredy exists", HttpStatus.BAD_REQUEST);
            }

            try {
                byte[] bytes = file.getBytes();
                File fileOut = new File(mp3filesbasepath + file.getOriginalFilename());

                if (!fileOut.exists()){
                    fileOut.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(fileOut);
                fos.write(bytes, 0, bytes.length);
                fos.flush();
                fos.close();


                audioModelRepository.save(new AudioModel(name, file.getOriginalFilename(), false));

                return ResponseEntity.ok("You successfully uploaded " + name + "!");
            } catch (Exception e) {
                log.error("handle upload failed", e);
                return new ResponseEntity("You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity("You failed to upload " + name + " because the file was empty.", HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value="/play/{id}", method = RequestMethod.GET)
    public ResponseEntity play(@PathVariable("id") int id){
        audioService.playMp3(id);
        return ResponseEntity.ok().build();
    }

}
