package framework.toolkit;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestStepStarted;

public class StepNameGetter implements ConcurrentEventListener {

    public static String STEP_NAME;

    public EventHandler<TestStepStarted> stepHandler = this::handleTestStepStarted;
    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestStepStarted.class, stepHandler);
    }

    private void handleTestStepStarted(TestStepStarted event){
        if(event.getTestStep() instanceof PickleStepTestStep){
            PickleStepTestStep testStep = (PickleStepTestStep) event.getTestStep();
            STEP_NAME = testStep.getStep().getText();
        }
    }
}
