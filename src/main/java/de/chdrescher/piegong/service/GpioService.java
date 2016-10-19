package de.chdrescher.piegong.service;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.pi4j.io.gpio.*;
import de.chdrescher.piegong.model.AudioModel;
import de.chdrescher.piegong.model.RingEvent;
import de.chdrescher.piegong.repo.AudioModelRepository;
import de.chdrescher.piegong.repo.RingEventRepository;
import org.apache.log4j.Logger;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class GpioService {
			
	private static final int NO_AUDIO_FOUND = 0;

    private Logger log = Logger.getLogger(GpioService.class);

	private boolean isReady = true;


    @Autowired
    private AudioService audioService;

    @Autowired
    private RingEventRepository ringEventRepository;

    @Autowired
    private AudioModelRepository audioModelRepository;

    @Value("${spring.active.profile:}")
    private String activeProfile;

    private GpioPinDigitalOutput myLed;

    private GpioController gpio;


    @PostConstruct
    public void init(){
        if (activeProfile == null || !activeProfile.equals("nogpio")){
            initGPIO();
        }
    }

    @PreDestroy
    public void cleanup(){
        gpio.shutdown();
    }


	public void initGPIO(){
			
		log.info("init gpio listener");
        
        // create gpio controller
        gpio = GpioFactory.getInstance();

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN);


        myLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04,   // PIN NUMBER
                "My LED",           // PIN FRIENDLY NAME (optional)
                PinState.HIGH);      // PIN STARTUP STATE (optional)
        myLed.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        myButton.setDebounce(2000);

        // create and register gpio pin listener
        myButton.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                log.info(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                
                if (event.getState().equals(PinState.HIGH)){
                	
                	if (isReady){
                		isReady = false;
                		
                		int playAudioModelId = getAudioModelIdToPlay();
                        storeRingEvent(new Date(), playAudioModelId);

                        if (playAudioModelId == NO_AUDIO_FOUND){
                            try {
                                audioService.playFallback();
                            } catch (URISyntaxException e) {
                                log.error("failed to play fallback", e);
                            }
                        } else {
                            audioService.playMp3(playAudioModelId);
                        }

                    	isReady = true;
                	}
                }
            }
            
        });
        
        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller
	}

    private void storeRingEvent(Date date, int playAudioModelId) {
        ringEventRepository.save(new RingEvent(date, playAudioModelId));
    }

    protected int getAudioModelIdToPlay() {
        List<AudioModel> activeAudioModels = audioModelRepository.findByActive(true);

        if (activeAudioModels.isEmpty()){
            return NO_AUDIO_FOUND;
        }
        if (activeAudioModels.size() == 1){
            return activeAudioModels.get(0).getId();
        }

        List<RingEvent> lastRingEvents = ringEventRepository.findAllByOrderByRingDateDesc();
        if (lastRingEvents.isEmpty()){
            return activeAudioModels.get(0).getId();
        }

        int lastAudioModelIdPlayed = lastRingEvents.get(0).getAudioModelId();
        if (lastAudioModelIdPlayed == NO_AUDIO_FOUND){
            return activeAudioModels.get(0).getId();
        }

        for (int i=0; i< activeAudioModels.size(); i++){
            if (activeAudioModels.get(i).getId() == lastAudioModelIdPlayed){
                if (i == activeAudioModels.size() -1 ){
                    return activeAudioModels.get(0).getId();
                } else {
                    return activeAudioModels.get(i+1).getId();
                }
            }
        }

        return NO_AUDIO_FOUND;
    }


}
