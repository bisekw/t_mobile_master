package framework.core;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestStepStarted;

public class StepsDetails  implements ConcurrentEventListener {
        public static String stepName;

    public EventHandler<TestStepStarted> stepStartedEventHandler = new EventHandler<TestStepStarted>() {
        @Override
        public void receive(TestStepStarted testStepStarted) {
            handleTestStepStarted(testStepStarted);
        }
    };
    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.removeHandlerFor(TestStepStarted.class, stepStartedEventHandler);
    }

    private void handleTestStepStarted(TestStepStarted event){
        if(event.getTestStep() instanceof PickleStepTestStep){
            PickleStepTestStep pickleStepTestStep = (PickleStepTestStep) event.getTestStep();
            stepName = pickleStepTestStep.getStep().getText();
        }
    }
}
