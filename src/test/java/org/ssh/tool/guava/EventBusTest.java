package org.ssh.tool.guava;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

//Google Guava EventBus简化生产/消费者模式使用
//http://macrochen.iteye.com/blog/1393697
public class EventBusTest {

    //简单订阅消息
    @Test
    public void shouldReceiveEvent() throws Exception {

        // given
        EventBus eventBus = new EventBus("test");
        EventListener listener = new EventListener();

        eventBus.register(listener);

        // when
        eventBus.post(new OurTestEvent(200));

        // then
        //assertEquals(200, listener.getLastMessage());
        org.fest.assertions.Assertions.assertThat(listener.getLastMessage()).isEqualTo(200);
    }

    //多订阅
    @Test
    public void shouldReceiveMultipleEvents() throws Exception {

        // given
        EventBus eventBus = new EventBus("test");
        MultipleListener multiListener = new MultipleListener();

        eventBus.register(multiListener);

        // when
        eventBus.post(new Integer(100));
        eventBus.post(new Long(800));

        // then
        org.fest.assertions.Assertions.assertThat(multiListener.getLastInteger()).isEqualTo(100);
        org.fest.assertions.Assertions.assertThat(multiListener.getLastLong()).isEqualTo(800L);
        
        //assertEquals(100, multiListener.getLastInteger().intValue());
        //assertEquals(800, multiListener.getLastLong().longValue());
    }

    //EventBus发送的消息都不是订阅者关心的称之为Dead Event
    @Test
    public void shouldDetectEventWithoutListeners() throws Exception {
     
        // given
        EventBus eventBus = new EventBus("test");
     
        DeadEventListener deadEventListener = new DeadEventListener();
        eventBus.register(deadEventListener);
     
        // when
        eventBus.post(new OurTestEvent(200));
     
        assertTrue(deadEventListener.isNotDelivered());
    }
    
    //Event的继承
    @Test    
    public void shouldGetEventsFromSubclass() throws Exception {
     
        // given
        EventBus eventBus = new EventBus("test");
        IntegerListener integerListener = new IntegerListener();
        NumberListener numberListener = new NumberListener();
        eventBus.register(integerListener);
        eventBus.register(numberListener);
     
        // when
        eventBus.post(new Integer(100));
     
        // then
        assertEquals(100, integerListener.getLastMessage().intValue());
        assertEquals(100, numberListener.getLastMessage().intValue());
     
        //when
        eventBus.post(new Long(200L));
     
        // then
        // this one should has the old value as it listens only for Integers
        assertEquals(100, integerListener.getLastMessage().intValue());
        assertEquals(200, numberListener.getLastMessage().intValue());
     }
    
    public class NumberListener {
        
        private Number lastMessage;
     
        @Subscribe
        public void listen(Number integer) {
            lastMessage = integer;
        }
     
        public Number getLastMessage() {
            return lastMessage;
        }
    }

        
    public class IntegerListener {
     
        private Integer lastMessage;
     
        @Subscribe
        public void listen(Integer integer) {
            lastMessage = integer;
        }
     
        public Integer getLastMessage() {
            return lastMessage;
        }
    }
    
    /**
     * Listener waiting for the event that any message was posted but not delivered to anyone
     */
    public class DeadEventListener {
     
        boolean notDelivered = false;
     
        @Subscribe
        public void listen(DeadEvent event) {
            notDelivered = true;
        }
     
        public boolean isNotDelivered() {
            return notDelivered;
        }
    }
    
    public class MultipleListener {

        public Integer lastInteger;
        public Long lastLong;

        @Subscribe
        public void listenInteger(Integer event) {
            lastInteger = event;
        }

        @Subscribe
        public void listenLong(Long event) {
            lastLong = event;
        }

        public Integer getLastInteger() {
            return lastInteger;
        }

        public Long getLastLong() {
            return lastLong;
        }
    }

    public class EventListener {
        public int lastMessage = 0;

        @Subscribe
        public void listen(OurTestEvent event) {
            lastMessage = event.getMessage();
        }

        public int getLastMessage() {
            return lastMessage;
        }
    }

    public class OurTestEvent {
        private final int message;

        public OurTestEvent(int message) {
            this.message = message;
        }

        public int getMessage() {
            return message;
        }
    }
}
