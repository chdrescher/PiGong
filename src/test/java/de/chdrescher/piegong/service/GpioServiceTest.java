package de.chdrescher.piegong.service;

import de.chdrescher.piegong.model.AudioModel;
import de.chdrescher.piegong.model.RingEvent;
import de.chdrescher.piegong.repo.AudioModelRepository;
import de.chdrescher.piegong.repo.RingEventRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GpioServiceTest {


    @Mock
    private AudioService audioService;

    @Mock
    private RingEventRepository ringEventRepository;

    @Mock
    private AudioModelRepository audioModelRepository;

    @InjectMocks
    private GpioService gpioService;

    @Test
    public void shouldGetNextAudioId(){
        List<AudioModel> activeAudioModels = new ArrayList<>();
        AudioModel audioModel1 = new AudioModel("model1", "1.mp3", true);
        AudioModel audioModel2 = new AudioModel("model2", "1.mp3", true);
        AudioModel audioModel3 = new AudioModel("model3", "1.mp3", true);

        audioModel1.setId(1);
        audioModel2.setId(2);
        audioModel3.setId(3);

        activeAudioModels.add(audioModel1);
        activeAudioModels.add(audioModel2);
        activeAudioModels.add(audioModel3);

        List<RingEvent> lastRingEvents = new ArrayList<>();
        lastRingEvents.add(new RingEvent(new Date(), audioModel3.getId()));

        when(audioModelRepository.findByActive(true)).thenReturn(activeAudioModels);
        when(ringEventRepository.findAllByOrderByRingDateDesc()).thenReturn(lastRingEvents);

        int nextAudioId = gpioService.getAudioModelIdToPlay();
        assertThat(nextAudioId, is(1));


    }


    @Test
    public void shouldGetNextAudioIdEventBefore(){
        List<AudioModel> activeAudioModels = new ArrayList<>();
        AudioModel audioModel1 = new AudioModel("model1", "1.mp3", true);
        AudioModel audioModel2 = new AudioModel("model2", "1.mp3", true);
        AudioModel audioModel3 = new AudioModel("model3", "1.mp3", true);

        audioModel1.setId(1);
        audioModel2.setId(2);
        audioModel3.setId(3);

        activeAudioModels.add(audioModel1);
        activeAudioModels.add(audioModel2);
        activeAudioModels.add(audioModel3);

        List<RingEvent> lastRingEvents = new ArrayList<>();
        lastRingEvents.add(new RingEvent(new Date(), 0));

        when(audioModelRepository.findByActive(true)).thenReturn(activeAudioModels);
        when(ringEventRepository.findAllByOrderByRingDateDesc()).thenReturn(lastRingEvents);

        int nextAudioId = gpioService.getAudioModelIdToPlay();
        assertThat(nextAudioId, is(1));


    }

}
